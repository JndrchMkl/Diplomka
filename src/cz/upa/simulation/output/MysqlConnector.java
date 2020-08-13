package cz.upa.simulation.output;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

import static cz.upa.simulation.domain.Settings.ACTUAL_ID_BUILD;

public class MysqlConnector {
    public static MysqlConnector instance = null;
    Connection conn;
    Statement stmt;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    String strDate = formatter.format(new Date(System.currentTimeMillis()));
    String buildVersion = "alpha 1.";


    public static MysqlConnector getInstance() {
        if (instance == null) {
            instance = new MysqlConnector();
        }
        return instance;
    }

    private MysqlConnector() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/user?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    "user", "user");
            stmt = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public int insertBuildRun() throws SQLException {
//        stmt = conn.createStatement();
        ResultSet resultSet = selectBuildVersion();
        ACTUAL_ID_BUILD = resultSet.getInt(1) + 1;
        int actualRunNumber = resultSet.getInt(2) + 1;
        String strInsert = "insert into build (Date,Build_version,Run_number) VALUES('"
                + strDate + "','"
                + buildVersion + actualRunNumber + "',"
                + actualRunNumber + ")";
        stmt.execute(strInsert);
        return actualRunNumber;
    }

    private ResultSet selectBuildVersion() throws SQLException {
        String strInsert = "SELECT id, Run_number from build order by run_number DESC, ID DESC";
        ResultSet rs = stmt.executeQuery(strInsert);
        rs.next();
        ACTUAL_ID_BUILD = rs.getInt(1) + 1;
        return rs;
    }

    public synchronized void insertEntityRecord(List<Double> intervalList, String name) {
        try {

            for (Double interval : intervalList) {
                String strInsert = "insert into entity (name ,tick_duration ,id_build) VALUES('"
                        + name + "',"
                        + interval + ","
                        + ACTUAL_ID_BUILD + ")";
                stmt.addBatch(strInsert);
            }
            stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
