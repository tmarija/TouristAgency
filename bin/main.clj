(ns main
   (:require [noir.session :as session]
            [ring.util.response :as response])
  (:use  [template :only [get-template]]
         [hiccup.form :only [form-to hidden-field submit-button]]
         [mongodb :only [get-all-destinations get-latest-destinations get-user-by-username delete-destination]]))

(defn logged-delete
  "Generates HTML for delete if user is logged."
  [destination]
  [:div 
    (form-to [:delete "/"]
      [:div
       (hidden-field :id (destination :_id))
       (submit-button {:class "button"} "Delete destination")])])

(defn show-one-destination
  "Generates HTML for one destination."
  [destination]
  [:div.destinationinfo
   [:h2 (:destination-name destination)]
   [:p.destinationprice (str ""(:destination-price destination))]
   [:img {:src (:destination-image destination)}]
    (str ""(:destination-description destination))
    (let [user (session/get :user)] 
     (if-not user () (logged-delete destination)))])
     
  
(defn show-all-destinations
  "Retrieves all destinations from database and displays them."
  []
  [:div.destinationinfo
   (let [destinations (get-all-destinations)]
   (for [destination destinations]
		(show-one-destination destination)))])

(defn show-one-destination-latest
  "Generates HTML for one destination."
  [destination]
  [:li
    [:h2.name (:destination-name destination)]
    [:img {:class "destinationimage" :src  (:destination-image destination)}]
    [:h2 (:destination-price destination)]])
  
(defn get-new-destinations
  "Retrieves latest destinations and displays them."
  []
  [:ul
   (let [destinations (get-latest-destinations)]
   (for [destination destinations]
		(show-one-destination-latest destination)))])

(defn do-delete-destination 
  "Delete destination."
  [id]
  (do
    (delete-destination id)
    (session/flash-put! :message-info "Successfully deleted destination.")
    (response/redirect "/")))

(defn main-page 
  "Displays main page."
  []
  (get-template "Tourist Agency" 
   [:div.content
    [:div#latest
           [:h1 "New destinations"]
           (get-new-destinations)]
    [:p.message (session/flash-get :message-info)]
    (show-all-destinations)]))

