(ns server
  (:require [compojure.route :as route]
            [noir.session :as session]
				    [ring.util.response :as response])
  (:use [compojure.core :only [defroutes GET POST DELETE PUT]]
        [ring.adapter.jetty :only [run-jetty]]
        [main :only [main-page do-delete-destination]]
        [login :only [login-page do-login do-logout]]
        [register :only [register-page do-register]]
        [mongodb :only [insert-user insert-user-destination get-user-by-username delete-destination]]
        [destination :only [destination-page add-new-destination]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [ring.middleware.params :only [wrap-params]]))

(defroutes handler
  (GET "/" [] (main-page))
  (DELETE "/" [id] (do-delete-destination (Integer/valueOf id)))
  (GET "/login" [] (let [user (session/get :user)] (if-not user (login-page) (main-page))))
  (POST "/login" [username password] (do-login username password))
  (GET "/logout" [] (do (do-logout) (response/redirect "/")))
  (GET "/register" [] (let [user (session/get :user)] (if-not user (register-page) (main-page))))
  (POST "/register" [first-name last-name email username password confirm-password] (do-register first-name last-name email username password confirm-password)) 
  (GET "/newdestination" [] (let [user (session/get :user)] (if user (destination-page) (login-page))))
  (POST "/newdestination" [destination-name destination-price destination-description destination-image] (add-new-destination destination-name destination-price destination-description destination-image))
  (route/resources "/")
  (route/not-found "Page not found."))

 (def app
  (-> #'handler
    (wrap-reload)
    (wrap-params)
    (session/wrap-noir-flash)
    (session/wrap-noir-session)
    (wrap-stacktrace)))


 (defn start-jetty-server []
   (run-jetty #'app {:port 8000 :join? false})
   (println "\nWelcome to the Tourist Agency. Browse to http://localhost:8000 to get started!"))
 
 (defn insert-test-user [] 
  (insert-user "Test user" "test@test.com" "test" "test"))
 
 (defn insert-test-data [] 
   (let [user (get-user-by-username "test")]
     (do
       (insert-user-destination "Hawaii" "800 e" "Hawaii is the most recent of the 50 U.S. states (joined the Union on August 21, 1959), and is the only U.S. state made up entirely of islands. It is the northernmost island group in Polynesia, occupying most of an archipelago in the central Pacific Ocean.
Hawaii’s diverse natural scenery, warm tropical climate, abundance of public beaches, oceanic surroundings, and active volcanoes make it a popular destination for tourists, (wind) surfers, biologists, and volcanologists alike. Due to its mid-Pacific location, Hawaii has many North American and Asian influences along with its own vibrant native culture. Hawaii has over a million permanent residents along with many visitors and U.S. military personnel. Its capital is Honolulu on the island of Oʻahu.
The state encompasses nearly the entire volcanic Hawaiian Island chain, which comprises hundreds of islands spread over 1,500 miles (2,400 km). At the southeastern end of the archipelago, the eight \"main islands\" are (from the northwest to southeast) Niʻihau, Kauaʻi, Oʻahu, Molokaʻi, Lānaʻi, Kahoʻolawe, Maui and the island of Hawaiʻi. The last is the largest and is often called \"The Big Island\" to avoid confusing the name of the island with the name of the state as a whole. The archipelago is physiographically and ethnologically part of the Polynesian subregion of Oceania.
Hawaii is the 8th smallest, the 11th least populous, but the 13th most densely populated of the 50 U.S. states. Hawaii's ocean coastline is approximately 750 miles (1,210 km) long, which is fourth in the United States after those of Alaska, Florida and California." "images/img1.jpg" "2014-02-27" (:_id user))
       (insert-user-destination "Aruba" "700 e" "Aruba is an island 33 kilometre long (20 mi) located about 1,600 kilometres (990 mi) west of the Lesser Antilles in the southern Caribbean Sea, located 27 kilometres (17 mi) north of the coast of Venezuela. Together with Bonaire and Curaçao, it forms a group referred to as the ABC islands. Collectively, Aruba and the other Dutch islands in the Caribbean are often called the Netherlands Antilles or the Dutch Caribbean.
Aruba is one of the four constituent countries that form the Kingdom of the Netherlands, along with the Netherlands, Curaçao and Sint Maarten. The citizens of these countries all share a single nationality: Dutch. Aruba has no administrative subdivisions, but, for census purposes, is divided into eight regions. Its capital is Oranjestad.
Unlike much of the Caribbean region, Aruba has a dry climate and an arid, cactus-strewn landscape. This climate has helped tourism as visitors to the island can reliably expect warm, sunny weather. It has a land area of 179 km2 (69.1 sq mi) and is densely populated, with a total of 102,484 inhabitants at the 2010 Census. It lies outside the hurricane belt." "images/img2.jpg" "2014-02-27" (:_id user))
       (insert-user-destination "Ibiza" "600 e" "Ibiza is an island in the Mediterranean Sea, 79 kilometres (49 miles) off the coast of the city of Valencia, in eastern Spain. It is the third largest of the Balearic Islands, an autonomous community of Spain. Its largest cities are Ibiza Town (Catalan: Vila d'Eivissa, or simply Vila), Santa Eulària des Riu, and Sant Antoni de Portmany. Its highest point, called Sa Talaiassa (or Sa Talaia), is 475 metres (1,558 feet) above sea level.
While it is one-sixth the size of nearby Majorca, Ibiza is over five times the size of Mykonos (Greece), or ten times the size of Manhattan Island. Ibiza has become famous for the association with nightlife and the electronic music that originated on the island. It is well known for its summer club scene which attracts very large numbers of tourists, though the island's government and the Spanish Tourist Office have controversially been working to promote more family-oriented tourism. Noted clubs include Space, Privilege, Amnesia, Ushuaïa Ibiza Beach Hotel, Pacha, DC10, Eden, and Es Paradis." "images/img3.jpg" "2014-02-27" (:_id user))
       (insert-user-destination "Mykonos" "500 e" "Mykonos is a Greek island, part of the Cyclades, lying between Tinos, Syros, Paros and Naxos. The island spans an area of 85.5 square kilometres (33.0 sq mi) and rises to an elevation of 341 metres (1,119 feet) at its highest point. There are 10,134 inhabitants (2011 census) most of whom live in the largest town, Mykonos, which lies on the west coast. The town is also known as Chora (i.e. the Town in Greek, following the common practice in Greece when the name of the island itself is the same as the name of the principal town).
Mykonos' nickname is The island of the winds. Tourism is a major industry." "images/img4.jpg" "2014-02-27" (:_id user)))))

 (defn -main [& args]
   (do
     (start-jetty-server)
      (let [user (get-user-by-username "test")]
       (if-not user (do (insert-test-user) (insert-test-data))))))
 