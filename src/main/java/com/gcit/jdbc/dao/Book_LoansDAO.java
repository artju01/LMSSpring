package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;

public class Book_LoansDAO extends BaseDAO implements ResultSetExtractor<List<BookLoans>> {

	public Book_LoansDAO(JdbcTemplate conn) {
		super(conn);
	}

	@Autowired
	BookDAO bookDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	BorrowerDAO borrowDAO;
	
	public void insert(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			template.update("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate, dateIn) values (?,?,?,?,?,?)",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), 
					loan.getBorrower().getCardNo(), loan.getDateOut(), loan.getDueDate(), 
					loan.getDateIn() });
		}
	}
	
	public void update(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			template.update("update tbl_book_loans set bookId = ?, branchId = ?, cardNo = ?, dateOut = ?, dueDate = ?, dateIn = ?  "
					+ "where bookId = ? and branchId = ? and cardNo = ?",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), 
							loan.getBorrower().getCardNo(), loan.getDateOut(), loan.getDueDate(), 
							loan.getDateIn(), loan.getBook().getBookId(), loan.getBranch().getBranchId(),
							loan.getBorrower().getCardNo()});
		}
	}
	
	public void updateDueDate(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			template.update("update tbl_book_loans set bookId = ?, branchId = ?, cardNo = ?, dueDate = ?"
					+ "where bookId = ? and branchId = ? and cardNo = ?",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), 
							loan.getBorrower().getCardNo(), loan.getDueDate(),
							loan.getBook().getBookId(), loan.getBranch().getBranchId(),
							loan.getBorrower().getCardNo()});
		}
	}
	
	public void delete(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			template.update("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo() });
		}
	}
	
	public int readBookLoansCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_book_loans", Integer.class);
	}
	
	public int readUnreturnBookLoansCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_book_loans where dateIn is null", Integer.class);
	}
	
	public BookLoans readOne(int bookId, int branchId, int cardNo) throws SQLException {
		List<BookLoans> bookloans = template.query(
				"select * from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookId, branchId, cardNo}, this);
		if (bookloans != null && bookloans.size() > 0) {
			return bookloans.get(0);
		} else {
			return null;
		}
	}

	public List<BookLoans> readAll() throws SQLException {
		return (List<BookLoans>) template.query(setPageLimits("select * from tbl_book_loans"), this);
	}
	
	public List<BookLoans> readAllUnreturn() throws SQLException {
		return (List<BookLoans>) template.query(setPageLimits("select * from tbl_book_loans where dateIn is null"), this);
	}
	
	public List<BookLoans> readAllByBook(Book bk) throws SQLException {
		List<BookLoans> read = template.query("select * from tbl_book_loans where bookId = ?",
				new Object[] {bk.getBookId()}, this);
		return read;
	}
	
	public List<BookLoans> readAllByBorrower(Borrower borrow) throws SQLException {
		List<BookLoans> read = template.query("select * from tbl_book_loans where cardNo = ?",
				new Object[] { borrow.getCardNo()}, this);
		return read;
	}

	public List<BookLoans> readAllByBorrowerWithNoReturn(Borrower borrow) throws SQLException {
		List<BookLoans> read = template.query("select * from tbl_book_loans where cardNo = ? and dateIn is null",
				new Object[] { borrow.getCardNo()}, this);
		return read;
	}
	
	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<BookLoans> bookLoans = new ArrayList<BookLoans>();
		while (rs.next()) {
			BookLoans loan = new BookLoans();
			loan.setBook(bookDAO.readOne(rs.getInt("bookId")));
			loan.setBranch(branchDAO.readOne(rs.getInt("branchId")));
			loan.setBorrower(borrowDAO.readOne(rs.getInt("cardNo")));
			loan.setDateOut(rs.getDate("dateOut"));
			loan.setDueDate(rs.getDate("dueDate"));
			loan.setDateIn(rs.getDate("dateIn"));
			bookLoans.add(loan);
		}
	
		return bookLoans;
	}



}
