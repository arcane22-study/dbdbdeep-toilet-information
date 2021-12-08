package com.arcane22.dbdbdeep.v1;


import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

public class DBManager {

    // Private LazyHolder class for singleton pattern
    private static class LazyHolder {
        private static final DBManager instance = new DBManager();
    }

    // Private instance vairables
    private Connection dbConnection;
    private Statement stmt, stmt2;

    // Constructor
    public DBManager() {
        try {
            dbConnection = makeDBConnection();
            stmt = dbConnection.createStatement();
            stmt2 = dbConnection.createStatement();

            createTables();
            insertTouristValues();
            insertToiletValues();
            insertCongestionValues();
            insertReviewValues();
        }
        catch(SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Return instance of DBManager (singleton pattern)
     * @return {DBManager} instance of DBManager
     */
    public static DBManager getInstance() {
        return LazyHolder.instance;
    }


    /*
     * Make postgre db connection
     * @return {Connection} result of DriverManager.getConnection()
     * @exception {SQLException} throws SQLException
     */
     private Connection makeDBConnection() throws SQLException {

        String url = "jdbc:postgresql://localhost/PublicToilet";
        String user = "postgres";
        String password = "hong3883";

        return DriverManager.getConnection(url, user, password);
    }


    /*
     * Insert values in table
     * @param {String} table, table name
     * @param {String...} values, values for inserting in table
     * @return {void}
     */
    public void insertValues(String table, String... values){

        try {
            if(stmt != null) {
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < values.length; i++) {
                    sb.append(values[i]);
                    if(i != values.length - 1) {
                        sb.append(", ");
                    }
                }
                stmt.executeUpdate(String.format("insert into %s values(%s);", table, sb.toString()));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     * Execute query
     * @param {String} queryStr, string of sql query
     * @return {ResultSet}, results of query
     */
    public ResultSet execQuery(String queryStr) {

        ResultSet resultSet = null;
        try {
            if(stmt != null) {
                resultSet = stmt.executeQuery(queryStr);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public ResultSet execQuery(String queryStr, int stmtNum) {

        ResultSet resultSet = null;
        try {
            switch(stmtNum) {
                case 1:
                    if(stmt != null)
                        resultSet = stmt.executeQuery(queryStr);
                    break;
                case 2:
                    if(stmt2 != null)
                        resultSet = stmt2.executeQuery(queryStr);
                    break;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }


    /*
     * Execute query
     * @param {String} queryStr, string of sql query
     * @return {void}
     */
    public boolean execUpdate(String queryStr) {

        try {
            if(stmt != null) {
                stmt.executeUpdate(queryStr);
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    /*
     * make current statement
     * @return {void}
     */
    public void makeCurrStatement(int stmtNum) {
        try {
            switch(stmtNum) {
                case 1:
                    if(stmt == null)
                        stmt = dbConnection.createStatement();
                    break;
                case 2:
                    if(stmt == null)
                        stmt2 = dbConnection.createStatement();
                    break;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     * Close current statement
     * @return {void}
     */
    public void closeCurrStatement(int stmtNum) {
        try {
            switch(stmtNum) {
                case 1:
                    if(stmt != null) {
                        stmt.close();
                        stmt = null;
                    }
                    break;
                case 2:
                    if(stmt2 != null) {
                        stmt2.close();
                        stmt2 = null;
                    }
                    break;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     * Method for creating tables (Tourist, Toilet, Congestion, Review)
     * @exception {SQLException}, can throw sql exception
     */
    private void createTables() throws SQLException{

        stmt.executeUpdate("drop table if exists Tourist");
        stmt.executeUpdate("drop table if exists Toilet");
        stmt.executeUpdate("drop table if exists Congestion");
        stmt.executeUpdate("drop table if exists Review");

        stmt.executeUpdate("create table Tourist(sName varchar(30), tType char(1), bName varchar(30), sAddress varchar(50), primary key(sName, bName));");
        stmt.executeUpdate("create table Toilet(tName varchar(30), address1 varchar(50), address2 varchar(50), unisex boolean, " +
                "mNum int, wNum int, CCTV boolean, changeDiaper varchar(25), pNumber varchar(12), openTime varchar(20), primary key(tName));");
        stmt.executeUpdate("create table Congestion(tName varchar(30), mNum int, wNum int, mUsing int, wUsing int);");
        stmt.executeUpdate("create table Review(tName varchar(30), reviewText varchar(100), starRate int);");
    }


    /*
     * Method for inserting values in Tourist table (read .csv file - 공공데이터 포털 data)
     * @exception {SQLException}, can throw sql exception
     * @exception {IOException}, can throw IO exception (because of file reading)
     */
    private void insertTouristValues() throws SQLException, IOException {

        BufferedReader bufferedReader = FileManager.getInstance().readFile("./data/jeju_tourist.csv");
        String tmp = bufferedReader.readLine();     // ignore first line (because it is attributes of data)
        tmp = bufferedReader.readLine();

        String[] split = null;
        char tType = '\0';

        while(tmp != null) {
            split = tmp.split(",");

            switch(split[1]) {
                case "음식점":
                    tType = 'f';
                    break;
                case "관광지":
                    tType = 's';
                    break;
                case "숙박시설":
                    tType =  'h';
                    break;
                case "화장실":
                    tType = 't';
                    break;
            }

            stmt.executeUpdate(String.format("insert into Tourist values('%s', '%c', '%s', '%s');", split[0], tType, split[2], split[3]));
            tmp = bufferedReader.readLine();
        }
    }


    /*
     * Method for inserting values in Toilet table (read jeju_public_toilet.csv file - 공공데이터 포털 data)
     * @exception {SQLException}, can throw sql exception
     * @exception {IOException}, can throw IO exception (because of file reading)
     */
    private void insertToiletValues() throws SQLException, IOException {

        BufferedReader bufferedReader = FileManager.getInstance().readFile("./data/jeju_public_toilet.csv");
        String tmp = bufferedReader.readLine();     // ignore first line (because it is attributes of data)
        tmp = bufferedReader.readLine();

        String[] split = null;
        boolean unisex, cctv;
        int mNum, wNum;
        unisex = cctv = false;
        mNum = wNum = 0;

        while(tmp != null) {
            split = tmp.split(",");
            unisex = (split[3].equals("Y")) ? true : false;
            mNum = Integer.parseInt(split[4]);
            wNum = Integer.parseInt(split[5]);
            cctv = (split[6].equals("Y")) ? true : false;

            stmt.executeUpdate(String.format("insert into Toilet values('%s', '%s', '%s', %b, %d, %d, %b, '%s', '%s', '%s');",
                    split[0], split[1], split[2], unisex, mNum, wNum, cctv, split[7], split[8], split[9]));
            tmp = bufferedReader.readLine();
        }
    }


    /*
     * Method for inserting values in Congestion table
     * @exception {SQLException}, can throw sql exception
     */
    private void insertCongestionValues() throws SQLException {

        ResultSet rs = stmt.executeQuery("select tName, mNum, wNum from Toilet");
        Statement stmt2 = dbConnection.createStatement();

        String tName = "";
        int mNum, wNum, mUsing, wUsing;
        mNum = wNum = mUsing = wUsing = 0;

        while(rs.next()) {
            tName = rs.getString(1);
            mNum = rs.getInt(2);
            wNum = rs.getInt(3);
            mUsing = (int) (Math.random() * (mNum + 1));
            wUsing = (int) (Math.random() * (wNum + 1));

            stmt2.executeUpdate(String.format("insert into Congestion values('%s', %d, %d, %d, %d);", tName, mNum, wNum, mUsing, wUsing));
        }
        stmt2.close();
    }


    /*
     * Method for inserting values in Review table
     * @exception {SQLException}, can throw sql exception
     * @exception {IOException}, can throw IO exception (because of file reading)
     */
    private void insertReviewValues() throws SQLException, IOException {

        BufferedReader bufferedReader = FileManager.getInstance().readFile("./data/toilet_review.csv");
        String tmp = bufferedReader.readLine();

        String[] split = null;
        int starRate = 0;

        while(tmp != null) {
            split = tmp.split(",");
            starRate = Integer.parseInt(split[2]);

            stmt.executeUpdate(String.format("insert into Review values('%s', '%s', %d);", split[0], split[1], starRate));
            tmp = bufferedReader.readLine();
        }
    }
}
