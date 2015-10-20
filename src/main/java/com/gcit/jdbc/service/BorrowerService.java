package com.gcit.jdbc.service;

import java.util.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.jdbc.dao.BookDAO;
import com.gcit.jdbc.dao.Book_CopiesDAO;
import com.gcit.jdbc.dao.Book_LoansDAO;
import com.gcit.jdbc.dao.BorrowerDAO;
import com.gcit.jdbc.dao.BranchDAO;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;
import com.gcit.jdbc.entity.Branch;

public class BorrowerService {
	@Autowired
	BorrowerDAO borrowerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	BookDAO bookDAO;
	@Autowired
	Book_CopiesDAO bookCopiesDAO;
	@Autowired
	Book_LoansDAO bookLoansDAO;
	
	
	public BorrowerService() {
	}
	
	List<Branch> getAllBranch() throws SQLException {
		return branchDAO.readAll();	
	}		
	
	public List<BookCopies> getAllBookCopiesForBranch(Branch branch) throws SQLException {
		return bookCopiesDAO.readAllByBranch(branch);
	}
	
	public List<BookCopies> getAllAvailableBookCopiesForBranch(Branch branch) throws SQLException {
		return bookCopiesDAO.readAllByBranchWithAtLeastOneCopy(branch);
	}
	
	public List<BookLoans> getAllBookLoansByBorrower(Borrower borrow) throws SQLException {
		return bookLoansDAO.readAllByBorrower(borrow);
	}
	
	public List<BookLoans> getAllBookLoansByBorrowerWithNotReturn(Borrower borrow) throws SQLException {
		return bookLoansDAO.readAllByBorrowerWithNoReturn(borrow);
	}
	
	public Book getBook (int bookId) throws SQLException {
		Book bk = bookDAO.readOne(bookId);
		return bk;
	}
	
	@Transactional
	public void checkOutBook(BookLoans loan) throws SQLException {
		Date currDate = new Date();
		loan.setDateOut(currDate);
		loan.setDateIn(null);
		
		//7 days for due date
		long theFuture = System.currentTimeMillis() + (86400 * 7 * 1000);
		Date nextWeek = new Date(theFuture);
		loan.setDueDate(nextWeek);
		
		BookCopies copy = bookCopiesDAO.readOne(loan.getBook().getBookId(), loan.getBranch().getBranchId());
		copy.setNoOfCopies(copy.getNoOfCopies()-1);
		
		try {
			bookCopiesDAO.update(copy);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		
		//check if there is the same record on the db or not.  
		//If yes, then update the data in DB
		//If no,  insert a new record 
		BookLoans loadData = bookLoansDAO.readOne(loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo());
		try {
			if (loadData != null) {
				bookLoansDAO.update(loan);
			}
			else {
				bookLoansDAO.insert(loan);
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
	@Transactional
	public void returnBook(BookLoans loan) throws SQLException {
		Date currDate = new Date();
		loan.setDateIn(currDate);
		
		BookCopies copy = bookCopiesDAO.readOne(loan.getBook().getBookId(), loan.getBranch().getBranchId());
		copy.setNoOfCopies(copy.getNoOfCopies()+1);
				
		try {
			bookCopiesDAO.update(copy);
			bookLoansDAO.update(loan);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
		
}

