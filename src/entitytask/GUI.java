package entitytask;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SystemCore sys = SystemCore.getInstance();
        System.out.println("Starting...");
        sys.run();
        System.out.println("Ending...");
    }
}
