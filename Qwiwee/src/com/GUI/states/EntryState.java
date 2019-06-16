package com.GUI.states;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.concurrent.Callable;

import javax.swing.JLabel;

import com.GUI.ColorConstants;
import com.GUI.states.StateManager.Substate;

import javaNK.util.GUI.swing.components.InteractiveLabel;
import javaNK.util.GUI.swing.containers.Window;
import javaNK.util.GUI.swing.state_management.State;
import javaNK.util.math.DimensionalHandler;

public class EntryState extends State
{
	protected final static Font FONT = new Font("Tahoma", Font.BOLD, 34);
	
	public EntryState(Window window) {
		super(window, 2);
		
		createPanel(new GridBagLayout(), DimensionalHandler.adjust(window.getDimension(), 100, 50), null);
		createPanel(new GridBagLayout(), DimensionalHandler.adjust(window.getDimension(), 100, 50), null);
		
		JLabel welcome = new JLabel ("Welcome to Qwiwee!");
		welcome.setForeground(Color.magenta);
		welcome.setFont(FONT);
		addComponent(0, welcome, 0, 0);
		
		InteractiveLabel login = new InteractiveLabel("Login");
		login.setForeground(ColorConstants.TEXT_COLOR_DARK);
		login.setFont(ConnectionState.LABEL_FONT);
		login.setFunction(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				StateManager.setState(Substate.LOGIN);
				return null;
			}
		});
		addComponent(1, login, 0, 0);
		
		InteractiveLabel register = new InteractiveLabel("Register");
		register.setForeground(ColorConstants.TEXT_COLOR_DARK);
		register.setFont(ConnectionState.LABEL_FONT);
		register.setFunction(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				StateManager.setState(Substate.REGISTER);
				return null;
			}
		});
		addComponent(1, register, 2, 0);
	}
	
	@Override
	public void applyPanels() {
		insertPanel(0, BorderLayout.CENTER);
		insertPanel(1, BorderLayout.SOUTH);
	}
}