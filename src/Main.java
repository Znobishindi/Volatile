public class Main {
    private static volatile boolean toggleSwitchPosition = false;
    private final static int NUMBER_OF_ATTEMPTS = 10;
    private final static int WAITING = 1000;

    public static void main(String[] args) {

        Thread user = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
                try {
                    Thread.sleep(WAITING);
                    System.out.println("Пользователь включает тумблер");
                    toggleSwitchPosition = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread box = new Thread(() -> {


            while (user.isAlive()) {
                if (toggleSwitchPosition) {
                    System.out.println("Волшебная коробка выключает тумблер");
                    toggleSwitchPosition = false;
                }
            }
        });
        user.start();
        box.start();
    }
}

