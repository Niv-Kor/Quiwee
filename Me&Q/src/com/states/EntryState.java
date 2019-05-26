package com.states;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.main.Window;
import com.states.StateManager.Substate;

public class EntryState extends State
{
	protected final static Font FONT = new Font("Tahoma", Font.BOLD, 34);
	private GridBagConstraints gbc;
	private JLabel login, register, or, welcome;
	
	public EntryState(Window w) {
		super(w);
	
		this.gbc = new GridBagConstraints();
		this.panes = new JPanel[1];
		panes[0] = new JPanel(new BorderLayout());
		panes[0].setPreferredSize(Window.DIM);
		
		this.welcome = new JLabel ("WELCOME TO ME&Q!!!!");
		welcome.setForeground(Color.magenta);
		welcome.setFont(FONT);
		
		JPanel northPane = new JPanel(new GridBagLayout());
		northPane.setPreferredSize(new Dimension(Window.DIM.width ,Window.DIM.height / 2));
		northPane.setBackground(Window.COLOR);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		northPane.add(welcome);
		
		this.login = new JLabel("Login");
		login.setForeground(Color.WHITE);
		login.setFont(ConnectionState.LABEL_FONT);
		
		this.or = new JLabel("|");
		or.setForeground(Color.WHITE);
		or.setFont(ConnectionState.LABEL_FONT);
		
		this.register = new JLabel("Register");
		register.setForeground(Color.WHITE);
		register.setFont(ConnectionState.LABEL_FONT);
		
		JPanel southPane = new JPanel(new GridBagLayout());
		southPane.setPreferredSize(new Dimension(Window.DIM.width ,Window.DIM.height / 2));
		southPane.setBackground(Window.COLOR);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		southPane.add(login);
		login.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
			}

			public void mousePressed(MouseEvent e) {
				StateManager.setState(Substate.LOGIN);
				
			}

			public void mouseReleased(MouseEvent e) {
				
			}
			public void mouseEntered(MouseEvent e) {
				
			}
			public void mouseExited(MouseEvent e) {
				
			}
			
		});
		gbc.gridx = 1;
		gbc.gridy = 0;
		southPane.add(or);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		southPane.add(register, gbc);
		
		register.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
			}

			public void mousePressed(MouseEvent e) {
				StateManager.setState(Substate.REGISTER);
			}

			public void mouseReleased(MouseEvent e) {
				
			}

			public void mouseEntered(MouseEvent e) {
				
			}

			public void mouseExited(MouseEvent e) {
				
			}
			
		});
	
		panes[0].add(northPane, BorderLayout.NORTH);
		panes[0].add(southPane, BorderLayout.SOUTH);
	}

	public void insertPanels() {
		window.insert(panes[0], BorderLayout.CENTER);
	}
}