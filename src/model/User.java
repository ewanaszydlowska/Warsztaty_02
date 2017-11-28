package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class User {

	private long id;
	private String username;
	private String email;
	private String password;
	private int userGroupId;

	public User() {
		super();
		this.id = 0l;
		this.username = "";
		this.email = "";
		this.password = "";
		this.userGroupId = 0;
	}

	public User(String username, String email, String password) {
		super();
		this.id = 0l;
		this.username = username;
		this.email = email;
		setPassword(password);
		this.userGroupId = 0;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public void checkPassword(String password) {
		BCrypt.checkpw(password, this.password);
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public long getId() {
		return id;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0l) {
			String sql = "INSERT INTO users(username, email, password, user_group_id) VALUES (?, ?, ?, ?);";
			String[] generatedColumns = { "ID" }; // jaka kolumna z bazy danych jest automatycznie generowana -> jest
													// auto_increment
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.username);
			ps.setString(2, this.email);
			ps.setString(3, this.password);
			ps.setInt(4, this.userGroupId);
			ps.executeUpdate(); // prepared statement trzyma w sobie dane ktore pobiera z bazy danych
			ResultSet gk = ps.getGeneratedKeys(); // baza zwraca wyniki w postaci resultset (wskazuje na przed pierwszą
													// daną) rekordy zwrocone przez baze

			if (gk.next()) { // przestawia na właściwe id
				this.id = gk.getLong(1); // kolumna 1 tabeli users
			}
			ps.close();
			gk.close();
		} else {
			String sql = "UPDATE users SET username=?, email=?, password=?, user_group_id=? WHERE id=?;"; // moze nowe
																											// preparedstatement?
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.username);
			ps.setString(2, this.email);
			ps.setString(3, this.password);
			ps.setInt(4, this.userGroupId);
			ps.setLong(5, this.id);
			ps.executeUpdate();
			ps.close();
		}
	}

	static public User loadUserById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM users WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			User loadedUser = new User();
			loadedUser.id = rs.getLong("id");
			loadedUser.username = rs.getString("username");
			loadedUser.email = rs.getString("email");
			loadedUser.password = rs.getString("password");
			loadedUser.userGroupId = rs.getInt("user_group_id");
			ps.close();
			rs.close();
			return loadedUser;
		}
		ps.close();
		return null;
	}

	static public User[] loadAllUsers(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM users;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			User loadedUser = new User();
			loadedUser.id = rs.getLong("id");
			loadedUser.username = rs.getString("username");
			loadedUser.email = rs.getString("email");
			loadedUser.password = rs.getString("password");
			loadedUser.userGroupId = rs.getInt("user_group_id");
			users.add(loadedUser);
		}
		User[] uArray = new User[users.size()];
		uArray = users.toArray(uArray);
		ps.close();
		rs.close();
		return uArray;
	}

	public void deleteUser(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM users WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, this.id);
			ps.executeUpdate();
			ps.close();
			this.id = 0;
		}
	}

	public static void loadAllByGroupId(Connection conn, int id) throws SQLException {
		ArrayList<String> members = new ArrayList<>();
		members.add("Członkowie grupy nr " + id);
		String sql = "SELECT username FROM users WHERE user_group_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String name = rs.getString("username");
			members.add(name);
		}
		ps.close();
		rs.close();

		String[] mArray = new String[members.size()];
		mArray = members.toArray(mArray);
		for (int i = 0; i < mArray.length; i++) {
			System.out.println(mArray[i]);
		}

	}

	public static void loadNotDoneById(Connection conn, long id) throws SQLException {
		List<String> exercises = new ArrayList<>();
		String sql = "SELECT * FROM exercise WHERE id NOT IN (SELECT exercise_id FROM solution WHERE users_id=?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			long exId = rs.getLong("id");
			String title = rs.getString("title");
			String desc = rs.getString("description");
			exercises.add(exId + ") " + title + ": " + desc);
		}
		ps.close();
		rs.close();
		for (String s : exercises) {
			System.out.println(s);
		}
	}

}
