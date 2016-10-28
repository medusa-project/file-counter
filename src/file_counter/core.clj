(ns file-counter.core
  (:use [file-counter.gui :only [show-gui]])
  (:gen-class))

;(defn -main [& args]
;  (let [dir (first args)
;        count (count-files dir (rest args))]
;    (println "The directory " dir " has " (:total count) " files, of which "
;             (:excluded count) " are excluded.")))

(defn -main [& args]
  (show-gui))