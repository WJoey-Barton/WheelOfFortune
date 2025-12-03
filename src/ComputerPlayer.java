import java.util.ArrayList;

public class ComputerPlayer extends Player{

    private HuffmanAlgo huffmanAlgo;
    private boolean isComputer;

    public ComputerPlayer(String name) {
        super(name);
        this.huffmanAlgo = new HuffmanAlgo();
        this.isComputer = true;
    }

    public char chooseLetter(ArrayList<Character> usedLetters) {

        String letter = huffmanAlgo.getLetterSet();
        char chosen = letter.toUpperCase().charAt(0);

        int attempts = 0;
        while(usedLetters.contains(chosen) && attempts < 26) {
            letter = huffmanAlgo.getLetterSet();
            chosen = letter.toUpperCase().charAt(0);
            attempts++;
        }
        return chosen;
    }

    public boolean isComputer() {
        return isComputer;
    }
    
    public void resetLetterSelection() {
        this.huffmanAlgo = new HuffmanAlgo();
    }
}
