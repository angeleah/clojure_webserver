(ns webserver.core
	(:gen-class :main true)
	(:use server.socket	
		[clojure.string :as str :only[split join]]))
	(import '(java.io PrintWriter BufferedReader InputStreamReader File FileReader))
	
(def port 5000)

(defn ok []
	(str "HTTP/1.1 200 OK\n" 
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 "<!DOCTYPE html>
			<title>Web Server</title>
		<body>
		<a href='/file1'>file1</a>
		<a href='/file2'>file2</a>
		<a href='/image.gif'>image.gif</a>
		<a href='/image.jpeg'>image.jpeg</a>
		<a href='/image.png'>image.png</a>
		<a href='/text-file.txt'>text-file.txt</a>
		<a href='/text-file.txt'>text-file.txt</a>
		<a href='/image.jpeg'>index.html</a>
		</body>\n"))
; 
; (defn process-file [file-name]
; 	(with-open [rdr (FileReader. file-name)]
; 	    (doall (line-seq rdr))))
		
(defn file1 [directory]
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
		(slurp (str directory "file1"))))

(defn echo-back []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 "variable_1 = 123459876 variable_2 = some_value\n"))
	
(defn image-jpeg []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: image/jpeg\n"
		 "\n"
			))

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
;   (loop
;     [request-pairs (zipmap [:method :request-uri :protocol-version] (str/split (read-line) #"\s")) line (read-line)]
;     (if (empty? line)
;       request-pairs
;       (recur (assoc request-pairs (keyword (first (split line #":\s+"))) (last (split line #":\s+")))
;         (read-line)))))	; oh! because I need to have read-line in the webserver function.

(defn make-webserver [directory]
(fn [in out]
	(binding
	      [ *in* (BufferedReader. (InputStreamReader. in))
	        *out* (PrintWriter. out)]
	(let [request-segments
	  (loop
	    [pairs (zipmap [:method :request-uri :protocol-version] (str/split (read-line) #"\s")) line (read-line)]
		    (if (empty? line)
		      pairs
		      (recur (assoc pairs (keyword (first (split line #":\s+"))) (last (split line #":\s+")))
		        (read-line))))]
		(cond (and (= (:request-uri request-segments) "/") (= (:method request-segments) "GET")) (println(ok))
			  (and (= (:request-uri request-segments) "/form") (= (:method request-segments) "PUT")) (println(ok))
			  (and (= (:request-uri request-segments) "/form") (= (:method request-segments) "POST")) (println(ok))
			  (and (= (:request-uri request-segments) "/redirect") (= (:method request-segments) "GET")) (println(redirect))
			  (and (= (:request-uri request-segments) "/file1") (= (:method request-segments) "GET")) (println(file1 directory))
			  (and (= (:request-uri request-segments) "/some-script-url?variable_1=123459876&variable_2=some_value") (= (:method request-segments) "GET")) (println(echo-back))
			  (and (= (:request-uri request-segments) "/image.jpeg") (= (:method request-segments) "GET")) (println(image-jpeg))
			 ; (and (= (:request-uri request-segments) "/image.png") (= (:method request-segments) "GET")) (println(image-png))
			 ; (and (= (:request-uri request-segments) "/image.gif") (= (:method request-segments) "GET")) (println(image-gif))
			  :else (println(not-found)))))))

(defn vector-to-string [command-vector]
	(str/join " " command-vector))

(defn parse-directory [commands] ;TODO- set a default directory to my public folder if one is not provided so that I will not break my server.
	(let [matches (re-find #"-d \S+" (vector-to-string commands))]
	  	(first(rest (str/split matches #" ")))))

(defn function-creator [function to create]
	(fn [username] (str function to create ", " username)))

(defn -main [& args]
	(create-server port (make-webserver (parse-directory args))))