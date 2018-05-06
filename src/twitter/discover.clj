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

(defn normalize-hashtag
 [hashtag]
 (clojure.string/lower-case hashtag))

(def tweet->hashtags
 (comp
  (partial map (comp normalize-hashtag :text))
  :hashtags
  :entities))

(defn tweets->hashtags
 [tweets]
 (flatten (map tweet->hashtags tweets)))

(defn hashtag->tweets!
 [hashtag]
 (q->tweets! (str "#" hashtag)))

(defn hashtags-for-hashtag!
 [hashtag]
 (tweets->hashtags
  (hashtag->tweets! hashtag)))

(defn hashtags-for-hashtags!
 ([hashtags]
  (pmap hashtags-for-hashtag! hashtags))
 ([hashtags depth]
  (loop [hs hashtags
         i 0]
   (if (< i depth)
    (recur
     (flatten (hashtags-for-hashtags! hs))
     (inc i))
    (flatten hs)))))

(defn hashtags-for-user!
 [username]
 (tweets->hashtags
  (qs->tweets!
   (map
    #(str % username)
    ["from:" "to:" "@"]))))

(defn user->tweets!
 [username depth]
 (let [hs (hashtags-for-user! username)
       hs' (hashtags-for-hashtags! hs depth)
       multiples-only
       (remove
        #(= 1 (val %))
        (frequencies hs'))]
  (pmap hashtag->tweets! (map key multiples-only))))
