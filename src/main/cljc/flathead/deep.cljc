(ns flathead.deep
  (:require [flathead.plain :as plain]
            [flathead.logic :as logic]))

(defn deep-merge-with
  "Deeply merges nested maps. If a key occurs in more than one map and
  any of the values is not a map then the mapping(s)
  from the latter (left-to-right) will be combined
  by calling (f val-in-result val-in-latter)."
  [f & values]
  (if (every? map? values)
    (apply merge-with (partial deep-merge-with f) values)
    (f values)))

(defn deep-merge
  "Deeply merges nested maps.
  Same as deep-merge-with where merge function is last."
  [& vs]
  (apply deep-merge-with last vs))

(defn values
  "Find recursively all values of a nested associative structure using a depth-first search (DFS).
  Returns a lazy sequence of values. Supports any seqable objects (see seqable?).
  For maps the values are map values (vals) and for other seqable objects the sequence itself.
  A branch? predicate can be used to define if object is part of the structure or a value. Values are leafs in DFS.
  You can only mark seqable objects as branches. The default version treat all sequential objects and maps as branches."
  ([nested-map] (values (some-fn sequential? map?) nested-map))
  ([branch? nested-map]
   (remove branch? (tree-seq branch? (logic/when* map? vals) nested-map))))

(defn map-values
  "Map recursively all values of a nested associative structure. Supports any seqable objects (see seqable?).
  For maps the values are mapped using plain/map-values and for other seqable objects using map function.
  A branch? predicate can be used to mark objects as part of structure and not a value.
  You can only mark seqable objects as branches. The default version treat all sequential objects and maps as branches."
  ([f nested-map] (map-values (some-fn sequential? map?) f nested-map))
  ([branch? f nested-map]
   (let [recursive-f (logic/if* branch? (partial map-values branch? f) f)]
     (cond
       (nil? nested-map) {} ;; same as plain/map-values
       (map? nested-map) (plain/map-values recursive-f nested-map)
       :else (map recursive-f nested-map)))))

(defn evolve
  "Ramda evolve for clojure."
  [transformations object]
  (if (map? object)
    (persistent!
      (reduce
        (fn [result [key transformation]]
          (if-let [original-value (key result)]
            (assoc! result key
                    (if (map? transformation)
                      (evolve transformation original-value)
                      (transformation original-value)))
            result))
        (transient object)
        transformations))
    object))

(defn apply-spec
  "Ramda apply spec for clojure.
  Creates a function, which produces an object of the same structure as the specification object.
  Object values are produced by applying the corresponding function in specification object to the arguments.
  All values in specification object must be functions. Specification object can be nested."
  ([specification] (apply-spec specification 0))
  ([specification argument-count]
   (case argument-count
     1 (fn [param] (map-values #(% param) specification))
     2 (fn [p1 p2] (map-values #(% p1 p2) specification))
     3 (fn [p1 p2 p3] (map-values #(% p1 p2 p3) specification))
     4 (fn [p1 p2 p3 p4] (map-values #(% p1 p2 p3 p4) specification))
     5 (fn [p1 p2 p3 p4 p5] (map-values #(% p1 p2 p3 p4 p5) specification))
     (fn [& params] (map-values #(apply % params) specification)))))
