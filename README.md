Word Search Kata
================
In this exercise you will build a program to complete a [word search](https://en.wikipedia.org/wiki/Word_search) problem.

Given a text file consisting of a list of words, and a series of rows of single-character lists representing the word search grid, this program should search for the words in the grid and return a set of x,y coordinates for each word found.

The point of this kata to to provide an larger than trivial exercise that can be used to practice TDD. A significant portion of the effort will be in determining what tests should be written and, more importantly, written next.

## Input ##

The first line of the text file will consist of the list of words to be found.  The following lines will consist of a list of single characters, A-Z. All lines in the file except the first will have the same length, and the number of rows will match the number of characters in a line.  This input represents the square grid of the word search.

The grid will always be square, and all words in the list will always be present in the grid. Words may be located horizontally, vertically, diagonally, and both forwards and backwards.  Words will never "wrap" around the edges of the grid.

The following is an example of the format of the input file:

<pre>
BONES,KHAN,KIRK,SCOTTY,SPOCK,SULU,UHURA
U,M,K,H,U,L,K,I,N,V,J,O,C,W,E
L,L,S,H,K,Z,Z,W,Z,C,G,J,U,Y,G
H,S,U,P,J,P,R,J,D,H,S,B,X,T,G
B,R,J,S,O,E,Q,E,T,I,K,K,G,L,E
A,Y,O,A,G,C,I,R,D,Q,H,R,T,C,D
S,C,O,T,T,Y,K,Z,R,E,P,P,X,P,F
B,L,Q,S,L,N,E,E,E,V,U,L,F,M,Z
O,K,R,I,K,A,M,M,R,M,F,B,A,P,P
N,U,I,I,Y,H,Q,M,E,M,Q,R,Y,F,S
E,Y,Z,Y,G,K,Q,J,P,C,Q,W,Y,A,K
S,J,F,Z,M,Q,I,B,D,B,E,M,K,W,D
T,G,L,B,H,C,B,E,C,H,T,O,Y,I,K
O,J,Y,E,U,L,N,C,C,L,Y,B,Z,U,H
W,Z,M,I,S,U,K,U,R,B,I,D,U,X,S
K,Y,L,B,Q,Q,P,M,D,F,C,K,E,A,B
</pre>

## Output ##
The output of the program is the location of each word found, each on a separate line.  The location will be represented as a series of x,y coordinates, where both x and y start at zero at the top-left of the grid.  From this position both x and y will increase, i.e. they will never be negative.  

Given the example input above, the following output would be expected:

<pre>
BONES: (0,6),(0,7),(0,8),(0,9),(0,10)
KHAN: (5,9),(5,8),(5,7),(5,6)
KIRK: (4,7),(3,7),(2,7),(1,7)
SCOTTY: (0,5),(1,5),(2,5),(3,5),(4,5),(5,5)
SPOCK: (2,1),(3,2),(4,3),(5,4),(6,5)
SULU: (3,3),(2,2),(1,1),(0,0)
UHURA: (4,0),(3,1),(2,2),(1,3),(0,4)
</pre>

## User Stories ##
*As the Puzzle Solver*<br />
*I want to search horizontally*<br />
*So that I can find words on the X axis*<br />

*As the Puzzle Solver*<br />
*I want to search vertically*<br />
*So that I can find words on the Y axis*<br />

*As the Puzzle Solver*<br />
*I want to search diagonally descending*<br />
*So that I can find words the descend along the X axis*<br />

*As the Puzzle Solver*<br />
*I want to search diagonally ascending*<br />
*So that I can find words that ascend along the X axis*<br />

*As the Puzzle Solver*<br />
*I want to search backwards*<br />
*So that I can find words in reverse along all axises*<br />

## FAQ ##

*It looks hard to generate test data.  How can do do this easily?*<br />
* If you need to generate test data there are many sites which will generate puzzles for you, such as [this one](http://puzzlemaker.discoveryeducation.com/WordSearchSetupForm.asp?campaign=flyout_teachers_puzzle_wordcross).

*How large can the grid be?*<br />
* Big or small, this is really up to you as long as you remember that the grid will always be square and that your solution should meet the requirements described above. This question is really outside the scope of the kata; the point is to focus on Test-Driving and software craftsmanship.

*How long or short can the words be?*<br />
* Words will be a minimum of two letters long, and will always fit within the grid along the axis on which it can be located.

## Clojure test constructs ##

In order to facilitate write tests in Clojure:

``` clojure

;;; Inputs

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


;;; Outputs
                      
(str "BONES: (0,6),(0,7),(0,8),(0,9),(0,10)\n"
                "KHAN: (5,9),(5,8),(5,7),(5,6)\n"
                "KIRK: (4,7),(3,7),(2,7),(1,7)\n"
                "SCOTTY: (0,5),(1,5),(2,5),(3,5),(4,5),(5,5)\n"
                "SPOCK: (2,1),(3,2),(4,3),(5,4),(6,5)\n"
                "SULU: (3,3),(2,2),(1,1),(0,0)\n"
                "UHURA: (4,0),(3,1),(2,2),(1,3),(0,4)")
                
(str "DRACO: (1,8),(2,9),(3,10),(4,11),(5,12)\n"
                "DUMBLEDORE: (13,9),(13,8),(13,7),(13,6),(13,5),(13,4),(13,3),(13,2),(13,1),(13,0)\n"
                "HARRY: (14,0),(14,1),(14,2),(14,3),(14,4)\n"
                "HERMIONE: (0,14),(1,14),(2,14),(3,14),(4,14),(5,14),(6,14),(7,14)\n"
                "RON: (2,13),(1,13),(0,13)\n"
                "SNAPE: (12,2),(11,3),(10,4),(9,5),(8,6)\n"
                "VOLDEMORT: (0,8),(1,7),(2,6),(3,5),(4,4),(5,3),(6,2),(7,1),(8,0)")

```