import java.util.Scanner;

public class JamdagniLavanyaA4Q1 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in); //get board rows and cols 
        System.out.print("Enter board rows (odd number): ");
        int rows = sc.nextInt();
        System.out.print("Enter board cols (odd number): ");
        int cols = sc.nextInt();

        playScrabble(new Game(rows, cols));//calling playScrabble game

    }

    public static void playScrabble(Game theGame) { //the playScrabble game function
        boolean exit = false;
        Scanner input;
        String word;
        int score;

        while (!exit) {//runs while exit is false
            System.out.println("This is your board:\n");
            theGame.displayBoard(); //calls the display board function
            System.out.println("\nPlease enter a word to play or type 0 to exit:");
            input = new Scanner(System.in);

            if(input.hasNext()) { //if the user enters an input
                if(input.hasNextInt()) { //if the input has an integer
                    if(input.nextInt() == 0) { //if the integer is 0
                        exit = true; //exits the game
                        System.out.println("\nGame Over!");
                        System.out.println("\nYour entered " + theGame.getNumWords() +" words with a final score of: " + theGame.getFinalScore());
                    } else { //otherwise prints an error 
                        System.out.println("\nError: you have entered a number that is not 0, please enter a word to play or type 0 to exit");
                    }
                } else { //otherwise asks for a word to play the gamr
                    word = input.next();

                    if(Game.isAlphabetical(word)) { //if word is an alphabet
                        score = theGame.locateWord(word); //gives the word to the place word function and assigns the returned the value in score variable

                        if(score > 0) { //if the score is greater than 0
                            System.out.println("\nYou scored " + score + " for " + word +"\n");
                        } else { 
                            System.out.println("\nError: could not place " + word + " on the board because it does not fit, please try again.");
                        }
                    } else {//if the word has something else than a letter
                        System.out.println("\nError: you have entered a word with non-alphabetical letter(s), please enter a word to play or type 0 to exit");
                    }
                }
            }

        }
    }
}

class Game {
    //following are some variables used ahead 
    private final int ROWS;
    private final int COLS;
    private final char[][] BOARD;
    private int finalScore;
    private int numWords;
    private final char BLANK = '.';
    private final char DOUBLE_LETTER = '!';
    private final char DOUBLE_WORD = '+';
    private final char DOUBLE_LETTER_WORD = '*';
    private final int[] LETTER_SCORES = new int[] {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };
    
    public Game(int rows, int cols) { //game function
        this.ROWS = rows;
        this.COLS = cols;
        BOARD = new char[ROWS][COLS]; //board function is assigned the rows and columns
        fillBoard();//calls the fill board function
        //initialises the variables 
        finalScore = 0;
        numWords = 0;
    }

    public void displayBoard() { //prints the board 

        for (int i = 0; i < BOARD.length; i++) {
            for (int j = 0; j < BOARD[i].length; j++) {
                System.out.print(BOARD[i][j]);
            }
            System.out.println();
        }
    }

    public void fillBoard() { //fill board function prints the pattern 

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (inBoth(i, j)) {//for the whole board
                    BOARD[i][j] = DOUBLE_LETTER_WORD; // for "*" score
                } else if (boardEdge(i, j)) {//for the edge of the board
                    BOARD[i][j] = DOUBLE_WORD; // for "+" score
                } else if (insideBoard(i, j)) { //for the inside of the board 
                    BOARD[i][j] = DOUBLE_LETTER; // for "!" score
                } else {
                    BOARD[i][j] = BLANK; // for "." score
                }
            }
        }
    }

    public boolean insideBoard(int row, int col) { //for the inside part of the board 
        boolean rowsInRange = row >= (ROWS / 2) - (COLS / 2) && row <= (ROWS / 2) + (COLS / 2);//pattern created for the rows
        boolean colsInRange = col >= (COLS / 2) - (ROWS / 2) && col <= (COLS / 2) + (ROWS /2);//pattern created for the columns
        boolean isDiagonal =  Math.abs(row - (ROWS / 2)) == Math.abs(col - (COLS / 2));//pattern created for the diagonal

        return isDiagonal && rowsInRange && colsInRange; //every condition is returned
    }


    public boolean boardEdge(int row, int col) {//for the edge of the board 

        return row == 0 || col == 0 || row == ROWS - 1 || col == COLS - 1; //pattern created according to these conditions
    }

    public boolean inBoth(int row, int col) { //for the whole board
        return insideBoard(row, col) && boardEdge(row, col);//pattern created according to the above conditions combined 
    }

    public int locateWord( String word) { // method to place word on board
        //initilized the variables used ahead 
        int score = 0;
        int maxVerScore = -1;
        int maxHoriScore = -1;
        int horiCheckRow = -1;
        int horiCheckCol  = -1;
        int verCheckRow = -1;
        int verCheckCol = -1;
        word = word.toUpperCase();//chose to keep the words upper case 

        if(numWords == 0) { //if no words are entered 
            if(word.length() <= COLS) {//if length of words is less or equal to columns of board
                score = analyzeWordScoreHorizontally(word, ROWS/2, (COLS/2) - (word.length()/2));//passes the value to function and sets returned val to the score

                if(score > 0) {//if score is >0
                    putWordHorizontally(word, ROWS/2, (COLS/2) - (word.length()/2));//passes the values to the fucntion
                    numWords++;//increases num of words by 1
                    finalScore += score;//final score stores the total score val
                }
            }
        } else {//if something was entered
            for(int i=0; i<word.length(); i++) {//runs for the word length
                for(int j=0; j<ROWS; j++) {//runs for the board rows
                    for(int k=0; k<COLS; k++) {//runs for the board columns
                        if(BOARD[j][k] == word.charAt(i)) {//board at this position matches the word at pos i
                            if(word.length() <= COLS && (k - i) >= 0) {//if word length is less than or equal to col length and diff in col and word length is >0
                                if(analyzeWordScoreHorizontally(word, j, k-i) >= maxHoriScore) {//score is greater than max horizontal score
                                    horiCheckRow = j;
                                    horiCheckCol = k-i;
                                    maxHoriScore = analyzeWordScoreHorizontally(word, horiCheckRow, horiCheckCol);//stores returned score from the fucntion to max horizontal score
                                }
                            }

                            if(word.length() <= ROWS && (j-i) >= 0){//if word length is less than or equal to row length and diff in row and word length is >0
                                if(analyzeWordScoreVertically(word, j-i, k) >= maxVerScore) {//score is greater than max vertical score
                                    verCheckRow = j-i;
                                    verCheckCol = k;
                                    maxVerScore = analyzeWordScoreVertically(word, verCheckRow, verCheckCol);//stores returned score from the fucntion to max vertical score
                                }
                            }
                        }
                    }
                }
            }

            if(maxVerScore > maxHoriScore) {//checks if maxVerScore is greater than maxHoriScore 
                score = maxVerScore;//so it sets the score to maxVerScore

                if(score > 0) {//if score is >0
                    putWordVertically(word, verCheckRow, verCheckCol);//passes these values to the function
                    numWords++;//increases no of words by 1
                    finalScore += score;//adds score to final score
                }
            } else {//else maxHoriScore is greater
                score = maxHoriScore;//so it sets the score to maxHoriScore

                if(score > 0) {//if score is >0
                    putWordHorizontally(word, horiCheckRow, horiCheckCol);//passes these values to the function
                    numWords++;//increases no of words by 1
                    finalScore += score;//adds score to final score
                }
            }
        }

        return score;//returns score
    }

    private int analyzeWordScoreVertically(String word, int row, int col) {//evaluates when to place the vertically
        int score  = 0;
        boolean doubleWord = false;
        int toAdd;
        boolean exit = false;

        if (row + word.length() <= ROWS) {//if the value is smaller than the row length
            for(int i=0; i<word.length() && row + i < ROWS && !exit; i++) {//runs for the whole word length and if row+i is less than board row length and user did not exit
                toAdd = 0;

                if(BOARD[row +i][col] == BLANK || BOARD[row +i][col] == DOUBLE_LETTER || BOARD[row +i][col] == DOUBLE_WORD || BOARD[row +i][col] == DOUBLE_LETTER_WORD) {//checks if the position in the board has the symbols 
                    toAdd = LETTER_SCORES[word.charAt(i) - 'A'];//adds the score of that letter at i pos in toAdd 

                    if(BOARD[row +i][col] == DOUBLE_LETTER) {//if that position has "!" symbol toAdd doubles
                        toAdd *= 2;
                    } else if(BOARD[row +i][col] == DOUBLE_WORD ) {//if that position has "+" symbol doubleWord becomes true
                        doubleWord = true;
                    } else if(BOARD[row +i][col] == DOUBLE_LETTER_WORD) {//if that position has "*" symbol doubleWord becomes true and toAdd doubles
                        toAdd *= 2;
                        doubleWord = true;
                    }
                } else if(BOARD[row +i][col] != word.charAt(i)) { //if the position does not have that char at i pos 
                    score = 0;
                    exit = true;
                    doubleWord = false;
                }

                score += toAdd;//score stores all the values
            }

            if(doubleWord) {//if the word is stored on the double word 
                score *= 2; //score doubles 
            }
        }

        return score; //returns the score
    }


    private int analyzeWordScoreHorizontally(String word, int row, int col) {
        int score  = 0;
        boolean doubleWord = false;
        int toAdd;
        boolean exit = false;


        if (col + word.length() <= COLS) {//if the value is smaller than the column length
            for(int i=0; i<word.length() && col + i < COLS && !exit; i++) {//runs for the whole word length and if col+i is less than board column length and user did not exit
                toAdd = 0;

                if(BOARD[row][col + i] == BLANK || BOARD[row][col + i] == DOUBLE_LETTER || BOARD[row][col + i] == DOUBLE_WORD || BOARD[row][col + i] == DOUBLE_LETTER_WORD) {//checks if the position in the board has the symbols 
                    toAdd = LETTER_SCORES[word.charAt(i) - 'A'];//adds the score of that letter at i pos in toAdd 

                    if(BOARD[row][col + i] == DOUBLE_LETTER) {//if that position has "!" symbol toAdd doubles
                        toAdd *= 2;
                    } else if(BOARD[row][col + i] == DOUBLE_WORD ) {//if that position has "+" symbol doubleWord becomes true
                        doubleWord = true;
                    } else if(BOARD[row][col + i] == DOUBLE_LETTER_WORD) {//if that position has "*" symbol doubleWord becomes true and toAdd doubles
                        toAdd *= 2;
                        doubleWord = true;
                    }
                } else if(BOARD[row][col + i] != word.charAt(i)) {//if the position does not have that char at i pos
                    score = 0;
                    exit = true;
                    doubleWord = false;
                }

                score += toAdd;//score stores all the values
            }

            if(doubleWord) {//if the word is stored on the double word 
                score *= 2; //score doubles 
            }
        }

        return score;//returns the score
    }

    private void putWordVertically(String word, int row, int col) { //this function places the word according to the above analyzed functions evaluation
        if(analyzeWordScoreVertically(word, row, col) > 0) {//if the returned score is greater than 0 
            for(int i=0; i<word.length() && row + i < ROWS; i++) {//runs for word length and row+i
                if(BOARD[row + i][col] == BLANK || BOARD[row +i][col] == DOUBLE_LETTER || BOARD[row +i][col] == DOUBLE_WORD || BOARD[row +i][col] == DOUBLE_LETTER_WORD) {//checks if the position in the board has the symbols 
                    BOARD[row +i][col] = word.charAt(i);//places word at that position
                }
            }
        }
    }

    private void putWordHorizontally(String word, int row, int col) { //this function places the word according to the above analyzed functions evaluation
        if(analyzeWordScoreHorizontally(word, row, col) > 0) {//if the returned score is greater than 0 
            for(int i=0; i<word.length() && col + i < COLS; i++) {//runs for word length and col+i
                if(BOARD[row][col + i] == BLANK || BOARD[row][col + i] == DOUBLE_LETTER || BOARD[row][col + i] == DOUBLE_WORD || BOARD[row][col + i] == DOUBLE_LETTER_WORD) {//checks if the position in the board has the symbols 
                    BOARD[row][col + i] = word.charAt(i);//places word at that position
                }
            }
        }
    }

    public static boolean isAlphabetical(String word) { //function checks if the entered input is an alphabet or not
        boolean isAlphabetical = true; 

        for(int i=0; i<word.length() && isAlphabetical; i++) { //runs for the whole word length and if it is an alphabet
            isAlphabetical = Character.isLetter(word.charAt(i)); //returns true if the char at that index is letter or not or vice versa
        }

        return isAlphabetical;//returns a boolean
    }

    public int getFinalScore() { //calls final score function
        return finalScore; //returns the finals score 
    }

    public int getNumWords() { //calls num words function
        return numWords; //returns the num of words 
    }

}

