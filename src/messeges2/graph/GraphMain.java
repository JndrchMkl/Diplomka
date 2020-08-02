package messeges2.graph;

import messeges2.db.MysqlConnector;

import java.sql.SQLException;

public class GraphMain {

    public static void main(String[] args) throws Exception {
        recordBuild();


        Thread gui = new Thread(new GraphSizePerTime());
        gui.start();
    }

    private static void recordBuild() throws SQLException {
        MysqlConnector conn = MysqlConnector.getInstance();
        conn.insertBuildRun();
    }


}
