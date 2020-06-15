(ns flathead.plain
  (:import (java.util Properties)))

(defn map-keys [f m] (into {} (map (fn [[key, value]] [(f key) value]) m)))

(defn map-values [f m] (into {} (map (fn [[key, value]] [key (f value)]) m)))

(defn sequence->map [sequence]
  (if (map? sequence) sequence (into {} (map-indexed vector sequence))))

(defn ->properties [object]
  (reduce (fn [^Properties result property]
            (.setProperty result (str (first property)) (str (second property)))
            result)
          (Properties.) object))
