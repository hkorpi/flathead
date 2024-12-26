(ns flathead.plain
  #?(:clj (:import (java.util Properties))))

(defn map-keys [f m] (into {} (map (fn [[key, value]] [(f key) value]) m)))

(defn map-values [f m] (into {} (map (fn [[key, value]] [key (f value)]) m)))

(defn sequence->map [sequence]
  (if (map? sequence) sequence (into {} (map-indexed vector sequence))))

(defn flat->tree
  "Transforms a flat associative array (map) to a nested structure so that all values
  are associated to a new nested or existing map using a specific key path, which is generated from
  the current key of the value using key->path function.

 This is an inverse of the deep/flat->tree function.

  A default implementation assumes that the flat keys are directly the paths."
  ([row] (flat->tree identity row))
  ([key->path row]
   (reduce (fn [obj [key value]] (assoc-in obj (key->path key) value)) {} row)))

#?(:clj
   (defn ->properties [object]
     (reduce (fn [^Properties result property]
               (.setProperty result (str (first property)) (str (second property)))
               result)
             (Properties.) object)))
