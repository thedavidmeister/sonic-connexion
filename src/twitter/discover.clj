(ns twitter.discover
 (:require
  twitter.api
  twitter.tweet))

(defn hashtags-for-user!
 [username]
 (twitter.tweet/tweets->hashtags
  (:statuses
   (twitter.api/search!
    (clojure.string/join
     " OR "
     (map
      #(str % username)
      ["from:" "to" "@"]))))))
