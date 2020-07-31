package messeges2.graph;

import messeges2.Entita;
import messeges2.FileWriting;
import messeges2.Matrika;
import messeges2.PostOffice;
import messeges2.db.MysqlConnector;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static messeges2.TimeUtils.systemSecondsTime;

public class GraphMain {

    public static void main(String[] args) throws Exception {
        MysqlConnector conn = MysqlConnector.getInstance();
        conn.insertBuildRun();

        Thread gui = new Thread(new GraphSizePerTime());
//        gui.setPriority(1);
        gui.start();
    }


}
