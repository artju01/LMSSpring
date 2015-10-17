package com.gcit.jdbc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	private BorrowerDAO borrowerDAO;
	private BranchDAO branchDAO;
	private BookDAO bookDAO;
	private Book_CopiesDAO bookCopiesDAO;
	private Book_LoansDAO bookLoansDAO;
	
	private BufferedReader in;
	
	public BorrowerService() {
		borrowerDAO = new BorrowerDAO();
		branchDAO = new BranchDAO();
		bookDAO = new BookDAO();
		bookCopiesDAO = new Book_CopiesDAO();
		bookLoansDAO = new Book_LoansDAO();
		
		in = new BufferedReader(new InputStreamReader(System.in));
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
		
		/*
		 *    Command input
		 */
		
	public void commandInput() throws IOException, SQLException {
		System.out.println("Enter the your Card Number: ");
		String keyboard = in.readLine();			
		int cardNumber = Integer.parseInt(keyboard);
			
		Borrower borrow = borrowerDAO.readOne(cardNumber);
		if (borrow != null) {
			borrowerCommand2(borrow);
		}
		else {
			while(borrow == null) {
				System.out.println("Invalid card number!\n\n");
				System.out.println("Enter the your Card Number: ");
				keyboard = in.readLine();			
				cardNumber = Integer.parseInt(keyboard);
				borrow = borrowerDAO.readOne(cardNumber);
			}
			borrowerCommand2(borrow);
		}
	}
	
	private void borrowerCommand2(Borrower borrow) throws IOException, SQLException {
		while (true) {
			System.out.println("1)Check out a book\n"
					+ "2)Return a Book\n"
					+ "3)Quit to Previous");
			
			String keyboard = in.readLine();
			int input = Integer.parseInt(keyboard);
			
			if (input == 1) {
				//check out a book
				chceckOutBookCommandLine(borrow);
			}
			else if (input == 2) {
				//return a book
				returnBookCommandLine(borrow);
			}
			else {
				break;
			}
		}
	}
	
	private void chceckOutBookCommandLine (Borrower borrow) throws IOException, SQLException {
		while (true) {
			//show all branch
			List<Branch> branches = this.getAllBranch();
			
			System.out.println("Pick the Branch you want to check out from:");
			int count = 1;
			for (Branch branch : branches) {
				System.out.println(count+") "+branch.getName());
				count++;
			}
			System.out.println(count+") Quit to previous");
			
			String keyboard = in.readLine();
			int input = Integer.parseInt(keyboard);
			
			if (input == count) {
				break;
			}
			else {
				System.out.println("Pick the Book you want to check out");
				Branch selectedBranch = branches.get(input-1);
				//get all book copies by this branch
				List<BookCopies> bookCopies = this.getAllAvailableBookCopiesForBranch(selectedBranch);
				List<Book> allBook = new ArrayList<Book>();
				int bookCount = 1;
				
				for (BookCopies copy : bookCopies) {
					Book bk = this.getBook(copy.getBook().getBookId());
					allBook.add(bk);
					System.out.println(bookCount+") "+bk.getTitle());
					bookCount++;
				}
				System.out.println(bookCount+") Quit to cancel operation");
				keyboard = in.readLine();
				int input2 = Integer.parseInt(keyboard);
				
				if (input2 == bookCount) {
					break;
				}
				else {
					Book selectedBook = allBook.get(input2-1);
					BookLoans loan = new BookLoans();
					loan.setBook(selectedBook);
					loan.setBorrower(borrow);
					loan.setBranch(selectedBranch);
					this.checkOutBook(loan);
				}
			}
		}
	}
	
	private void returnBookCommandLine(Borrower borrow) throws SQLException, IOException {
		while (true) {
			System.out.println("Select a book for return");
			List<BookLoans> loans = this.getAllBookLoansByBorrowerWithNotReturn(borrow);
			if (loans.size() == 0) {
				System.out.println("You have no book for return");
			}
			else {
				int bookCount = 1;
				List<Book> allBook = new ArrayList<Book>();
				for (BookLoans loan : loans) {
					Book bk = this.getBook(loan.getBook().getBookId());
					allBook.add(bk);
					System.out.println(bookCount+") "+bk.getTitle());
					bookCount++;
				}
				System.out.println(bookCount+") Quit to cancel operation");
				
				String keyboard = in.readLine();
				int input2 = Integer.parseInt(keyboard);
				
				if (input2 == bookCount) {
					break;
				}
				else {
					BookLoans selectedBookLoan = loans.get(input2-1);
					this.returnBook(selectedBookLoan);
					break;
				}
			}
		}
	}
}

