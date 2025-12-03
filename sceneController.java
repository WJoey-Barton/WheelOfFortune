//Joey Barton

/*
This class controls the user interface for the Wheel of Fortune game, 
updating the puzzle board, handling player actions, and syncing all visuals 
with the underlying GameEngine.
*/
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class sceneController extends GameEngine {

    @FXML private VBox PuzzleBoard_VBox;
    @FXML private Label Result_Label;

    @FXML private TextField Player1_TextField;
    @FXML private TextField Player2_TextField;
    @FXML private TextField Player3_TextField;

    @FXML private Label Player1_Label;
    @FXML private Label Player2_Label;
    @FXML private Label Player3_Label;

    @FXML private Label Initialize_Label;

    @FXML private Label score1_TextField;
    @FXML private Label score2_TextField;
    @FXML private Label score3_TextField;

    @FXML private TextField userInput_TextField;
    @FXML private Label Letters_Label;

    @FXML private StackPane Wheel_Container;
    @FXML private Label Category_TextField;
    

    @FXML private Button Spin_Button;
    @FXML private Button GuessLetter_Button;
    @FXML private Button SolvePuzzle_Button;
    @FXML private Button StartGame_Button;

    private PhraseLayout phraseLayout;
    private List<List<LetterSquare>> currentPhraseLayout;
    private Wheel wheel;
    private final java.util.Set<Character> guessedLetters = new java.util.HashSet<>();

    private int currentPlayerIndex = 0;
    private final int nextRoundStarterIndex = 0;
    private int lastWheelValue = 0;
    private boolean hasSpunThisTurn = false;

    private ComputerController compController;

    private final String[] roundSequence = {
        "regularRound", 
        "regularRound",
        "regularRound"
    };

    public sceneController() {
        super(); // Initialize GameEngine in constructor
    }
    
//initialize() prints the opening phrase "WHEEL OF FORTUNE" on the Puzzle Board
    @FXML
    public void initialize() {
        phraseLayout = new PhraseLayout();
        loadPhrase("WHEEL OF FORTUNE");
        Letters_Label.setText("Guessed: ");
        Initialize_Label.setText("Enter player names above each score box.\n      Leave blank for computer players.\n              Then click START GAME!");
        revealAllLetters();
        setupWheel();
        

        compController = new ComputerController(this);
        disableGameControls(true);

        Player1_Label.setVisible(false);
        Player2_Label.setVisible(false);
        Player3_Label.setVisible(false);

        Player1_TextField.setVisible(true);
        Player2_TextField.setVisible(true);
        Player3_TextField.setVisible(true);

        

    }

    public void startGame(List<Player> players) {
        // Add players to GameEngine
        for (Player player : players) {
            super.addPlayer(player);
        }

        this.currentPlayerIndex = 0;
        updateAllScores(0, 0, 0);

        Player1_Label.setText(players.get(0).getName());
        Player2_Label.setText(players.get(1).getName());
        Player3_Label.setText(players.get(2).getName());

        // Setup rounds in GameEngine
        setupGameRounds();
        
        
        super.startRound(); // Use GameEngine's startRound
        highlightCurrentPlayer();
        loadNewRound();
    }

    private void disableGameControls(boolean disable) {
        Spin_Button.setDisable(disable);
        GuessLetter_Button.setDisable(disable);
        SolvePuzzle_Button.setDisable(disable);
        userInput_TextField.setDisable(disable);
    }
    
 // Builds the round list from roundSequence
    private void setupGameRounds() {
        ArrayList<Round> rounds = new ArrayList<>();
        
        for (String roundType : roundSequence) {
            Phrase phrase = getRandomPhrase();
            Round round = new regularRound(phrase, new ArrayList<>(getPlayers()));
            rounds.add(round);
        }
        
        super.setRounds(rounds);
    }
    
  // Updates UI when moving to next round
    private void loadNewRound() {
        if (super.isGameFinished()) {
            Player winner = getPlayers().stream()
                .max((a, b) -> Integer.compare(a.getPoints(), b.getPoints()))
                .orElse(getPlayers().get(0));

            Result_Label.setText("GAME OVER — WINNER: " + winner.getName());
            return;
        }

    // Get the new current round's phrase
        Phrase currentPhrase = super.getCurrentPhrase();
        if (currentPhrase != null) {
            prepareNextRound();
            loadPhrase(currentPhrase.getAnswer());
            highlightCurrentPlayer();
            Result_Label.setText("New Round: " + currentPhrase.getCategory());
            Category_TextField.setText("Category: " + currentPhrase.getCategory());
        }
    }

    // Start Game Button
    @FXML
    private void StartGameButton_clicked() {
       List<Player> players = new ArrayList<>();

       String[] computerNames = {"CPU_1", "CPU_2", "CPU_3"};
       int computerCount = 0;

       String name1 = Player1_TextField.getText().trim();
       if(name1.isEmpty()) {
        players.add(new ComputerPlayer(computerNames[computerCount++]));
       } else {
        players.add(new Player(name1));
       }

       String name2 = Player2_TextField.getText().trim();
       if(name2.isEmpty()) {
        players.add(new ComputerPlayer(computerNames[computerCount++]));
       } else {
        players.add(new Player(name2));
       }

       String name3 = Player3_TextField.getText().trim();
       if(name3.isEmpty()) {
        players.add(new ComputerPlayer(computerNames[computerCount++]));
       } else {
        players.add(new Player(name3));
       }

       Initialize_Label.setVisible(false);

       Player1_Label.setVisible(true);
       Player2_Label.setVisible(true);
       Player3_Label.setVisible(true);

       Player1_TextField.setVisible(false);
       Player2_TextField.setVisible(false);
       Player3_TextField.setVisible(false);

       StartGame_Button.setVisible(false);

       disableGameControls(false);
       startGame(players);
    }

  // Syncs UI index with GameEngine's active player
    private void syncCurrentPlayer() {
        Player gameEngineCurrentPlayer = super.getCurrentPlayer();
        List<Player> players = getPlayers();
        this.currentPlayerIndex = players.indexOf(gameEngineCurrentPlayer);
    }
    
//loadPhrase() takes a phrase or word as a String param, then sets the Puzzle Board correctly
    public void loadPhrase(String phrase) {
        currentPhraseLayout = phraseLayout.layoutPhrase(phrase);
        displayPhrase(currentPhraseLayout);
    }
    
//displayPhrase() formats the Puzzle Board
    private void displayPhrase(List<List<LetterSquare>> layout) {
        PuzzleBoard_VBox.getChildren().clear();

        for (List<LetterSquare> row : layout) {

            HBox rowBox = new HBox(5);
            rowBox.setAlignment(Pos.CENTER);

            for (LetterSquare square : row) {
                if (square.isSpace()) {
                    Region spacer = new Region();
                    spacer.setPrefWidth(20);
                    rowBox.getChildren().add(spacer);
                } else {
                    Label label = createLetterSquare(square);
                    rowBox.getChildren().add(label);
                }
            }

            PuzzleBoard_VBox.getChildren().add(rowBox);
        }
    }

//createLetterSquare() takes each LetterSquare as a param and formats the styling. 
// It then stores reference to the state in .setUserData
    private Label createLetterSquare(LetterSquare square) {
        Label label = new Label(square.isGuessed()
                ? String.valueOf(square.getLetter())
                : "");

        label.setPrefSize(40, 40);
        label.setAlignment(Pos.CENTER);

        label.setStyle(
                "-fx-border-color: #2c3e50; -fx-border-width: 2;"
                + "-fx-background-color: white; -fx-font-size: 20px; -fx-font-weight: bold;"
        );

        label.setUserData(square);
        return label;
    }

//updateDisplay() sets the current instance of the Puzzle Board. Either guessed letter, !guessed letter, or space.
    private void updateDisplay() {
        for (int i = 0; i < PuzzleBoard_VBox.getChildren().size(); i++) {
            HBox rowBox = (HBox) PuzzleBoard_VBox.getChildren().get(i);

            for (javafx.scene.Node node : rowBox.getChildren()) {
                if (node instanceof Label label) {
                    LetterSquare square = (LetterSquare) label.getUserData();
                    if (square != null && square.isGuessed()) {
                        label.setText(String.valueOf(square.getLetter()));
                    }
                }
            }
        }
    }

    // Override the guessLetter method to maintain UI functionality while using GameEngine logic
    public void guessLetterForUI(char letter) {

        letter = Character.toUpperCase(letter);

        // Call parent's guessLetter (void return)
        super.guessLetter(letter);
        int matches = 0;

        for(List<LetterSquare> row : currentPhraseLayout) {
            for(LetterSquare square : row) {
                if(!square.isSpace() && square.getLetter() == letter && !square.isGuessed()) {
                    square.setGuessed(true);
                    matches++;
                }
            }
        }

        if(matches > 0) {
            updateDisplay();
        }
        // Update UI display
        //guessLetterUI(letter);
    }

    // Rename the existing guessLetter to avoid conflict with parent
    public int guessLetterUI(char letter) {
        letter = Character.toUpperCase(letter);
        int matches = 0;

        for (List<LetterSquare> row : currentPhraseLayout) {
            for (LetterSquare square : row) {
                if (!square.isSpace()
                        && square.getLetter() == letter
                        && !square.isGuessed()) {

                    square.setGuessed(true);
                    matches++;
                }
            }
        }

        if (matches > 0) updateDisplay();
        return matches;
    }

//GuessLetterButton_clicked() uses the userInput_TextField to receive a letter, and sends it to guessLetter()
    @FXML
    private void GuessLetterButton_clicked() {

        if(!hasSpunThisTurn) {
            Result_Label.setText("You must spin the wheel first!");
            return;
        }
        if (super.getCurrentPhrase() == null) {
            Result_Label.setText("No phrase loaded.");
            return;
        }

        String input = userInput_TextField.getText().trim().toUpperCase();

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            Result_Label.setText("Enter a single letter.");
            return;
        }

        char letter = input.charAt(0);
        updateLetterLabel(letter);
        
        guessLetterForUI(letter);
        
        int matches = countMatchesInUI(letter);
        Player currentPlayer = super.getCurrentPlayer();

        if (matches > 0) {
            int earned = matches * lastWheelValue;
            currentPlayer.addPoints(earned);
            
            syncCurrentPlayer();
            updatePlayerScore(currentPlayerIndex + 1, currentPlayer.getPoints());

            Result_Label.setText(
                currentPlayer.getName() + " guessed " + letter + " and found " + matches +
                " match(es), earning " + earned + " points!");
            
            //Resets the spin state
            hasSpunThisTurn = false;

            System.out.println("=== CHECKING IF SOLVED ===");
            System.out.println("Current phrase: " + super.getCurrentPhrase());
            System.out.println("Is Solved? " + super.getCurrentPhrase().isSolved());

            // Check if phrase is solved
            if (super.getCurrentPhrase().isSolved()) {
                System.out.println(">>> PUZZLE SOLVED BY: " + currentPlayer.getName() + " <<<");
                loadNewRound();
                return;
            }

            if(currentPlayer instanceof ComputerPlayer) {
                compController.checkAndExecuteComputerTurn();
            }

        } else {
            Result_Label.setText(currentPlayer.getName() + " guessed " + letter + ". No matches. Next player's turn.");
            hasSpunThisTurn = false;
            super.nextPlayer();
            syncCurrentPlayer();
            highlightCurrentPlayer();
        }

        

        userInput_TextField.clear();
    }

    public void computerGuessLetter(char letter) {
        userInput_TextField.setText(String.valueOf(letter));
        GuessLetterButton_clicked();
    }

//SolvePuzzleButton_clicked() uses the userInput_TextField to receive the complete word or phrase, and checks it against the puzzle
    @FXML
    private void SolvePuzzleButton_clicked() {

        if(!hasSpunThisTurn) {
            Result_Label.setText("You must spin the wheel first!");
            return;
        }

        String attempt = userInput_TextField.getText().trim().toUpperCase();

        if (attempt.isEmpty()) {
            Result_Label.setText("Enter a solution.");
            return;
        }

        // Get the actual current phrase being displayed
        String actualPhrase;
        if (super.getCurrentPhrase() != null) {
            actualPhrase = super.getCurrentPhrase().getAnswer().toUpperCase().trim();
        } else {
            // Fallback to the phrase bank if no GameEngine phrase
            actualPhrase = "WHEEL OF FORTUNE"; // Default phrase
        }

        // Debug output to help troubleshoot
        System.out.println("User attempt: '" + attempt + "'");
        System.out.println("Actual phrase: '" + actualPhrase + "'");

        // Use GameEngine's solvePhrase method
        super.solvePhrase(attempt);

        if (attempt.equalsIgnoreCase(actualPhrase)) {
            Result_Label.setText("Correct! The phrase was: " + actualPhrase);
            revealAllLetters();
            hasSpunThisTurn = false;
            loadNewRound();
        } else {
            Result_Label.setText("Incorrect. The phrase is: " + actualPhrase + ". Next player's turn.");
            hasSpunThisTurn = false;
            super.nextPlayer();
            syncCurrentPlayer();
            highlightCurrentPlayer();
        }

        userInput_TextField.clear();
    }

//revealAllLetters() displays completed puzzle after user guesses correctly
    private void revealAllLetters() {
        for (List<LetterSquare> row : currentPhraseLayout) {
            for (LetterSquare square : row) {
                if (!square.isSpace()) {
                    square.setGuessed(true);
                }
            }
        }
        updateDisplay();
    }

//SpinButton_clicked() spins the wheel
    @FXML
    private void SpinButton_clicked() {
        if (wheel != null && !wheel.isSpinning()) {
            wheel.spin();
            //hasSpunThisTurn = true;
            // Note: GameEngine's spinWheel() is called in handleWheelResult
        }
    }

    public void computerSpin() {
        SpinButton_clicked();
    }

//setupWheel() adds the game wheel to the GUI. Updates the Result_Label (beneath the wheel), and sends the result to handleWheelResult()
    private void setupWheel() {
        wheel = new Wheel(300);

        wheel.setOnSpinComplete(result -> {
            Result_Label.setText("Result: " + result);
            handleWheelResult(result);
        });

        Wheel_Container.getChildren().add(wheel);
    }

//handleWheelResult() receives the result from the Game Wheel, and displays the result in the console
    private void handleWheelResult(String result) {
        Player currentPlayer = super.getCurrentPlayer();

        switch (result) {
            case "Bankrupt":
                currentPlayer.addPoints(-currentPlayer.getPoints());
                lastWheelValue = 0;
                hasSpunThisTurn = false;
                syncCurrentPlayer();
                updatePlayerScore(currentPlayerIndex + 1, currentPlayer.getPoints());
                Result_Label.setText(currentPlayer.getName() + " went bankrupt!");
                super.nextPlayer();
                syncCurrentPlayer();
                highlightCurrentPlayer();
                break;

            case "Lose Turn":
                lastWheelValue = 0;
                Result_Label.setText(currentPlayer.getName() + " lost their turn!");
                hasSpunThisTurn = false;
                super.nextPlayer();
                syncCurrentPlayer();
                highlightCurrentPlayer();
                break;

            default:
                try {
                    int pts = Integer.parseInt(result.replace("$", ""));
                    lastWheelValue = pts;
                    hasSpunThisTurn = true;
                    
                    // Use GameEngine's spinWheel if it's a regular round
                    if (super.getCurrentRound() instanceof regularRound) {
                        super.spinWheel();
                    }
                    
                    Result_Label.setText(currentPlayer.getName() + " spun for " + pts + " points. Guess a letter!");
                } catch (Exception e) {
                    lastWheelValue = 0;
                    hasSpunThisTurn = false;
                    Result_Label.setText("Invalid wheel result.");
                }
                break;
        }
    }

//updatePlayerScore() receives the playerNumber and total score as params, sets individual score
    public void updatePlayerScore(int num, int score) {
        String s = "$" + score;

        switch (num) {
            case 1 -> score1_TextField.setText(s);
            case 2 -> score2_TextField.setText(s);
            case 3 -> score3_TextField.setText(s);
        }
    }

//updateAllScores() receives all three players scores as params and sets them
    public void updateAllScores(int a, int b, int c) {
        updatePlayerScore(1, a);
        updatePlayerScore(2, b);
        updatePlayerScore(3, c);
    }

 // Resets board & UI between rounds
    private void prepareNextRound() {

        lastWheelValue = 0;
        hasSpunThisTurn = false;
        guessedLetters.clear();
        Letters_Label.setText("Guessed: ");

        // Reset all square states
        if (currentPhraseLayout != null) {
            for (List<LetterSquare> row : currentPhraseLayout) {
                for (LetterSquare square : row) {
                    square.setGuessed(false);
                }
            }
        }

        //updateDisplay();
        Letters_Label.setText("Guessed: ");

        // Set starting player
        currentPlayerIndex = nextRoundStarterIndex;

        for(Player player : getPlayers()) {
            if(player instanceof ComputerPlayer) {
                ((ComputerPlayer) player).resetLetterSelection();
            }
        }
    }

 // Highlights the active player's label in UI
    private void highlightCurrentPlayer() {

        Player1_Label.setStyle("");
        Player2_Label.setStyle("");
        Player3_Label.setStyle("");

        switch (currentPlayerIndex) {
            case 0 -> Player1_Label.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            case 1 -> Player2_Label.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            case 2 -> Player3_Label.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        }

        compController.checkAndExecuteComputerTurn();
    }

//updateLetterLabel() this sets the Label (beneath Puzzle Board) for bonus round, or used letters if need be. 
    public void updateLetterLabel(char letter) {
        letter = Character.toUpperCase(letter);

        if (!guessedLetters.contains(letter)) {
            guessedLetters.add(letter);
            Letters_Label.setText("Guessed: " + guessedLetters);
        }
    }

    public boolean canComputerGuess() {
        return hasSpunThisTurn;
    }

    public ArrayList<Character> getGuessedLetters() {
        return new ArrayList<>(guessedLetters);
    }

    private int countMatchesInUI(char letter) {
        int matches = 0;
        for(List<LetterSquare> row : currentPhraseLayout) {
            for(LetterSquare square : row) {
                if(!square.isSpace() && square.getLetter() == letter && square.isGuessed()) {
                    matches++;
                }
            }
        }
        return matches;
    }

// Selects a random phrase from phraseBank
    private Phrase getRandomPhrase() {
        int index = (int)(Math.random() * phraseBank.length);
        String answer = phraseBank[index][0];
        String category = phraseBank[index][1];
        return new Phrase(answer, category);
    }

    private final String[][] phraseBank = {
        {"HELLO WORLD", "PHRASE"},
        {"JAVA PROGRAMMING", "TOPIC"},
        {"WHEEL OF FORTUNE", "TV SHOW"},
        {"COMPUTER SCIENCE", "SUBJECT"},
        {"NEW YORK CITY", "PLACE"},
        {"THE PRICE IS RIGHT", "TV SHOW"},
        {"SUMMER VACATION", "THING"},
        {"WINTER WONDERLAND", "THING"},
        {"THE UNITED STATES", "PLACE"},
        {"BREAKFAST SANDWICH", "FOOD"},
        {"GALAXY FAR AWAY", "MOVIE LINE"},
        {"SCIENCE AND TECHNOLOGY", "CATEGORY"},
        {"THE FINAL COUNTDOWN", "PHRASE"},
        {"UNDER THE WEATHER", "IDIOM"},
        {"FAMOUS LAST WORDS", "PHRASE"},
        {"A PIECE OF CAKE", "IDIOM"}
    };
}


