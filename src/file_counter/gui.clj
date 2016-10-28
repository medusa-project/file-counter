(ns file-counter.gui
  (:use seesaw.core
        [seesaw.chooser :only [choose-file]]
        seesaw.mig
        [seesaw.bind :only [bind property transform notify-later]]
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
  (text :multi-line? true
        :text ".DS_Store\nThumbs.db"
        :rows 10
        :columns 40))
(def run-button
  (button :text "Run"))
(def directory-count-label (label :text "<Dir count>"))
(def total-file-count-label (label :text "<Total Files count>"))
(def excluded-file-count-label (label :text "<Excluded Files count>"))
(def included-file-count-label (label :text "<Valid Files count>"))

;;;; Wiring

(bind root-directory (transform #(.toString %)) (property directory-label :text))
(listen directory-button
        :action (fn [e] (when-let [new-dir (choose-file directory-button :selection-mode :dirs-only)]
                          (reset! root-directory new-dir))))

(bind directory-count directory-count-label)
(bind total-file-count total-file-count-label)
(bind excluded-file-count excluded-file-count-label)
(bind included-file-count included-file-count-label)
(bind exclusion-text-area (transform #(parse-exclusions %)) exclusions)

;;;I don't know why doing the reset of the atoms inside count-root-directory doesn't reset these because of the bind, but
;;;it doesn't seem to happen. So we reset the labels manually as part of the process.
(defn reset-labels []
  (doseq [label [directory-count-label total-file-count-label excluded-file-count-label included-file-count-label]]
    (config! label :text "0")))

(listen run-button
        :action (fn [e]
                  (reset-labels)
                  (future (count-root-directory))))

;;;; Create layout and application

(defn create-layout []
  (mig-panel :items [[directory-button ""]
                     [directory-label "span, wrap"]
                     [(scrollable exclusion-text-area) "span, wrap"]
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
  (reset! exclusions (parse-exclusions (.getText exclusion-text-area)))
  (invoke-later
    (-> (frame :title "File Counter",
               :content (create-layout),
               :on-close :exit)
        pack!
        show!)))

