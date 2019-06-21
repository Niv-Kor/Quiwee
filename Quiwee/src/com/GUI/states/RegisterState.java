package com.GUI.states;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import com.GUI.states.StateManager.Substate;
import com.main.User;
import javaNK.util.GUI.swing.containers.Window;
import javaNK.util.IO.StringVarifier;
import javaNK.util.data.MysqlModifier;
import javaNK.util.files.ImageHandler;

public class RegisterState extends AuthenticationState
{
	public RegisterState(Window window) {
		super(window);
		
		//labels
		gridConstraints.insets = new Insets(10, 103, 10, 10);
		addComponent(emailLab, 0, 1);
		
		gridConstraints.insets = new Insets(10, 73, 10, 10);
		addComponent(nameLab, 0, 2);
		
		gridConstraints.insets = new Insets(10, 72, 10, 10);
		addComponent(passwordLab, 0, 3);
		
		gridConstraints.insets = new Insets(10, 19, 10, 10);
		addComponent(repeatLab, 0, 4);
		
		//text fields
		addComponent(1, emailField, 1, 1);
		addComponent(1, nameField, 1, 2);
		addComponent(1, passField, 1, 3);
		addComponent(1, repeatField, 1, 4);
	}
	
	@Override
	protected void buttonAction() {
		String email = emailField.getText();
		String name = nameField.getText();
		String pass = passField.getDecodedPassword();
		String rep = repeatField.getDecodedPassword();
		
		//fail
		if (!StringVarifier.varifyEmail(email)) {
			fail("Email is illegal.");
			return;
		}
		if (!StringVarifier.varify(name)) {
			fail("Name is illegal.");
			return;
		}
		else if (!StringVarifier.varifyPassword(pass, 8, 20)) {
			if (pass.length() < 8 || pass.length() > 20)
				fail("Password must be between 8-20 characters long.");
			else
				fail("Password is illegal.");
			
			return;
		}
		else if (!rep.equals(pass)) {
			fail("Please repeat your password again.");
			return;
		}
		
		if (User.login(email, pass)) {
			fail("This account already exists.");
			User.logout();
		}
		else {
			String query = "INSERT INTO users(email, full_name, password) "
						 + "VALUES('" + email + "', '" + name + "', '" + pass + "')";
			
			try { MysqlModifier.write(query); }
			catch (SQLException e) {}
			
			window.dispose();
			StateManager.setState(Substate.DASHBOARD);
		}
	}
	
	@Override
	protected void fail(String message) {
		super.fail(message);
		passField.clear();
		repeatField.clear();
	}
	
	@Override
	protected String getTitle() { return "Sign Up"; }
	
	@Override
	protected String getActionName() { return "Sign up"; }

	@Override
	protected ImageIcon getActionIcon() { return ImageHandler.loadIcon("sign_up.png"); }
}