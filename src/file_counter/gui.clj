(ns file-counter.gui
  (:use seesaw.core
        [seesaw.chooser :only [choose-file]]
        seesaw.mig
        [seesaw.bind :only [bind property transform]]
        [file-counter.counter :only [count-root-directory root-directory
                                     directory-count total-file-count
                                     excluded-file-count included-file-count
                                     exclusions parse-exclusions]]))

;;;; Named controls

(def directory-label
  (label :text "<Chosen Directory>"))
(def directory-button
  (button :text "Choose Directory"))
(def exclusion-text-area
  (scrollable (text :multi-line? true
                    :text ".DS_Store\nThumbs.db"
                    :rows 10
                    :columns 40)))
(def run-button
  (button :text "Run"))
(def directory-count-label "<Dir count>")
(def total-file-count-label "<Total Files count>")
(def excluded-file-count-label "<Excluded Files count>")
(def included-file-count-label "<Valid Files count>")

;;;; Wiring

(bind root-directory (transform #(.toString %)) (property directory-label :text))
(listen directory-button
        :action (fn [e] (when-let [new-dir (choose-file directory-button :selection-mode :dirs-only)]
                          (reset! root-directory new-dir))))

(bind directory-count (transform #(.toString %)) (property directory-count-label :text))
;(bind total-file-count (property total-file-count-label :text))
;(bind excluded-file-count (property excluded-file-count-label :text))
;(bind included-file-count (property included-file-count-label :text))
(bind (property exclusion-text-area :text) (transform #(parse-exclusions %)) exclusions)
(listen run-button
        :action (fn [e] (count-root-directory)))


;;;; Create layout and application

(defn create-layout []
  (mig-panel :items [[directory-button ""]
                     [directory-label "span, wrap"]
                     [exclusion-text-area "span, wrap"]
                     [run-button "wrap"]
                     ["Directories:" ""]
                     [directory-count-label "wrap"]
                     ["Total Files:" ""]
                     [total-file-count-label "wrap"]
                     ["Excluded Files:" ""]
                     [excluded-file-count-label "wrap"]
                     ["Included Files:" ""]
                     [included-file-count-label "wrap"]]))

(defn show-gui []
  (native!)
  (invoke-later
    (-> (frame :title "File Counter",
               :content (create-layout),
               :on-close :exit)
        pack!
        show!)))

