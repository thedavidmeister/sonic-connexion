(ns twitter.tweet)

(def tweet->hashtags
 (comp
  set
  (partial map :text)
  :hashtags
  :entities))

(defn tweets->hashtags
 [tweets]
 (apply
  clojure.set/union
  (map tweet->hashtags tweets)))
