(ns flathead.deep-test
  (:require [clojure.test :as t]
            [flathead.deep :as deep]))


(t/deftest deep-merge-flat-objects
  (t/is (= (deep/deep-merge nil) nil))
  (t/is (= (deep/deep-merge {}) {}))
  (t/is (= (deep/deep-merge {:a 1}) {:a 1}))
  (t/is (= (deep/deep-merge {:a 1} {:b 1}) {:a 1 :b 1})))

(t/deftest deep-merge-nested-objects
  (t/is (= (deep/deep-merge {:a {:a 1}} {:a {:b 1}}) {:a {:a 1 :b 1}})))

