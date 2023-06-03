import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class WordLengthTask extends RecursiveTask<HashMap<Integer, Long>> {
    private File file;
    private final HashMap<Integer, Long> wordLengths = new HashMap<>();
    public WordLengthTask(File file) {
        this.file = file;
    }

    @Override
    protected HashMap<Integer, Long> compute() {
        if (file.isDirectory()) {
            List<WordLengthTask> subTasks = new ArrayList<>();
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    WordLengthTask subTask = new WordLengthTask(f);
                    subTasks.add(subTask);
                    subTask.fork();
                }
                for (WordLengthTask subTask : subTasks) {
                    appendLengths(subTask.join());
                }
            }
        } else {
            try {
                getWordLength();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.wordLengths;
    }

    private boolean isValidToken(String token) {
        return token.matches("\\p{L}[\\p{L}-']*");
    }

    private List<String> readWords() throws IOException {
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

    private void getWordLength() throws IOException {
        List<String> words = readWords();
        for (String word : words) {
            int length = word.length();
            if (wordLengths.containsKey(length)) {
                wordLengths.put(length, wordLengths.get(length) + 1);
            } else {
                wordLengths.put(length, 1L);
            }
        }
    }
}