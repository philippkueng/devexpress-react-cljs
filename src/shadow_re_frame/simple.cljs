(ns shadow-re-frame.simple
  "Example of `re-frame-simple`, an alternate `re-frame` syntax for simple use cases."
  (:require
   [re-view.re-frame-simple :as db]
   [reagent.core :as reagent]
   [reagent.dom :as dom]
   [shadow-re-frame.welcome :as text]
   [shadow-re-frame.devexpress :as devexpress]))

;;
;; For a complete introduction to `re-view.re-frame-simple`, see the readme:
;; https://github.com/braintripping/re-view/blob/master/re-frame-simple/README.md
;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; A COUNTER
;;
;; Example of...
;;
;; 1. Reading data using `db/get-in`
;;
;; 2. Writing data using `db/update-in!`
;;
;;


(defn counter
  "Given a counter id, render it as an interactive widget."
  [id]

  ;; NOTICE: `db/get-in`
  (let [total (db/get-in [:counters id])]

    ;; NOTICE: `db/update-in!`
    [:div.button {:on-mouse-down #(do
                                    (.preventDefault %)
                                    (db/update-in! [:counters id] inc))}
     total
     [:br]
     (if (pos? total)
       (take total (repeat id))
       [:span {:style {:color "#888"}} "click me!"])]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; NAMED UPDATES
;;
;; `defupdate` associates a keyword with an update function.
;;  this can be dispatched like any other re-frame handler.
;;

(db/defupdate :initialize
              "Initialize the `db` with the preselected emoji as counter IDs."
              [db]
              {:counter-ids (shuffle ["👹" "👺" "💩" "👻💀️"
                                       "👽" "👾" "🤖" "🎃"
                                       "😺" "👏" "🙏" "👅"
                                       "👂" "👃" "👣" "👁"
                                       "👀" "👨‍" "🚒" "👩‍✈️"
                                       "👞" "👓" "☂️" "🎈"
                                       "📜" "🏳️‍🌈" "🚣" "🏇"])})

(db/defupdate :new-counter
              "Create a new counter, using an ID from the pre-selected emoji."
              [db]
              (-> db
                  (assoc-in [:counters (peek (:counter-ids db))] 0)
                  (update :counter-ids pop)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Named queries
;;
;; use `defquery` to create named queries that read data using
;; `db/get` and `db/get-in`.
;;
;; `defquery` def's an ordinary Clojure function:
;;


(db/defquery counter-ids
             "Return the list of counters in the db, by id."
             []
             (-> (db/get :counters)
                 (keys)))

;;
;; a component that uses the query will update when its data changes.
;;

(def divider [:div.font-large
              {:style {:margin "2rem 0 1rem"}}
              "〰️〰️〰️〰️〰️〰️〰️"])

(defn basic-table []
  [:div
   [:h1 "Basic table"]
   (let [rows (for [item (range 12)]
                {:product (str "Product " item)
                 :region (nth ["APAC" "US" "EUROPE"] (rand-int 3))
                 :amount (rand-int 100)
                 :saleDate "22.12.2019"
                 :customer "John"
                 :url (str "https://example.org/product" item)
                 :id item
                 :active (if (even? (rand-int 2))
                           "true"
                           "false")})
         columns [{:name "id" :title "ID"}
                  {:name "product" :title "Product"}
                  {:name "region" :title "Region"}
                  {:name "amount" :title "Amount"}
                  {:name "saleDate" :title "Sale Date"}
                  {:name "customer" :title "Customer"}
                  {:name "url" :title "URL"}
                  {:name "active" :title "Active"}]]
     [devexpress/data-grid {:dataSource (clj->js rows)
                            :defaultColumns (clj->js ["id"
                                                      "product"
                                                      "region"
                                                      "amount"
                                                      "saleDate"
                                                      "customer"
                                                      "url"
                                                      "active"])
                            :showBorders true}])])

(defn basic-table-with-sorting-and-paging []
  [:div
   [:h1 "Basic table with sorting and paging"]
   (let [rows (for [item (range 12)]
                {:product (str "Product " item)
                 :region (nth ["APAC" "US" "EUROPE"] (rand-int 3))
                 :amount (rand-int 100)
                 :saleDate "22.12.2019"
                 :customer "John"
                 :url (str "https://example.org/product" item)
                 :id item
                 :active (if (even? (rand-int 2))
                           "true"
                           "false")})]
     [devexpress/data-grid {:dataSource (clj->js rows)
                            :showBorders true}
      [devexpress/paging {:defaultPageSize 10}]
      [devexpress/pager {:showPageSizeSelector true
                         :allowedPageSizes (clj->js [5, 10, 20])}]
      [devexpress/sorting {:mode "multiple"}]
      [devexpress/column {:dataField "id"
                          :caption "ID"
                          :width 50
                          :defaultSortOrder "desc"}]
      [devexpress/column {:dataField "product"
                          :caption "Product"}]
      [devexpress/column {:dataField "region"
                          :caption "Region"}]
      [devexpress/column {:dataField "amount"
                          :caption "Amount"
                          :width 70}]
      [devexpress/column {:dataField "saleDatet"
                          :caption "Sale Date"}]
      [devexpress/column {:dataField "customer"
                          :caption "Customer"}]
      [devexpress/column {:dataField "url"
                          :caption "URL"}]
      [devexpress/column {:dataField "active"
                          :caption "Active"}]])])

(defn basic-table-with-column-chooser []
  [:div
   [:h1 "Basic table with column chooser and custom cell render function"]
   (let [rows (for [item (range 12)]
                {:image "https://images.unsplash.com/photo-1513757378314-e46255f6ed16?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2250&q=80"
                 :product (str "Product " item)
                 :subProducts (for [sub-product (range (rand-int 5))]
                                {:name (str "Sub-Product " sub-product)})
                 :region (nth ["APAC" "US" "EUROPE"] (rand-int 3))
                 :amount (rand-int 100)
                 :saleDate "22.12.2019"
                 :customer "John"
                 :url (str "https://example.org/product" item)
                 :id item
                 :active (if (even? (rand-int 2))
                           "true"
                           "false")})]
     [devexpress/data-grid {:dataSource (clj->js rows)
                            :showBorders true}
      [devexpress/column-chooser {:enabled true
                                  :mode "select"
                                  :allowSearch true
                                  :title "Custom Title FTW!"}]
      [devexpress/paging {:defaultPageSize 10}]
      [devexpress/pager {:showPageSizeSelector true
                         :allowedPageSizes (clj->js [5, 10, 20])}]
      [devexpress/sorting {:mode "multiple"}]
      [devexpress/column {:dataField "id"
                          :caption "ID"
                          :width 50
                          :defaultSortOrder "desc"}]
      [devexpress/column {:dataField "image"
                          :caption "Image"
                          :cellRender (fn [data]
                                        (reagent/as-element [:img {:src (.-value data)
                                                                   :height 40
                                                                   :width 40}]))
                          :width 60}]
      [devexpress/column {:dataField "product"
                          :caption "Product"}]
      [devexpress/column {:dataField "subProducts"
                          :caption "Sub-Products"
                          :cellRender (fn [data-raw]
                                        (let [data (js->clj (.-value data-raw) :keywordize-keys true)]
                                          (reagent/as-element (if-not (= 0 (count data))
                                                                [:ul {:style {:padding-left 14
                                                                              :margin 0
                                                                              :list-style-type "circle"}}
                                                                 (for [sub-product data]
                                                                   [:li (:name sub-product)])]
                                                                [:span "No sub-products"]))))}]
      [devexpress/column {:dataField "region"
                          :caption "Region"}]
      [devexpress/column {:dataField "amount"
                          :caption "Amount"
                          :width 70}]
      [devexpress/column {:dataField "saleDate"
                          :caption "Sale Date"
                          :dataType "date"}]
      [devexpress/column {:dataField "customer"
                          :caption "Customer"}]
      [devexpress/column {:dataField "url"
                          :caption "URL"
                          :cellRender (fn [data]
                                        (reagent/as-element [:a {:href (.-value data)} "Link"]))}]
      [devexpress/column {:dataField "active"
                          :caption "Active"
                          :visible false}]])])

(defn root-view
  "Render the page"
  []
  [:div {:style {:background-color "#f5f5f5"
                 :height "100vh"
                 :width "100hw"
                 :paedding "20px"}}
   #_[basic-table]
   #_[basic-table-with-sorting-and-paging]
   [basic-table-with-column-chooser]])


;; data-grid
;; clicking on rows should be able to trigger re-frame events
;;  - at the same time we need buttons in the cell to trigger another event
;; - expand a data-grid row to show details within an entry
;; - render MUI chips into a grid cell (have it be deletable) -> TagBox
;; - export: export to excel, CSV, pdf?
;; - drag & drop to re-order rows
;; tree-list (for the filter key-value pairs)
;; data-grid editing
;; - dropdowns to change a value -> trigger a re-frame event
;; forms
;; - dropdowns
;; - validations & formatting
;;  - numbers, phone-numbers, postcode, etc.
;; maps
;;  - routing and locations (showing details for the locations, clustering)
;; charts
;; file-saver for creating the downloadable zip files

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Boilerplate code to get the page to render:

(defn ^:dev/after-load render []
  (dom/render [root-view]
    (js/document.getElementById "shadow-re-frame")))

(defn init []

  ;; initialize the db, create an example counter
  (db/dispatch [:initialize])
  (db/dispatch [:new-counter])
  ;; render to page
  (render))
