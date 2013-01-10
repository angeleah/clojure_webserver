;TODO I need to ba able to parse out the file extension in order to construct the header with the content type.
;TODO I need to create something that builds the <html><body> BODY CONTENTS </body></html>. Maybe split opening and closing into separate functions.
;TODO Need to implement the CRLF at the end of the header.
;TODO- set a default directory to my public folder if one is not provided so that I will not break my server.
;TODO pull out request-segments and separate the in and out.
;TODO - to do all in binary, create the string headers and then convert it all to binary, combine it with the body. How do you deal with the CRLF?
;TODO - there's always a need to pay attention to exceptions (in particular, IOException and FileNotFoundException)
;TODO - Header must contain host to be valid 1.1
;TODO - what about mixed data types? files that have both text/html and images?  This is likely.
;TODO - Should I have two different writers and pass which one? Then I will still need to pass CRLF.
;-------
;TEST IDEA - Check for the host in 1.1 or accepting an absolute url in 1.1.?
;---------
;QUESTIONS - message body vs actual HTML page body.  How do you not return a string?/ should all this be a string?
;			- How can I avoid passing the file and directory eveywhere?
;			-Do I always want to be sending to standard out????? can you even send bytes to standard out?
;			-Getting the method validated for the request.
;			-spacing on decoding? is that a protocal issue?
;----------
; java.io.FileInputStream, java.io.ByteArrayOutputStream
;*** OutputSreamWriter
;(maybe something like (write headers)
						; (write CRLF)
						; write message-body)
; new BufferedWriter(new OutputStreamWriter(System.out))
(ns webserver.core
	(:gen-class :main true)
	(:use server.socket	
		[clojure.string :as str :only[split join]]))
	(import '(java.io PrintWriter BufferedReader InputStreamReader File FileReader FileInputStream ByteArrayOutputStream)
			'(clojure.java.io))
(def port 5000)

(defn read-characters [directory file]
	(slurp (str directory file)))

(defn read-bytes [directory file]
	(with-open[stream (clojure.java.io/reader (str directory file))]
		(loop [c (.read stream) bytes []]
           (if (not= c -1)
				(recur (.read stream) (conj bytes c))
				bytes))))

(defn decode-request [url]
	(java.net.URLDecoder/decode url))
	;there needs to be a space between variable_1 = Operators in the return value
	
; (defn write-binary [data]
; 	(with-open[writer(java.io.ByteArrayOutputStream.)]
; 		(.toByteArray )))

(defn decode-params [url]
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 (decode-request url)))
	
(defn ok [directory file]
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 (read-characters directory file)))
	
(defn post-ok []
	(str "HTTP/1.1 200 OK\n" 
	 	 "Content-Type: text/html\n"
		 "\n"))
	
(defn parse-file [directory file]
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
		(read-characters directory file)))
		
(defn parse-query-string [url]
	)

(defn echo-back []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 "variable_1 = 123459876 variable_2 = some_value\n"))
	;this needs a space between variable_1 = 123459876
	;and variable_2 = some_value

(defn image-gif []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: image/gif\n"
		 "\n"
	 	 ))

(defn image-png []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: image/png\n"
		 "\n"
	 	 ))

(defn image-jpeg []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: image/jpeg\n"
		 "\n"
	 	 ))
	
(defn not-found []
	(str "HTTP/1.1 404 Not found\n"
    	 "Content-Type: text/html\n"
		 "\n"
    	 "<h1>404 Not Found</h1>\n"))

(defn redirect []
	(str "HTTP/1.1 302 Found\n"
		 "Location: http://localhost:5000/\n"
		 "Content-Type: text/html\n"
		 "\n"))

; (defn process-request []
;	[ *in* (BufferedReader. (InputStreamReader. in))
;	  *out* (PrintWriter. out)]]
;   (loop
;     [request-pairs (zipmap [:method :request-uri :protocol-version] (str/split (read-line) #"\s")) line (read-line)]
;     (if (empty? line)
;       request-pairs
;       (recur (assoc request-pairs (keyword (first (split line #":\s+"))) (last (split line #":\s+")))
;         (read-line)))))

(defn make-webserver [directory]
(fn [in out]
	(binding
	      [ *in* (BufferedReader. (InputStreamReader. in))
	        *out* (ByteArrayOutputStream. out)]
	(let [request-segments
	  (loop
	    [pairs (zipmap [:method :request-uri :protocol-version] (str/split (read-line) #"\s")) line (read-line)]
		    (if (empty? line)
		      pairs
		      (recur (assoc pairs (keyword (first (split line #":\s+"))) (last (split line #":\s+")))
		        (read-line))))]
		(cond (and (= (:request-uri request-segments) "/") (= (:method request-segments) "GET")) (println(parse-file directory (:request-uri request-segments)))
			  (and (= (:request-uri request-segments) "/form") (= (:method request-segments) "PUT")) (println(parse-file directory (:request-uri request-segments)))
			  (and (= (:request-uri request-segments) "/form") (= (:method request-segments) "POST")) (println(post-ok))
			  (and (= (:request-uri request-segments) "/redirect") (= (:method request-segments) "GET")) (println(redirect))
			  (and (= (:request-uri request-segments) "/file1") (= (:method request-segments) "GET")) (println(parse-file directory (:request-uri request-segments)))
			  (and (= (:request-uri request-segments) "/some-script-url?variable_1=123459876&variable_2=some_value") (= (:method request-segments) "GET")) (println(echo-back))
			  (and (= (:request-uri request-segments) "/image.jpeg") (= (:method request-segments) "GET")) ((println(image-jpeg directory))(read-bytes directory (:request-uri request-segments)))
			  (and (= (:request-uri request-segments) "//form?variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F") (= (:method request-segments) "GET"))(println(decode-request "variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F")))
			 ; (and (= (:request-uri request-segments) "/image.png") (= (:method request-segments) "GET")) (println(image-png))
			 ; (and (= (:request-uri request-segments) "/image.gif") (= (:method request-segments) "GET")) (println(image-gif))
			  :else (println(not-found))))))

(defn vector-to-string [command-vector]
	(str/join " " command-vector))

(defn parse-directory [commands]
	(let [matches (re-find #"-d \S+" (vector-to-string commands))]
	  	(first(rest (str/split matches #" ")))))

(defn function-creator [function to create]
	(fn [username] (str function to create ", " username)))

(defn -main [& args]
	(create-server port (make-webserver (parse-directory args))))