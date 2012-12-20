(defproject webserver "1.0.0-SNAPSHOT"
  :description "webserver in clojure"
  :dependencies [[org.clojure/clojure "1.3.0"]
				[server-socket "1.0.0"]]
  :dev-dependencies [[speclj "2.1.1"]]
  :test-path "spec/"
  :main webserver.core
  :java-source-path "src/"
)