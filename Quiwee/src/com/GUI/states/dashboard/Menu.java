package com.GUI.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.concurrent.Callable;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import com.GUI.constants.ColorConstants;
import com.GUI.constants.FontConstants;
import com.GUI.states.StateManager;
import com.GUI.states.StateManager.Substate;
import com.GUI.states.dashboard.Dashboard.Tabs;
import javaNK.util.GUI.swing.components.InteractiveIcon;
import javaNK.util.GUI.swing.components.InteractiveLabel;
import javaNK.util.files.ImageHandler;
import javaNK.util.math.DimensionalHandler;

public class Menu extends JPanel
{
	private static final long serialVersionUID = -2874727025893098043L;
	private static final Color SELECTED_COLOR = ColorConstants.COLOR_2.brighter();
	private static final String ICONS_PATH = "menu_icons/";
	
	private InteractiveIcon calendarIco, settingsIco, clientsIco, servicesIco, logoutIco;
	private InteractiveLabel calendarLab, settingsLab, clientsLab, servicesLab, logoutLab;
	private GridBagConstraints gbc;

	public Menu(Dashboard dashboard, Dimension dim) {
		super(new BorderLayout());
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		setPreferredSize(dim);
		this.gbc = new GridBagConstraints();
		
		//east labels pane
		JPanel labelsPane = new JPanel(new GridBagLayout());
		labelsPane.setPreferredSize(DimensionalHandler.adjust(this, 70, 100));
		labelsPane.setOpaque(false);
		add(labelsPane, BorderLayout.EAST);
		
		this.calendarLab = new InteractiveLabel("Calendar");
		calendarLab.setForeground(Color.WHITE);
		calendarLab.setFont(FontConstants.SMALL_LABEL_FONT);
		calendarLab.setSelectColor(SELECTED_COLOR);
		calendarLab.setHoverColor(ColorConstants.TEXT_COLOR_SELECTED);
		calendarLab.mousePressed(null);
		calendarLab.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.CALENDAR);
				calendarLab.select();
				settingsLab.release();
				servicesLab.release();
				clientsLab.release();
				logoutLab.release();
				return null;
			}
		});
		
		gbc.insets = new Insets(33, -5, 10, 10);
		addComponent(labelsPane, calendarLab, 0, 0);
		
		this.settingsLab = new InteractiveLabel("Settings");
		settingsLab.setForeground(Color.WHITE);
		settingsLab.setFont(FontConstants.SMALL_LABEL_FONT);
		settingsLab.setSelectColor(SELECTED_COLOR);
		settingsLab.setHoverColor(ColorConstants.TEXT_COLOR_SELECTED);
		settingsLab.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.SETTINGS);
				settingsLab.select();
				calendarLab.release();
				servicesLab.release();
				clientsLab.release();
				logoutLab.release();
				return null;
			}
		});
		
		gbc.insets = new Insets(16, -13, 10, 10);
		addComponent(labelsPane, settingsLab, 0, 1);
		
		this.clientsLab = new InteractiveLabel("Clients");
		clientsLab.setForeground(Color.WHITE);
		clientsLab.setFont(FontConstants.SMALL_LABEL_FONT);
		clientsLab.setSelectColor(SELECTED_COLOR);
		clientsLab.setHoverColor(ColorConstants.TEXT_COLOR_SELECTED);
		clientsLab.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.CLIENTS);
				clientsLab.select();
				calendarLab.release();
				settingsLab.release();
				servicesLab.release();
				logoutLab.release();
				return null;
			}
		});
		
		gbc.insets = new Insets(16, -24, 10, 10);
		addComponent(labelsPane, clientsLab, 0, 2);
		
		this.servicesLab = new InteractiveLabel("Services");
		servicesLab.setForeground(Color.WHITE);
		servicesLab.setFont(FontConstants.SMALL_LABEL_FONT);
		servicesLab.setSelectColor(SELECTED_COLOR);
		servicesLab.setHoverColor(ColorConstants.TEXT_COLOR_SELECTED);
		servicesLab.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				dashboard.setTab(Tabs.SERVICES);
				servicesLab.select();
				calendarLab.release();
				settingsLab.release();
				clientsLab.release();
				logoutLab.release();
				return null;
			}
		});
		
		gbc.insets = new Insets(16, -11, 10, 10);
		addComponent(labelsPane, servicesLab, 0, 3);
		
		this.logoutLab = new InteractiveLabel("Log out");
		logoutLab.setForeground(Color.WHITE);
		logoutLab.setFont(FontConstants.SMALL_LABEL_FONT);
		logoutLab.setHoverColor(ColorConstants.TEXT_COLOR_SELECTED);
		logoutLab.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				StateManager.setState(Substate.ENTRY);
				return null;
			}
		});
		
		gbc.insets = new Insets(430, 0, 20, 10);
		addComponent(labelsPane, logoutLab, 0, 4);
		
		//west icons pane
		JPanel iconsPane = new JPanel(new GridBagLayout());
		iconsPane.setPreferredSize(DimensionalHandler.adjust(this, 30, 100));
		iconsPane.setOpaque(false);
		add(iconsPane, BorderLayout.WEST);
		
		this.calendarIco = new InteractiveIcon(ImageHandler.loadIcon(ICONS_PATH + "calendar.png"));
		calendarIco.setFunction(calendarLab.getFunction());
		gbc.insets = new Insets(30, 20, 10, 10);
		addComponent(iconsPane, calendarIco, 0, 0);
		
		this.settingsIco = new InteractiveIcon(ImageHandler.loadIcon(ICONS_PATH + "settings.png"));
		settingsIco.setFunction(settingsLab.getFunction());
		gbc.insets = new Insets(10, 20, 10, 10);
		addComponent(iconsPane, settingsIco, 0, 1);
		
		this.clientsIco = new InteractiveIcon(ImageHandler.loadIcon(ICONS_PATH + "clients.png"));
		clientsIco.setFunction(clientsLab.getFunction());
		addComponent(iconsPane, clientsIco, 0, 2);
		
		this.servicesIco = new InteractiveIcon(ImageHandler.loadIcon(ICONS_PATH + "services.png"));
		servicesIco.setFunction(servicesLab.getFunction());
		addComponent(iconsPane, servicesIco, 0, 3);
		
		this.logoutIco = new InteractiveIcon(ImageHandler.loadIcon(ICONS_PATH + "logout.png"));
		logoutIco.setFunction(logoutLab.getFunction());
		gbc.insets = new Insets(420, 20, 20, 10);
		addComponent(iconsPane, logoutIco, 0, 4);
	}
	
	private void addComponent(JPanel panel, Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		panel.add(c, gbc);
	}
}