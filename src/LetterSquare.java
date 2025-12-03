//Joey Barton

/*
This class models one tile on the puzzle board, 
holding a letter or space and tracking whether the letter has been guessed.
*/

public class LetterSquare {

    private final char letter;
    private final boolean isSpace;
    private boolean isGuessed;

    public LetterSquare(char letter, boolean isSpace) {
        this.letter = letter;
        this.isSpace = isSpace;
        this.isGuessed = false;
    }
// Gets the letter from square
    public char getLetter() {
        return this.letter;
    }
// Checks if the square is a space in the phrase
    public boolean isSpace() {
        return this.isSpace;
    }
// Checks if the letter is guessed in the square
// If so, then set the guessed letter to the square
    public boolean isGuessed() {
        return this.isGuessed;
    }
    
    public void setGuessed(boolean guessed) {
        this.isGuessed = guessed;
    }

    
}


