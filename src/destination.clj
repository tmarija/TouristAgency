(ns destination
  (:require [noir.session :as session]
            [ring.util.response :as response])
  (:use [template :only [get-template]]
        [hiccup.form :only [form-to label text-field text-area submit-button]]
        [mongodb :only [insert-new-destination]]))

(defn check-destination-data
  "Checks destination data."
  [destination-name destination-price destination-description destination-image]
  (cond
    (> 5 (.length destination-name)) "Destination name must be at least 5 characters long."
    (> 1 (.length destination-price)) "Destination price must be at least 1 character long."
    (> 5 (.length destination-description)) "Destination description must be at least 5 characters long."
    (> 5 (.length destination-image)) "Destination image path must be at least 5 characters long."
    :else true))

(defn add-new-destination
  "Adds new destination."
  [destination-name destination-price destination-description destination-image]
  (let [destination-error-message (check-destination-data destination-name destination-price destination-description destination-image)]
    (if-not (string? destination-error-message)
      (do 
        (insert-new-destination destination-name destination-price destination-description destination-image)
        (response/redirect "/"))
      (do
        (session/flash-put! :destination-error destination-error-message)
        (session/flash-put! :destination-name destination-name)
        (session/flash-put! :destination-price destination-price)
        (session/flash-put! :destination-description destination-description)
        (session/flash-put! :destination-image destination-image)
        (response/redirect "/newdestination")))))
                
   
(defn destination-page
  "Show destination page"
  []
  (get-template "Destination page"
   [:div.content
    [:p.destinationtitle "Enter  information about new destination!"]
     [:p.destinationerror (session/flash-get :destination-error)]
    (form-to [:post "/newdestination"]
             [:div.newdestinationform
              [:div
              (label {:class "destinationlabel"} :destination-name "Destination name")
               (text-field :destination-name (session/flash-get :destination-name))]
              [:div
               (label {:class "destinationlabel"} :destination-price "Price")
                (text-field :destination-price (session/flash-get :destination-price))]
              [:div
               (label {:class "destinationlabel"} :destination-image "Path to destination image")
                (text-field :destination-image (session/flash-get :destination-image))]
              [:div
               (label {:class "destinationlabel desc"} :destination-description "Description")
                (text-area {:class "textarea"} :destination-description (session/flash-get :destination-description))]
               [:div
                (submit-button {:class "button"} "Save destination")]])]))
                
                                    
 
