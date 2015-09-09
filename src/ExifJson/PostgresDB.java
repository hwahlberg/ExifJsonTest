/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExifJson;

import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author bbnthwa
 */
public class PostgresDB {
        private Connection con = null;
    private boolean writeflag = false;
    private int dbError;

    public PostgresDB() {
        writeflag = false;
    }

    /**
     *
     * @param writeflag
     */
    public void setWriteflag(boolean writeflag) {
        this.writeflag = writeflag;
    }

    public void open(String host, String database, String user, String password) {
        String url = "jdbc:postgresql://" + host + "/" + database;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

            // no autocommit
            con.setAutoCommit(false);

        } catch (SQLException ex) {
            System.err.println("open failed: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (SQLException ex) {
            }
        }

    }
    
    public void writeJsonObject(JsonObject jo) {
        
     // String sqltest = "insert into bilder values (2,'{\"call\": \"yyyyyy\"}')";
        String sql = "insert into bilder values (3,'" + jo  + "')";
     // System.out.println("sql=" + sql);
        this.insert(sql);
        
    }

    /**
     *
     * @return
     */
    public int getDbError() {
        return dbError;
    }

    /**
     *
     * Wrapper for insert
     *
     * @param sql
     * @return true if success
     */
    public int insert(String sql) {


        if (writeflag) {
            try {
                doInsert(sql);
            } catch (SQLException ex) {
                System.err.println("insert failed: " + ex.getMessage());
                try {
                    con.rollback();
                } catch (Exception e) {
                }

                return parseError(ex);
            }
        }

        return 0;
    }

    /**
     * Execute insert statement
     *
     * @param sql
     * @throws SQLException
     */
    private void doInsert(String sql) throws SQLException {
        Statement stmt = null;

        stmt = con.createStatement();
        stmt.executeUpdate(sql);
        con.commit();
        if (stmt != null) {
            stmt.close();
        }

    }

    /**
     *
     * @param ex
     */
    private int parseError(SQLException ex) {
        if (ex.getMessage().contains("violates unique constraint")) {
            dbError = -1;
        } else if (ex.getMessage().contains("current transaction is aborted")) {
            dbError = -2;
        } else {
            dbError = -3;
        }

        return dbError;
    }

}
