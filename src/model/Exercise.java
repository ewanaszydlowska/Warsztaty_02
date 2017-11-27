package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
			String sql = "INSERT INTO exercise(title, description) VALUES (?, ?);";
			String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.name);
			ps.setString(2, this.description);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				this.id = rs.getInt(1);
			}
			ps.close();
			rs.close();
		} else {
			String sql = "UPDATE exercise SET title=?, description=? WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.name);
			ps.setString(2, this.description);
			ps.setInt(3, this.id);
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
			loadedEx.name = rs.getString("title");
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
			loadedEx.name = rs.getString("title");
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
	
	public static void loadAllByUserId(Connection conn, long id) throws SQLException {
		ArrayList<String> solutions = new ArrayList<String>();
		String sql = "SELECT exercise.id, solution.description FROM exercise JOIN solution ON exercise.id = solution.exercise_id "
				+ "WHERE solution.users_id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			int exerciseId = rs.getInt("exercise.id");
			String exercise = Integer.toString(exerciseId);
			String solutionDesc = rs.getString("solution.description");
			solutions.add("zadanie " + exercise + ". " + solutionDesc);
		}
		ps.close();
		rs.close();

		String[] sArray = new String[solutions.size()];
		sArray = solutions.toArray(sArray);
		for (int i = 0; i < sArray.length; i++) {
			System.out.println(sArray[i]);
		}
		
	}
	
}
