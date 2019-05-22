package com.states.dashboard;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.concurrent.Callable;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.states.StateManager;
import com.states.StateManager.Substate;
import com.utility.OptionLabel;

public class Menu extends JPanel
{
	private static final long serialVersionUID = -2874727025893098043L;
	private final static Font LABEL_FONT = new Font("Tahoma",Font.PLAIN, 18);
	
	private OptionLabel settings, services, clients, logout;
	private GridBagConstraints gbc;

	public Menu () {
		super(new GridBagLayout());
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		this.settings = new OptionLabel("Settings");
		settings.setForeground(Color.WHITE);
		settings.setFont(LABEL_FONT);
	
		settings.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				services.release();
				clients.release();
				logout.release();
				return null;
			}
		});
		
		this.services = new OptionLabel("Services");
		services.setForeground(Color.WHITE);
		services.setFont(LABEL_FONT);

		services.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				settings.release();
				clients.release();
				logout.release();
				return null;
			}
		});
		
		this.clients = new OptionLabel("Clients");
		clients.setForeground(Color.WHITE);
		clients.setFont(LABEL_FONT);

		clients.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				settings.release();
				services.release();
				logout.release();
				return null;
			}
		});
		
		this.logout = new OptionLabel("Log out");
		logout.setForeground(Color.WHITE);
		logout.setFont(LABEL_FONT);

		logout.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				StateManager.setState(Substate.ENTRY);
				return null;
			}
		});
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		insertComponent(settings, 0, 0);
		gbc.insets = new Insets(10, 10, 10, 10);
		insertComponent(clients, 0, 1);
		gbc.insets = new Insets(10, 10, 10, 10);
		insertComponent(services, 0, 2);
		gbc.insets = new Insets(450, 10, 20, 10);
		insertComponent(logout, 0, 3);
	}
	
	protected void insertComponent(Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		add(c, gbc);
	}
}