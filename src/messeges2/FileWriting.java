package messeges2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileWriting<T> {

    private static final double MEG = (Math.pow(1024, 2));
    private static final int RECORD_COUNT = 4000000;
    private static final String RECORD = "Help I am trapped in a fortune cookie factory\n";
    private static final int RECORD_SIZE = RECORD.getBytes().length;
    private final String name;
    private final String path;

    public FileWriting(String path, String name) {
        this.name = name;
        this.path = path;
    }

    public static void main(String[] args) throws Exception {
        FileWriting<String> fw = new FileWriting<>(FileWriting.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "kkk");
        List<String> records = new ArrayList<>(RECORD_COUNT);
        int size = 0;
        for (int i = 0; i < RECORD_COUNT; i++) {
            records.add(RECORD);
            size += RECORD_SIZE;
        }
        System.out.println(records.size() + " 'records'");
        System.out.println(size / MEG + " MB");

        for (int i = 0; i < 10; i++) {
            System.out.println("\nIteration " + i);

            fw.writeRaw(records);
            fw.writeBuffered(records, 8192);
            fw.writeBuffered(records, (int) MEG);
            fw.writeBuffered(records, 4 * (int) MEG);
        }
    }

    private void writeRaw(List<T> records) {
        try {
            File file = new File(name + ".txt");
            FileWriter writer = null;
            writer = new FileWriter(file);

            write(records, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBuffered(List<T> records, int bufSize) {
        try {

            File file = new File(name + ".txt");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

            write(records, bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(List<T> records, Writer writer) throws IOException {
        for (T record : records) {
            writer.write("" + (record));
        }
        // writer.flush(); // close() should take care of this
        writer.close();
    }
}
