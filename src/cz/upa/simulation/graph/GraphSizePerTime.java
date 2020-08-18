package cz.upa.simulation.graph;

import cz.upa.simulation.output.FileReading;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class GraphSizePerTime implements Runnable {

    public static final String CHART_TITLE = "Population size per time";
    public static final String X_TITLE = "Time";
    public static final String Y_TITLE = "nEntities";
    public static final String SERIES_NAME = "population";


    @Override
    public void run() {
        TreeMap<Double, Double> map = new TreeMap<>(); // sorted pair of values
        List<Double> nEntities = new LinkedList<>();
        List<Double> timeSeries = new LinkedList<>();
        try {
            BufferedReader reader = FileReading.readActualBuildOutput();
            if (reader != null) {
                String line = "";
                String[] data;
                while (line != null) {
                    line = reader.readLine();
                    if (line != null) {
                        data = line.split(";");
                        double n = Double.parseDouble(data[0]);
                        double time = Double.parseDouble(data[1]) / 1000000000.0;
                        map.put(time < 0 ? 0 : time, n);
                        nEntities.add(n);
                        timeSeries.add(time < 0 ? 0 : time);
                    }
                }
            }

            // Create Chart
            final XYChart chart = QuickChart.getChart(CHART_TITLE, X_TITLE, Y_TITLE, SERIES_NAME, timeSeries, nEntities);
            final XYChart chart1 = QuickChart.getChart(CHART_TITLE, X_TITLE, Y_TITLE, SERIES_NAME,
                    new ArrayList<Double>(map.keySet()), new ArrayList<Double>(map.values()));
            // Customize Chart
            customise(chart1);

            // Show it
            final SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
            final SwingWrapper<XYChart> sw1 = new SwingWrapper<>(chart1);
            sw.displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            sw1.displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void customise(XYChart chart) {
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setPlotMargin(0);
        chart.getStyler().setPlotContentSize(.95);
    }


}
