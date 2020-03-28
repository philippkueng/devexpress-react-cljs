(ns shadow-re-frame.devexpress
  (:require ["devextreme-react/data-grid.js" :as devextreme-react-data-grid]
            [reagent.core :as r]))


;; mapping of devexpress components
(def column (r/adapt-react-class (aget devextreme-react-data-grid "Column")))
(def column-chooser (r/adapt-react-class (aget devextreme-react-data-grid "ColumnChooser")))
(def column-fixing (r/adapt-react-class (aget devextreme-react-data-grid "ColumnFixing")))
(def data-grid (r/adapt-react-class (aget devextreme-react-data-grid "DataGrid")))
(def grouping (r/adapt-react-class (aget devextreme-react-data-grid "Grouping")))
(def group-panel (r/adapt-react-class (aget devextreme-react-data-grid "GroupPanel")))
(def pager (r/adapt-react-class (aget devextreme-react-data-grid "Pager")))
(def paging (r/adapt-react-class (aget devextreme-react-data-grid "Paging")))
(def search-panel (r/adapt-react-class (aget devextreme-react-data-grid "SearchPanel")))
(def sorting (r/adapt-react-class (aget devextreme-react-data-grid "Sorting")))

