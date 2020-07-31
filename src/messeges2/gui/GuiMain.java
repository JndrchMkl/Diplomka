package messeges2.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import messeges2.Settings;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiMain extends Application {
    /// ENTITY Spinners
    @FXML
    private Spinner<Double> spinnerTalentFrom;
    @FXML
    private Spinner<Double> spinnerTalentTo;
    @FXML
    private Spinner<Double> spinnerPatienceFrom;
    @FXML
    private Spinner<Double> spinnerPatienceTo;
    @FXML
    private Spinner<Double> spinnerStrengthFrom;
    @FXML
    private Spinner<Double> spinnerStrengthTo;
    @FXML
    private Spinner<Double> spinnerPerceptionFrom;
    @FXML
    private Spinner<Double> spinnerPerceptionTo;
    @FXML
    private Spinner<Double> spinnerRangeLifeLengthFrom;
    @FXML
    private Spinner<Double> spinnerRangeLifeLengthTo;
    @FXML
    private Spinner<Integer> spinnerMultiplierLiveCost;
    @FXML
    private Spinner<Integer> spinnerMultiplierIncome;
    @FXML
    private Spinner<Integer> spinnerChildExpense;

    /// Simulation Spinners
    @FXML
    private Spinner<Integer> spinnerSizeEntitySet;

    @FXML
    private ListView<ListItemIntent> listViewIntents;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("setting.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Simulation settings");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

    }

    @FXML
    public void initialize() {
        initSpinner(spinnerTalentFrom, Settings.RANGE_TALENT_FROM, 1, 1);
        initSpinner(spinnerTalentTo, Settings.RANGE_TALENT_TO, 1, 1);
        initSpinner(spinnerPatienceFrom, Settings.RANGE_PATIENCE_FROM, 1, 1);
        initSpinner(spinnerPatienceTo, Settings.RANGE_PATIENCE_TO, 1, 1);
        initSpinner(spinnerStrengthFrom, Settings.RANGE_STRENGTH_FROM, 1, 1);
        initSpinner(spinnerStrengthTo, Settings.RANGE_STRENGTH_TO, 1, 1);
        initSpinner(spinnerPerceptionFrom, Settings.RANGE_PERCEPTION_FROM, 1, 1);
        initSpinner(spinnerPerceptionTo, Settings.RANGE_PERCEPTION_TO, 1, 1);
        initSpinner(spinnerRangeLifeLengthFrom, Settings.RANGE_LIFE_LENGTH_FROM, 1, 1);
        initSpinner(spinnerRangeLifeLengthTo, Settings.RANGE_LIFE_LENGTH_TO, 1, 1);
        initSpinner(spinnerMultiplierLiveCost, Settings.MULTIPLIER_LIVE_COST, 1, 1);
        initSpinner(spinnerMultiplierIncome, Settings.MULTIPLIER_INCOME, 1, 1);
        initSpinner(spinnerChildExpense, Settings.VALUE_CHILD_EXPENSE, 1, 1);
        initSpinner(spinnerSizeEntitySet, Settings.SIZE_ENTITY_SET, 1, 1);

        initCheckList();
    }

    private void initCheckList() {
        listViewIntents.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listViewIntents.setCellFactory(CheckBoxListCell.forListView(ListItemIntent::selectedProperty, new StringConverter<ListItemIntent>() {
            @Override
            public String toString(ListItemIntent object) {
                return object.getName();
            }

            @Override
            public ListItemIntent fromString(String string) {
                return new ListItemIntent(string);
            }
        }));
        listViewIntents.setOnMouseClicked(event -> {
            ListItemIntent selectedItem = listViewIntents.getSelectionModel().getSelectedItem();
            selectedItem.setSelected(!selectedItem.selectedProperty().get());
        });

       listViewIntents.getItems().add(new ListItemIntent("Item 1"));
       listViewIntents.getItems().add(new ListItemIntent("Item 2"));
       listViewIntents.getItems().add(new ListItemIntent("Item 3"));
    }

    private void initSpinner(Spinner<Double> spinner, double value, int incrementStep, int decrementStep) {
        spinner.getEditor().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    spinner.increment(incrementStep);
                    System.out.println("++++");
                    break;
                case DOWN:
                    spinner.decrement(decrementStep);
                    break;
            }
        });
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000.0));
        spinner.getValueFactory().setValue(value);
    }

    private void initSpinner(Spinner<Integer> spinner, int value, int incrementStep, int decrementStep) {
        AtomicInteger actualValue = new AtomicInteger();
        spinner.getEditor().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    spinner.increment(incrementStep);
                    actualValue.set(spinner.getValue());
                    System.out.println("---"+actualValue.toString());
                    break;
                case DOWN:
                    spinner.decrement(decrementStep);
                    break;
            }
        });
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000));
        spinner.getValueFactory().setValue(value);
    }
}
