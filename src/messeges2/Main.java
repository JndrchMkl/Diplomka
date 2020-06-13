package messeges2;

import org.w3c.dom.ls.LSOutput;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int x = 4;
        HashMap<String, Queue<String>> postOffice = new HashMap<>();
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            while (true) {
                System.out.println("Hello " + threadName);
                System.out.print(Arrays.toString(postOffice.get(threadName).toArray()));
                postOffice.get("Thread-4").add("ZabijimTe, ");
            }

        };

        int i = 0;
        while (i++ < 5) {
            Thread thread = new Thread(task);
            postOffice.put(thread.getName(), new LinkedList<>());
            thread.start();

        }

        task.run();

        System.out.println("Done!");
    }
}
