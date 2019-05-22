package com.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.main.Window;
import com.states.State;
import com.states.dashboard.calendar.Calendar;

public class Dashboard extends State {
	public Dashboard(Window w) {
		super(w);
		
		panes = new JPanel[2];
		
		//west pane
		panes[0] = new Menu();
		panes[0].setPreferredSize(new Dimension(Window.DIM.width - Calendar.DIM.width, Window.DIM.height));
		panes[0].setBackground(new Color(36,134,215));
		
		//east pane
		panes[1] = new Calendar();
		panes[1].setPreferredSize(Calendar.DIM);
		panes[1].setBackground(Window.COLOR.brighter());
	}

	public void insertPanels() {
		window.insert(panes[0], BorderLayout.WEST);
		window.insert(panes[1], BorderLayout.EAST);
	}
}