(set-env!
 :dependencies
 '[
   [org.clojure/clojure "1.10.0-alpha4"]
   [samestep/boot-refresh "0.1.0-20160620.111804-1"]
   [http-kit "2.3.0"]
   [cheshire "5.8.0"]
   [com.taoensso/timbre "4.10.0"]
   [environ "1.1.0"]
   [funcool/cuerdas "2.0.5"]
   [medley "1.0.0"]]

 :source-paths   #{"src"})

(require
 '[samestep.boot-refresh :refer [refresh]])

; REPL

(deftask repl-server
 []
 (comp
  (watch)
  (refresh)
  (repl :server true)))

(deftask repl-client
 []
 (repl :client true))

; Convenience wrappers

(require
 'twitter.discover)

(deftask user-report
 [u user USER str "the twitter user to run a report for"]
 (twitter.discover/user-report! user))
