package messeges2.utils;

public final class StringUtils {
    public static String s(Double number) {
        return number + "";
    }

    public static String s(Integer number) {
        return number + "";
    }

    public static Double d(String s) {
        return Double.valueOf(s);
    }
}
