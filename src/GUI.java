
import entitytask.Entity;
import entitytask.SystemCore;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {
    public static final int SIZE = 50;
    private SystemCore sys = SystemCore.getInstance();
    private int click = 0;
    private Thread calculation;
    @FXML
    private TreeTableView treeTableGeneticAlg;

    @FXML
    private Button start;
    @FXML
    private Slider timeLine;
    @FXML
    private TextField tick;


    @FXML
    public void onScrollTime(ScrollEvent event){
        timeLine.setValue(Math.round(timeLine.getValue()+1));
        if ((sys.history().size()-1)<timeLine.getValue()){
            timeLine.setValue(sys.history().size()-1);

        }
        tick.setText(""+timeLine.getValue());
        TreeItem<Entity> rootNode = prepareRootNode();
        rootNode.setExpanded(true);
        treeTableGeneticAlg.setRoot(rootNode);
    }
    @FXML
    private void startOnAction(ActionEvent event) {
        SystemCore sys = SystemCore.getInstance();
        System.out.println("Starting...");

        calculation = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {

                sys.run();


                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        // Fill table rows with population data from gen. alg.
                        TreeItem<Entity> rootNode = prepareRootNode();
                        // Create the root node and add children
                        rootNode.setExpanded(true);
                        treeTableGeneticAlg.setRoot(rootNode);
                    }
                });

                return true;
            }
        });
        if (++click % 2 == 1) {
            start.setText("Cancel");
            calculation.start();
        } else {
            start.setText("Start");
            calculation.interrupt();
        }

            System.out.println("Ending...");
        }

        private TreeItem<Entity> prepareRootNode () {
            // First node
            TreeItem<Entity> rootNode = new TreeItem<>(new Entity(null, "sd", 0.0));


            // Build nodes
            Integer index = (int) timeLine.getValue();
            for (Entity e : sys.history().get(index)) {
                TreeItem<Entity> entityNode = new TreeItem<>(e);
                for (Entity potomek : e.getChildren()) {
                    entityNode.getChildren().add(new TreeItem<>(potomek));
                }
                rootNode.getChildren().add(entityNode);
            }
            return rootNode;
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

        // Add columns to the TreeTableView
        treeTableGeneticAlg.getColumns().add(col1);
        treeTableGeneticAlg.getColumns().add(col2);
        treeTableGeneticAlg.getColumns().add(col3);
        treeTableGeneticAlg.getColumns().add(col4);
        treeTableGeneticAlg.getColumns().add(col5);

        // Fill table rows with population data from sys. alg.
        TreeItem<Entity> rootNode = prepareRootNode();


        // Create the root node and add children
        rootNode.setExpanded(true);
        treeTableGeneticAlg.setRoot(rootNode);
    }

    private void timeLine() {
        timeLine.setMax(SystemCore.END_TICK);
        timeLine.setMax(100);
        timeLine.setValue(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("window.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("Diplomova prace");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        initializateComponents();
    }

    private void initializateComponents() {

        // Add columns to to tree table view
        tableView();

        //set maximum value
        timeLine();

    }
}
