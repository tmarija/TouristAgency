(ns mongodb
  (:require [noir.session :as session]
            [clj-time.format :as time-format]
            [clj-time.core :as time])
  (:use [somnium.congomongo]))

(def conn 
  (make-connection "tourist_agency"))

(set-connection! conn)

(defn- generate-id [collection]
  "Generate entity identifier." 
  (:seq (fetch-and-modify :sequences {:_id collection} {:$inc {:seq 1}}
                          :return-new? true :upsert? true)))

(defn- insert-entity [collection values]
   "Insert an entity into database."
  (insert! collection (assoc values :_id (generate-id collection))))

(defn insert-user
  [name email username password]
  "Insert user into database." 
  (insert-entity :users 
                  {:name name
                   :email email
                   :username username
                   :password password}))

(defn get-user-by-username [username]
  "Find user by username."  
  (fetch-one :users :where {:username username}))

(defn get-user-by-email [email]
  "Find user by email."  
  (fetch-one :users :where {:email email}))

(defn insert-user-destination
  [destination-name destination-price destination-description destination-image date-added user-id]
  "Insert destination into database." 
  (insert-entity :user-destinations 
                  {:destination-name destination-name
                   :destination-price destination-price
                   :destination-description destination-description
                   :destination-image destination-image
                   :date-added date-added
                   :user-id user-id}))

(defn get-all-destinations []
  "Return all destinations from the database." 
  (fetch :user-destinations :sort {:destination-price -1}))

(defn get-latest-destinations []
  "Find the latest three destinations." 
  (fetch :user-destinations :sort {:date-added -1} :limit 3))

(def parser-formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss"))

(defn insert-new-destination
  "Inserts data for new destination into data base."
  [destination-name destination-price destination-description destination-image]
  (let [user (session/get :user)]
    (insert-entity :user-destinations
                   {:destination-name destination-name
                   :destination-price destination-price
                   :destination-description destination-description
                   :destination-image destination-image
                   :date-added (time-format/unparse parser-formatter (time/now))
                   :user-id (:_id user)})))

(defn delete-destination [id]
  "Deletes destination from the database."
  (destroy! :user-destinations {:_id id}))