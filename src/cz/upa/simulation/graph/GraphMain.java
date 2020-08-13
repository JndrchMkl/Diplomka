package cz.upa.simulation.graph;

import cz.upa.simulation.domain.Entita;
import cz.upa.simulation.domain.Matrika;
import cz.upa.simulation.domain.Settings;
import cz.upa.simulation.messaging.PostOffice;
import cz.upa.simulation.output.MysqlConnector;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class GraphMain {

    public static void main(String[] args) throws Exception {
        recordBuild();


        Thread gui = new Thread(new RecorderSizePerTime());
//        Thread gui = new Thread(new GraphSizePerTime());
        gui.start();
        startSimulation();
    }

    private static void startSimulation() {
        for (int i = 0; i < Settings.SIZE_ENTITY_SET; i++) {
            Double talent = ThreadLocalRandom.current().nextDouble(Settings.RANGE_TALENT_FROM, Settings.RANGE_TALENT_TO);
            new Entita(new Matrika(),new PostOffice(), 0.0, talent);
        }
    }

    private static void recordBuild() throws SQLException {
        MysqlConnector conn = MysqlConnector.getInstance();
        conn.insertBuildRun();
    }


}
