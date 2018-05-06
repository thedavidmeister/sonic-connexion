(ns twitter.api
 (:require
  org.httpkit.client
  twitter.data
  cheshire.core
  taoensso.timbre
  twitter.spec
  [clojure.spec.alpha :as spec]))

(defn -fetch-token!
 [client-key client-secret]
 {:post [(spec/valid? :twitter.token/token %)]}
 (let [response @(org.httpkit.client/post
                  twitter.data/token-url
                  {:basic-auth [client-key client-secret]
                   :form-params {"grant_type" "client_credentials"}})]
  (if (= 200 (:status response))
   (cheshire.core/parse-string
    (:body response)
    true)
   (taoensso.timbre/error (:status response) (:body response)))))
(def fetch-token! (memoize -fetch-token!))
