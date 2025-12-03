/* Christina Kc 
PlayerState.java */

import java.util.ArrayList;

/*
This class keeps track of a player's status during a single round of the game, 
including the letters they have guessed, the points they have earned for that round, 
and whether they are currently bankrupt.
*/
public class PlayerState {

    // Stores all letters this player has guessed during the round
    private final ArrayList<Character> guesses;

    // Points earned in the current round ONLY
    private int roundScore;

    // Decides whether this player is currently bankrupt
    private boolean isBankrupt;


    // The constructor nitializes a fresh state for each new round
    public PlayerState() {
        this.guesses = new ArrayList<>();
        this.roundScore = 0;
        this.isBankrupt = false;
    }


    // Adds points to the player's round score
    public void updateScore(int points) {
        roundScore += points;
    }

    // Resets the round score and clears all guesses
    public void resetRound() {
        roundScore = 0;
        guesses.clear();
    }

    // Records a guessed letter for this player
    public void addGuess(char guess) {
        guesses.add(guess);
    }


    // Returns how many points the player earned this round
    public int getRoundScore() {
        return roundScore;
    }

    // Returns a copy of the player's guessed letters
    public ArrayList<Character> getGuesses() {
        return new ArrayList<>(guesses);
    }

    // Returns whether the player is bankrupt
    public boolean isBankrupt() {
        return isBankrupt;
    }

    // Sets whether the player is bankrupt
    public void setBankrupt(boolean bankrupt) {
        this.isBankrupt = bankrupt;
    }

    // Determines whether the player is allowed to spin the wheel
    public boolean canSpin() {
        return !isBankrupt;
    }

    // Marks the player as having lost their turn
    public void loseTurn() {
        
    }
}

