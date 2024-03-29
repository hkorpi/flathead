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

(defn values [m]
  (-> m vals
      (map #(if (map? %) (values %) %))
      flatten))

(defn map-values
  "Map recursively all values of a nested map.
  A value? predicate can be used to mark some specific map as a value."
  ([fn nested-map] (map-values (constantly false) fn nested-map))
  ([value? fn nested-map]
   (plain/map-values
     (logic/if* (every-pred map? (complement value?))
                (partial map-values value? fn)
                fn)
     nested-map)))

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
