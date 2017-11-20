package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.User;

public class TestClass {

	public static void main(String[] args) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");

			// -------------------sprawdzenie metody saveToDB---------------------------
			// User u = new User("ewa", "email36@mail.com", "password");

			// u.setUserGroupId(1); //normalnie z obiektu user_group get(id)
			// u.saveToDB(conn);

			// -------------------sprawdzenie metody loadUserById------------------------
			// User user2 = User.loadUserById(conn, 12);
			// user2.setEmail("aaa@aaa.pl");
			// user2.saveToDB(conn);

			// -------------------sprawdzenie metody loadAllUsers------------------------
			 User[] users = User.loadAllUsers(conn);
			 for (int i = 0; i < users.length; i++) {
			 User newUser = users[i];
			 System.out.println(newUser.getId() + newUser.getUsername() +
			 newUser.getEmail() + newUser.getPassword() + newUser.getUserGroupId());
			 }

			// -------------------sprawdzenie metody deleteUser--------------------------
			// user2.deleteUser(conn);
			// System.out.println(user2.getId());

			// Exercise.loadAllByUserId(conn, 15l);
			// Solution.loadAllByExerciseId(conn, 2);
			User.loadAllByGroupId(conn, 1);

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
