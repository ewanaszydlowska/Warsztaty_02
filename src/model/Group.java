package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group {
	
	private int id;
	private String name;
	
	public Group() {
		this.id = 0;
		this.name = "";
	}
	
	public Group(String name) {
		this.id = 0;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO user_group(name) VALUES(?);";
			String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.name);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				this.id = rs.getInt(1);
			}
			ps.close();
			rs.close();
		} else {
			String sql = "UPDATE user_group SET name=? WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.name);
			ps.setInt(2, this.id);
			ps.executeUpdate();
			ps.close();
		}
	}
	
	public static Group loadGroupById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM user_group WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			Group loadedGroup = new Group();
			loadedGroup.id = rs.getInt("id");
			loadedGroup.name = rs.getString("name");
			return loadedGroup;
		}
		ps.close();
		rs.close();
		return null;
	}
	
	public static Group[] loadAllGroups(Connection conn) throws SQLException {
		ArrayList<Group> groups = new ArrayList<Group>();
		String sql = "SELECT * FROM user_group;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Group loadedGroup = new Group();
			loadedGroup.id = rs.getInt("id");
			loadedGroup.name = rs.getString("name");
			groups.add(loadedGroup);
		}
		ps.close();
		rs.close();
		Group[] gArray = new Group[groups.size()];
		gArray = groups.toArray(gArray);
		return gArray;
	}
	
	public void deleteGroup(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user_group WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, this.id);
			ps.executeUpdate();
			ps.close();
			this.id = 0;
		}
	}

}
