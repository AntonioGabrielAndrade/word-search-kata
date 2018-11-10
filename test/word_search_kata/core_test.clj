(ns word-search-kata.core-test
  (:require [clojure.test :refer :all]
            [word-search-kata.core :refer :all]
            [clojure.string :as str]))

(def input1 (str "BONES,KHAN,KIRK,SCOTTY,SPOCK,SULU,UHURA\n"
                     "U,M,K,H,U,L,K,I,N,V,J,O,C,W,E\n"
                     "L,L,S,H,K,Z,Z,W,Z,C,G,J,U,Y,G\n"
                     "H,S,U,P,J,P,R,J,D,H,S,B,X,T,G\n"
                     "B,R,J,S,O,E,Q,E,T,I,K,K,G,L,E\n"
                     "A,Y,O,A,G,C,I,R,D,Q,H,R,T,C,D\n"
                     "S,C,O,T,T,Y,K,Z,R,E,P,P,X,P,F\n"
                     "B,L,Q,S,L,N,E,E,E,V,U,L,F,M,Z\n"
                     "O,K,R,I,K,A,M,M,R,M,F,B,A,P,P\n"
                     "N,U,I,I,Y,H,Q,M,E,M,Q,R,Y,F,S\n"
                     "E,Y,Z,Y,G,K,Q,J,P,C,Q,W,Y,A,K\n"
                     "S,J,F,Z,M,Q,I,B,D,B,E,M,K,W,D\n"
                     "T,G,L,B,H,C,B,E,C,H,T,O,Y,I,K\n"
                     "O,J,Y,E,U,L,N,C,C,L,Y,B,Z,U,H\n"
                     "W,Z,M,I,S,U,K,U,R,B,I,D,U,X,S\n"
                     "K,Y,L,B,Q,Q,P,M,D,F,C,K,E,A,B"))

(def input2 (str "DRACO,DUMBLEDORE,HARRY,HERMIONE,RON,SNAPE,VOLDEMORT\n"
                      "T,M,V,U,U,H,P,Z,T,B,K,Y,D,E,H\n"
                      "A,G,I,Q,A,A,W,R,K,M,N,E,I,R,A\n"
                      "O,D,T,T,G,G,O,H,R,E,V,V,S,O,R\n"
                      "R,H,C,H,R,M,K,W,F,P,Y,N,Q,D,R\n"
                      "U,J,J,I,E,D,H,H,B,T,A,E,H,E,Y\n"
                      "D,M,K,D,T,O,F,Q,Y,P,J,L,C,L,P\n"
                      "T,O,L,Z,H,Y,V,G,E,M,L,E,O,B,R\n"
                      "Y,O,S,I,T,U,Y,I,R,I,V,M,F,M,B\n"
                      "V,D,P,M,J,I,D,J,L,A,K,T,Z,U,U\n"
                      "H,O,R,J,J,O,B,G,A,E,Y,R,L,D,S\n"
                      "Y,Z,X,A,I,J,M,N,M,C,R,U,M,Z,R\n"
                      "L,X,X,U,C,P,E,X,L,A,Y,U,J,R,B\n"
                      "P,W,P,D,W,O,P,A,M,M,I,O,W,L,E\n"
                      "N,O,R,Y,B,G,A,T,A,I,Y,N,A,Q,L\n"
                      "H,E,R,M,I,O,N,E,C,J,R,H,C,X,Q"))


(deftest try-match-up-test
  (let [grid [["N" "A" "A"]
              ["U" "A" "A"]
              ["S" "A" "A"]]
        word "SUN"
        match-position [0 2]
        no-match-position [2 1]]
    (testing "can match word and return its positions in the grid"
      (is (= [[0 2] [0 1] [0 0]]
             (try-match-up grid word match-position))))
    (testing "return nil when there's no match"
      (is (nil? (try-match-up grid word no-match-position))))))

(deftest try-match-down-test
  (let [grid [["S" "S" "A"]
              ["U" "U" "A"]
              ["N" "A" "A"]]
        word "SUN"
        match-position [0 0]
        no-match-position [0 1]]
    (testing "can match word and return its positions in the grid"
      (is (= [[0 0] [0 1] [0 2]]
             (try-match-down grid word match-position))))
    (testing "return nil when there's no match"
      (is (nil? (try-match-down grid word no-match-position))))))

(deftest try-match-forward-test
  (let [grid [["S" "S" "A"]
              ["S" "U" "N"]
              ["N" "A" "A"]]
        word "SUN"
        match-position [0 1]
        no-match-position [1 0]]
    (testing "can match word and return its positions in the grid"
      (is (= [[0 1] [1 1] [2 1]]
             (try-match-forward grid word match-position))))
    (testing "return nil when there's no match"
      (is (nil? (try-match-forward grid word no-match-position))))))

(deftest try-match-backwards-test
  (let [grid [["S" "S" "A"]
              ["N" "U" "S"]
              ["N" "A" "A"]]
        word "SUN"
        match-position [2 1]
        no-match-position [1 0]]
    (testing "can match word and return its positions in the grid"
      (is (= [[2 1] [1 1] [0 1]]
             (try-match-backwards grid word match-position))))
    (testing "return nil when there's no match"
      (is (nil? (try-match-backwards grid word no-match-position))))))

(deftest stringify-result-test
  (is (= (str "SUN: (2,1),(1,1),(0,1)\n"
              "RUN: (1,0),(1,1),(1,2)")
         (stringify-result {"SUN" [[2 1] [1 1] [0 1]]
                            "RUN" [[1 0] [1 1] [1 2]]}))))

(deftest search-test
  (testing "can find words in input 1"
    (is (= (search input1)
           (str "BONES: (0,6),(0,7),(0,8),(0,9),(0,10)\n"
                "KHAN: (5,9),(5,8),(5,7),(5,6)\n"
                "KIRK: (4,7),(3,7),(2,7),(1,7)\n"
                "SCOTTY: (0,5),(1,5),(2,5),(3,5),(4,5),(5,5)\n"
                "SPOCK: (2,1),(3,2),(4,3),(5,4),(6,5)\n"
                "SULU: (3,3),(2,2),(1,1),(0,0)\n"
                "UHURA: (4,0),(3,1),(2,2),(1,3),(0,4)"))))

  (testing "can find words in input 2"
    (is (= (search input2)
           (str "DRACO: (1,8),(2,9),(3,10),(4,11),(5,12)\n"
                "DUMBLEDORE: (13,9),(13,8),(13,7),(13,6),(13,5),(13,4),(13,3),(13,2),(13,1),(13,0)\n"
                "HARRY: (14,0),(14,1),(14,2),(14,3),(14,4)\n"
                "HERMIONE: (0,14),(1,14),(2,14),(3,14),(4,14),(5,14),(6,14),(7,14)\n"
                "RON: (2,13),(1,13),(0,13)\n"
                "SNAPE: (12,2),(11,3),(10,4),(9,5),(8,6)\n"
                "VOLDEMORT: (0,8),(1,7),(2,6),(3,5),(4,4),(5,3),(6,2),(7,1),(8,0)")))))

;(run-tests 'word-search-kata.core-test)