package cz.upa.simulation.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static cz.upa.simulation.domain.Settings.ACTUAL_ID_BUILD;
import static cz.upa.simulation.utils.StringUtils.s;
import static cz.upa.simulation.utils.TimeUtils.systemNanoTime;

public class RecorderSizePerTime implements Runnable {
    String PATH = "";
    String id = s(ACTUAL_ID_BUILD);
    String fileName = PATH.concat(id).concat(".txt");
    String directoryName = PATH.concat("output");
    File file;
    BufferedWriter bw;
    FileWriter fw;

    // nahravaci kolekce, sem entity budou vkládat své zpravy o zmeně
    public static ConcurrentLinkedQueue<Double[]> recordMessages = new ConcurrentLinkedQueue<>();
    Queue<Double[]> que = new LinkedList<>();

    // kolekce pro casove znacky, ktere prislusi zmenam stavu
    List<Integer> popSizes = new LinkedList<>();
    List<Double> timeSigns = new LinkedList<>();


    double simulationTimeStart;
    double actualSimulationTime;
    double timeout = 0;
    double now = 0;
    private int actualSize = 0;
    private boolean isTimeToRenderRightNow = false;

    public RecorderSizePerTime() {
        this.simulationTimeStart = systemNanoTime();
        actualSimulationTime = simulationTimeStart;
        popSizes.add(actualSize);
        timeSigns.add(timeout);

        file=  new File(directoryName + "/" +fileName);
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {

                //income - nastaveni stopek, vybrani "posty",...
                now = systemNanoTime();
                timeout = timeout + (now - actualSimulationTime);
                actualSimulationTime = now;
                withdrawMessages();


                // zpracovani zprav, tj. zarazeni zpravy do kolekce,....
                while (!que.isEmpty()) {
                    Double[] data = que.poll();
                    if (data[0] == 0) { // 0 = "dead"
                        double timeSign = data[1] - simulationTimeStart;
                        bw.write(--actualSize + ";" + timeSign);
                    }
                    if (data[0] == 1.0) { // 1 = "new_born"
                        double timeSign = data[1] - simulationTimeStart;
                        bw.write(--actualSize + ";" + timeSign);
                    }
                }
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bw.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void withdrawMessages() {
        que = new LinkedList<>(recordMessages);
        recordMessages = new ConcurrentLinkedQueue<>();
    }
}
