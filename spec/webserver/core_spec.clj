(ns webserver.core-spec
  (:use [speclj.core] 
		[webserver.core]))
		
(describe "it should be able to create a map of the initial request line to keys describing the parts of the line."

	(it "should be able to return me a method, the request URI, and the protocol version"
		(should= {:method "GET" :request-uri "/" :protocol-version "HTTP/1.1"}
		(with-in-str "GET / HTTP/1.1"(process-initial-request-line))))
)
