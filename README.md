# sonic-connexion

Social media helpers

## This is all REPL based in boot

`$ boot repl`

Need to provide twitter key/secret in `.lein-env` as per environ like:

```clojure
{
  :twitter-key "abc123"
  :twitter-secret "xyz890"
}
```

## User -> Interesting tweets

Find tweets that might be interesting to a user, based on hashtags in their
timeline.

```clojure
(twitter.discover/user-report! "thedavidmeister")

; returns a lot... ending like this:

{:text
 "All six of my popular Potteries #sagas are now available in a new format and with stunning new covers. \"Kaine has a… https://t.co/vDHxKcNR1O",
 :retweet_count 27,
 :favorite_count 17,
 :hashtags ("sagas"),
 :url "https://twitter.com/MargaretKaine/status/991993020408061952"}
{:text
 "Announcement: The #ClojuTRE 2018 tickets are now for sale - go &amp; grab yours: https://t.co/REBejJlIZH #Clojure… https://t.co/jRSdFOA4gR",
 :retweet_count 29,
 :favorite_count 23,
 :hashtags ("clojutre" "clojure"),
 :url "https://twitter.com/clojuTRE/status/991997914774953991"}
{:text "Iridescent tube #openframeworks https://t.co/GeHYR9yIgW",
 :retweet_count 35,
 :favorite_count 243,
 :hashtags ("openframeworks"),
 :url "https://twitter.com/zachlieberman/status/992397462538211328"}
{:text
 "Over the past year, we've seen #VRdevs create some amazing experiences using #ReactVR. 1yr later and we're excited… https://t.co/S4mCBQw9tU",
 :retweet_count 52,
 :favorite_count 101,
 :hashtags ("vrdevs" "reactvr"),
 :url "https://twitter.com/Oculus_Dev/status/991724611934019584"})
 ```

Works like this:

- Search twitter for tweets to/from/@ given username by popularity and time
- Find all hashtags in those tweets
- Search twitter for each of these hashtags
- Find all hashtags in those tweets
- Remove all hashtags that only show up once
- Search twitter for each of these hashtags
- Remove all tweets without at least one favorite AND one retweet
- Sort by retweets
- Print out a report

Hits to twitter are sent in parallel and cached in memory to avoid hitting rate
limits.
