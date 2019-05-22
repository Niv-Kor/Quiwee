package com.states;
import com.main.Window;
import com.states.dashboard.Dashboard;

public class StateManager {
	public static enum Substate {
		REGISTER(RegisterState.class),
		LOGIN(LoginState.class),
		DASHBOARD(Dashboard.class),
		ENTRY(EntryState.class);
		
		private Class<? extends State> c;
		private State instance;
		
		private Substate(Class<? extends State> c) {
			this.c = c;
		}
		
		public void createInstance(Window w) {
			try { this.instance = c.asSubclass(State.class).getConstructor(Window.class).newInstance(w); }
			catch (Exception e) { System.err.println("Cannot create an instance of class " + getSubstateClass().getName()); }
		}
		
		public Class<?> getSubstateClass() { return c; }
		public State getSubstateInstance() { return instance; }
	}
	
	private static Window window;
	private static State currentState;
	
	public static void init(Window w) {
		window = w;
		
		Substate[] substates = Substate.values();
		for (Substate s : substates) s.createInstance(w);
	}
	
	public static void setState(Substate s) {
		State instance = s.getSubstateInstance();
		window.setState(currentState, instance);
		currentState = instance;
	}
}