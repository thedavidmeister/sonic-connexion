(ns twitter.discover
 (:require
  twitter.api))

(defn for-user!
 [username]
 (twitter.api/search!
  username))
