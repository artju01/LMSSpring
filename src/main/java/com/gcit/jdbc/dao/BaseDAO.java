package com.gcit.jdbc.dao;


import org.springframework.jdbc.core.JdbcTemplate;


public abstract class BaseDAO {
	protected JdbcTemplate template;
	
	
	public BaseDAO(JdbcTemplate conn) {
		this.template = conn;
	}
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	protected int pageNo;
	protected int pageSize = 10;
	
	
	
	protected String setPageLimits(String query) {
		StringBuilder sb = new StringBuilder(query);
		
		sb.append("  LIMIT " + (pageNo - 1)*pageSize + "," + pageSize*pageNo);
		
		return sb.toString();
	}

}
