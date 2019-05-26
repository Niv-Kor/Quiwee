package com.states;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.main.Window;

public abstract class State {
	protected JPanel[] panes;
	protected Window window;
	
	public State(Window w) {
		this.window = w;
	}
	
	protected void createPanel(int index, Dimension d, Color c) {
		panes[index] = new JPanel();
		panes[index].setPreferredSize(d);
		panes[index].setBackground(c);
	}
	
	public abstract void insertPanels();
	public JPanel[] getPanes() { return panes; }
}