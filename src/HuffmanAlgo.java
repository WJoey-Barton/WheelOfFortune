//Hashmap of <Int, Array> with each array containing that level of the huffman's tree nodes
//Functionality to prevent duplicate letters being chosen.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HuffmanAlgo {

    private Map<Integer, String[]> Huffman;
    private List<String> usedLetters;
    private Random rand;

    public HuffmanAlgo() {
        this.Huffman = new HashMap<>();
        this.usedLetters = new ArrayList<String>();
        this.rand = new Random();

        //Vowels Separate
        String[] vowels = {"e", "o", "a", "u", "i"};

        //Vowels Included
        String[] level2 = {"e", "h", "r", "s", "n", "t", "a"};
        String[] level3 = {"c", "d", "l", "i", "o", "u"};
        String[] level4 = {"b", "p", "g", "y", "w", "m", "f"};
        String[] level5 = {"v", "k"};
        String[] level6 = {"x", "q", "z", "j"};

        Huffman.put(1, vowels);
        Huffman.put(2, level2);
        Huffman.put(3, level3);
        Huffman.put(4, level4);
        Huffman.put(5, level5);
        Huffman.put(6, level6);
    }

    public String getLetterSet() {
        //We want there to be a higher chance that the computer will choose a decent letter.
        //We want vowels separate, with a chance for the computer to buy a vowel if they have enough money.
        int num = rand.nextInt(1000);
        int letterSet;

        //50% Chance
        if(num < 500) {
            letterSet = 2;
            
        //25% Chance
        } else if(num < 750) {
            letterSet = 3;
        
        //15% Chance
        } else if(num < 900) {
            letterSet = 4;
            
        //5% Chance
        } else if (num < 950) {
            letterSet = 5;
        
        //5% Chance
        } else {
            letterSet = 6;
            
        }
        return charFromLetterSet(letterSet);
    }

    public String charFromLetterSet(int letterSet) {
        //The weighted random number is chosen, giving the selected set of letters
        String[] chosenLetterSet = Huffman.get(letterSet);

        //Gets available letters from chosen letter set.
        List<String> availableLetters = new ArrayList<String>();
        if(chosenLetterSet != null) {
            for(String letter : chosenLetterSet) {
                if(!usedLetters.contains(letter)) {
                    availableLetters.add(letter);
                }
            }
        }

        //If that letter set is null (all letters chosen and removed), choose from set of letters one set better.
        //If the letter set chosen is 2 and null, choose a letter from set 6.
        //This should be done recursively instead.
        if(availableLetters.isEmpty()) {
            if(letterSet < 6) {
                return charFromLetterSet(letterSet + 1);
            } else {
                return charFromLetterSet(2);
            }
        } 

        //Choose a random letter from the selected letter set
        String letterChoice = availableLetters.get(rand.nextInt(availableLetters.size()));
        usedLetters.add(letterChoice);
        return letterChoice;
    }

    
}
