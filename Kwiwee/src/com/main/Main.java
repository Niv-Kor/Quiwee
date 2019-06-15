package com.main;
import com.GUI.states.StateManager;
import com.GUI.states.StateManager.Substate;
import javaNK.util.data.MysqlModifier;

public class Main {
	public static void main(String[] args) {
		//connect to database
		String host = "sql7.freemysqlhosting.net";
		String schema = "sql7287323";
		String username = "sql7287323";
		String password = "EQjYc7Nidj";
		MysqlModifier.connect(host, schema, username, password);
		
		StateManager.setState(Substate.ENTRY);
	}
}