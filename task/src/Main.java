import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\Polya\\IdeaProjects\\lab4\\task\\dataBigger");

        HashMap<Integer, Long> result = new HashMap<>();

        int threads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(threads);
        System.out.println("Threads number: " + threads);

        long start = System.currentTimeMillis();
        WordLengthTask task = new WordLengthTask(dir);
        result = forkJoinPool.invoke(task);
        long end = System.currentTimeMillis();
        System.out.println("TIME PARALLEL: " + (end - start));



        for (int key : result.keySet()) {
            System.out.println(key + " " + result.get(key));
        }

        System.out.println();

        long start1 = System.currentTimeMillis();
        WordLength wl = new WordLength(dir);
        long end1 = System.currentTimeMillis();
        System.out.println("TIME SEQUENTIAL: " + (end1 - start1));
        HashMap<Integer, Long> resultSequential = wl.getWordLengths();
        for (int key : resultSequential.keySet()) {
            System.out.println(key + " " + resultSequential.get(key));
        }
        System.out.println();
        System.out.println();

        RandomVariableCharacteristics characteristics = new RandomVariableCharacteristics(result);
        HashMap<Integer, Double> distributionLaw = characteristics.getDistributionLaw();
        System.out.println("Закон розподілу:");
        for (int key : distributionLaw.keySet()) {
            System.out.println(key + " " + distributionLaw.get(key));
        }
        System.out.println();
        System.out.println("Мат. сподівання: " + characteristics.getMean());

        System.out.println();
        System.out.println("Дисперсія: " + characteristics.getVariance());

        System.out.println();
        System.out.println("Середнє квадратичне відхилення: " + characteristics.getStandardDeviation());
    }
}