(ns twitter.spec
 (:require
  [clojure.spec.alpha :as spec]))

(spec/def :twitter.token/token_type #{"bearer"})
(spec/def :twitter.token/access_token string?)

(spec/def :twitter.token/token
 (spec/keys
  :req-un [:twitter.token/token_type
           :twitter.token/access_token]))
