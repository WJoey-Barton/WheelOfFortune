//Nate Simpson

public interface Round {

/*
This interface specifies methods for starting and ending a round, 
determining the winning player, retrieving the round’s phrase, and
providing identifying information such as a unique round ID and round type.
*/

    // Starts the round
    void startRound();

    // Ends the round
    void endRound();

    // Return the winning player for this round
    Player getWinner();
    
    // Get unique identifier for this round (for hashcode)
    String getRoundId();
    
    // Get round type (for hashcode) - Will be used in STEM EXPO
    String getRoundType();
    
    // Get the phrase for this round
    Phrase getPhrase();
}

