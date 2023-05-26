import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank {
    public static final int NTEST = 10000;
    private final int[] accounts;
    private long ntransacts = 0;

    private final Object sync = new Object();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        int i;
        for (i = 0; i < accounts.length; i++) {
            accounts[i] = initialBalance;
        }
        ntransacts = 0;
    }

    //початковий метод
    public void transfer(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;
        if (ntransacts % NTEST == 0) {
            test();
        }
    }

    //синхронізований метод
    public synchronized void syncTransfer(int from, int to, int amount) {
        while (accounts[from] < amount) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;
        notifyAll();
        if (ntransacts % NTEST == 0) {
            test();
        }
    }

    //метод з використанням синхронізованого блоку
    public void syncBlockTransfer(int from, int to, int amount) {
        synchronized (sync) {
            while (accounts[from] < amount) {
                try {
                    sync.wait();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            sync.notifyAll();
            if (ntransacts % NTEST == 0) {
                test();
            }
        }
    }
    //метод з використанням ReentrantLock
    public void lockTransfer(int from, int to, int amount) {
        lock.lock();
        try {
            while (accounts[from] < amount) {
                condition.await();
            }
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            condition.signalAll();
            if (ntransacts % NTEST == 0) {
                test();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }
    }
    public void test() {
        int sum = 0;
        for (int i = 0; i < accounts.length; i++) {
            sum += accounts[i];
        }
        System.out.println("Transactions:" + ntransacts+ " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }
}