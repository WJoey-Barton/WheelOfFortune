//Nate Simpson

import java.util.ArrayList;
import java.util.Random;

/*
This class models a standard gameplay round, managing its phrase, players, wheel results, winner selection, 
and unique identification through hashcode and equality.
*/

public class regularRound implements Round {

    private final Phrase phrase;  // The phrase for this round
    private Player winner;       // Winner after round ends
    private final int[] wheelValues = {100, 200, 500,
            -1, // Lose a Turn
            -2}; // Bankrupt

    private final Random rand = new Random();
    private final ArrayList<Player> players;
    
    // Add fields for hashcode functionality
    private final String roundId;
    private final String roundType;
    private static int roundCounter = 0;

    public regularRound(Phrase phrase, ArrayList<Player> players) {
        this.phrase = phrase;
        this.players = players;
        this.roundType = "REGULAR";
        this.roundId = "ROUND_" + (++roundCounter) + "_" + phrase.getCategory();
    }

     // reset player states, reveal blanks, etc
    @Override
    public void startRound() {
        System.out.println("Regular Round started!");
    }

    // Determine winner (highest score)
    @Override
    public void endRound() {

        this.winner = players.get(0);
        System.out.println("Regular Round ended.");
    }

    // Return the winner of the round
    @Override
    public Player getWinner() {
        return winner;
    }

    // Wheel Logic
    public int spinWheel() {
        int index = rand.nextInt(wheelValues.length);
        return wheelValues[index];
    }
    // When the pointer lands on a value, interpret it
    public String interpretOutcome(int result) {
        if (result == -1) {
        return "LoseTurn";
        }
        if (result == -2) {
        return "Bankrupt";
        }
        return "Points";
    }
    
    // Add getPhrase method that GameEngine needs for extracting phrase
    @Override
    public Phrase getPhrase() {
        return phrase;
    }
    // Gets the RoundID
    @Override
    public String getRoundId() {
        return roundId;
    }
    // Gets the round type (in this case just focusing on one type of round)
    @Override
    public String getRoundType() {
        return roundType;
    }
    
    // Simple and clear hashcode function for Round objects
    @Override
    public int hashCode() {
        // Combine round type, ID, and category
        String combinedR = roundType + "_" + roundId + "_" + phrase.getCategory();
        return Math.abs(combinedR.hashCode());
    }
    
   @Override
public boolean equals(Object obj) {

    // If both references point to the same object then they are equal
    if (this == obj) return true;

    // If compared with null OR different class then they are NOT equal
    if (obj == null || getClass() != obj.getClass()) return false;

    // Safe cast after type check
    regularRound that = (regularRound) obj;

    // Two rounds are equal if both their IDs and types match
    return roundId.equals(that.roundId) && roundType.equals(that.roundType);
}

@Override
public String toString() {

    // Returns a readable representation of the round,
    // showing round type, ID, and its phrase category
    return "Round{" + roundType + " - " + roundId + " - " + phrase.getCategory() + "}";
}
}


