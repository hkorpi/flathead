(ns flathead.flatten-test
  (:require [clojure.test :as t]
            [flathead.flatten :as flat]))

(defmacro test-conversions [tree flat]
  `(t/is (= (flat/tree->flat "-" ~tree) ~flat))
  `(t/is (= (flat/flat->tree #"-" ~flat) ~tree)))

(t/deftest nils
  (t/is (= (flat/tree->flat "-" nil) {}))
  (t/is (= (flat/flat->tree "-" nil) {})))

(t/deftest nesting
  (test-conversions {} {})
  (test-conversions {:a 1} {:a 1})
  (test-conversions {:a {:a 1}} {:a-a 1})
  (test-conversions {:a {:a {:a 1}}} {:a-a-a 1})
  (test-conversions {:a {:a {:a {:a 1}}}} {:a-a-a-a 1}))

(t/deftest nesting-2keys
  (test-conversions {:a 1 :b 2} {:a 1 :b 2})
  (test-conversions {:a 1 :b {:a 1 :b 2}} {:a 1 :b-a 1 :b-b 2})
  (test-conversions {:a 1 :b {:a 1 :b {:a 1 :b 2}}} {:a 1 :b-a 1 :b-b-a 1 :b-b-b 2}))

(t/deftest sequence->map-empty
  (t/is (= (flat/sequence->map nil) {}))
  (t/is (= (flat/sequence->map []) {}))
  (t/is (= (flat/sequence->map '()) {})))

(t/deftest sequence->map-1
  (t/is (= (flat/sequence->map [1]) {:0 1}))
  (t/is (= (flat/sequence->map '(1)) {:0 1})))

(t/deftest sequence->map-nested
  (t/is (= (flat/sequence->map [[1]]) {:0 {:0 1}}))
  (t/is (= (flat/sequence->map [1 [1]]) {:0 1 :1 {:0 1}})))



