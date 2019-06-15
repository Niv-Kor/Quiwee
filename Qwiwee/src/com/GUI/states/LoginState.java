package com.GUI.states;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.GUI.states.StateManager.Substate;
import com.main.User;
import javaNK.util.GUI.swing.containers.Window;

public class LoginState extends ConnectionState
{
	public LoginState(Window window) {
		super(window);
		
		//labels
		gridConstraints.insets = new Insets(10, 67, 10, 10);
		addComponent(1, usernameLab, 0, 1);
		addComponent(1, passwordLab, 0, 2);
		
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		
		//text fields
		addComponent(1, usernameField, 1, 1);
		addComponent(1, passField, 1, 2);
		
		//button
		addComponent(2, button, 1, 3);
		
		//error label
		addComponent(2, error, 1, 4);
	}
	
	@Override
	protected ActionListener getButtonAction() {
		return new ActionListener() {
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
				else {
					window.dispose();
					StateManager.setState(Substate.DASHBOARD);
				}
			}
		};
	}
	
	@Override
	protected String getTitle() { return "Login"; }
	
	@Override
	protected String getActionName() { return getTitle().toLowerCase(); }
}