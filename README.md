# Flathead
[![Build Status](https://travis-ci.org/hkorpi/flathead.svg?branch=master)](https://travis-ci.org/hkorpi/flathead)
[![codecov](https://codecov.io/gh/hkorpi/flathead/branch/master/graph/badge.svg)](https://codecov.io/gh/hkorpi/flathead)
[![Clojars Project](https://img.shields.io/clojars/v/flathead.svg)](https://clojars.org/flathead)

A Clojure library containing utility functions for nested objects. 
The purpose is to augment existing core functionality.
This library also contains conversions to and from flat representation of a nested structure.

This library is divided to four parts:
* **flatten**: conversions from nested objects to/from plain flat objects.
* **deep**: utility functions for nested objects
* **plain**: utility functions for plain objects
* **logic**: logic related utility functions e.g. higher order logical functions

```clj
[flathead "0.0.7"]
```

## Usage
Convert a flat object to a nested tree object:
```clj
(flatten/flat->tree {:a_x 1, :a_y 2 :b 3}) -> {a: {:x 1 :y 2} :b 3}
```
and its inverse - nested tree to a flat representation:
```clj
(flatten/tree->flat {a: {:x 1 :y 2} :b 3}) -> {:a_x 1, :a_y 2 :b 3}
```


