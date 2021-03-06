(ns elso.core
  (:require
    [elso.feladatok :as tasks]
    [reagent.core :as reagent :refer [atom]]
    [reagent.dom :as rdom]
    [reagent.session :as session]
    [reitit.frontend :as reitit]
    [clerk.core :as clerk]
    [accountant.core :as accountant]))

;; -------------------------
;; gombok létrehozása
(def actual-theme (atom "dark-theme"))

(defn gomb [theme label]
      [:button.gomb
       {:on-click
        (fn [katt] (reset! actual-theme theme))}
       label])

(defn gombok []
      [:div
       ;[:div @actual-theme]
       [:div.gombok

        [gomb "light-theme" "Light theme"]
        [gomb "dark-theme" "Dark theme"]]])

(defn practice []
      [:div
       [gombok]])



;; -------------------------
;; Routes

(def router
  (reitit/router
    [["/" :index]
     ["/items"
      ["" :items]
      ["/:item-id" :item]]
     ["/about" :about]
     ["/tasks" :tasks]]))

(defn path-for [route & [params]]
      (if params
        (:path (reitit/match-by-name router route params))
        (:path (reitit/match-by-name router route))))

;; -------------------------
;; Page components

(defn home-page []
      (fn []
          [:span.main
           [:h1 "hii"]
           [practice]
           [:ul
            [:li [:a {:href (path-for :items)} "Items of elso"]]
            [:li [:a {:href "/broken/link"} "Broken link"]]]]))



(defn items-page [])



(defn item-page []
      (fn []
          (let [routing-data (session/get :route)
                item (get-in routing-data [:route-params :item-id])]
               [:span.main
                [:h1 (str "Item " item " of elso")]
                [:p [:a {:href (path-for :items)} "Back to the list of items"]]])))


(defn about-page []
      (fn [] [:span.main
              [:h1 "About elso"]]))

(defn task-page []
      (fn [] [:span.main
              [:h1 "Feladatok"]
              [tasks/feladatok]]))


;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
      (case route
            :index #'home-page
            :about #'about-page
            :items #'items-page
            :item #'item-page
            :tasks #'task-page))


;; -------------------------
;; Page mounting component

(defn current-page []
      (fn []
          (let [page (:current-page (session/get :route))]
               [:div.page-container {:class @actual-theme}
                [:div.page-inner
                 [:header
                  [:p [:a {:href (path-for :index)} "Home"] " | "
                   [:a {:href (path-for :tasks)} "Tasks"]]]
                 [page]
                 [:footer
                  [:p "This shit made by duplajezus "
                   [:a
                    {:href "https://www.instagram.com/duplajezus/"}
                    "Me :)"]
                   [:img {:src "https://i1.sndcdn.com/artworks-0BjzfXYQ6U974KcS-HHIL4Q-t500x500.jpg"
                          :width "200px"
                          :height "200px"}]]]]])))

;; -------------------------
;; Initialize app

(defn mount-root []
      (rdom/render [current-page] (.getElementById js/document "app")))

(defn init! []
      (clerk/initialize!)
      (accountant/configure-navigation!
        {:nav-handler
         (fn [path]
             (let [match (reitit/match-by-path router path)
                   current-page (:name (:data match))
                   route-params (:path-params match)]
                  (reagent/after-render clerk/after-render!)
                  (session/put! :route {:current-page (page-for current-page)
                                        :route-params route-params})
                  (clerk/navigate-page! path)))

         :path-exists?
         (fn [path]
             (boolean (reitit/match-by-path router path)))})
      (accountant/dispatch-current!)
      (mount-root))

