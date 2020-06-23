package messeges2.graph;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static messeges2.TimeUtils.systemSecondsTime;

public class Test {

    public static void main(String[] args) throws Exception {

        double phase = 0;
        double[][] initdata = getSineData(phase);

        // Create Chart
        final XYChart chart = QuickChart.getChart("Simple XChart Real-time Demo", "Time", "nEntities", "sine", initdata[0], initdata[1]);

        // Show it
        final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();
        double timeBegin = System.nanoTime();
        double timeStopWatch = 0;

        List<Double> times = new LinkedList<>();
        List<Double> nEntities = new LinkedList<>();
        while (true) {
            double now = System.nanoTime();
            double interval = now - timeBegin; // nutno pocitat s intervalem rovnym 0. POZOR!!! Nedělit s intervalem rovným 0!!!
            timeStopWatch += interval;
            timeBegin = now;
            phase += 2 * Math.PI * 2 / 20.0;

            Thread.sleep(100);
            times.add(timeStopWatch / 1000000000);
            nEntities.add(ThreadLocalRandom.current().nextDouble(0.0, 100.0));

            chart.updateXYSeries("sine", times, nEntities, null);
            sw.repaintChart();
        }

    }

    private static double[][] getSineData(double phase) {

        double[] xData = new double[100];
        double[] yData = new double[100];
        for (int i = 0; i < xData.length; i++) {
            double radians = phase + (2 * Math.PI / xData.length * i);
            xData[i] = radians;
            yData[i] = Math.sin(radians);
        }
        return new double[][]{xData, yData};
    }
}
