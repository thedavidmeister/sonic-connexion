(ns twitter.api
 (:require
  org.httpkit.client
  twitter.data
  cheshire.core
  taoensso.timbre
  twitter.spec
  environ.core
  [clojure.spec.alpha :as spec]))

(defn check-200-status
 [response]
 (when-not (= 200 (:status response))
  (taoensso.timbre/error (:status response) (:body response))))

(defn parse-body
 [response]
 (check-200-status response)
 (cheshire.core/parse-string (:body response) true))

(defn -fetch-token!
 ([]
  (-fetch-token!
   (environ.core/env :twitter-key)
   (environ.core/env :twitter-secret)))
 ([client-key client-secret]
  {:post [(spec/valid? :twitter.token/token %)]}
  (let [response @(org.httpkit.client/post
                   twitter.data/token-url
                   {:basic-auth [client-key client-secret]
                    :form-params {"grant_type" "client_credentials"}})]
   (parse-body response))))
(def fetch-token! (memoize -fetch-token!))

(defn with-auth!
 [params]
 (let [token (fetch-token!)]
  (assoc-in
   params
   [:headers "Authorization"]
   (str (:token_type token) " " (:access_token token)))))

(defn with-defaults
 [params]
 (update
  params
  :query-params
  #(merge
    {:count 100}
    %)))

(defn -search!
 ([q] (-search! q nil))
 ([q params]
  (let [response @(org.httpkit.client/get
                   twitter.data/search-url
                   (-> params
                    (assoc-in [:query-params :q] q)
                    with-auth!
                    with-defaults))]
   (taoensso.timbre/debug "Rate limit remaining:" (-> response :headers :x-rate-limit-remaining))
   (parse-body response))))
(def search! (memoize -search!))
