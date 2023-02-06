(ns build
  (:require [juxt.pack.api :as pack]))

(defn jar
 [_]
 (pack/library))
