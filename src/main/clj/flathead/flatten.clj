(ns flathead.flatten
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [flathead.plain :as plain])
  (:import (java.util.regex Pattern)))

(defn key-path
  "Split key to a key path. Key path item separator is separator regular expression."
  [key separator] (map keyword (str/split (name key) separator)))

(defn- add-prefix
  "Add a prefix to a key. Returns a new key with specified prefix.
  If the prefix is nil then the original key is returned."
  [prefix separator key]
  (if (nil? prefix)
    key (->> key name (str prefix separator) keyword)))

(defn flat->tree
  "Transforms a flat associative array (map) to a nested structure so that all values
  are associated to a new nested or existing map using a specific key path, which is generated from
  the current key of the value.

  A key path is a sequence of keys and it is generated from splitting the current key
  using a given separator. Separator is a regular expression.

  e.g. {:a_x 1, :a_y 2 :b 3} -> {a: {:x 1 :y 2} :b 3}"

  [^Pattern separator row]
  (reduce (fn [obj [key value]] (assoc-in obj (key-path key separator) value))
          {} row))

(defn tree->flat
  "Transforms a nested associative array (map) to a flat one.
  This is an inverse of the flat->tree function.

  The nested structure is not allowed to contain cycles it must be a tree.

  If nested structure contains also other sequences
  they can be first converted to map using function sequence->map"

  ([^String separator object] (tree->flat nil separator object))
  ([^String prefix ^String separator object]
   (reduce
     (fn [row [key value]]
       (let [flat-key (add-prefix prefix separator key)]
         (if (map? value)
           (merge row (tree->flat (name flat-key) separator value))
           (assoc row flat-key value))))
     {} object)))

(defn sequence->map
  "When sequences are part of the nested structure they can be first converted to maps
  and then to flat objects using function tree->flat."
  [object]
  (let [convert
        (fn [[key value :as entry]]
          (if (seqable? value)
            [key (plain/sequence->map value)]
            entry))]
    (walk/postwalk
      #(if (map-entry? %) (convert %) %)
      object)))