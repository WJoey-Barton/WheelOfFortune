//Joey Barton
//Takes the phrase as a param
//Formats for the Puzzle Board
import java.util.ArrayList;
import java.util.List;

public class PhraseLayout {

    private static final int MAX_CHARS_PER_ROW = 12;

    public List<List<LetterSquare>> layoutPhrase(String phrase) {

        phrase = phrase.toUpperCase();
        String[] words = phrase.split(" ");

        List<List<LetterSquare>> rows = new ArrayList<>();
        List<LetterSquare> currentRow = new ArrayList<>();

        int count = 0; // how many characters are already in this row

        for (String word : words) {

            int wordLength = word.length();

            // Do we need a space before adding this word?
            int spaceNeeded = 0;
            if (count > 0) {
                spaceNeeded = 1;
            }

            // Check if the row has room for space + word
            int totalNeeded = count + spaceNeeded + wordLength;

            // If not enough room → start new row
            if (totalNeeded > MAX_CHARS_PER_ROW) {
                rows.add(currentRow);
                currentRow = new ArrayList<>();
                count = 0;
            }

            // Add space between words (if not first word)
            if (count > 0) {
                currentRow.add(new LetterSquare(' ', true));
                count++;
            }

            // Add each character of the word
            for (char c : word.toCharArray()) {
                currentRow.add(new LetterSquare(c, false));
                count++;
            }
        }

        // Add last row if not empty
        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        return rows;
    }
}
