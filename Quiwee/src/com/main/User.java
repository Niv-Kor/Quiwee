package com.main;
import java.sql.SQLException;
import java.util.List;
import javaNK.util.data.MysqlModifier;

public class User
{
	private static String email, name, password;

	public static boolean login(String mail, String pass) {
		String query = "SELECT * FROM users;";
		List<String> emails, passes;
		
		try {
			emails = MysqlModifier.readAllVARCHAR(query, "email");
			passes = MysqlModifier.readAllVARCHAR(query, "password");
			
			for (int i = 0; i < emails.size(); i++) {
				if (emails.get(i).equals(mail) && passes.get(i).equals(pass)) {
					email = mail;
					password = pass;
					return true;
				}
			}
		}
		catch (SQLException e) {}
		
		//failed
		logout();
		return false;
	}

	public static void logout() {
		email = null;
		password = null;
	}

	public static boolean isLogged() { return email != null; }
	public static String getKey() { return email; }
	public static String getPass() { return password; }
	public static String getName() { return name; }
}