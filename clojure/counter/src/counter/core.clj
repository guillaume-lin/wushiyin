(ns counter.core)
(use :reload 'counter.test)

;;;  start server first
(use '[clojure.tools.nrepl.server :only (start-server stop-server)])
(defonce server (start-server :port 7888))


;;; a foo function, you know it
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

;;; define the download count
(def  download-count 0)

;;; increment it
(defn inc-download-count
  []
  (def download-count (inc download-count)))


;;; load download count from file
(defn load-download-count
  "load download count from file"
  [file]
  (def download-count (try 
                        (Integer/parseInt (slurp file))
                        (catch Exception e 
                          0
                        ))))

;;; save download count to file
(defn save-download-count
  "save download count to file"
  [file download-count]
  (spit file download-count))

;;; here is the response
(defn response
  [req]
  (or (print-req req)
  (do
    (load-download-count "/tmp/downloadcount.txt")
    (inc-download-count)
    (save-download-count "/tmp/downloadcount.txt" download-count) 
    (str  download-count))))

;;;
;;; here is the main entry point, we should dispatch function call here
;;;
(defn count
"this is the entry point for the app, we will dispatch calls from here"
[req]
  {
    :status 200,
    :headers {"content-type" "text/plain"},
    :body  (response req)
    })


