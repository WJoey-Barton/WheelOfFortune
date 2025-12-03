/* Christina Kc 

The GameEngine is the central backend system that controls players, 
rounds, scoring, phrase progress, and turn order for the entire game.
HashMap is used to store each player's status, allowing fast access to player information.
Such as round score, guessed letters, and special states (bankrupt, lose-a-turn, whether they can spin).
*/

import java.util.ArrayList;
import java.util.HashMap;

public class GameEngine {

    private final HashMap<Player, PlayerState> playerStates;
    private final ArrayList<Player> players;
    private ArrayList<Round> rounds;
    private int currentRoundIndex;
    private Player currentPlayer;
    private Phrase currentPhrase;
    private boolean gameFinished;

    //hash table for phrase-player mapping
    private final HashMap<String, ArrayList<Player>> phrasePlayerMap;

    public GameEngine() {
        this.playerStates = new HashMap<>();
        this.players = new ArrayList<>();
        this.rounds = new ArrayList<>();
        this.currentRoundIndex = 0;
        this.gameFinished = false;
        this.phrasePlayerMap = new HashMap<>();
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            playerStates.put(player, new PlayerState());
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        playerStates.remove(player);
    }

     // Spin wheel handling which is the wheel logic in sceneController
     // This method serves as a placeholder for round logic

    public void spinWheel() {
        if (gameFinished || !(getCurrentRound() instanceof regularRound)) return;
        
    }

    public void guessLetter(char letter) {
        if (gameFinished || currentPhrase == null) return;
        
        letter = Character.toUpperCase(letter);
        
        // Guess handling in which the UI logic is in sceneController
        currentPhrase.revealLetter(letter);
        
        if (currentPhrase.isSolved()) {
            endCurrentRound();
        }
    }

    public void startRound() {
        if (currentRoundIndex >= rounds.size()) {
            endGame();
            return;
        }
        
        Round currentRound = rounds.get(currentRoundIndex);
        currentPhrase = extractPhraseFromRound(currentRound);
        
        for (PlayerState state : playerStates.values()) {
            state.resetRound();
        }
        
        currentRound.startRound();
        currentPlayer = players.get(0);
    }

    public void endCurrentRound() {
        Round currentRound = getCurrentRound();
        if (currentRound == null) return;
        
        currentRound.endRound();
        Player roundWinner = currentRound.getWinner();
        
        if (roundWinner != null) {
            PlayerState winnerState = playerStates.get(roundWinner);
            // Add bonus points for winning the round
            roundWinner.addPoints(winnerState.getRoundScore() + 1000);
        }
        
        currentRoundIndex++;
        if (currentRoundIndex < rounds.size()) {
            startRound();
        } else {
            endGame();
        }
    }

    public void nextPlayer() {
        if (players.isEmpty()) return;
        
        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();
        currentPlayer = players.get(nextIndex);
    }

    public void playerBuzz(Player player) {
        // Simplified - no toss up rounds needed for now - STEMEXPO
        currentPlayer = player;
    }

    public void solvePhrase(String solution) {
        if (currentPhrase != null && solution.equalsIgnoreCase(currentPhrase.getAnswer())) {
            currentPhrase.revealAllLetters();
            endCurrentRound();
        } else {
            nextPlayer();
        }
    }

    private void handleSpinResult(int spinResult) {
        if (spinResult == -1) {
            // Lose turn will move to next player
            nextPlayer();
        } else if (spinResult == -2) {
            // Bankrupt which reset player score and move to next player
            currentPlayer.addPoints(-currentPlayer.getPoints());
            nextPlayer();
        }
    }

    private Phrase extractPhraseFromRound(Round round) {
        if (round instanceof regularRound) {
            return ((regularRound) round).getPhrase();
        }
        return null;
    }

    private void endGame() {
        gameFinished = true;
    }

    public void setRounds(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    public Round getCurrentRound() {
        return currentRoundIndex < rounds.size() ? rounds.get(currentRoundIndex) : null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Phrase getCurrentPhrase() {
        return currentPhrase;
    }

    public HashMap<Player, PlayerState> getPlayerStates() {
        return new HashMap<>(playerStates);
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

     // Hashcode for phrases - simple but effective
     
    public int calculatePhraseHash(String phrase) {
        if (phrase == null) return 0;
        phrase = phrase.toUpperCase().trim();
        
        int hash = 0;
        for (int i = 0; i < phrase.length(); i++) {
            hash = hash * 31 + phrase.charAt(i);
        }
        return Math.abs(hash);
    }

    
     // Add player to a round and triggers rehash if new phrase
    
    public void addPlayerToRound(Round round, Player player) {
        String phrase = round.getPhrase().getAnswer();
        
        // Check if new phrase (rehash trigger)
        if (!phrasePlayerMap.containsKey(phrase)) {
            System.out.println("New phrase detected: " + phrase + " - Rehashing!");
            rehash();
        }
        
        phrasePlayerMap.computeIfAbsent(phrase, k -> new ArrayList<>()).add(player);
        System.out.println("Added " + player.getName() + " to phrase: " + phrase);
    }

    
     // Move player between rounds
    
    public void movePlayer(Player player, Round fromRound, Round toRound) {
        String fromPhrase = fromRound.getPhrase().getAnswer();
        String toPhrase = toRound.getPhrase().getAnswer();
        
        // Remove from old
        ArrayList<Player> fromList = phrasePlayerMap.get(fromPhrase);
        if (fromList != null) {
            fromList.remove(player);
        }
        
        // Add to new
        addPlayerToRound(toRound, player);
        System.out.println("Moved " + player.getName() + " from " + fromPhrase + " to " + toPhrase);
    }

    
     // List players in a round
    
    public ArrayList<Player> getPlayersInRound(Round round) {
        String phrase = round.getPhrase().getAnswer();
        return phrasePlayerMap.getOrDefault(phrase, new ArrayList<>());
    }

    
      // Delete player from game
     
    public void deletePlayer(Player player) {
        // Remove from all phrases
        for (ArrayList<Player> playerList : phrasePlayerMap.values()) {
            playerList.remove(player);
        }
        
        // Remove from game
        players.remove(player);
        playerStates.remove(player);
        System.out.println("Deleted " + player.getName() + " from game");
    }

    // A simple rehash that recalculate hashes when new phrase added
     
    private void rehash() {
        System.out.println("Rehashing table...");
        for (String phrase : phrasePlayerMap.keySet()) {
            int hash = calculatePhraseHash(phrase);
            System.out.println("Phrase: " + phrase + " -> Hash: " + hash);
        }
    }

}

