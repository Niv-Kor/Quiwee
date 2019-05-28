package com.states.dashboard;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.main.Window;
import com.states.StateManager;
import com.states.StateManager.Substate;

public class Settings extends InterfacePanel {
	private final static Font FONT=new Font("Aller Display",Font.BOLD,16);
	private final static Dimension FIELD_DIM=new Dimension(120,25);
	private final static Dimension BUTTON_DIM=new Dimension(90,20);
	private final static Dimension DIM = new Dimension((int) (Window.DIM.width / 1.3), Window.DIM.height);
	private static ActionListener al;
	
	private JLabel currentPassL, newPassL, repeatPassL,currentUserNameL, newUserNameL,title;
	private JTextField currentPassF,newPassF,repeatPassF,currentUserNameF,newUserNameF;
	private JButton save,clear; 
	private GridBagConstraints gbc;
	
	private void location(JPanel panel, Component c, int x, int y) {
		gbc.gridx=x;
		gbc.gridy=y;
		panel.add(c,gbc);
	}
	
	public Settings() {
		super(new GridBagLayout());
		setPreferredSize(DIM);
		
		this.gbc=new GridBagConstraints();
		
		
		//Labels
	
		this.title=new JLabel("Settings");
		title.setFont(new Font(FONT.getFontName(), FONT.getStyle(), 30));
		title.setForeground(Color.WHITE);
		
		this.currentPassL=new JLabel("Current password");
		currentPassL.setFont(FONT);
		currentPassL.setForeground(Color.WHITE);
		
		this.newPassL=new JLabel("New password");
		newPassL.setFont(FONT);
		newPassL.setForeground(Color.WHITE);
		
		this.repeatPassL=new JLabel("Repeat new password");
		this.repeatPassL.setFont(FONT);
		this.repeatPassL.setForeground(Color.WHITE);
		
		this.currentUserNameL=new JLabel("Current user name");
		this.currentUserNameL.setFont(FONT);
		this.currentUserNameL.setForeground(Color.WHITE);
		
		this.newUserNameL=new JLabel("New user name");
		this.newUserNameL.setFont(FONT);
		this.newUserNameL.setForeground(Color.WHITE);
		
		//Text field
		
		this.currentPassF=new JTextField();
		this.currentPassF.setPreferredSize(FIELD_DIM);
		
		this.newPassF=new JTextField();
		this.newPassF.setPreferredSize(FIELD_DIM);
		
		this.repeatPassF=new JTextField();
		this.repeatPassF.setPreferredSize(FIELD_DIM);
		
		this.currentUserNameF=new JTextField();
		this.currentUserNameF.setPreferredSize(FIELD_DIM);

		this.newUserNameF=new JTextField();
		this.newUserNameF.setPreferredSize(FIELD_DIM);
		
		//Buttons
		
		this.save = new JButton ("Save");
		save.setBackground(Color.white);
		save.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				selectionButtonPressed();				
			}

			private void selectionButtonPressed() {
				StateManager.setState(Substate.LOGIN);
				
			} 
			} );
		
		this.clear=new JButton ("clear");
		this.clear.setBackground(Color.white);
	}

	@Override
	public void apply(JPanel panel) {
		Insets defaultInsets = new Insets(10, 10, 10, 10);
		
		gbc.insets = new Insets(10, -270, 150, 10);
		location(panel,title,0,0);
		gbc.insets = defaultInsets;
		
		location(panel,currentPassL,0,1);
		location(panel,newPassL,0,2);
		location(panel,repeatPassL,0,3);
		location(panel,currentUserNameL,0,4);
		location(panel,newUserNameL,0,5);
		location(panel,currentPassF,1,1);
		location(panel,newPassF,1,2);
		location(panel,repeatPassF,1,3);
		location(panel,currentUserNameF,1,4);
		location(panel,newUserNameF,1,5);
		gbc.insets = new Insets(90, 10, 10, 10);
		location(panel,save,0,6);
		location(panel,clear,1,6);
	}
}