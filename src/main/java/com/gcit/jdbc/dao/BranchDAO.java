package com.gcit.jdbc.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.Borrower;
import com.gcit.jdbc.entity.Branch;



public class BranchDAO extends BaseDAO implements ResultSetExtractor<List<Branch>>{

	public BranchDAO(JdbcTemplate conn) {
		super(conn);
	}

	public void insert(Branch branch) throws SQLException {
		template.update("insert into tbl_library_branch (branchName, branchAddress) values (?,?)",
				new Object[] { branch.getName(), branch.getAddress() });
	}
	
	public void update(Branch branch) throws SQLException {
		template.update("update tbl_library_branch set branchName = ? , branchAddress = ? where branchId = ?",
				new Object[] { branch.getName(), branch.getAddress(), branch.getBranchId() });
	}

	public void delete(Branch branch) throws SQLException {
		template.update("delete from tbl_book_copies where branchId = ?",
				new Object[] { branch.getBranchId() });
		
		template.update("delete from tbl_book_loans where branchId = ?",
				new Object[] { branch.getBranchId() });
		
		template.update("delete from tbl_library_branch where branchId = ?",
				new Object[] { branch.getBranchId() });
	}
	
	public int readBranchCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_library_branch", Integer.class);
	}
	
	public Branch readOne(int branchId) throws SQLException {
		List<Branch> branches = template.query(
				"select * from tbl_library_branch where branchId = ?",
				new Object[] { branchId }, this);
		if (branches != null && branches.size() > 0) {
			return branches.get(0);
		} else {
			return null;
		}
	}

	public List<Branch> readAllByNameWithPage(String branchName, int pageNo) throws SQLException {
		String searchText = '%'+branchName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_library_branch");
		query = "select * from ("+query+") as t1 where branchName like ?";
		return template.query(query, new Object[] { searchText }, this);
	}
	
	public List<Branch> readAll() throws SQLException {
		List<Branch> read = template.query(setPageLimits("select * from tbl_library_branch"), this);
		return read;
	}
	
	public List<Branch> readAllNoLimit() throws SQLException {
		List<Branch> read = template.query("select * from tbl_library_branch", this);
		return read;
	}

	public List<Branch> readAllByBook(Book bk) throws SQLException {
		return template.query(
				"select * from tbl_library_branch where branchId in (select branchId from tbl_book_copies where bookId = ?)",
				new Object[] { bk.getBookId() }, this);
	}

	public List<Branch> readAllByBorrower(Borrower brw) throws SQLException {
		return template.query(
				"select * from tbl_library_branch where branchId in (select branchId from tbl_book_loans where cardNo = ?)",
				new Object[] { brw.getCardNo() }, this);
	}
	
	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Branch> branches = new ArrayList<Branch>();
		while (rs.next()) {
			Branch branch = new Branch();
			branch.setBranchId(rs.getInt("branchId"));
			branch.setName(rs.getString("branchName"));
			branch.setAddress(rs.getString("branchAddress"));
			branches.add(branch);
		}
		
		return branches;
	}	

}
