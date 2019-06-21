package com.GUI.states;
import java.util.HashMap;
import java.util.Map;

import com.GUI.states.dashboard.Dashboard;
import com.GUI.windows.AuthenticationWindow;
import com.GUI.windows.MainWindow;

import javaNK.util.GUI.swing.containers.Window;
import javaNK.util.GUI.swing.state_management.State;
import javaNK.util.debugging.Logger;

public class StateManager
{
	public static enum Substate {
		REGISTER(ContainerWindow.CONNECTION_WINDOW, RegisterState.class),
		LOGIN(ContainerWindow.CONNECTION_WINDOW, LoginState.class),
		DASHBOARD(ContainerWindow.MAIN_WINDOW, Dashboard.class),
		ENTRY(ContainerWindow.MAIN_WINDOW, EntryState.class);
		
		private static enum ContainerWindow {
			MAIN_WINDOW(MainWindow.class),
			CONNECTION_WINDOW(AuthenticationWindow.class);
			
			private Window window;
			
			private ContainerWindow(Class<? extends Window> c) {
				try {
					this.window = c.asSubclass(Window.class).getConstructor().newInstance();
					window.setVisible(false);
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		
		private Window window;
		private ContainerWindow containerWindow;
		private Class<? extends State> stateClass;
		
		private Substate(ContainerWindow container, Class<? extends State> c) {
			this.containerWindow = container;
			this.window = container.window;
			this.stateClass = c;
		}
		
		public static void setState(Substate state, Map<Class<? extends State>, State> stateMap) {
			if (state == null) return;
			
			ContainerWindow containerWindow = state.containerWindow;
			Window window = containerWindow.window;
			State instance = state.createInstance();
			stateMap.put(state.getStateClass(), instance);
			
			window.applyState(instance);
			window.setVisible(true);
		}
		
		/**
		 * Create an instance of the state.
		 * Every State instance needs a mutable window where it has the room to stretch,
		 * and cannot exist without one.
		 * A state can take place in more than one window simultaneously.
		 * 
		 * @param window - The window the will contain the state instance
		 * @return an instance of the state that fits the size of the argument window.
		 */
		private State createInstance() {
			//create instance
			try { return stateClass.asSubclass(State.class).getConstructor(Window.class).newInstance(window); }
			catch (Exception e) { Logger.error("Cannot create an instance of class " + stateClass.getName()); }
			return null;
		}
		
		public Class<? extends State> getStateClass() { return stateClass; }
	}
	
	private static Map<Class<? extends State>, State> stateMap = new HashMap<>();
	
	/**
	 * Set a state on a window.
	 * 
	 * @param window - The window that needs to contain the state
	 * @param substate - The requested state to set
	 */
	public static void setState(Substate substate) {
		Substate.setState(substate, stateMap);
	}
	
	public static State getAppliedState(Class<? extends State> stateClass) {
		return stateMap.get(stateClass);
	}
}