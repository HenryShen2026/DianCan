package com.example.diancan.comm;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private String dbClassString = "com.mysql.jdbc.Driver";
//    private String dbConString = "jdbc:mysql://db4free.net:3306/diancan";
//    private String dbConString = "jdbc:mysql://192.168.58.200:3306/diancan";
//    private String dbConString = "jdbc:mysql://192.168.0.76:3306/diancan";
//    private String dbConString = "jdbc:mysql://10.1.11.16:3306/diancan?useSSL=false";
private String dbConString = "jdbc:mysql://172.20.10.9:3306/diancan";
//    private String dbConString = "jdbc:mysql://172.16.0.151:3306/diancan";
    private String dbUserString = "chesterwu";
    private String dbPwdString = "1qaz2wsx";
    public Connection connection(){
        try {
            Class.forName(dbClassString);
            Log.d("main", dbConString +","+ dbUserString+","+ dbPwdString);
            Connection conn = DriverManager.getConnection(dbConString, dbUserString, dbPwdString);
            return conn;
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
