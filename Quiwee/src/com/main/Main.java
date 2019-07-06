package com.main;
import java.sql.SQLException;
import com.GUI.states.StateManager;
import com.GUI.states.StateManager.Substate;
import javaNK.util.data.MysqlConnection;
import javaNK.util.data.MysqlModifier;
import javaNK.util.debugging.Logger;

public class Main {
	public static void main(String[] args) {
		//connect to database
		String host = "sql7.freemysqlhosting.net";
		String schema = "sql7287323";
		String username = "sql7287323";
		String password = "EQjYc7Nidj";
		try {
			MysqlConnection connection = new MysqlConnection(host, schema, username, password);
			MysqlModifier.connect(connection);
		}
		catch (SQLException e) {
			Logger.error("Unable to establish a connection to the data base.");
			return;
		}
		
		StateManager.setState(Substate.ENTRY);
	}
}