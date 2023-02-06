(ns io.dominic.nrepl-bind
  (:require
   [nrepl.middleware :as middleware :refer [set-descriptor!]]))

(def ^:private bind-vars (atom #{}))
(defn add-bound-var
  [var]
  (swap! bind-vars conj var)
  nil)

(defmacro try-bind-vars
  [& syms]
  `(run! add-bound-var (keep requiring-resolve (quote ~syms))))

;; the handler
(defn- wrap-bind
  [h]
  (fn [{:keys [session] :as msg}]
    (swap!
      session
      (fn [session]
        (reduce
          (fn [session var]
            (cond-> session
              (not (contains? session var))
              (assoc var @var)))
          session
          @bind-vars)))
    (h msg)))

(set-descriptor! #'wrap-bind {:requires #{"clone"}})
