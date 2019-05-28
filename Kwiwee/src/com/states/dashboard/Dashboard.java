package com.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.main.Window;
import com.states.State;
import com.states.dashboard.calendar.Calendar;

public class Dashboard extends State
{
	public static enum Tabs {
		SERVICES(new ServicesTab()),
		CALENDAR(new Calendar()); 
		
		private JPanel tab;
		
		private Tabs(JPanel tabPanel) {
			this.tab = tabPanel;
			tab.setPreferredSize(Calendar.DIM);
			tab.setOpaque(false);
		}
		
		public void applyOn(Window window, Tabs current) {
			if (current != null) {
				JPanel mainWindowPane = window.getMainPanel();
				BorderLayout layout = (BorderLayout) mainWindowPane.getLayout();
				mainWindowPane.remove(layout.getLayoutComponent(BorderLayout.EAST));
			}
			
			window.insert(tab, BorderLayout.EAST);
			window.revalidate();
			window.invalidate();
			window.repaint();
		}
		
		public JPanel getPanel() { return tab; }
	}
	
	private Tabs currentTab;
	
	public Dashboard(Window w) {
		super(w);
		
		panes = new JPanel[2];
		
		//west pane
		panes[0] = new Menu(this);
		panes[0].setPreferredSize(new Dimension(Window.DIM.width - Calendar.DIM.width, Window.DIM.height));
		panes[0].setBackground(new Color(36,134,215));
	}
	
	public void insertPanels() {
		window.insert(panes[0], BorderLayout.WEST);
		setTab(Tabs.SERVICES);
	}
	
	public void setTab(Tabs tab) {
		tab.applyOn(window, currentTab);
		currentTab = tab;
	}
}