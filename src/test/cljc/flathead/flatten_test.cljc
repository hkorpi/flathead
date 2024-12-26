(ns flathead.flatten-test
  (:require [clojure.test :as t]
            [flathead.flatten :as flat]))

(defmacro test-conversions [tree flat]
  `(do
     (t/is (= (flat/tree->flat "-" ~tree) ~flat))
     (t/is (= (flat/flat->tree #"-" ~flat) ~tree))))

(t/deftest nils
  (t/is (= (flat/tree->flat "-" nil) {}))
  (t/is (= (flat/flat->tree "-" nil) {})))

(t/deftest nesting
  (test-conversions {} {})
  (test-conversions {:a 1} {:a 1})
  (test-conversions {:a [1]} {:a [1]})
  (test-conversions {:a {:a 1}} {:a-a 1})
  (test-conversions {:a {:a {:a 1}}} {:a-a-a 1})
  (test-conversions {:a {:a {:a {:a 1}}}} {:a-a-a-a 1}))

(t/deftest nesting-2keys
  (test-conversions {:a 1 :b 2} {:a 1 :b 2})
  (test-conversions {:a 1 :b {:a 1 :b 2}} {:a 1 :b-a 1 :b-b 2})
  (test-conversions {:a 1 :b {:a 1 :b {:a 1 :b 2}}} {:a 1 :b-a 1 :b-b-a 1 :b-b-b 2}))

(t/deftest other-key-types
  (t/is (= (flat/tree->flat nil "-" {0 0}) {0 0}))
  (t/is (= (flat/tree->flat nil "-" {:a {0 0}}) {:a-0 0})))

(t/deftest sequences
  (t/is (= (flat/tree->flat nil sequential? "-" [0 1 2]) {0 0 1 1 2 2}))
  (t/is (= (flat/tree->flat nil sequential? "-" [0 1 [0 1 2]]) {0 0 1 1 :2-0 0 :2-1 1 :2-2 2}))
  (t/is (= (flat/tree->flat nil "-" {:a [0 1 2]}) {:a [0 1 2]}))
  (t/is (= (flat/tree->flat nil (some-fn map? sequential?) "-" {:a [0 1 2]}) {:a-0 0 :a-1 1 :a-2 2})))


