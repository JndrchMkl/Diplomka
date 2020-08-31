package cz.upa.simulation.gui;

import cz.upa.simulation.domain.Settings;
import cz.upa.simulation.graph.Record;
import cz.upa.simulation.output.FileReading;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphSettings implements Runnable {

    public static final String CHART_TITLE = "Evolution of population";
    public static final String X_TITLE = "X";
    public static final String Y_TITLE = "Y";
    double[] initX = {0};
    double[] initY = {0};
    LinkedList<Record> records;
    LinkedList<Double> listX;
    LinkedList<Double> listY;

    public CheckBox checkboxRelativeTalent;
    public CheckBox checkboxRelativePropensity;
    public CheckBox checkboxPopulationSize;
    public CheckBox checkboxPopulationSizeAVG;
    public CheckBox checkboxPopulationSizeEX;
    public CheckBox checkboxPopulationSizeOverTalent;
    public Spinner<Integer> spinnerPopulationSizeOverTalent;
    public CheckBox checkboxProductivity;
    public CheckBox checkboxProductivityAVG;
    public CheckBox checkboxProductivityEX;
    public CheckBox checkboxThievesCount;
    public CheckBox checkboxThievesCountAVG;
    public CheckBox checkboxThievesCountEX;
    public CheckBox checkboxMurderCount;
    public CheckBox checkboxMurderCountAVG;
    public CheckBox checkboxMurderCountEX;
    public CheckBox checkboxPopulationBorn;
    public CheckBox checkboxPopulationDeath;
    public CheckBox checkboxProductivityPopulationSize;
    public CheckBox checkboxProductivityPopulationSizeEX;
    public CheckBox checkboxProductivityPopulationSizeAVG;
    public CheckBox checkboxPropensityCrime;
    public CheckBox checkboxPropensityCrimeAVG;
    public CheckBox checkboxPropensityCrimeEX;
    public CheckBox checkboxPropensityCrimePopSize;
    public CheckBox checkboxPropensityCrimePopSizeAVG;
    public CheckBox checkboxPropensityCrimePopSizeEX;
    public Button btnShow;


    @FXML
    public void initialize() {
        initSpinner(spinnerPopulationSizeOverTalent, 11, 1, 1);

        btnShow.setOnAction(event -> {
            XYChart chart = new XYChart(600, 400);

            if (checkboxPopulationSize.isSelected()) {
                popSize();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PopulationSize", initX, initY);
                else
                    chart.addSeries("PopulationSize", listX, listY);
            }
            if (checkboxPopulationSizeAVG.isSelected()) {
                popAVG();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PopulationSizeAVG", initX, initY);
                else
                    chart.addSeries("PopulationSizeAVG", listX, listY);
            }
            if (checkboxPopulationSizeEX.isSelected()) {
                popSD();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PopulationSizeSD", initX, initY);
                else
                    chart.addSeries("PopulationSizeSD", listX, listY);
            }
            if (checkboxPopulationBorn.isSelected()) {
                born();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("Born", initX, initY);
                else
                    chart.addSeries("Born", listX, listY);
            }
            if (checkboxPopulationDeath.isSelected()) {
                death();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("Death", initX, initY);
                else
                    chart.addSeries("Death", listX, listY);
            }
            if (checkboxPopulationSizeOverTalent.isSelected()) {
                popOver();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PopulationSizeOverTalent", initX, initY);
                else
                    chart.addSeries("PopulationSizeOverTalent", listX, listY);
            }
            if (checkboxProductivity.isSelected()) {
                productivity();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("Productivity", initX, initY);
                else
                    chart.addSeries("Productivity", listX, listY);
            }
            if (checkboxProductivityAVG.isSelected()) {
                productivityAVG();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ProductivityAVG", initX, initY);
                else
                    chart.addSeries("ProductivityAVG", listX, listY);
            }
            if (checkboxProductivityEX.isSelected()) {
                productivityEX();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ProductivityEX", initX, initY);
                else
                    chart.addSeries("ProductivityEX", listX, listY);
            }
            if (checkboxThievesCount.isSelected()) {
                thieves();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ThievesCount", initX, initY);
                else
                    chart.addSeries("ThievesCount", listX, listY);
            }
            if (checkboxThievesCountAVG.isSelected()) {
                thievesAVG();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ThievesCountAVG", initX, initY);
                else
                    chart.addSeries("ThievesCountAVG", listX, listY);
            }
            if (checkboxThievesCountEX.isSelected()) {
                thievesEX();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ThievesCountEX", initX, initY);
                else
                    chart.addSeries("ThievesCountEX", listX, listY);
            }
            if (checkboxMurderCount.isSelected()) {
                murderer();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("MurderCount", initX, initY);
                else
                    chart.addSeries("MurderCount", listX, listY);
            }
            if (checkboxMurderCountAVG.isSelected()) {
                murdererAVG();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("MurderCountAVG", initX, initY);
                else
                    chart.addSeries("MurderCountAVG", listX, listY);
            }
            if (checkboxMurderCountEX.isSelected()) {
                murdererEX();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("MurderCountEX", initX, initY);
                else
                    chart.addSeries("MurderCountEX", listX, listY);
            }
            if (checkboxPropensityCrime.isSelected()) {
                propensityCrime();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PropensityCrime", initX, initY);
                else
                    chart.addSeries("PropensityCrime", listX, listY);
            }
            if (checkboxPropensityCrimeAVG.isSelected()) {
                propensityCrimeAVG();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PropensityCrimeAVG", initX, initY);
                else
                    chart.addSeries("PropensityCrimeAVG", listX, listY);
            }
            if (checkboxPropensityCrimeEX.isSelected()) {
                propensityCrimeEX();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PropensityCrimeEX", initX, initY);
                else
                    chart.addSeries("PropensityCrimeEX", listX, listY);
            }

            /// DEPENDED ON POPULATION SIZE
            if (checkboxProductivityPopulationSize.isSelected()) {
                productionOnPopSize();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ProductivityPopulationSize", initX, initY);
                else
                    chart.addSeries("ProductivityPopulationSize", listX, listY);
            }
            if (checkboxProductivityPopulationSizeEX.isSelected()) {
                productionOnPopSizeEX();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ProductivityPopulationSizeEX", initX, initY);
                else
                    chart.addSeries("ProductivityPopulationSizeEX", listX, listY);
            }
            if (checkboxProductivityPopulationSizeAVG.isSelected()) {
                productionOnPopSizeAVG();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("ProductivityPopulationSizeAVG", initX, initY);
                else
                    chart.addSeries("ProductivityPopulationSizeAVG", listX, listY);
            }
            if (checkboxPropensityCrimePopSize.isSelected()) {
                propensityCrimeOnPopSize();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PropensityCrimePopSize", initX, initY);
                else
                    chart.addSeries("PropensityCrimePopSize", listX, listY);
            }
            if (checkboxPropensityCrimePopSizeAVG.isSelected()) {
                propensityCrimeOnPopSizeAVG();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PropensityCrimePopSizeAVG", initX, initY);
                else
                    chart.addSeries("PropensityCrimePopSizeAVG", listX, listY);
            }
            if (checkboxPropensityCrimePopSizeEX.isSelected()) {
                propensityCrimeOnPopSizeEx();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("PropensityCrimePopSizeEX", initX, initY);
                else
                    chart.addSeries("PropensityCrimePopSizeEX", listX, listY);
            }

            /// RELATIVE ON POPULATION SIZE
            if (checkboxRelativePropensity.isSelected()) {
                relativePropensity();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("RelativePropensity", initX, initY);
                else
                    chart.addSeries("RelativePropensity", listX, listY);
            }
            if (checkboxRelativeTalent.isSelected()) {
                relativeTalent();
                if (listX.size() < 1 || listY.size() < 1)
                    chart.addSeries("RelativeTalent", initX, initY);
                else
                    chart.addSeries("RelativeTalent", listX, listY);
            }

            customise(chart);
            new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        });
    }


    public GraphSettings() {
        records = records();
        listX = new LinkedList<>();
        listY = new LinkedList<>();
    }

    public void show() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("graphs.fxml"));
            Parent root = fxmlLoader.load();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Graph settings");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        show();
    }

    private void customise(XYChart chart) {
        chart.setTitle(CHART_TITLE);
        chart.setXAxisTitle(X_TITLE);
        chart.setYAxisTitle(Y_TITLE);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setYAxisLabelAlignment(Styler.TextAlignment.Centre);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setPlotMargin(0);
        chart.getStyler().setPlotContentSize(.95);
    }

    private double calculateMedian(List<Double> l) {
        double median;
        int index = l.size() / 2;
        if (l.size() % 2 == 0)
            median = ((l.get(index) + l.get(index - 1) / 2));
        else
            median = l.get(index);
        return median;
    }

    private void initSpinner(Spinner<Integer> spinner, int value, int incrementStep, int decrementStep) {
        AtomicInteger actualValue = new AtomicInteger();
        spinner.getEditor().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    spinner.increment(incrementStep);
                    actualValue.set(spinner.getValue());
                    break;
                case DOWN:
                    spinner.decrement(decrementStep);
                    actualValue.set(spinner.getValue());
                    break;
            }
        });
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000));
        spinner.getValueFactory().setValue(value);
    }

    private LinkedList<Record> records() {
        LinkedList<Record> records = new LinkedList<>();
        try {
            BufferedReader reader = FileReading.readActualBuildOutput();
            if (reader != null) {
                String line = "";
                String[] data;
                while (line != null) {
                    line = reader.readLine();
                    if (line != null) {
                        data = line.split(";");

                        if (!"".equals(data[0])) {
                            String operator = data[0];
                            double time = Double.parseDouble(data[1]) / 1000000000.0;
                            double talent = Double.parseDouble(data[2]);
                            double propensityCrime = Double.parseDouble(data[3]);
                            double isThief = "0.0".equals(data[4]) ? 0.0 : 1.0;
                            double isMurderer = "0.0".equals(data[5]) ? 0.0 : 1.0;
                            Record record = new Record(operator, time > 0 ? time : 0, talent, propensityCrime, isThief, isMurderer);
                            records.add(record);
                        }
                    }
                }
            }
            Collections.sort(records);
            return records;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private void popOver() {
        clear();
        double nEntities = 0;
        for (Record r : records) {
            if (r.getTalent() > spinnerPopulationSizeOverTalent.getValue()) {
                if (r.getOperator().equals("++")) {
                    listY.add(++nEntities);
                } else if (r.getOperator().equals("--")) {
                    listY.add(--nEntities);
                } else {
                    continue;
                }
                listX.add(r.getTime());
            }
        }
    }

    private void popSize() {
        clear();
        double nEntities = 0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                listY.add(++nEntities);
            } else if (r.getOperator().equals("--")) {
                listY.add(--nEntities);
            } else {
                continue;
            }
            listX.add(r.getTime());
        }
    }

    private void born() {
        clear();
        double nEntities = 0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                listY.add(++nEntities);
            } else {
                continue;
            }
            listX.add(r.getTime());
        }
    }

    private void death() {
        clear();
        double nEntities = 0;
        for (Record r : records) {
            if (r.getOperator().equals("--")) {
                listY.add(++nEntities);
            } else {
                continue;
            }
            listX.add(r.getTime());
        }
    }

    private void popAVG() {
        // prumerny pocet entit od zacatku
        clear();
        double nEntities = 0;
        double sum = 0.0;
        double i = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nEntities;
            } else if (r.getOperator().equals("--")) {
                --nEntities;
            } else {
                continue;
            }
            sum += nEntities;
            i++;
            listY.add(sum / i);
            listX.add(r.getTime());
        }
    }

    private void popSD() {
        // stredni hodnota poctu entit od zacatku
        clear();
        double nEntities = 0;
        List<Double> l = new LinkedList<>();
        int count=0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nEntities;
            } else if (r.getOperator().equals("--")) {
                --nEntities;
            } else {
                continue;
            }
//            if (++count>99) {
                l.add(nEntities);
                listY.add(calculateSD(l));
                listX.add(r.getTime());
//            }
        }
    }

    public static double calculateSD(List<Double> numArray)    {
        double sum = 0.0;
        double standardDeviation = 0.0;
        int length = numArray.size();

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    private void productivity() {
        clear();
        double productivity = 0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                productivity += r.getTalent();
                listY.add(productivity);
            } else if (r.getOperator().equals("--")) {
                productivity -= r.getTalent();
                listY.add(productivity);
            } else {
                continue;
            }
            listX.add(r.getTime());
        }
    }


    private void productivityAVG() {
        // prumerny pocet entit od zacatku
        clear();
        double productivity = 0;
        double sum = 0.0;
        double i = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                productivity += r.getTalent();
            } else if (r.getOperator().equals("--")) {
                productivity -= r.getTalent();
            } else {
                continue;
            }
            sum += productivity;
            i++;
            listY.add(sum / i);
            listX.add(r.getTime());
        }
    }

    private void propensityCrimeEX() {
        // stredni hodnota poctu entit od zacatku
        clear();
        double productivity = 0;
        List<Double> l = new LinkedList<>();
        double i=0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                productivity += r.getPropensityCrime();
            } else if (r.getOperator().equals("--")) {
                productivity -= r.getTalent();
            } else {
                continue;
            }
            l.add(productivity / ++i);

            listY.add(calculateSD(l));
            listX.add(r.getTime());
        }
    }

    private void propensityCrime() {
        clear();
        double productivity = 0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                productivity += r.getTalent();
                listY.add(productivity);
            } else if (r.getOperator().equals("--")) {
                productivity -= r.getTalent();
                listY.add(productivity);
            } else {
                continue;
            }
            listX.add(r.getTime());
        }
    }


    private void propensityCrimeAVG() {
        // prumerny pocet entit od zacatku
        clear();
        double productivity = 0;
        double sum = 0.0;
        double i = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                productivity += r.getTalent();
            } else if (r.getOperator().equals("--")) {
                productivity -= r.getTalent();
            } else {
                continue;
            }
            sum += productivity;
            i++;
            listY.add(sum / i);
            listX.add(r.getTime());
        }
    }

    private void productivityEX() {
        // stredni hodnota poctu entit od zacatku
        clear();
        double productivity = 0;
        List<Double> l = new LinkedList<>();
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                productivity += r.getTalent();
            } else if (r.getOperator().equals("--")) {
                productivity -= r.getTalent();
            } else {
                continue;
            }
            l.add(productivity);

            listY.add(calculateMedian(l));
            listX.add(r.getTime());
        }
    }

    private void thieves() {
        clear();
        double nThieves = 0;
        for (Record r : records) {
            if (r.getOperator().equals("steal") && r.getIsThief().equals(1.0)) {
                listY.add(++nThieves);
            } else if (r.getOperator().equals("--") && r.getIsThief().equals(1.0)) {
                listY.add(--nThieves);
            } else {
                continue;
            }
            listX.add(r.getTime());
        }
    }

    private void thievesAVG() {
        clear();
        double nThieves = 0;
        double sum = 0.0;
        double i = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("steal") && r.getIsThief().equals(1.0)) {
                ++nThieves;
            } else if (r.getOperator().equals("--") && r.getIsThief().equals(1.0)) {
                --nThieves;
            } else {
                continue;
            }
            sum += nThieves;
            i++;
            listY.add(sum / i);
            listX.add(r.getTime());
        }
    }

    private void thievesEX() {
        clear();
        double nThieves = 0;
        List<Double> l = new LinkedList<>();
        for (Record r : records) {
            if (r.getOperator().equals("steal") && r.getIsThief().equals(1.0)) {
                ++nThieves;
            } else if (r.getOperator().equals("--") && r.getIsThief().equals(1.0)) {
                --nThieves;
            } else {
                continue;
            }
            l.add(nThieves);
            listY.add(calculateMedian(l));
            listX.add(r.getTime());
        }
    }

    private void murderer() {
        clear();
        double nMurderer = 0;
        for (Record r : records) {
            if (r.getOperator().equals("murder") && r.getIsMurderer().equals(1.0)) {
                listY.add(++nMurderer);
            } else if (r.getOperator().equals("--") && r.getIsMurderer().equals(1.0)) {
                listY.add(--nMurderer);
            } else {
                continue;
            }
            listX.add(r.getTime());
        }
    }

    private void murdererAVG() {
        clear();
        double nMurderers = 0;
        double sum = 0.0;
        double i = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("murder") && r.getIsMurderer().equals(1.0)) {
                ++nMurderers;
            } else if (r.getOperator().equals("--") && r.getIsMurderer().equals(1.0)) {
                --nMurderers;
            } else {
                continue;
            }
            sum += nMurderers;
            i++;
            listY.add(sum / i);
            listX.add(r.getTime());
        }
    }

    private void murdererEX() {
        clear();
        double nMurderers = 0;
        List<Double> l = new LinkedList<>();
        for (Record r : records) {
            if (r.getOperator().equals("murder") && r.getIsMurderer().equals(1.0)) {
                ++nMurderers;
            } else if (r.getOperator().equals("--") && r.getIsMurderer().equals(1.0)) {
                --nMurderers;
            } else {
                continue;
            }
            l.add(nMurderers);
            listY.add(calculateMedian(l));
            listX.add(r.getTime());
        }
    }


    private void productionOnPopSize() {
        clear();
        double nPopulation = 0;
        Double productivity = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                productivity += r.getTalent();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                productivity -= r.getTalent();
            } else {
                continue;
            }
            listX.add(productivity);
            listY.add(nPopulation);
        }
    }

    private void productionOnPopSizeAVG() {
        clear();
        double nPopulation = 0.0;
        Double productivity = 0.0;
        double sum = 0.0;
        double i = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                productivity += r.getTalent();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                productivity -= r.getTalent();
            } else {
                continue;
            }
            sum += productivity;
            i++;
            listX.add(sum / i);
            listY.add(nPopulation);
        }
    }

    private void productionOnPopSizeEX() {
        clear();
        double nPopulation = 0.0;
        Double productivity = 0.0;
        List<Double> l = new LinkedList<>();
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                productivity += r.getTalent();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                productivity -= r.getTalent();
            } else {
                continue;
            }
            l.add(productivity);
            listX.add(calculateMedian(l));
            listY.add(nPopulation);
        }
    }

    private void propensityCrimeOnPopSize() {
        clear();
        double nPopulation = 0;
        Double propensityCrime = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                propensityCrime += r.getPropensityCrime();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                propensityCrime -= r.getPropensityCrime();
            } else {
                continue;
            }
            listX.add(propensityCrime);
            listY.add(nPopulation);
        }
    }

    private void propensityCrimeOnPopSizeAVG() {
        clear();
        double nPopulation = 0.0;
        Double propensityCrime = 0.0;
        double sum = 0.0;
        double i = 0.0;
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                propensityCrime += r.getPropensityCrime();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                propensityCrime -= r.getPropensityCrime();
            } else {
                continue;
            }
            sum += propensityCrime;
            i++;
            listX.add(sum / i);
            listY.add(nPopulation);
        }
    }

    private void propensityCrimeOnPopSizeEx() {
        clear();
        double nPopulation = 0.0;
        Double propensityCrime = 0.0;
        List<Double> l = new LinkedList<>();
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                propensityCrime += r.getPropensityCrime();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                propensityCrime -= r.getPropensityCrime();
            } else {
                continue;
            }
            l.add(propensityCrime);
            listX.add(calculateMedian(l));
            listY.add(nPopulation);
        }
    }
    private void relativePropensity() {
        clear();
        double nPopulation = 0.0;
        Double propensityCrime = 0.0;
        Double hundred = Settings.RANGE_PATIENCE_TO - Settings.RANGE_PATIENCE_FROM;
        List<Double> l = new LinkedList<>();
        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                propensityCrime += r.getPropensityCrime();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                propensityCrime -= r.getPropensityCrime();
            } else {
                continue;
            }
            double val = 100*(propensityCrime / nPopulation)/hundred;
            listY.add(val);
            listX.add(nPopulation);
        }
    }

    private void relativeTalent() {
        clear();
        double nPopulation = 0.0;
        Double talent = 0.0;
        Double hundred = Settings.RANGE_TALENT_TO - Settings.RANGE_TALENT_FROM;

        for (Record r : records) {
            if (r.getOperator().equals("++")) {
                ++nPopulation;
                talent += r.getTalent();
            } else if (r.getOperator().equals("--")) {
                --nPopulation;
                talent -= r.getTalent();
            } else {
                continue;
            }
            double val = 100*(talent / nPopulation)/hundred;
            listY.add(val);
            listX.add(nPopulation);
        }
    }


    private void clear() {
        listX.clear();
        listY.clear();
    }

}
