(ns file-counter.counter
  (:use [clojure.string :only [split-lines trim blank?]]))

(def root-directory (atom (java.io.File. ".")))
(def directory-count (atom 0))
(def total-file-count (atom 0))
(def excluded-file-count (atom 0))
(def included-file-count (atom 0))
(def exclusions (atom #{}))

(defn reset-counts []
  (doseq [atom [directory-count total-file-count excluded-file-count included-file-count]]
    (reset! atom 0)))

(defn count-directory []
  (swap! directory-count inc'))

(defn count-file [file]
  (swap! total-file-count inc')
  (if (contains? @exclusions (.getName file))
    (swap! excluded-file-count inc')
    (swap! included-file-count inc')))

(defn count-entry [entry]
  (if (.isDirectory entry)
    (count-directory)
    (count-file entry)))

(defn count-root-directory []
  (reset-counts)
  (doseq [entry (file-seq @root-directory)]
    (count-entry entry)))

(defn parse-exclusions [text]
  (->> text
       (split-lines)
       (map trim)
       (filter (complement blank?))
       (set)))