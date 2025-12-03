//Joey Barton
// This class holds the JavaFX logic

/*
This project is a multi-player Wheel of Fortune-style puzzle game 
where three players compete over three rounds by spinning a wheel, guessing letters and solving hidden phrases. 
Each round functions as its own "room" with a unique phrase, category, and player progress. 
The core game logic is handled by the GameEngine, which manages 
players, rounds, turn order, phrase progression, and winning conditions. 
The engine stores each player's status in a HashMap<Player, PlayerState>, 
allowing fast access to player information such as round score, guessed letters, and special states 
(bankrupt, lose-a-turn, whether they can spin). 
A separate HashMap also maps phrases to players using a hash function that processes phrase Strings.
Players are represented by the Player class, which stores each player's name and accumulated points, 
while PlayerState tracks per-round details such as their guesses and whether they can continue their turn. 
The phrase class contains the current puzzle's answer and category 
and provides methods for revealing letters as players guess them. 
The graphical interface is managed by the SceneController, which links the backend game logic to the JavaFX GUI. 
It displays the puzzle board, wheel, player scores, guessed letters, and round results, 
and handles user actions such as spinning, guessing, and solving. 
The SceneController uses a PhraseLayout to generate the grid of 
LetterSquare objects that form the visible puzzle board. 
Wheel results interact directly with the GameEngine to apply penalties (Bankrupt, Lose-A-Turn) 
or reward players with point values based on correctly guessed letters.
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/WheelOfFortune3.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            System.err.println("COULD NOT LOAD FXML: " + e.getMessage());
        }
    }    

}
