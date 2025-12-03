import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class ComputerController {

    private sceneController controller;
    private static final int COM_SPIN_DELAY = 1500;
    private static final int COM_GUESS_DELAY = 6000;

    public ComputerController(sceneController controller) {
        this.controller = controller;
    }

    public void executeComputerTurn(ComputerPlayer computerPlayer) {
        PauseTransition spinDelay = new PauseTransition(Duration.millis(COM_SPIN_DELAY));
        spinDelay.setOnFinished(e -> {
            
            controller.computerSpin();
            PauseTransition guessDelay = new PauseTransition(Duration.millis(COM_GUESS_DELAY));
            guessDelay.setOnFinished(g -> {
                
                if(controller.canComputerGuess()) {
                    char letter = computerPlayer.chooseLetter(controller.getGuessedLetters());
                    controller.computerGuessLetter(letter);
                } 
            });
            guessDelay.play();
        });
        spinDelay.play();
    }

    public void checkAndExecuteComputerTurn() {
        Player currentPlayer = controller.getCurrentPlayer();

        if(currentPlayer instanceof ComputerPlayer) {
            executeComputerTurn((ComputerPlayer) currentPlayer); 
        }
    }
    
}
