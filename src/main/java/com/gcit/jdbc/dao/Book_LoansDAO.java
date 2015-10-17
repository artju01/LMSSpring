package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;

public class Book_LoansDAO extends BaseDAO {

	
	public void insert(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			save("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate, dateIn) values (?,?,?,?,?,?)",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), 
					loan.getBorrower().getCardNo(), loan.getDateOut(), loan.getDueDate(), 
					loan.getDateIn() });
		}
	}
	
	public void update(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			save("update tbl_book_loans set bookId = ?, branchId = ?, cardNo = ?, dateOut = ?, dueDate = ?, dateIn = ?  "
					+ "where bookId = ? and branchId = ? and cardNo = ?",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), 
							loan.getBorrower().getCardNo(), loan.getDateOut(), loan.getDueDate(), 
							loan.getDateIn(), loan.getBook().getBookId(), loan.getBranch().getBranchId(),
							loan.getBorrower().getCardNo()});
		}
	}
	
	public void updateDueDate(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			save("update tbl_book_loans set bookId = ?, branchId = ?, cardNo = ?, dueDate = ?"
					+ "where bookId = ? and branchId = ? and cardNo = ?",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), 
							loan.getBorrower().getCardNo(), loan.getDueDate(),
							loan.getBook().getBookId(), loan.getBranch().getBranchId(),
							loan.getBorrower().getCardNo()});
		}
	}
	
	

	public void delete(BookLoans loan) throws SQLException {
		if (loan.getBorrower() != null && loan.getBranch() != null && loan.getBook() != null) {
			save("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
					new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo() });
		}
	}
	
	public int readBookLoansCount() throws SQLException {
		return readCount("select count(*) from tbl_book_loans");
	}
	
	public int readUnreturnBookLoansCount() throws SQLException {
		return readCount("select count(*) from tbl_book_loans where dateIn is null");
	}
	
	
	@SuppressWarnings("unchecked")
	public BookLoans readOne(int bookId, int branchId, int cardNo) throws SQLException {
		List<BookLoans> bookloans = (List<BookLoans>) read(
				"select * from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookId, branchId, cardNo});
		if (bookloans != null && bookloans.size() > 0) {
			return bookloans.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BookLoans> readAll() throws SQLException {
		return (List<BookLoans>) read(setPageLimits("select * from tbl_book_loans"), null);
	}
	
	@SuppressWarnings("unchecked")
	public List<BookLoans> readAllUnreturn() throws SQLException {
		return (List<BookLoans>) read(setPageLimits("select * from tbl_book_loans where dateIn is null"), null);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BookLoans> readAllByBook(Book bk) throws SQLException {
		List<BookLoans> read = (List<BookLoans>) read("select * from tbl_book_loans where bookId = ?",
				new Object[] {bk.getBookId()});
		return read;
	}
	
	@SuppressWarnings("unchecked")
	public List<BookLoans> readAllByBorrower(Borrower borrow) throws SQLException {
		List<BookLoans> read = (List<BookLoans>) read("select * from tbl_book_loans where cardNo = ?",
				new Object[] { borrow.getCardNo()});
		return read;
	}

	@SuppressWarnings("unchecked")
	public List<BookLoans> readAllByBorrowerWithNoReturn(Borrower borrow) throws SQLException {
		List<BookLoans> read = (List<BookLoans>) read("select * from tbl_book_loans where cardNo = ? and dateIn is null",
				new Object[] { borrow.getCardNo()});
		return read;
	}
	
	@Override
	protected List<BookLoans> convertResult(ResultSet rs) throws SQLException {
		List<BookLoans> bookLoans = new ArrayList<BookLoans>();
		BookDAO bookDAO = new BookDAO();
		BranchDAO branchDAO = new BranchDAO();
		BorrowerDAO borrowDAO = new BorrowerDAO();
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
		
		conn.close();
		
		return bookLoans;
	}



}
