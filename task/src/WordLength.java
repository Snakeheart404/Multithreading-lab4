import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordLength {
    private final HashMap<Integer, Long> wordLengths = new HashMap<>();

    public WordLength(File file) {
        computeWordLengths(file);
    }

    public HashMap<Integer, Long> getWordLengths() {
        return wordLengths;
    }

    private void computeWordLengths(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    computeWordLengths(f);
                }
            }
        } else {
            try {
                var lengths = this.getWordLength(file);
                this.appendLengths(lengths);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private List<String> readWords(File file) throws IOException {
        List<String> wordList = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] tokens = currentLine.split("[\\s,:;.?!]+");
            for (String token : tokens) {
                if ((isValidToken(token))) {
                    wordList.add(token.toLowerCase());
                }
            }
        }
        return wordList;
    }

    private void appendLengths(HashMap<Integer, Long> lengths) {
        for (var length : lengths.entrySet()) {
            var key = length.getKey();
            var value = length.getValue();
            if (wordLengths.containsKey(length.getKey())) {
                wordLengths.put(key, wordLengths.get(key) + value);
            } else {
                wordLengths.put(key, value);
            }
        }
    }

    private HashMap<Integer, Long> getWordLength(File file) throws IOException {
        HashMap<Integer, Long> result = new HashMap<>();
        List<String> words = readWords(file);
        for (String word : words) {
            int length = word.length();
            if (wordLengths.containsKey(length)) {
                wordLengths.put(length, wordLengths.get(length) + 1);
            } else {
                wordLengths.put(length, 1L);
            }
        }
        return result;
    }

    private boolean isValidToken(String token) {
        return token.matches("\\p{L}[\\p{L}-']*");
    }
}
