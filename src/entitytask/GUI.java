package entitytask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.Stack;

import static entitytask.SystemCore.INIT_SET;

public class GUI extends Application {
    private final SystemCore sys = SystemCore.getInstance();

    @FXML
    private TreeTableView<Entity> treeTableGeneticAlg;

    @FXML
    private Button start;
    @FXML
    private Slider timeLine;
    @FXML
    private TextField tick;


    @FXML
    public void onScrollTime(ScrollEvent event) {
//        setDiscreteSteps();
//        tick.setText("" + timeLine.getValue());
//        TreeItem<Entity> rootNode = prepareRootNode();
//        rootNode.setExpanded(true);
//        treeTableGeneticAlg.setRoot(rootNode);
    }

    public void onTimeLineSliderChange() {
        timeLine.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
                setTimeLineSliderProperties();
                setDiscreteSteps();
                tick.setText(""+(int)timeLine.getValue());

                fillTable();
            }
        });
    }

    private void fillTable() {
        TreeItem<Entity> rootNode = prepareRootNode();
        rootNode.setExpanded(true);
        treeTableGeneticAlg.setRoot(rootNode);
    }

    private void setDiscreteSteps() {
        long value = Math.round(timeLine.getValue());

        // set steps by one
        timeLine.setValue(value);

        //never get over maximum time value
        int maxIndex = sys.history().size() - 1;
        if ((maxIndex) < value) {
            timeLine.setValue(maxIndex);
        }
    }

    @FXML
    private void startOnAction(ActionEvent event) {
        SystemCore sys = SystemCore.getInstance();
        new Thread(new Task() {
            @Override
            protected Object call() throws Exception {

                sys.run();


                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        // Fill table rows with population data from gen. alg.
                        fillTable();

                    }
                });

                return true;
            }
        }).start();
//        if (++click % 2 == 1) {
//            start.setText("Cancel");
//            calculation.start();
//        } else {
//            start.setText("Start");
//            calculation.interrupt();
//        }

    }

    private TreeItem<Entity> prepareRootNode() {
        // First node
        TreeItem<Entity> rootNode = new TreeItem<>(new Entity(null, "Jedinec", 0.0));


        int index = (int) timeLine.getValue()-1;
        List<Entity> actualTablePopulation;
        if (index > 0 | index < sys.history().size()) {
            actualTablePopulation = sys.history().get(index);
        } else {
            actualTablePopulation=INIT_SET;
        }

        // Build nodes
        BuildTreeItemNodes(rootNode, actualTablePopulation);

        //set maximum of slider
        setTimeLineSliderProperties();
        return rootNode;
    }

    private void BuildTreeItemNodes(TreeItem<Entity> rootNode, List<Entity> population) {
        for (Entity e : population) {
            TreeItem<Entity> entityNode = new TreeItem<>(e);
            Stack<Entity> nodes = new Stack<>();
            Stack<TreeItem<Entity>> root1 = new Stack<>();
            nodes.push(e);
            root1.push(entityNode);
            while (!nodes.isEmpty()) {
                Entity curr = nodes.pop();
                TreeItem<Entity> cur = root1.pop();
                for (Entity child : curr.getChildren()) {
                    TreeItem<Entity> treeItem = new TreeItem<>(child);
                    cur.getChildren().add(treeItem);
                    root1.push(treeItem);
                    nodes.push(child);
                }
            }
            rootNode.getChildren().add(entityNode);
        }
    }


    private void tableView() {
        //// Create three columns
        TreeTableColumn<Entity, Integer> col1 = new TreeTableColumn<>("ID - Name");
        col1.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        TreeTableColumn<Entity, Double> col2 = new TreeTableColumn<>("Talent");
        col2.setCellValueFactory(new TreeItemPropertyValueFactory<>("talent"));
        TreeTableColumn<Entity, Double> col3 = new TreeTableColumn<>("Sources");
        col3.setCellValueFactory(new TreeItemPropertyValueFactory<>("sources"));
        TreeTableColumn<Entity, Entity> col4 = new TreeTableColumn<>("parentA");
        col4.setCellValueFactory(new TreeItemPropertyValueFactory<>("parentA"));
        TreeTableColumn<Entity, Entity> col5 = new TreeTableColumn<>("parentB");
        col5.setCellValueFactory(new TreeItemPropertyValueFactory<>("parentB"));

        col1.setMaxWidth(1f * Integer.MAX_VALUE * 40); // 40% width
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        col4.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        col5.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width

        // Add columns to the TreeTableView
        treeTableGeneticAlg.getColumns().add(col1);
        treeTableGeneticAlg.getColumns().add(col2);
        treeTableGeneticAlg.getColumns().add(col3);
        treeTableGeneticAlg.getColumns().add(col4);
        treeTableGeneticAlg.getColumns().add(col5);

        // Fill table rows with population data from sys. alg.
        fillTable();
    }

    private void setTimeLineSliderProperties() {
        timeLine.setMax(SystemCore.END_TICK);
        timeLine.setMin(1);
        timeLine.setMajorTickUnit((int) (SystemCore.END_TICK / 5));
        timeLine.setMinorTickCount(SystemCore.END_TICK);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("window.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Diplomova prace");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();


        initializateComponents();
    }

    private void initializateComponents() {

        // Add columns to to tree table view
        tableView();

        //set maximum value
        setTimeLineSliderProperties();
        onTimeLineSliderChange();


    }
}
