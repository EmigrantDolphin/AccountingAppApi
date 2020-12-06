package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.Abstract;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.ServerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AccountingAppAbstraction {
    private static String url = "jdbc:mysql://localhost:3306/accounting_app_store";
    private static String username = "user";
    private static String password = "";
    private static Connection connection;

    public static void setServerOptions(ServerOptions serverOptions) throws Exception{
        if (!url.equals("")){
            throw new Exception("server connection is already setup. you are not allowed to change it");
        }

        url = serverOptions.server;
        username = serverOptions.username;
        password = serverOptions.password;
    }

    protected Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){

            try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch (ClassNotFoundException ex){
                throw new SQLException(ex);
            }

            connection = DriverManager.getConnection(url, username, password);
        }
        connection.setAutoCommit(true);
        return connection;
    }
}
