package com.GUI.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.GUI.constants.ColorConstants;
import com.GUI.states.dashboard.calendar.CalendarTab;

import javaNK.util.GUI.swing.containers.Window;
import javaNK.util.GUI.swing.state_management.State;

public class Dashboard extends State
{
	public static enum Tabs {
		CALENDAR(new CalendarTab()),
		SETTINGS(new SettingsTab()),
		SERVICES(new ServicesTab()),
		CLIENTS(new ClientsTab());
		
		private JPanel tab;
		
		private Tabs(JPanel tabPanel) {
			this.tab = tabPanel;
			tab.setPreferredSize(Tab.DIM);
			tab.setOpaque(false);
		}
		
		public void applyOn(Window window, Tabs current) {
			if (current != null) {
				JPanel mainWindowPane = window.getMainPanel();
				BorderLayout layout = (BorderLayout) mainWindowPane.getLayout();
				mainWindowPane.remove(layout.getLayoutComponent(BorderLayout.CENTER));
			}
			
			window.insertPanel(tab, BorderLayout.CENTER);
			window.revalidate();
			window.invalidate();
			window.repaint();
		}
		
		public JPanel getPanel() { return tab; }
	}
	
	private Tabs currentTab;
	
	public Dashboard(Window window) {
		super(window, 2);
		
		Dimension windowDim = window.getPreferredSize();
		Dimension menuDim = new Dimension(windowDim.width - Tab.DIM.width, windowDim.height);
		createPanel(new Menu(this, menuDim), menuDim, ColorConstants.COLOR_3);
	}
	
	public void setTab(Tabs tab) {
		tab.applyOn(window, currentTab);
		currentTab = tab;
	}

	@Override
	public void applyPanels() {
		insertPanel(0, BorderLayout.WEST);
		setTab(Tabs.CALENDAR);
	}
	
	@Override
	public JPanel[] getPanes() {
		JPanel[] panels = new JPanel[2];
		panels[0] = panes[0];
		panels[1] = currentTab.getPanel();
		return panels;
	}
}