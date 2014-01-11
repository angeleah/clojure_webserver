(defproject webserver "1.0.0-SNAPSHOT"
  :description "webserver in clojure"
  :dependencies [[org.clojure/clojure "1.5.1"]
				[server-socket "1.0.0"]]
  :profiles {:dev {:dependencies [[speclj "2.9.1"]]}}
  :plugins [[speclj "2.9.1"]]
  :test-paths ["spec"]
  :main webserver.core
  :java-source-path "src/"
)
