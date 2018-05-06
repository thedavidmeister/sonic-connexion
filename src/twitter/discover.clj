(ns twitter.discover
 (:require
  twitter.api
  cuerdas.core
  medley.core))

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

(defn user->interesting-tweets!
 [username depth]
 (let [hs (hashtags-for-user! username)
       hs' (hashtags-for-hashtags! hs depth)
       ; remove any hashtags that only appear once
       multiples-only
       (remove
        #(= 1 (val %))
        (frequencies hs'))
       tweets (flatten (pmap hashtag->tweets! (map key multiples-only)))
       favourites (partial remove (comp zero? :favorite_count))
       retweets (partial remove (comp zero? :retweet_count))]
  (->> tweets
   favourites
   retweets)))

(defn tweet->url
 [tweet]
 (str
  "https://twitter.com/"
  (-> tweet :user :screen_name)
  "/status/"
  (-> tweet :id)))

(defn tweet-report
 [tweets]
 (let [row-fn (fn [tweet]
               (-> tweet
                (select-keys [:text :retweet_count :favorite_count])
                (update :text cuerdas.core/clean)
                (assoc :hashtags (tweet->hashtags tweet))
                (assoc :url (tweet->url tweet))))
       ; putting the highest at the bottom actually makes it easier to read at
       ; the REPL
       sort-fn (partial sort-by :retweet_count)]
  (clojure.pprint/pprint
   (map
    row-fn
    (sort-fn (medley.core/distinct-by :id tweets))))))

(defn user-report!
 [username]
 (tweet-report
  (user->interesting-tweets! username 1)))
