import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 author Cay Horstmann
 */
public class AsynchBankTest {
    public static final int NACCOUNTS = 1000;
    public static final int INITIAL_BALANCE = NACCOUNTS * 10000;
    public static void main(String[] args) {
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);

        ///old
//        ArrayList<TransferThread> threads = new ArrayList<>();
//        for (int i = 0; i < NACCOUNTS; i++) {
//            threads.add(new TransferThread(b, i, INITIAL_BALANCE));
//        }
//        try {
//            long start = System.currentTimeMillis();
//            for (var thread : threads) {
//                thread.start();
//            }
//            for (var thread : threads) {
//                thread.join();
//            }
//
//            long end = System.currentTimeMillis();
//
//            System.out.println("TIME: " + (end - start));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        ///


        ForkJoinPool pool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        for (int i = 0; i < NACCOUNTS; i++){
            TransferTask t = new TransferTask(b, i, INITIAL_BALANCE);
            pool.invoke(t);
        }

        pool.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("TIME: " + (end - start));

    }
}