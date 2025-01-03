(defproject flathead "0.0.7"
  :description "Utility library for nested objects."
  :url "https://github.com/hkorpi/flathead"
  :license {:name "Apache License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies []
  :source-paths ["src/main/cljc"]
  :test-paths ["src/test/cljc" "src/test/clj"]
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.7.0"]
            [lein-changelog "0.3.2"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.12.0"]
                                  [org.clojure/tools.namespace "1.5.0"]]}}
  :deploy-repositories [["releases" :clojars]]
  :aliases {"update-readme-version" ["shell" "sed" "-i" "s/\\\\[flathead \"[0-9.]*\"\\\\]/[flathead \"${:version}\"]/" "README.md"]}
  :release-tasks [["shell" "git" "diff" "--exit-code"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["changelog" "release"]
                  ["update-readme-version"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["vcs" "push"]])
