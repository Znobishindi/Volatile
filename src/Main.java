import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static volatile boolean toggleSwitchPosition = false;
    private final static int NUMBER_OF_ATTEMPTS = 10;
    private final static int WAITING = 1000;

    public static void main(String[] args) {
        ReentrantLock locker = new ReentrantLock();
        Condition cond = locker.newCondition();

        Thread user = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
                try {
                    Thread.sleep(WAITING);

                    locker.lock();
                    System.out.println("Пользователь включает тумблер");
                    toggleSwitchPosition = true;
                    cond.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    locker.unlock();
                }
            }
        });
        Thread box = new Thread(() -> {


            while (user.isAlive()) {
                try {
                    locker.lock();

                    while (!toggleSwitchPosition) {
                        cond.await();
                    }
                    System.out.println("Волшебная коробка выключает тумблер");
                    toggleSwitchPosition = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    locker.unlock();
                }
            }
        });
        user.start();
        box.start();
    }
}
