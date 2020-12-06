package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.ServerOptions;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.Abstract.AccountingAppAbstraction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class AccountingApplication {

	public static void main(String[] args) {
		if (!trySetupMysqlServer()){
			GlobalMessage.show("Failed to establish connection to database. Check credentials and try again.");
			return;
		}

		SpringApplication.run(AccountingApplication.class, args);
	}


	public static boolean trySetupMysqlServer(){
		final String serverInfoPath = "./serverInfo.txt";
		FileDriver<ServerOptions> fileDriver = new FileDriver<>();
		var loadedServerOptions = fileDriver.importFile(serverInfoPath);

		var server = new JTextField(loadedServerOptions.server);
		var username = new JTextField(loadedServerOptions.username);
		var password = new JTextField(loadedServerOptions.password);

		var fields = new Object[]{
				"jdbc server", server,
				"username", username,
				"password", password,
		};

		int result = JOptionPane.showConfirmDialog(null, fields, "title", JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION){
			var serverOptions = new ServerOptions();
			serverOptions.server = server.getText();
			serverOptions.username = username.getText();
			serverOptions.password = password.getText();

			fileDriver.exportFile(serverOptions, "./serverInfo.txt");

			if( tryConnectToDB(serverOptions) ){
				try{
					AccountingAppAbstraction.setServerOptions(serverOptions);
					return true;
				}catch (Exception e){
					GlobalMessage.show("Failed to set server options: " + e.getMessage());
				}
			}
		}

		return false;
	}

	public static boolean tryConnectToDB(ServerOptions serverOptions){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch (ClassNotFoundException ex){
			GlobalMessage.show("Failed to find jdbc.Diver(lib is in this project, worked on my machines:/): " + ex.getMessage());
		}

		try{
			Connection connection = DriverManager.getConnection(serverOptions.server, serverOptions.username, serverOptions.password);
			connection.close();
			return true;
		}catch (SQLException e){
			GlobalMessage.show("Failed to connect to database: " + e.getMessage());
		}
		return false;
	}

}
