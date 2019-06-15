package com.GUI.states;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import com.GUI.states.StateManager.Substate;
import com.main.User;

import javaNK.util.GUI.swing.containers.Window;
import javaNK.util.data.MysqlModifier;

public class RegisterState extends ConnectionState
{
	public RegisterState(Window window) {
		super(window);
		
		//labels
		gridConstraints.insets = new Insets(10, 106, 10, 10);
		addComponent(1, emailLab, 0, 1);
		
		gridConstraints.insets = new Insets(10, 67, 10, 10);
		addComponent(1, usernameLab, 0, 2);
		
		gridConstraints.insets = new Insets(10, 72, 10, 10);
		addComponent(1, passwordLab, 0, 3);
		
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		addComponent(1, repeatLab, 0, 4);
		
		//text fields
		addComponent(1, emailField, 1, 1);
		addComponent(1, usernameField, 1, 2);
		addComponent(1, passField, 1, 3);
		addComponent(1, repeatField, 1, 4);
		
		//button
		addComponent(2, button, 1, 5);
		
		//error message
		addComponent(2, error, 1, 6);
	}
	
	@Override
	protected ActionListener getButtonAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = usernameField.getText();
				String pass = passField.getText();
				String rep = repeatField.getText();
				
				//fail
				if (name.equals("")) {
					fail("No username entered.");
					return;
				}
				else if (pass.equals("")) {
					fail("No password entered.");
					return;
				}
				else if (!rep.equals(pass)) {
					fail("Please repeat your password again.");
					return;
				}
				
				if (User.login(name, pass)) {
					fail("This account already exists.");
					User.logout();
				}
				else {
					String query = "INSERT INTO users(username, password) "
								 + "VALUES('" + name + "', '" + pass + "');";
					
					try { MysqlModifier.write(query); }
					catch (SQLException e) {}
					
					window.dispose();
					StateManager.setState(Substate.DASHBOARD);
				}
			}
		};
	}
	
	@Override
	protected void fail(String message) {
		super.fail(message);
		passField.setText("");
		repeatField.setText("");
	}
	
	@Override
	protected String getTitle() { return "Registration"; }
	
	@Override
	protected String getActionName() { return "register"; }
}