package com.main;
import java.util.List;
import com.database.SQLModifier;

public class User
{
	private static String username, password;

	public static boolean login(String u, String p) {
		String query = "SELECT * FROM users;";
		List<String> names = SQLModifier.readAllVARCHAR(query, "username");
		List<String> passes = SQLModifier.readAllVARCHAR(query, "password");

		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).equals(u) && passes.get(i).equals(p)) {
				username = u;
				password = p;
				return true;
			}
		}
		
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