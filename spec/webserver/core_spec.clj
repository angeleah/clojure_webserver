(ns webserver.core-spec
  (:use [speclj.core :refer :all]
		[webserver.core :refer :all]
		[clojure.string :as str :only [split]]))
		
(describe "it should be able to create a map of the initial request line to keys describing the parts of the line."

	(it "should be able to return me a method, the request URI, and the protocol version"
		(should= {:method "GET" :request-uri "/" :protocol-version "HTTP/1.1"} (process-initial-request-line "GET / HTTP/1.1")))
		
	(it	"should be able to parse out a directory"
		(should= "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public/" (parse-directory ["/usr/bin/java" "-jar" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/target/webserver-1.0.0-SNAPSHOT-standalone.jar" "-p" "5000" "-d" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public/"])))
		
	(it	"should be able to create a vector to a string"
		(should= "/usr/bin/java -jar /Users/angeleah/Development/8th_light_apprenticeship/webserver/target/webserver-1.0.0-SNAPSHOT-standalone.jar -p 5000 -d /Users/angeleah/Development/8th_light_apprenticeship/webserver/public/" (vector-to-string ["/usr/bin/java" "-jar" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/target/webserver-1.0.0-SNAPSHOT-standalone.jar" "-p" "5000" "-d" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public/"])))
)

(run-specs)