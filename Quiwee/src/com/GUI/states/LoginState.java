package com.GUI.states;
import java.awt.Insets;
import javax.swing.ImageIcon;
import com.GUI.states.StateManager.Substate;
import com.main.User;
import javaNK.util.GUI.swing.containers.Window;
import javaNK.util.files.ImageHandler;

public class LoginState extends AuthenticationState
{
	public LoginState(Window window) {
		super(window);
		
		//labels
		gridConstraints.insets = new Insets(10, 97, 10, 10);
		addComponent(emailLab, 0, 1);
		gridConstraints.insets = new Insets(10, 67, 10, 10);
		addComponent(passwordLab, 0, 2);
		
		//text fields
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		addComponent(emailField, 1, 1);
		addComponent(passField, 1, 2);
	}
	
	@Override
	protected void buttonAction() {
		String email = emailField.getText();
		String pass = passField.getDecodedPassword();
		
		//fail
		if (email.equals("")) {
			fail("No email entered.");
			return;
		}
		else if (pass.equals("")) {
			fail("No password entered.");
			return;
		}
		
		if (!User.login(email, pass)) fail("This account does not exist.");
		else {
			window.dispose();
			StateManager.setState(Substate.DASHBOARD);
		}
	}
	
	@Override
	protected String getTitle() { return "Login"; }
	
	@Override
	protected String getActionName() { return getTitle().toLowerCase(); }

	@Override
	protected ImageIcon getActionIcon() { return ImageHandler.loadIcon("sign_in.png"); }
}