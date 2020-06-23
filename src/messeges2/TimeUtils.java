package messeges2;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.Instant;

public class TimeUtils extends Thread {


    public static long systemMillisTime() {
        return System.currentTimeMillis();
    }

    public static long systemNanoTime() {
        return System.nanoTime();
    }

    public static double systemSecondsTime() {
        return System.nanoTime() / 1000000000.0;
    }

    public static long nowNanoTime() {
        return Instant.now().getNano();
    }

    public static long threadCpuTime() {
        return ManagementFactory.getThreadMXBean()
                .getThreadCpuTime(Thread.currentThread().getId());
    }


//    private static void performanceTest() {
//        TimeUtils tu = new TimeUtils();
//        for (int i = 0; i < 10; i++) {
//            long now = tu.nowNanoTime();
//            long later = tu.nowNanoTime();
//            System.out.println("Elapsed time was " + (now - later) + " nanoseconds.");
//        }
//    }
//
//    public static void main(String[] args) {
//        performanceTest();
//    }
}
