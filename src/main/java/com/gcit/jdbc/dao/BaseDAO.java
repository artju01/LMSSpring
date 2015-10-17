package com.gcit.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public abstract class BaseDAO {
	
	protected abstract Object convertResult(ResultSet rs) throws SQLException;
	protected Connection conn;
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	protected int pageNo;
	protected int pageSize = 10;
	
	protected Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/library", 
				"root", "");
		return conn;
	}
	
	protected int saveWithId(String query, Object[] values) throws SQLException {
		conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		int count = 1;
		for(Object obj : values) {
			stmt.setObject(count, obj);
			count++;
		}
		stmt.executeUpdate();
		
		ResultSet rs = stmt.getGeneratedKeys();
		
		if(rs.next()) {
			int result = rs.getInt(1);
			conn.close();
			return result;
		}
		else 
			conn.close();
			return -1;
	}

	protected void save(String query, Object[] values) throws SQLException {
		conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		
		int count = 1;
		for(Object obj : values) {
			stmt.setObject(count, obj);
			count++;
		}
		
		stmt.executeUpdate();
		
		conn.close();
	}
	
	protected Object read(String query, Object[] values) throws SQLException {
		conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);

		if(values != null) {
			int count = 1;
			for(Object obj : values) {
				stmt.setObject(count, obj);
				count++;
			}
		}
		
		ResultSet rs = stmt.executeQuery();
		//conn.close();
		
		return this.convertResult(rs);

	}
	
	protected int readCount(String query) throws SQLException {
		conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while (rs.next()) {
			count = rs.getInt("count(*)");
		}
		conn.close();
		return count;
	}
	
	protected String setPageLimits(String query) {
		StringBuilder sb = new StringBuilder(query);
		
		sb.append("  LIMIT " + (pageNo - 1)*pageSize + "," + pageSize*pageNo);
		
		return sb.toString();
	}

}
