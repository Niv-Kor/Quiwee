package com.states;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.main.Window;

public abstract class ConnectionState extends State {
	protected final static Font TITLE_FONT = new Font("Arial", Font.BOLD, 30);
	protected final static Font LABEL_FONT = new Font("Arial", Font.BOLD, 18);
	protected final static Dimension DIM = new Dimension(550, 400);
	
	protected JLabel title, error;
	protected GridBagConstraints gbc;
	protected JLabel username, password;
	protected JTextField usernameField, passField;
	protected JButton button;
	
	public ConnectionState(Window w) {
		super(w);
		
		this.title = new JLabel("<HTML><U>" + getTitle() + "</U></HTML>");
		title.setFont(TITLE_FONT);
		
		this.error = new JLabel();
		error.setForeground(Color.RED);
		
		gbc = new GridBagConstraints();
		resetInsets();
		
		int sideWidth = (Window.DIM.width - DIM.width) / 2;
		int sideHeight = (Window.DIM.height - DIM.height) / 2;
		Dimension sideDim = new Dimension(sideWidth, sideHeight);
		panes = new JPanel[5];
		
		panes[0] = new JPanel(new GridBagLayout());
		panes[0].setPreferredSize(DIM);
		panes[0].setBackground(Color.WHITE);
		
		for (int i = 1; i < panes.length; i++)
			createPanel(i, sideDim, Window.COLOR);
	}
	
	protected void resetInsets() {
		gbc.insets = new Insets(10, 10, 10, 10);
	}

	public void insertPanels() {
		window.insert(panes[0], BorderLayout.CENTER);
		window.insert(panes[1], BorderLayout.WEST);
		window.insert(panes[2], BorderLayout.NORTH);
		window.insert(panes[3], BorderLayout.EAST);
		window.insert(panes[4], BorderLayout.SOUTH);
	}
	
	protected void insertComponent(int paneIndex, Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		panes[paneIndex].add(c, gbc);
	}
	
	protected void fail(String message) {
		error.setText(message);
	}
	
	protected abstract String getTitle();
	protected abstract String getActionName();
	protected abstract ActionListener action();
}