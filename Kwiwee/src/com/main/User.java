package com.main;
import java.sql.SQLException;
import java.util.List;
import javaNK.util.data.MysqlModifier;

public class User
{
	private static String username, password;

	public static boolean login(String u, String p) {
		String query = "SELECT * FROM users;";
		List<String> names, passes;
		
		try {
			names = MysqlModifier.readAllVARCHAR(query, "username");
			passes = MysqlModifier.readAllVARCHAR(query, "password");
			
			for (int i = 0; i < names.size(); i++) {
				if (names.get(i).equals(u) && passes.get(i).equals(p)) {
					username = u;
					password = p;
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
		username = null;
		password = null;
	}

	public static boolean isLogged() { return username != null; }
	public static String getKey() { return username; }
	public static String getPass() { return password; }
}