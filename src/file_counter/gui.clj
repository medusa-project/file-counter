(ns file-counter.gui
  (:use seesaw.core
        [seesaw.chooser :only [choose-file]]
        [file-counter.counter :only [count-root-directory root-directory directory-count total-file-count
                                     excluded-file-count included-file-count exclusions]]
        seesaw.mig))

(def directory-label "Dir label")
(def directory-button "Dir button")
(def exclusion-text-area "Exclusion text")
(def run-button "Run button")
(def directory-count-label "Dir count")
(def total-file-count-label "Total Files count")
(def excluded-file-count-label "Excluded Files count")
(def included-file-count-label "Valid Files count")

(defn create-layout []
  (mig-panel :items [[directory-button ""]
                     [directory-label "span wrap"]
                     [exclusion-text-area "span wrap"]
                     [run-button "wrap"]
                     ["Directories:" ""]
                     [directory-count-label "wrap"]
                     ["Total Files:" ""]
                     [total-file-count-label "wrap"]
                     ["Excluded Files:" ""]
                     [excluded-file-count-label "wrap"]
                     ["Included Files:" ""]
                     [included-file-count-label "wrap"]]))



