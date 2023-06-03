import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\Polya\\IdeaProjects\\lab4\\task\\data");

        HashSet<String> result = new HashSet<String>();

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        CommonWordsTask task = new CommonWordsTask(dir);
        result = forkJoinPool.invoke(task);

        for (String wordResult : result)
        {
            System.out.println(wordResult);
        }

    }
}