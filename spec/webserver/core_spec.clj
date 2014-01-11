(ns webserver.core-spec
  (:use [speclj.core :refer :all]
		[webserver.core :refer :all]))

(describe "determining a directory"
	(it "should be able to create a vector to a string"
		(should= "/usr/bin/java -jar /Users/angeleah/Development/8th_light_apprenticeship/webserver/target/webserver-1.0.0-SNAPSHOT-standalone.jar -p 5000 -d /Users/angeleah/Development/8th_light_apprenticeship/webserver/public" (vector-to-string ["/usr/bin/java" "-jar" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/target/webserver-1.0.0-SNAPSHOT-standalone.jar" "-p" "5000" "-d" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public"])))

	(it "should be able to parse out a directory"
	 	(should= "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public" (parse-directory ["/usr/bin/java" "-jar" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/target/webserver-1.0.0-SNAPSHOT-standalone.jar" "-p" "5000" "-d" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public"])))

	(it	"should return the default directory if no arguments are passed"
		(should= "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public" (get-directory)))

	(it "should return a port number"
		(should= 5000 (port)))
)
