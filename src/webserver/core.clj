(ns webserver.core
	(:use server.socket
		[clojure.string :as str :only [split]]))
	(import '(java.io PrintWriter BufferedReader InputStreamReader))

(def port 5000)

(defn ok []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 "<h1>Yo, This is my sweet web server!</h1>\n"))

(defn not-found []
	(str "HTTP/1.1 404 Not found\n"
    	 "Content-Type: text/html\n"
		 "\n"
    	 "<h1>404 Not Found</h1>\n"))

(defn process-initial-request-line [line]
	(zipmap
		[:method :request-uri :protocol-version]
		(str/split line #"\s")))

(defn webserver [in out]
	(binding
	      [ *in* (BufferedReader. (InputStreamReader. in))
	        *out* (PrintWriter. out)]
		(if (= (:request-uri (process-initial-request-line (read-line))) "/") (println(ok)) (println(not-found)))))

(defn -main []
(create-server port webserver))
