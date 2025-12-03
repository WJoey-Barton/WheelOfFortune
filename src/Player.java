/* Christina Kc 
Player.java */

/*
This class stores a player’s identity and total score, 
providing methods for updating points and ensuring players can be uniquely compared.
*/

import java.util.Random;

public class Player {

    // Player's display name
    private final String name;

    // Player’s total points across the game
    private int points;

    // Unique ID assigned to each player 
    private final int playerID;

    Random rand = new Random();


    // Constructor: creates a player with a name and ID, starting at 0 points
    public Player(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
        this.points = 0;
    }

    public Player(String name) {
        this.name = name;
        this.playerID = rand.nextInt(100);
        this.points = 0;
    }


    // Returns the player's name
    public String getName() {
        return name;
    }

    // Returns the player's current score
    public int getPoints() {
        return points;
    }

    // Adds points to the player's total score
    public void addPoints(int points) {
        this.points += points;
    }


    // Generates a unique hash code based on playerID and name
    @Override
    public int hashCode() {
        return playerID * 31 + name.hashCode();
    }


    // Checks if two players are considered equal
    @Override
    public boolean equals(Object obj) {

        
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Player player = (Player) obj;
        return playerID == player.playerID && name.equals(player.name);
    }
}

