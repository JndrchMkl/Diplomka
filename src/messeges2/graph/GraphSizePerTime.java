package messeges2.graph;

import messeges2.Entita;
import messeges2.Matrika;
import messeges2.PostOffice;
import messeges2.Settings;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

import static messeges2.MessageType.WE_HAVE_A_BABY;
import static messeges2.StringUtils.d;

public class GraphSizePerTime implements Runnable {

    public static final String CHART_TITLE = "Population size per time";
    public static final String X_TITLE = "Time";
    public static final String Y_TITLE = "nEntities";
    public static final String SERIES_NAME = "population";

    private Matrika matrika = new Matrika();
    private PostOffice postOffice = new PostOffice();
    private boolean isRunning = true;

    @Override
    public void run() {
        double timeBegin = System.nanoTime();
        double timeStopWatch = 0;
        double[][] initdata = new double[2][1];
        List<Double> times = new LinkedList<>();
        List<Integer> nEntities = new LinkedList<>();
        int nRecords = 0;


        initdata[0] = new double[]{0};
        initdata[1] = new double[]{2};

        // Create Chart
        final XYChart chart = QuickChart.getChart(CHART_TITLE, X_TITLE, Y_TITLE, SERIES_NAME, initdata[0], initdata[1]);

        // Show it
        final SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
        sw.displayChart();
        try {
            startSimulation();

            while (isRunning) {
                double now = System.nanoTime();
                double interval = now - timeBegin; // nutno pocitat s intervalem rovnym 0. POZOR!!! Nedělit s intervalem rovným 0!!!
                timeStopWatch += interval;
                timeBegin = now;

                times.add(timeStopWatch / 1000000000);
                nEntities.add(matrika.nRecords());

                chart.updateXYSeries(SERIES_NAME, times, nEntities, null);
                sw.repaintChart();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void startSimulation() {
        for (int i = 0; i < 2; i++) {
            Double talent = ThreadLocalRandom.current().nextDouble(Settings.RANGE_TALENT_FROM, Settings.RANGE_TALENT_TO + 1);
            new Entita(matrika, postOffice, 0.0, talent);
        }
    }
}
