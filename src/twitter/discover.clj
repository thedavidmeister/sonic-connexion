(ns twitter.discover
 (:require
  twitter.api
  twitter.tweet))

(defn q->tweets!
 [q]
 (:statuses (twitter.api/search! q)))

(defn hashtags-for-hashtag!
 [hashtag]
 (twitter.tweet/tweets->hashtags
  (q->tweets! (str "#" hashtag))))

(defn hashtags-for-user!
 [username]
 (twitter.tweet/tweets->hashtags
  (q->tweets!
   (clojure.string/join
    " OR "
    (map
     #(str % username)
     ["from:" "to" "@"])))))
