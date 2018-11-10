(ns word-search-kata.core
  (:gen-class)
  (:require [clojure.string :as str]))

(defn within-bounds? [grid [col row]]
  (let [dimension (count grid)]
    (and (not (neg? col))
         (not (neg? row))
         (< col dimension)
         (< row dimension))))

(defn valid-position? [grid position]
  (within-bounds? grid position))

(defn get-row [grid row]
  (nth grid row))

(defn get-col [row-array col]
  (nth row-array col))

(defn get-char [string]
  (first string))

(defn get-letter [grid [col row :as position]]
  (when (valid-position? grid position)
    (-> grid
        (get-row row)
        (get-col col)
        (get-char))))

(defn position-has-letter? [grid letter position]
  (= letter (get-letter grid position)))

(defn next-pos-up [[col row]]
  [col (dec row)])

(defn next-pos-down [[col row]]
  [col (inc row)])

(defn next-pos-forward [[col row]]
  [(inc col) row])

(defn next-pos-backwards [[col row]]
  [(dec col) row])

(defn next-pos-forward-up [[col row]]
  [(inc col) (dec row)])

(defn next-pos-forward-down [[col row]]
  [(inc col) (inc row)])

(defn next-pos-backwards-up [[col row]]
  [(dec col) (dec row)])

(defn next-pos-backwards-down [[col row]]
  [(dec col) (inc row)])

(defn match? [coords word]
  (= (count coords) (count word)))

(defn try-match [grid word position next-pos-fn]
  (loop [letter-index 0
         pos position
         coords [position]]
    (if (match? coords word)
      coords
      (let [next-letter-index (inc letter-index)
            next-letter (get word next-letter-index)
            next-position (next-pos-fn pos)]
        (when (position-has-letter? grid next-letter next-position)
          (recur next-letter-index next-position (conj coords next-position)))))))

(defn try-match-up [grid word position]
  (try-match grid word position next-pos-up))

(defn try-match-down [grid word position]
  (try-match grid word position next-pos-down))

(defn try-match-forward [grid word position]
  (try-match grid word position next-pos-forward))

(defn try-match-backwards [grid word position]
  (try-match grid word position next-pos-backwards))

(defn try-match-diag-forward-up [grid word position]
  (try-match grid word position next-pos-forward-up))

(defn try-match-diag-forward-down [grid word position]
  (try-match grid word position next-pos-forward-down))

(defn try-match-diag-backwards-up [grid word position]
  (try-match grid word position next-pos-backwards-up))

(defn try-match-diag-backwards-down [grid word position]
  (try-match grid word position next-pos-backwards-down))

(defn try-match-vertically [grid word position]
  (or (try-match-up grid word position)
      (try-match-down grid word position)))

(defn try-match-horizontally [grid word position]
  (or (try-match-forward grid word position)
      (try-match-backwards grid word position)))

(defn try-match-diagonally [grid word position]
  (or (try-match-diag-forward-up grid word position)
      (try-match-diag-forward-down grid word position)
      (try-match-diag-backwards-up grid word position)
      (try-match-diag-backwards-down grid word position)))

(defn try-match-word [grid word position]
  (or (try-match-vertically grid word position)
      (try-match-horizontally grid word position)
      (try-match-diagonally grid word position)))

(defn next-position [dimension [col row]]
  (if (= (inc col) dimension)
    [0 (inc row)]
    [(inc col) row]))

(defn fill-word-coordinates [result word grid]
  (let [letter (first word)
        dimension (count grid)]
    (loop [position [0 0]]
      (if (position-has-letter? grid letter position)
          (if-let [coordinates (try-match-word grid word position)]
            (assoc result word coordinates)
            (recur (next-position dimension position)))
          (recur (next-position dimension position))))))



;;; ----- Stringify result -----


(defn coord->str [[col row]]
  (str "(" col "," row ")"))

(defn word-coords->str [[word coords]]
  (str word ": " (str/join #"," (map coord->str coords))))

(defn stringify-result [result]
  (str/join "\n" (map word-coords->str result)))


;;; ----------------------------


(defn parse-lines [input]
  (str/split-lines input))

(defn to-letter-grid [lines]
  (map #(str/split % #",") (drop 1 lines)))

(defn parse-words [lines]
  (str/split (first lines) #","))

(defn find-words [grid words]
  (reduce #(fill-word-coordinates %1 %2 grid) {} words))

(defn search [input]
  (let [lines (parse-lines input)
        grid (to-letter-grid lines)
        words (parse-words lines)
        result (find-words grid words)]
    (stringify-result result)))
