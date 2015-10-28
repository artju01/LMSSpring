package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.BookCopiesByBranch;

public class BookCopiesByBranchDAO extends BaseDAO implements ResultSetExtractor<List<BookCopiesByBranch>>{ 
	
	@Autowired
	BranchDAO branchDAO;
	
	public BookCopiesByBranchDAO(JdbcTemplate conn) {
		super(conn);
	}

	public List<BookCopiesByBranch> readAll() throws SQLException {
		List<BookCopiesByBranch> read = template.query("select branchId,sum(noOfcopies) from tbl_book_copies group by branchId", this);
		return read;
	}
	 
	
	@Override
	public List<BookCopiesByBranch> extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		List<BookCopiesByBranch> copies = new ArrayList<BookCopiesByBranch>();	
		while (rs.next()) {
			BookCopiesByBranch copy = new BookCopiesByBranch();		
			copy.setBranch(branchDAO.readOne(rs.getInt("branchId")));
			copy.setNoOfCopies(rs.getInt("sum(noOfCopies)"));
			copies.add(copy);
		}

		return copies;
	}
}
