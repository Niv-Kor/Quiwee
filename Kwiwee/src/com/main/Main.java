package com.main;
import com.database.SQLModifier;
import com.states.StateManager;
import com.states.StateManager.Substate;

public class Main {
	public static void main(String[] args) {
		SQLModifier.init();
		Window window = new Window();
		StateManager.init(window);
		StateManager.setState(Substate.LOGIN);
		
		//new Dashboard(window);
	}
}