package com.main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.states.State;

public class Window extends JFrame {
	private static final long serialVersionUID = 3923886508152842668L;
	private final static String NAME = "Me&Q";
	public final static Dimension DIM = new Dimension(1000, 700);
	public final static Color COLOR = Color.GRAY.darker().darker().darker();
	
	private JPanel mainPanel;
	
	public Window() {
		super(NAME);
		setSize(DIM);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//panel
		this.mainPanel = new JPanel(new BorderLayout());
		mainPanel.setPreferredSize(DIM);
		mainPanel.setBackground(COLOR);
		
		add(mainPanel);
		setVisible(true);
		pack();
	}
	
	public JPanel getMainPanel() { return mainPanel; }
	
	public void insert(JPanel panel, String location) {
		try { mainPanel.add(panel, location); }
		catch(IllegalArgumentException e) {
			System.err.println("The location \"" + location + "\" is not part of BorderLayout finals.");
		}
	}
	
	public void remove(JPanel panel) {
		mainPanel.remove(panel);
	}
	
	public void setState(State currentState, State newState) {
		if (currentState != null) {
			JPanel[] oldPanes = currentState.getPanes();
			
			for (int i = 0; i < oldPanes.length; i++)
				remove(oldPanes[i]);
		}
		
		newState.insertPanels();
		setVisible(true);
	}
}