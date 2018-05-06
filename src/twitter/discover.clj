(ns twitter.discover
 (:require
  twitter.api
  twitter.tweet))

(defn for-user!
 [username]
 (let [user-tweets
       (:statuses
        (twitter.api/search! username))
       user-hashtags (twitter.tweet/tweets->hashtags user-tweets)]
  user-hashtags))
