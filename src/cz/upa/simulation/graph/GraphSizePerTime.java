package cz.upa.simulation.graph;

import cz.upa.simulation.domain.Settings;
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
        TreeMap<Double, String> map = new TreeMap<>(); // sorted pair of values
        List<Double> popSize = new LinkedList<>();
        double nEntities = Settings.SIZE_ENTITY_SET - 1;
        try {
            BufferedReader reader = FileReading.readActualBuildOutput();
            if (reader != null) {
                String line = "";
                String[] data;
                while (line != null) {
                    line = reader.readLine();
                    if (line != null) {
                        data = line.split(";");
//                        double n = Double.parseDouble(data[0]);
                        String operator = data[0];
                        double time = Double.parseDouble(data[1]) / 1000000000.0;
                        map.put(time < 0 ? 0 : time, operator);

                    }
                }
            }
            for (String operator : map.values()) {
                if (operator.equals("++"))
                    popSize.add(++nEntities);
                else if (operator.equals("--"))
                    popSize.add(--nEntities);
            }
            // Create Chart
            final XYChart chart = QuickChart.getChart(CHART_TITLE, X_TITLE, Y_TITLE, SERIES_NAME, new ArrayList<>(map.keySet()), popSize);

            // Add minor series
//            chart.addSeries("death", new ArrayList<>(map.keySet()), death);
//            chart.addSeries("popSize", new ArrayList<>(map.keySet()), popSize);

            // Customize Chart
            customise(chart);

            // Show it
            new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

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
