(ns file-counter.counter)

(def root-directory (atom (.getCanonicalFile (java.io.File. "."))))
(def directory-count (atom 0))
(def total-file-count (atom 0))
(def excluded-file-count (atom 0))
(def included-file-count (atom 0))
(def exclusions (atom #{}))

(defn reset-counts []
  (doseq [directory-count total-file-count excluded-file-count included-file-count]
    #(reset! % 0)))

(def count-entry [entry]
  (if (.isDirectory entry)
    (count-directory)
    (count-file entry)))

(def count-directory []
  (swap! directory-count inc'))

(def count-file [file]
  (swap! total-file-count inc')
  (if (contains? @exclusions (.getName file))
    (swap! excluded-file-count inc')
    (swap! included-file-count inc')))

(def count-root-directory []
  (reset-counts)
  (doseq (file-seq root-directory) #(count-entry %)))
