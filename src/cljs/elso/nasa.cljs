(ns elso.nasa
  (:require [reagent.core :as reagent :refer [atom]]))







;Ági a NASA-nál dolgozik webfejlesztőként.
; Egyik napon a NASA úgy döntött,
; hogy a CSS kódjaikban található összes hexadecimális színkódot át szeretnék írni
; decimális RGB-formátumra. Írj egy konvertal nevű függvényt, amely egy hexadecimális
; színkódot (string) vár paraméterben, és visszaadja a színnek megfelelő decimális
; RGB-kódot rgb({vörös szín mennyisége}, {zöld szín mennyisége}, {kék szín mennyisége})
; formátumban!
(defn hex->dec [hex] (.parseInt js/window hex 16))

(defn konvertal [string-hexa]
      (let [[hex-0 hex-1 hex-2 hex-3 hex-4 hex-5 hex-6] string-hexa
            ;hex-1   (nth string-hexa 1)
            ;hex-2   (nth string-hexa 2)
            ;hex-3   (nth string-hexa 3)
            ;hex-4   (nth string-hexa 4)
            ;hex-5   (nth string-hexa 5)
            ;hex-6   (nth string-hexa 6)
            hex-1-2 (str hex-1 hex-2)
            hex-3-4 (str hex-3 hex-4)
            hex-5-6 (str hex-5 hex-6)
            r       (hex->dec hex-1-2)
            g       (hex->dec hex-3-4)
            b       (hex->dec hex-5-6)]
            ;hex->dec (fn [hex] (.parseInt js/window hex 16))]


           (str "rgb(" r "," g "," b")")))


(def hex-atom (atom ""))

(defn color-box [rgb-color]
      [:div {:style {:left 0
                     :top 0
                     :pointer-events "none"
                     :position "fixed"
                     :opacity  "0.9"
                     :height   "100vh"
                     :width    "100vw"
                     :background rgb-color}}])



(defn user-interface []
      [:div
       [:div (konvertal @hex-atom)]
       [color-box (konvertal @hex-atom)]
       [:input {:placeholder "Insert your hex!"
                :on-change (fn [event] (reset! hex-atom (.-value (.-target event))))}]])


(defn feladat []
      [:div
        [:h1 "nasa feladat"]
        [user-interface]
        (konvertal "#FF0077")])