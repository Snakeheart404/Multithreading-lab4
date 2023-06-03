import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class CommonWordsTask extends RecursiveTask<HashSet<String>> {
    private File file;
    private  HashSet<String> commonWords = new HashSet<>();
    public CommonWordsTask (File file) {
        this.file = file;
    }

    @Override
    protected HashSet<String> compute() {
        if (file.isDirectory()) {
            List<CommonWordsTask> subTasks = new ArrayList<>();
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    CommonWordsTask subTask = new CommonWordsTask(f);
                    subTasks.add(subTask);
                    subTask.fork();
                }
                for (CommonWordsTask subTask : subTasks) {
                    findCommonWords(subTask.join());
                }
            }
        } else {
            try {
                readWords();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.commonWords;
    }

    private boolean isValidToken(String token) {
        return token.matches("\\p{L}[\\p{L}-']*");
    }

    private void readWords() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] tokens = currentLine.split("[\\s,:;.?!]+");
            for (String token : tokens) {
                if ((isValidToken(token))) {
                    commonWords.add(token.toLowerCase());
                }
            }
        }
    }

    private void findCommonWords(HashSet<String> words) {
        if (commonWords.isEmpty()) {
            commonWords.addAll(words);
        }
        else{
            commonWords.retainAll(words);
        }
    }
}