(ns webserver.core
	(:gen-class :main true)
	(:use server.socket	
		[clojure.string :as str :only[split join]]))

(defn vector-to-string [command-vector]
	(str/join " " command-vector))

(defn parse-directory [commands]
	(let [matches (re-find #"-d \S+" (vector-to-string commands))]
	  	(first(rest (str/split matches #" ")))))

(defn get-directory
	([] (get-directory ["-d" "/Users/angeleah/Development/8th_light_apprenticeship/webserver/public"]))
	([commands] (parse-directory commands)))

(defn port [] 5000)

(defn make-webserver [])

(defn -main [& args]
	(create-server port (make-webserver (get-directory args))))
