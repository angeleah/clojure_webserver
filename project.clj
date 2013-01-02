(defproject webserver "1.0.0-SNAPSHOT"
  :description "webserver in clojure"
  :dependencies [[speclj "2.5.0"]
				[org.clojure/clojure "1.4.0"]
				[server-socket "1.0.0"]]
  :plugins [[speclj "2.5.0"]]
  :test-paths ["spec/"]
  :main webserver.core
  :java-source-path "src/"
)