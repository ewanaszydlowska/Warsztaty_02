package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

public class Exercise {

	private int id;
	private String name;
	private String description;
	
	public Exercise() {
		this.id = 0;
		this.name = "";
		this.description = "";
	}
	
	public Exercise(String name, String description) {
		this.id = 0;
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
	
	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO exercise(name, description) VALUES (?, ?);";
			String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.name);
			ps.setString(1, this.description);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				this.id = rs.getInt(1);
			}
			ps.close();
			rs.close();
		} else {
			String sql = "UPDATE exercise SET name=?, description=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.name);
			ps.setString(2, this.description);
			ps.executeUpdate();
			ps.close();
		}
	}
	
	static public Exercise loadExerciseById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM exercise WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			Exercise loadedEx = new Exercise();
			loadedEx.id = rs.getInt("id");
			loadedEx.name = rs.getString("name");
			loadedEx.description = rs.getString("description");
			ps.close();
			rs.close();
			return loadedEx;
		}
		ps.close();
		return null;
	}
	
	static public Exercise[] loadAllExercises(Connection conn) throws SQLException {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		String sql = "SELECT * FROM exercise;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Exercise loadedEx = new Exercise();
			loadedEx.id = rs.getInt("id");
			loadedEx.name = rs.getString("name");
			loadedEx.description = rs.getString("description");
			exercises.add(loadedEx);
		}
		Exercise[] eArray = new Exercise[exercises.size()];
		eArray = exercises.toArray(eArray);
		ps.close();
		rs.close();
		return eArray;
	}
	
	public void deleteExercise(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM exercise WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, this.id);
			ps.executeUpdate();
			ps.close();
			this.id = 0;
		}
		
	}
	
}
