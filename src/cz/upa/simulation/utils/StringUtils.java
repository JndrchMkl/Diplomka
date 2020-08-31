package cz.upa.simulation.utils;

import java.math.BigDecimal;

public final class StringUtils {
    public static String s(Double number) {
        return number + "";
    }

    public static String s(boolean condition) {
        return condition + "";
    }

    public static String s(Integer number) {
        return number + "";
    }

    public static String s(BigDecimal number) {
        return number + "";
    }

    public static Double d(String s) {
        return Double.valueOf(s);
    }
}
