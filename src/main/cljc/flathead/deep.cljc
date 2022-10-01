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