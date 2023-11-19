package delivery.utils;

public class Utils {
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds* 1000L);
        } catch (Exception ignore) {}
    }
}
