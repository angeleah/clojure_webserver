(ns webserver.core
	(:use server.socket
		[clojure.string :as str :only [split]]))
	(import '(java.io PrintWriter BufferedReader InputStreamReader))

(def port 5000)

(defn webserver [in out]
	(binding
	      [ *in* (BufferedReader. (InputStreamReader. in))
	        *out* (PrintWriter. out)]
	      (println "HTTP/1.0 200 OK")
	      (println "Content-Type: text/html")
	      (println "")
	      (loop [line (read-line)]
	        (println (str line "<br/>"))
	        (if-not (empty? line)
	          (recur (read-line))))))

(defn process-initial-request-line []
	(zipmap
		[:method :request-uri :protocol-version]
		(str/split (read-line) #"\s")))

(defn -main []
(create-server port webserver))
