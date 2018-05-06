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

(defn hashtags-for-hashtags!
 ([hashtags]
  (apply clojure.set/union (pmap hashtags-for-hashtag! hashtags)))
 ([hashtags depth]
  (loop [hs hashtags
         i 0]
   (if (< i depth)
    (recur
     (hashtags-for-hashtags! hs)
     (inc i))
    hs))))

(defn hashtags-for-user!
 [username]
 (twitter.tweet/tweets->hashtags
  (q->tweets!
   (clojure.string/join
    " OR "
    (map
     #(str % username)
     ["from:" "to" "@"])))))
