# TouristAgency

This is a web application in Clojure wich uses libraries: Ring, Mongo-session, Compojure, lib-noir, Hiccup, clj-time, CongoMongo. In this application you can see destinations that the agency offers sorted by price (from highest to lowest). You can see their price, description and image. On the top of the main page you can see 3 latest destinations in offer. Logged user can add new destinations, as well as delete existing ones. Test account is initially inserted (username: test password: test) as well as some test data. You can register new accounts, too.

## Usage

Download and install Leiningen. http://leiningen-win-installer.djpowell.net/

Download and install MongoDB. http://www.mongodb.org/downloads

Start MongoDB.

Start your application - cd to project location - lein run

##References

Clojure programming, Chas Emerick, Brian Carper and Cristophe Grand

Developing and Deploying a Simple Clojure Web Application and A brief overview of the Clojure web stack for learning Ring, Compojure and Hiccup

CongoMongo library for using MongoDB with Clojure - mongo.clj

Hickory library for parsing HTML used to extract data from web page - extract_data.clj.

## License

Distributed under the Eclipse Public License, the same as Clojure.
