(ns file-counter.core
  (:use [file-counter.gui :only [create-layout]])
  (:gen-class))

;;;This might not be how to do it with the GUI, but
;;;it shows the basic procedure required.
;;;For a GUI version we'd probably have atoms for the various counts,
;;;bind them to fields in the GUI, and increment them as we pass iterate
;;;through the files, instead of just getting the counts in one go.
;;;Allows the user to see that progress is happening, and uses just one
;;;pass through the sequence, e.g. with doseq.
(defn count-files
  ([directory] (count-files directory nil))
  ([directory exclusions]
   (let [exclusions (set exclusions)
         directory (java.io.File. directory)
         files (filter #(.isFile %) (file-seq directory)) ]
     {:total    (count files)
      :excluded (count (filter #(contains? exclusions (.getName %)) files))})))

(defn -main [& args]
  (let [dir (first args)
        count (count-files dir (rest args))]
    (println "The directory " dir " has " (:total count) " files, of which "
             (:excluded count) " are excluded.")))

