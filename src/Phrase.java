/* Christina Kc 
Phrase.java

This class manages puzzle answers and tracks revealed letters for Wheel of Fortune
 */


import java.util.ArrayList;


public class Phrase {

    private String answer;

    private final String category;

    private ArrayList revealedLetters;

    private String[] words;



    public Phrase(String answer, String category) {

        this.answer = answer;
        this.category = category;
        setAnswer(answer);
    }



    public void setAnswer(String answer) {

        this.answer = answer.toUpperCase();
        this.revealedLetters = new ArrayList();
        this.words = answer.toUpperCase().split(" ");


        
        for (int i = 0; i < this.answer.length(); i++) {

            char currentChar = this.answer.charAt(i);

            if (currentChar == ' ') {

                revealedLetters.add(' ');

            } else {

                revealedLetters.add(null);

            }
        }
    }

    public String getAnswer() {

        return answer;
    }

    public void revealLetter(char letter) {

        letter = Character.toUpperCase(letter);

        
        for (int i = 0; i < answer.length(); i++) {

            if (answer.charAt(i) == letter && revealedLetters.get(i) == null) {

                revealedLetters.set(i, letter);
            }
        }
    }

    public String getCategory() {
        return category;
    }

    public String[] getWords() {
        return words.clone();
    }

    public int getWordCount() {
        return words.length;
    }

    public boolean isLetterRevealed(int position) {

        if (position < 0 || position >= revealedLetters.size()) {

            return false;
        }

        return revealedLetters.get(position) != null;
    }



    public char getCharacterAt(int position) {

        if (position < 0 || position >= revealedLetters.size()) {
            return ' ';
        }

        Object ch = revealedLetters.get(position);

        return (ch != null) ? (Character) ch : ' ';
    }

    public char getCharAt(int position) {

        return getCharacterAt(position);
    }

    public boolean isRevealed(int position) {

        return isLetterRevealed(position);
    }

    public char[] getAllCharacters() {

        return answer.toCharArray();
    }

    public ArrayList getRevealedLetters() {

        return new ArrayList(revealedLetters);
    }

    public int getTotalCharacters() {

        return answer.replace(" ", "").length();
    }

    public void revealAllLetters() {

        for (int i = 0; i < answer.length(); i++) {

            revealedLetters.set(i, answer.charAt(i));
        }
    }

    public int getLength() {

        return answer.length();
    }

    public String getDisplayText() {

        StringBuilder display = new StringBuilder();

        for (int i = 0; i < answer.length(); i++) {

            Object currentChar = revealedLetters.get(i);

            if (currentChar == null) {

                display.append("_ ");

            } else if (currentChar.equals(' ')) {

                display.append("   ");

            } else {

                display.append(currentChar).append(" ");
            }
        }
        return display.toString().trim();
    }

    public boolean isSolved() {

        for (char c : answer.toCharArray()) {

            if (Character.isLetter(c) && !revealedLetters.contains(Character.toUpperCase(c))) {
                return false;
            }
        }
        return true;
    }

}
