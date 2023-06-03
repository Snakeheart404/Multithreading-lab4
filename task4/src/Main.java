import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\Polya\\IdeaProjects\\lab4\\task\\data");

        ArrayList<String> keywordsIT = new ArrayList<>(Arrays.asList("algorithm", "computer", "program"));

        for (int i = 0; i < keywordsIT.size(); i++) {
            String lowercase = keywordsIT.get(i).toLowerCase();
            keywordsIT.set(i, lowercase);
        }
        //ArrayList<String> keywords = new ArrayList<>(Arrays.asList("frog", "cat"));
        ArrayList<String> result = new ArrayList<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        KeyWordsTask task = new KeyWordsTask(dir, keywordsIT);
        result = forkJoinPool.invoke(task);

        System.out.print("For keywords [ ");
        for (int i = 0; i < keywordsIT.size(); i++)
        {
            System.out.print(keywordsIT.get(i) + " ");
        }
        System.out.println("] the following documents were found: ");
       for (int i = 0; i < result.size(); i++)
       {
           System.out.println(result.get(i));
       }

    }
}