(ns file-counter.counter
  (:use [clojure.string :only [split-lines trim blank?]])
  (:import (java.nio.file Files LinkOption)))

(def root-directory (atom (java.io.File. ".")))
(def directory-count (atom 0))
(def total-file-count (atom 0))
(def excluded-file-count (atom 0))
(def included-file-count (atom 0))
(def exclusions (atom #{}))

(defn reset-counts []
  (doseq [atom [directory-count total-file-count excluded-file-count included-file-count]]
    (reset! atom 0)))

;;;Needed for some optional arguments in Java call below
(def link-options (into-array LinkOption nil))

;;;Return the subdirectories to facilitate recursive processing
(defn process-directory [path]
  (let [entries  (->> path
                      (.toFile)
                      (.listFiles)
                      (map #(.toPath %))
                      (filter #(not (Files/isSymbolicLink %))))
        directories (filter #(Files/isDirectory % link-options) entries)
        files (filter #(Files/isRegularFile % link-options) entries)
        excluded-files (filter #(contains? @exclusions (.toString (.getFileName %))) files)
        fc (count files)
        efc (count excluded-files)
        ifc (- fc efc)]
    (swap! directory-count #(+ % (count directories)))
    (swap! total-file-count #(+ % fc))
    (swap! excluded-file-count #(+ % efc))
    (swap! included-file-count #(+ % ifc))
    directories))

(defn count-directories [path-list]
  (when (not (empty? path-list))
    (recur `(~@(process-directory (first path-list)) ~@(rest path-list)))))

(defn count-root-directory []
  (reset-counts)
  (count-directories (list (.toPath @root-directory))))

(defn parse-exclusions [text]
  (->> text
       (split-lines)
       (map trim)
       (filter (complement blank?))
       (set)))