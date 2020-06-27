package messeges2.graph;

import messeges2.Entita;
import messeges2.Matrika;
import messeges2.PostOffice;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static messeges2.TimeUtils.systemSecondsTime;

public class GraphMain {

    public static void main(String[] args) throws Exception {
        Thread gui = new Thread(new GraphSizePerTime());
        gui.setPriority(1);
        gui.start();
    }


}
