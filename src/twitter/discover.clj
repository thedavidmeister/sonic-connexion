(ns twitter.discover
 (:require
  twitter.api
  twitter.tweet))

(defn q->tweets!
 [q]
 (:statuses (twitter.api/search! q)))

(defn qs->tweets!
 [& qs]
 (q->tweets! (clojure.string/join " OR " (flatten qs))))

(defn hashtag->tweets!
 [hashtag]
 (q->tweets! (str "#" hashtag)))

(defn hashtags-for-hashtag!
 [hashtag]
 (twitter.tweet/tweets->hashtags
  (hashtag->tweets! hashtag)))

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
  (qs->tweets!
   (map
    #(str % username)
    ["from:" "to:" "@"]))))

(defn user->tweets!
 [username depth]
 (let [hs (hashtags-for-user! username)
       hs' (hashtags-for-hashtags! hs depth)]
  ; (pmap hashtag->tweets! hs')))
  hs'))
