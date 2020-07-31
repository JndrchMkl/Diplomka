package messeges2.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import messeges2.Settings;
import messeges2.graph.GraphMain;

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

    // Run Simulation buttons
    @FXML
    private Button buttonSizePerTime;

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

        buttonSizePerTime.setOnAction(event-> {
            try {
                setActualConfiguration();
                GraphMain.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
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
        Callback<ListView<ListItemIntent>, ListCell<ListItemIntent>> wrappedCellFactory = listViewIntents.getCellFactory();
        listViewIntents.setCellFactory(listView -> {
                    CheckBoxListCell<ListItemIntent> cell = wrappedCellFactory != null ? (CheckBoxListCell<ListItemIntent>) wrappedCellFactory.call(listView) : new CheckBoxListCell<>();
                    cell.setSelectedStateCallback(ListItemIntent::selectedProperty);

                    Platform.runLater(() -> {
                        if (cell.getItem() != null)
                            cell.setDisable(cell.getItem().isDisabled());
                    });

                    cell.itemProperty().addListener(new ChangeListener<ListItemIntent>() {
                        @Override
                        public void changed(ObservableValue<? extends ListItemIntent> observable, ListItemIntent oldValue, ListItemIntent newValue) {
                            // I cannot modify here the checkbox cell...
                        }

                    });


                    return cell;
                }
        );

        listViewIntents.setOnMouseClicked(event -> {
            ListItemIntent selectedItem = listViewIntents.getSelectionModel().getSelectedItem();
            if (selectedItem != null)
                selectedItem.setSelected(!selectedItem.selectedProperty().get());
        });

        listViewIntents.getItems().add(new ListItemIntent("intentLookForYourNewPartner", true));
        listViewIntents.getItems().add(new ListItemIntent("intentDecideWhoIsPartnerRightNow", true));
        listViewIntents.getItems().add(new ListItemIntent("intentSteal"));
        listViewIntents.getItems().add(new ListItemIntent("intentMurder"));
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
                    System.out.println("---" + actualValue.toString());
                    break;
                case DOWN:
                    spinner.decrement(decrementStep);
                    break;
            }
        });
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000));
        spinner.getValueFactory().setValue(value);
    }

    private void setActualConfiguration() {
        Settings.RANGE_TALENT_FROM = spinnerTalentFrom.getValue();
        Settings.RANGE_TALENT_TO = spinnerTalentTo.getValue();
        Settings.RANGE_PATIENCE_FROM = spinnerPatienceFrom.getValue();
        Settings.RANGE_PATIENCE_TO = spinnerPatienceTo.getValue();
        Settings.RANGE_STRENGTH_FROM = spinnerStrengthFrom.getValue();
        Settings.RANGE_STRENGTH_TO = spinnerStrengthTo.getValue();
        Settings.RANGE_PERCEPTION_FROM = spinnerPerceptionFrom.getValue();
        Settings.RANGE_PERCEPTION_TO = spinnerPerceptionTo.getValue();
        Settings.RANGE_LIFE_LENGTH_FROM = spinnerRangeLifeLengthFrom.getValue();
        Settings.RANGE_LIFE_LENGTH_TO = spinnerRangeLifeLengthTo.getValue();
        Settings.MULTIPLIER_LIVE_COST = spinnerMultiplierLiveCost.getValue();
        Settings.MULTIPLIER_INCOME = spinnerMultiplierIncome.getValue();
        Settings.VALUE_CHILD_EXPENSE = spinnerChildExpense.getValue();
        Settings.SIZE_ENTITY_SET = spinnerSizeEntitySet.getValue();

       Settings.INTENT_LOOK_FOR_PARTNER= listViewIntents.getItems().get(0).isSelected();
       Settings.INTENT_DECIDE_RIGHT_NOW= listViewIntents.getItems().get(1).isSelected();
       Settings.INTENT_STEAL= listViewIntents.getItems().get(2).isSelected();
       Settings.INTENT_MURDER= listViewIntents.getItems().get(3).isSelected();
    }
}
