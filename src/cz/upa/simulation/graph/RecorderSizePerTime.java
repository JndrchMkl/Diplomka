package cz.upa.simulation.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static cz.upa.simulation.domain.Settings.ACTUAL_ID_BUILD;
import static cz.upa.simulation.domain.Settings.IS_SIMULATION_RUNNING;
import static cz.upa.simulation.utils.StringUtils.s;
import static cz.upa.simulation.utils.TimeUtils.systemNanoTime;

public class RecorderSizePerTime implements Runnable {
    private String PATH;
    private String id;
    private String fileName;
    private String directoryName;
    private File file;
    private BufferedWriter bw;
    private FileWriter fw;

    // nahravaci kolekce, sem entity budou vkládat své zpravy o zmeně
    public static ConcurrentLinkedQueue<Double[]> recordMessages;
    private Queue<Double[]> que;


    private List<Integer> popSizes;
    private List<Double> timeSigns;


    private double simulationTimeStart;
    private double actualSimulationTime;
    private double timeout;
    private int actualSize;

    public RecorderSizePerTime() {
        this.simulationTimeStart = systemNanoTime();
        actualSimulationTime = simulationTimeStart;
        PATH = "";
        id = s(ACTUAL_ID_BUILD);
        fileName = PATH.concat(id).concat(".csv");
        directoryName = PATH.concat("output");
        timeout = 0;
        actualSize = 0;
        popSizes = new LinkedList<>();
        timeSigns = new LinkedList<>();
        que = new LinkedList<>();
        recordMessages = new ConcurrentLinkedQueue<>();

        popSizes.add(actualSize);
        timeSigns.add(timeout);

        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        file = new File(directoryName + "/" + fileName);
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
            while (IS_SIMULATION_RUNNING) {
                withdrawMessages();

                // zpracovani zprav, tj. zarazeni zpravy do kolekce,....
                while (!que.isEmpty()) {
                    Double[] data = que.poll();
                    double timeSign = data[1] - simulationTimeStart;
                    if (data[0] == 0.0) { // 0 = "dead"
                        bw.write(l("--", s(new BigDecimal(timeSign)),s(BigDecimal.valueOf(data[2])),s(BigDecimal.valueOf(data[3])),s(data[4]),s(data[5])));
                    }
                    if (data[0] == 1.0) { // 1 = "new_born"
                        bw.write(l("++" ,s(new BigDecimal(timeSign)),s(BigDecimal.valueOf(data[2])),s(BigDecimal.valueOf(data[3])),s(data[4]),s(data[5])));
                    }
                    if (data[0] == 2.0) { // 2 = "steal"
                        bw.write(l("steal" ,s(new BigDecimal(timeSign)),s(new BigDecimal(data[2])),s(BigDecimal.valueOf(data[3])),s(data[4]),s(data[5])));
                    }
                    if (data[0] == 3.0) { // 3 = "murder"
                        bw.write(l("murder", s(new BigDecimal(timeSign)), s(new BigDecimal(data[2])), s(BigDecimal.valueOf(data[3])), s(data[4]), s(data[5])));
                    }
                }
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String l(String... lineList) {
        StringBuilder line= new StringBuilder();
        for (String item : lineList) {
            line.append(item).append(";");
        }
        line.append("\n");
        return line.toString();
    }
    private void withdrawMessages() {
        que = new LinkedList<>(recordMessages);
        recordMessages.clear();
    }
}
