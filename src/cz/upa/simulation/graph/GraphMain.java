package cz.upa.simulation.graph;

import cz.upa.simulation.domain.Entita;
import cz.upa.simulation.domain.Settings;
import cz.upa.simulation.messaging.PostOffice;
import cz.upa.simulation.messaging.SimulationTimer;
import cz.upa.simulation.messaging.Societies;
import cz.upa.simulation.output.MysqlConnector;

import java.io.*;
import java.sql.SQLException;
import java.util.SplittableRandom;

import static cz.upa.simulation.domain.Settings.ACTUAL_ID_BUILD;
import static cz.upa.simulation.utils.StringUtils.s;

public class GraphMain {
    public static int seed = 1882020;

    public static void main(String[] args) throws Exception {
        recordBuildFile();

        Thread gui = new Thread(new RecorderSizePerTime());
        gui.start();
        new Thread(new SimulationTimer()).start();
        startSimulation();
    }

    private static void startSimulation() {
        Societies societies = new Societies();
        PostOffice po = societies.found();
        societies.found();
        societies.found();

        SplittableRandom r = new SplittableRandom(seed);
        for (int i = 0; i < Settings.SIZE_ENTITY_SET; i++) {
            double talent = r.nextDouble(Settings.RANGE_TALENT_FROM, Settings.RANGE_TALENT_TO);
            Settings.ACTUAL_POPULATION_TALENT += talent;

            Entita entita = new Entita(societies, po, 0.0, talent, "", "");
            RecorderSizePerTime.recordMessages.add(new Double[]{1.0, 0.0, talent, 0.0, 0.0, 0.0});
        }
//        new Thread(new SocietyGarbageCollector(societies)).start();
    }

    private static void recordBuild() throws SQLException {
        MysqlConnector conn = MysqlConnector.getInstance();
        conn.insertBuildRun();
    }

    private static void recordBuildFile() {
        File directory = new File("output");
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File("output" + "/" + "v.txt");
        try {
            FileReader r = new FileReader(file);
            BufferedReader br = new BufferedReader(r);
            String s = br.readLine();
            int version = Integer.parseInt(s) + 1;
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s(version));
            ACTUAL_ID_BUILD = version;
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
