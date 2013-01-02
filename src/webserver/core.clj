(ns webserver.core
	(:gen-class :main true)
	(:use server.socket	
		[clojure.string :as str :only [split]]))
	(import '(java.io PrintWriter BufferedReader InputStreamReader File FileReader)) 

(def port 5000)

(defn ok []
	(str "HTTP/1.1 200 OK\n"  ;host information?  adding content length?  Is this required?  What is the point?
	 	 "Content-Type: text/html\n"
		 "\n"; the body will change so I will want to send the "data" to okay and place it in body. The data will be gathered
		;from pointing a route /index to an action.-> index that will contain the code
		;to loop through the directory.
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
		</body>\n"));replace this hardcoded line with something like (read-everything in the folder of
			; public.  That should go into a seq, then I can go from seq to building up a link tag.
			;consider if there is a folder(s) inside public to search recursively and construct the proper path / for the level deep.

			;("request-uri")) investgate this
	;reading html and not having a string.Generate the links ;slurp for reading text-files.
(defn file1 []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 (println(slurp "/public/text-file.txt")))) ; this will be removed and replaced with the reading of the file contents into the ok response.

(defn echo-back []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: text/html\n"
		 "\n"
	 	 "variable_1 = 123459876 variable_2 = some_value\n")) ; this will be removed and replaced with the reading of the file contents into the ok response.
	
(defn image-jpeg []
	(str "HTTP/1.1 200 OK\n"
	 	 "Content-Type: image/jpeg\n"
		 "\n"));explore the image/* wildcard.  What about when images are on a page with html?

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
    	 "<h1>404 Not Found</h1>\n"));for razzle dazzle I could add a custom not found page.

(defn redirect []
	(str "HTTP/1.1 302 Found\n"
		 "Location: http://localhost:5000/\n";this location needs dynamic generation.( need to build up the location with the host, etc.)?
		 "Content-Type: text/html\n"  ; how would you dynamically build this up without hardcoding the redirect url into the redirect function?
		;i.e. /redirect then call what? how do you detect the redirect url?
		 "\n"))

(defn process-initial-request-line [line]
	(zipmap
		[:method :request-uri :protocol-version]
		(str/split line #"\s")))

(defn read-document [request-uri]) ;if I read the document to get the contents, I need to also determine length.  If I do that 2x it is not dry.
	;if I do it once it violates SRP.  ?? dilema.

(defn webserver [in out]
	(binding
	      [ *in* (BufferedReader. (InputStreamReader. in))
	        *out* (PrintWriter. out)]
	(let [initial-request-segments (process-initial-request-line (read-line))]
		(cond (and (= (:request-uri initial-request-segments) "/") (= (:method initial-request-segments) "GET")) (println(ok))
			  (and (= (:request-uri initial-request-segments) "/form") (= (:method initial-request-segments) "PUT")) (println(ok))
			  (and (= (:request-uri initial-request-segments) "/form") (= (:method initial-request-segments) "POST")) (println(ok))
			  (and (= (:request-uri initial-request-segments) "/redirect") (= (:method initial-request-segments) "GET")) (println(redirect))
			  (and (= (:request-uri initial-request-segments) "/file1") (= (:method initial-request-segments) "GET")) (println(file1))
			  (and (= (:request-uri initial-request-segments) "/some-script-url?variable_1=123459876&variable_2=some_value") (= (:method initial-request-segments) "GET")) (println(echo-back))
			  (and (= (:request-uri initial-request-segments) "/image.jpeg") (= (:method initial-request-segments) "GET")) (println(image-jpeg))
			 ; (and (= (:request-uri initial-request-segments) "/image.png") (= (:method initial-request-segments) "GET")) (println(image-png))
			 ; (and (= (:request-uri initial-request-segments) "/image.gif") (= (:method initial-request-segments) "GET")) (println(image-gif))
			  :else (println(not-found))))))
			;for each of these routes do I need something like path of my public directory + request uri in a routes file.
			; then I need to check that I can get it and read it.As long at that exists, then I can put together a response to point to
			;and dynamically add in the host, content, content-type, content-length, etc.

			;Therefore, before opening a file, you may want to check first whether it exists. To assist you with this, the File class is equipped with the exists() method. Here is an example of calling it:

			;        // Find out if the file exists already  this can help determine 404 or 200, etc.
			 ;       if( fleExample.exists() ) {

(defn parse-directory [arglist]
	 (let [command (re-find #"-d \S+" arglist)]
 		(first(rest (str/split command #" ")))))


;should this be that webserver calls make- webserver with the directory?
(defn make-webserver [directory-path]
	(fn [?] webserver))

;(defn parse-args [;args or &args?]]
;	)

;(defn -main [& args]
;	(println arglis))
	
	
;	(parse-directory arglist))

;(defn -main [& args]
;	(create-server port webserver))
	;(create-server port make-webserver and pass in directory ))
	;need to parse the args to grab what comes after the -d.  do I do this here or inside my webserver? I still do not get how I can get to the args.
	