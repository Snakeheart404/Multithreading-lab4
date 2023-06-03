import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class KeyWordsTask extends RecursiveTask<ArrayList<String>> {
    private File file;
    private  ArrayList<String> keywords;
    public KeyWordsTask(File file, ArrayList<String> keywords) {
        this.file = file;
        this.keywords = keywords;
    }

    @Override
    protected ArrayList<String> compute() {
        ArrayList<String> foundFiles = new ArrayList<>();
        if (file.isDirectory()) {
            List<KeyWordsTask> subTasks = new ArrayList<>();
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    KeyWordsTask subTask = new KeyWordsTask(f, keywords);
                    subTasks.add(subTask);
                    subTask.fork();
                }
                for (KeyWordsTask subTask : subTasks) {
                    foundFiles.addAll(subTask.join());
                }
            }
        } else {
            try {
                if(findFile())
                {
                    foundFiles.add(file.getPath());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return foundFiles;
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

    private boolean findFile() throws IOException {
        List<String> words = this.readWords();

        words.retainAll(keywords);
        return !words.isEmpty();

    }
}