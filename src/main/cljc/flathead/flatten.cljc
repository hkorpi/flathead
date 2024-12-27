(ns flathead.flatten
  (:require [clojure.string :as str]
            [flathead.plain :as plain]
            [flathead.deep :as deep])
  #?(:clj (:import (java.util.regex Pattern))))

(defn key-path
  "Split key to a key path. Key path item separator is separator regular expression."
  [separator key] (map keyword (str/split (name key) separator)))

(defn key-name [key] (if (keyword? key) (name key) (str key)))

(defn conj-key
  "Add a prefix to a key. Returns a new key with specified prefix.
  If the prefix is nil then the original key is returned."
  [root-key separator key]
  (if (nil? root-key)
    key (keyword (str (key-name root-key) separator (key-name key)))))

(defn flat->tree
  "Transforms a flat associative array (map) to a nested structure so that all values
  are associated to a new nested or existing map using a specific key path, which is generated from
  the current key of the value.

  A key path is a sequence of keys, and it is generated from splitting the current key
  using a given separator. Separator is a regular expression.

  e.g. {:a_x 1, :a_y 2 :b 3} -> {a: {:x 1 :y 2} :b 3}"

  [#?(:clj ^Pattern separator :default separator) row]
  (plain/flat->tree (partial key-path separator) row))

(defn tree->flat
  "Transforms a nested associative structure to a flat one.
  The flat key is created by concatenating the child key to the object root key using the given separator.
  This is an inverse of the flat->tree function. This is based on deep/tree->flat (see deep/tree->flat).
  Supports any seqable objects (see seqable?). Other sequences are transformed to maps using plain/sequence->map.

  Arguments:
  - separator - A separator string used to form the flat map keys.
  - tree      - A tree object is converted to a flat representation
  - root-key  - Root key defines the root key for the tree object - use nil for empty
  - branch?   - A branch? predicate can be used to mark objects as part of tree structure and not a value see deep/tree->flat.
  The default implementation will branch only inside maps."

  ([separator tree] (tree->flat nil separator tree))
  ([root-key separator tree]
   (tree->flat root-key map? separator tree))
  ([root-key branch? separator tree]
   (deep/tree->flat #(conj-key %1 separator %2) branch? root-key tree)))
