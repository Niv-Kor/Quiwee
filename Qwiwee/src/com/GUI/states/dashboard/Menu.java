package com.GUI.states.dashboard;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.concurrent.Callable;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.GUI.states.StateManager;
import com.GUI.states.StateManager.Substate;
import com.GUI.states.dashboard.Dashboard.Tabs;

import javaNK.util.GUI.swing.components.InteractiveLabel;
import javaNK.util.files.FontHandler;
import javaNK.util.files.FontHandler.FontStyle;

public class Menu extends JPanel
{
	private static final long serialVersionUID = -2874727025893098043L;
	private static final Font LABEL_FONT = FontHandler.load("Ubuntu", FontStyle.PLAIN, 18);
	
	private InteractiveLabel calendar, settings, services, clients, logout;
	private GridBagConstraints gbc;

	public Menu(Dashboard dashboard) {
		super(new GridBagLayout());
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		this.calendar = new InteractiveLabel("Calendar");
		calendar.setForeground(Color.WHITE);
		calendar.setFont(LABEL_FONT);
		calendar.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.CALENDAR);
				settings.release();
				services.release();
				clients.release();
				logout.release();
				return null;
			}
		});
		
		this.settings = new InteractiveLabel("Settings");
		settings.setForeground(Color.WHITE);
		settings.setFont(LABEL_FONT);
		settings.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.SETTINGS);
				calendar.release();
				services.release();
				clients.release();
				logout.release();
				return null;
			}
		});
		
		this.services = new InteractiveLabel("Services");
		services.setForeground(Color.WHITE);
		services.setFont(LABEL_FONT);

		services.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.SERVICES);
				calendar.release();
				settings.release();
				clients.release();
				logout.release();
				return null;
			}
		});
		
		this.clients = new InteractiveLabel("Clients");
		clients.setForeground(Color.WHITE);
		clients.setFont(LABEL_FONT);

		clients.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.CLIENTS);
				calendar.release();
				settings.release();
				services.release();
				logout.release();
				return null;
			}
		});
		
		this.logout = new InteractiveLabel("Log out");
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
		insertComponent(calendar, 0, 0);
		gbc.insets = new Insets(10, 10, 10, 10);
		insertComponent(settings, 0, 1);
		gbc.insets = new Insets(10, 10, 10, 10);
		insertComponent(clients, 0, 2);
		gbc.insets = new Insets(10, 10, 10, 10);
		insertComponent(services, 0, 3);
		gbc.insets = new Insets(450, 10, 20, 10);
		insertComponent(logout, 0, 4);
	}
	
	protected void insertComponent(Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		add(c, gbc);
	}
}