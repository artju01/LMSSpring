package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;

public class BorrowerDAO extends BaseDAO implements ResultSetExtractor<List<Borrower>>{

	public BorrowerDAO(JdbcTemplate conn) {
		super(conn);
	}

	public void insert(Borrower borrow) throws SQLException {
		template.update("insert into tbl_borrower (name, address, phone) values (?,?,?)",
				new Object[] { borrow.getName(), borrow.getAddress(), borrow.getPhone() });
	}
	
	public void update(Borrower borrow) throws SQLException {
		template.update("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[] { borrow.getName(), borrow.getAddress(), borrow.getPhone(), borrow.getCardNo() });
		
		template.update("delete from tbl_book_loans where cardNo = ?", 
				new Object[] { borrow.getCardNo() });
		
		if (borrow.getLoans() != null) {
			for (BookLoans loan : borrow.getLoans()) {
				if (loan.getBorrower() != null && loan.getBook() != null && loan.getBranch() != null) {
					template.update("insert into tbl_book_loans (bookId, branchId, cardNo, dteOut, dueDate, dateIn)", 
							new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo(),
							loan.getDateOut(), loan.getDueDate(), loan.getDateIn()});	
				}
			}
		}
	}

	public void delete(Borrower borrow) throws SQLException {
		template.update("delete from tbl_book_loans where cardNo = ?",
				new Object[] { borrow.getCardNo()});
		
		template.update("delete from tbl_borrower where cardNo = ?",
				new Object[] { borrow.getCardNo()});
	}

	public int readBorrowerCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_borrower", Integer.class);
	}
	
	public Borrower readOne(int cardNo) throws SQLException {
		List<Borrower> borrows = template.query("select * from tbl_borrower where cardNo = ?"
				, new Object[] {cardNo}, this);
		if (borrows != null && borrows.size() > 0) {
			return borrows.get(0);
		}
		else {
			return null;
		}
	}

	public List<Borrower> readAll() throws SQLException {
		return template.query(setPageLimits("select * from tbl_borrower"), this);
	}
	
	public List<Borrower> readAllNoLimit() throws SQLException {
		return template.query("select * from tbl_borrower", this);
	}
	
	
	public List<Borrower> readByBook(Book bk) throws SQLException {
		List<Borrower> read = template.query("select * from tbl_borrower where cardNo in (select cardNo from table_book_loans where bookId = ?)"
				, new Object[] {bk.getBookId()}, this );
		return read;
	}

	public List<Borrower> readAllByNameWithPage(String branchName, int pageNo) throws SQLException {
		String searchText = '%'+branchName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_borrower");
		query = "select * from ("+query+") as t1 where name like ?";
		return template.query(query, new Object[] { searchText }, this);
	}
	
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Borrower> borrows = new ArrayList<Borrower>();
		while (rs.next()) {
			Borrower borrow = new Borrower();
			borrow.setCardNo(rs.getInt("cardNo"));
			borrow.setName(rs.getString("name"));
			borrow.setAddress(rs.getString("address"));
			borrow.setPhone(rs.getString("phone"));
			borrows.add(borrow);
		}
		
		return borrows;
	}


}
