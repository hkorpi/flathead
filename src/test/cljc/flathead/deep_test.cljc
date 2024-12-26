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

(t/deftest values
  (t/is (= (deep/values 1) '(1)))
  (t/is (= (deep/values "asdf") '("asdf")))
  (t/is (= (deep/values string? "asdf") '(\a \s \d \f)))
  (t/is (= (deep/values nil) '(nil)))
  (t/is (= (deep/values nil? nil) '()))
  (t/is (= (deep/values {}) '()))
  (t/is (= (deep/values {:a nil}) '(nil)))
  (t/is (= (deep/values {:a 1}) '(1)))
  (t/is (= (deep/values {:a 1 :b 1}) '(1 1)))
  (t/is (= (deep/values {:a 1 :b {:c 2 :d 2}}) '(1 2 2)))
  (t/is (= (deep/values {:a 1 :b [{:c 2 :d 2}]}) '(1 2 2)))
  (t/is (= (deep/values {:a 1 :b [{:c 2 :d [3 3 3]}]}) '(1 2 3 3 3)))
  (t/is (= (deep/values map? {:a 1 :b [{:c 2 :d 2}]}) '(1 [{:c 2 :d 2}]))))

(t/deftest map-values-flat-objects-identity
  (t/is (= (deep/map-values identity nil) {}))
  (t/is (= (deep/map-values identity {}) {}))
  (t/is (= (deep/map-values identity {:a 1}) {:a 1}))
  (t/is (= (deep/map-values identity {:a 1 :b 1}) {:a 1 :b 1}))
  (t/is (= (deep/map-values identity {:a 1 :b [2 2]}) {:a 1 :b '(2 2)}))
  (t/is (= (deep/map-values map? identity {:a 1 :b [2 2]}) {:a 1 :b [2 2]})))

(t/deftest map-values-flat-objects-inc
  (t/is (= (deep/map-values inc nil) {}))
  (t/is (= (deep/map-values inc {}) {}))
  (t/is (= (deep/map-values inc {:a 1}) {:a 2}))
  (t/is (= (deep/map-values inc {:a 1 :b 1}) {:a 2 :b 2})))

(t/deftest map-values-nested-objects-inc
  (t/is (= (deep/map-values inc {:a {:b 1}}) {:a {:b 2}}))
  (t/is (= (deep/map-values inc {:a {:b 1} :b 1}) {:a {:b 2} :b 2}))
  (t/is (= (deep/map-values inc {:a {:b 1 :c 1}}) {:a {:b 2 :c 2}}))
  (t/is (= (deep/map-values inc {:a {:b 1 :c [2 2]}}) {:a {:b 2 :c [3 3]}}))
  (t/is (= (deep/map-values inc {:a {:b 1 :c [{:b 2}]}}) {:a {:b 2 :c [{:b 3}]}})))

(t/deftest evolve-flat-objects
  (t/is (= (deep/evolve {} {}) {}))
  (t/is (= (deep/evolve {} {:a 1}) {:a 1}))
  (t/is (= (deep/evolve {:b str} {:a 1}) {:a 1}))
  (t/is (= (deep/evolve {:a str} {:a 1}) {:a "1"})))

(t/deftest evolve-nested-objects
  (t/is (= (deep/evolve {:a {:b str}} {}) {}))
  (t/is (= (deep/evolve {:a {:b 1}} {:a {:c 1}}) {:a {:c 1}}))
  (t/is (= (deep/evolve {:a {:b 1}} {:a 1}) {:a 1}))
  (t/is (= (deep/evolve {:a {:b str}} {:a {:b 1}}) {:a {:b "1"}}))
  (t/is (= (deep/evolve {:a {:b str}} {:a {:b 1 :c 2}}) {:a {:b "1" :c 2}})))

(t/deftest apply-spec-flat-objects
  (t/is (= ((deep/apply-spec nil) 1) {}))
  (t/is (= ((deep/apply-spec {}) 1) {}))
  (t/is (= ((deep/apply-spec {:a identity}) 1) {:a 1}))
  (t/is (= ((deep/apply-spec {:a identity} 1) 1) {:a 1}))
  (t/is (= ((deep/apply-spec {:a inc}) 1) {:a 2}))
  (t/is (= ((deep/apply-spec {:a +} 2) 1 1) {:a 2})))

(t/deftest apply-spec-nested-objects
  (t/is (= ((deep/apply-spec {:a {:b identity}}) 1) {:a {:b 1}}))
  (t/is (= ((deep/apply-spec {:a {:b inc}}) 1) {:a {:b 2}})))

(t/deftest sequence->map-empty
  (t/is (= (deep/sequence->map nil) nil))
  (t/is (= (deep/sequence->map []) {}))
  (t/is (= (deep/sequence->map '()) {})))

(t/deftest sequence->map-1
  (t/is (= (deep/sequence->map [1]) {0 1}))
  (t/is (= (deep/sequence->map '(1)) {0 1})))

(t/deftest sequence->map-nested
  (t/is (= (deep/sequence->map [[1]]) {0 {0 1}}))
  (t/is (= (deep/sequence->map [1 [1]]) {0 1 1 {0 1}}))
  (t/is (= (deep/sequence->map [1 [1] nil {:a nil :b [2]}])
           {0 1 1 {0 1} 2 nil 3 {:a nil :b {0 2}}})))
