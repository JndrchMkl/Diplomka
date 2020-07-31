package messeges2.db;

import java.sql.*;
import java.text.SimpleDateFormat;

import static messeges2.Settings.ACTUAL_ID_BUILD;

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
        int actualRunNumber = selectBuildVersion() + 1;
        String strInsert = "insert into build (Date,Build_version,Run_number) VALUES('"
                + strDate + "','"
                + buildVersion + actualRunNumber + "',"
                + actualRunNumber + ")";
        stmt.execute(strInsert);
        return actualRunNumber;
    }

    private int selectBuildVersion() throws SQLException {
        String strInsert = "SELECT id, Run_number from build order by Run_number desc";
        ResultSet rs = stmt.executeQuery(strInsert);
        rs.next();
        ACTUAL_ID_BUILD = rs.getInt(1) + 1;
        return rs.getInt(2);
    }
}
