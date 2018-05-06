(set-env!
 :dependencies
 '[
   [org.clojure/clojure "1.10.0-alpha4"]
   [samestep/boot-refresh "0.1.0-20160620.111804-1"]
   [http-kit "2.3.0"]]

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
