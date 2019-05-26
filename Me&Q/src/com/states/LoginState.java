package com.states;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import com.main.User;
import com.main.Window;
import com.states.StateManager.Substate;

public class LoginState extends ConnectionState {
	public LoginState(Window w) {
		super(w);
		
		//labels
		this.username = new JLabel("Username: ");
		username.setForeground(Color.BLACK);
		username.setFont(LABEL_FONT);
		
		this.password = new JLabel("Password: ");
		password.setForeground(Color.BLACK);
		password.setFont(LABEL_FONT);
		
		insertComponent(0, title, 0, 0);
		
		gbc.insets = new Insets(10, 67, 10, 10);
		
		insertComponent(0, username, 0, 1);
		
		gbc.insets = new Insets(10, 72, 10, 10);
		
		insertComponent(0, password, 0, 2);
		
		resetInsets();
		
		//text fields
		Dimension fieldDim = new Dimension(200, 30);
		Border border = new MatteBorder(1, 1, 1, 1, Color.BLACK);
		
		this.usernameField = new JTextField();
		usernameField.setPreferredSize(fieldDim);
		usernameField.setBackground(Color.WHITE.darker());
		usernameField.setBorder(border);
		
		this.passField = new JTextField();
		passField.setPreferredSize(fieldDim);
		passField.setBackground(Color.WHITE.darker());
		passField.setBorder(border);
		
		insertComponent(0, usernameField, 1, 1);
		insertComponent(0, passField, 1, 2);
		
		//button
		this.button = new JButton(getActionName());
		button.setPreferredSize(new Dimension(fieldDim.width / 2, fieldDim.height));
		button.addActionListener(action());
		
		insertComponent(0, button, 1, 3);
		insertComponent(0, error, 1, 4);
	}

	protected String getTitle() { return "Login"; }
	protected String getActionName() { return getTitle().toLowerCase(); }

	protected ActionListener action() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = usernameField.getText();
				String pass = passField.getText();
				
				//fail
				if (name.equals("")) {
					fail("No username entered.");
					return;
				}
				else if (pass.equals("")) {
					fail("No password entered.");
					return;
				}
				
				if (!User.login(name, pass)) fail("This account does not exist.");
				else StateManager.setState(Substate.DASHBOARD);
			}
		};
		
		return al;
	}
}