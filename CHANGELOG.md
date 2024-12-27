# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).


## [Unreleased]
### Changed
- deep/map-values supports any seqable objects
- deep/map-values values? predicate changed to branch? and there is more flexibility 
  to filter which seqable objects are supported
- flatten/tree->flat / flatten/flat->tree support seqable objects and other key types (e.g. int, string) than keywords
- deep/tree->flat / deep/flat->tree provide a flat conversion where flat keys are path vectors
### Fixed
- deep/values

## [0.0.6] — 2023-02-22
### Added
- deep/apply-spec: Ramda apply spec for clojure.

## [0.0.5] — 2022-10-01
### Changed
Convert to common clojure

## [0.0.4] — 2021-06-28
### Changed
- flatten/sequence->map: Nils will be kept as nils.

## [0.0.3] — 2020-08-05
### Added
- deep/map-values: map recursively all values of a nested map

[0.0.3]: https://github.com/hkorpi/flathead/compare/0.0.0...0.0.3
[0.0.4]: https://github.com/hkorpi/flathead/compare/0.0.3...0.0.4
[0.0.5]: https://github.com/hkorpi/flathead/compare/0.0.4...0.0.5
[0.0.6]: https://github.com/hkorpi/flathead/compare/0.0.5...0.0.6
[Unreleased]: https://github.com/hkorpi/flathead/compare/0.0.6...HEAD
