package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Solution {
	
	private int id;
	private Date created;
	private Date updated;
	private String description;
	private int exerciseId;
	private long usersId;
	
	public Solution() {
		this.id = 0;
		this.created = null;
		this.updated = null;
		this.description = "";
		this.exerciseId = 0;
		this.usersId = 0l;
	}
	
	public Solution(Date created, Date updated, String description) {
		this.id = 0;
		this.created = created;
		this.updated = updated;
		this.description = description;
		this.exerciseId = 0;
		this.usersId = 0l;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}

	public long getUsersId() {
		return usersId;
	}

	public void setUsersId(long usersId) {
		this.usersId = usersId;
	}

	public int getId() {
		return id;
	}
	public void saveToDB(Connection conn) throws SQLException {
		if(this.id == 0l) {
			String sql = "INSERT INTO solution(created, updated, description, exercise_id, users_id) VALUES (NOW(), NOW(), ?, ?, ?);";
			String[] generatedColumns = {"ID"};		
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.description);
			ps.setInt(2, this.exerciseId);
			ps.setLong(3, this.usersId);
			ps.executeUpdate();						
			ResultSet gk = ps.getGeneratedKeys();	
			
			if (gk.next()) {				
				this.id = gk.getInt(1);
			}
			ps.close();
			gk.close();
			
			Solution tempSolution = Solution.loadSolutionById(conn, this.id);
			this.created = tempSolution.created;
			this.updated = tempSolution.updated;
			
		} else {
			String sql = "UPDATE solution SET updated=NOW(), description=?, exercise_id=?, users_id=? WHERE id=?;"; //moze nowe preparedstatement?
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.description);
			ps.setInt(2, this.exerciseId);
			ps.setLong(3, this.usersId);
			ps.setInt(4, this.id);
			ps.executeUpdate();
			ps.close();
			
			Solution tempSolution = Solution.loadSolutionById(conn, this.id);
			this.updated = tempSolution.updated;
		}
	}
	
	static public Solution loadSolutionById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM solution WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = rs.getInt("id");
			loadedSolution.created = rs.getDate("created");
			loadedSolution.updated = rs.getDate("updated");
			loadedSolution.description = rs.getString("description");
			loadedSolution.exerciseId = rs.getInt("exercise_id");
			loadedSolution.usersId = rs.getLong("users_id");
			ps.close();
			rs.close();
			return loadedSolution;
		}
		ps.close();
		return null;
	}
	
	static public Solution[] loadAllSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM solution;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = rs.getInt("id");
			loadedSolution.created = rs.getDate("created");
			loadedSolution.updated = rs.getDate("updated");
			loadedSolution.description = rs.getString("description");
			loadedSolution.exerciseId = rs.getInt("exercise_id");
			loadedSolution.usersId = rs.getLong("users_id");
			solutions.add(loadedSolution);
		}
		Solution[] sArray = new Solution[solutions.size()];
		sArray = solutions.toArray(sArray);
		ps.close();
		rs.close();
		return sArray;
	}
	
	public void deleteSolution(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM solution WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, this.id);
			ps.executeUpdate();
			ps.close();
			this.id = 0;
		}
	}
	
	public static void loadAllByExerciseId (Connection conn, int id) throws SQLException {
		ArrayList<String> solutions = new ArrayList<String>();
		String beginList = "Rozwiazania zadania o id " + id + ":";
		solutions.add(beginList);
		String sql = "SELECT description FROM solution WHERE exercise_id=? ORDER BY updated DESC;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			String desc = rs.getString("description");
			solutions.add(" * " + desc);
		}
		ps.close();
		rs.close();
		
		String[] sArray = new String[solutions.size()];
		sArray = solutions.toArray(sArray);
		for (int i = 0; i < sArray.length; i++) {
			System.out.println(sArray[i]);
		}
	}
	
	public static List<Solution> loadArrayByExerciseId (Connection conn, int id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM solution WHERE exercise_id=? ORDER BY updated DESC;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = rs.getInt("id");
			loadedSolution.created = rs.getDate("created");
			loadedSolution.updated = rs.getDate("updated");
			loadedSolution.description = rs.getString("description");
			loadedSolution.exerciseId = rs.getInt("exercise_id");
			loadedSolution.usersId = rs.getLong("users_id");
			solutions.add(loadedSolution);
		}
		ps.close();
		rs.close();
		
		return solutions;
	}
	
	public static boolean checkIfExists(Connection conn, int exId, long userId) throws SQLException {
		String sql = "SELECT * FROM solution WHERE exercise_id=? AND users_id=? ORDER BY updated DESC;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, exId);
		ps.setLong(2, userId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			return true;
		} else {
			return false;
		}
	}
	
}
