package com.gcit.jdbc.service;

//TODO  handle more than 45 char  try +catch

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.gcit.jdbc.dao.BookDAO;
import com.gcit.jdbc.dao.BranchDAO;
import com.gcit.jdbc.dao.Book_CopiesDAO;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.Branch;

public class LibrarianService {
	private BranchDAO branchDAO;
	private BookDAO bookDAO;
	private Book_CopiesDAO bookCopiesDAO;
	
	private BufferedReader in;
	
	public LibrarianService () {
		in = new BufferedReader(new InputStreamReader(System.in));
		branchDAO = new BranchDAO();
		bookDAO = new BookDAO();
		bookCopiesDAO = new Book_CopiesDAO();
	}
	public void updateLibrary(Branch branch) throws SQLException {
		try {
			branchDAO.update(branch);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void updateBookCopies(BookCopies copy) {
		try {
			bookCopiesDAO.update(copy);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public List<BookCopies> getBookCopiesByBranch (int branchId) throws SQLException {
		Branch br = new Branch();
		br.setBranchId(branchId);
		List<BookCopies> copies = bookCopiesDAO.readAllByBranch(br);
		return copies;
	}
	
	public List<Branch> getAllBranch() throws SQLException {
		List<Branch> branches = branchDAO.readAll();
		return branches;
	}
	
	public List<Book> getAllBooks() throws SQLException {
		List<Book> books = bookDAO.readAll();
	
		/*for(Book b : books) {
			assignBookData(b);
		}*/
		
		return books;
	}
	
	public Book getBook (int bookId) throws SQLException {
		Book bk = bookDAO.readOne(bookId);
		//assignBookData(bk);
		return bk;
	}
	/*
	private void assignBookData (Book bk) throws SQLException {
		bk.setCopies(bookCopiesDAO.readAllByBook(bk));
	}*/

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* 
	 * 							 USER INTERFACE
	 */
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void librarianCommandInput() throws SQLException, IOException {
		librarianStep1();
	}
 	
	
	private void librarianStep1() throws SQLException, IOException {
		while (true) {
			System.out.println("1) Enter Branch you manage \n2) Quite to previous ");
			String keyboard = in.readLine();
			int input = Integer.parseInt(keyboard);
			if (input == 1) {
				librarianStep2();
			}
			else {
				break;
			}
		}
		
	}
	
	private void librarianStep2() throws SQLException, IOException {
		//get all branch.
		while (true) {
			List<Branch> branches =  this.getAllBranch();
			int count = 1;
			for (Branch br : branches) {
				System.out.println(count+") "+br.getName());
				count++;
			}
			System.out.println(count+") Quit to previous\n");
			
			//get only one branch from a list of branch.
			System.out.println("Enter Branch you manage :");
			String keyboard = in.readLine();
			int branchIndex = Integer.parseInt(keyboard);
			
			if (branchIndex == count) {
				break;
			}
			
			Branch currBranch = branches.get(branchIndex-1);
			librarianStep3(currBranch);
		}
	}
	
	private void librarianStep3(Branch branch) throws IOException, SQLException {
		while(true) {
			System.out.println("1)Update the details of the Library\n"
					+ "2)Add copies of Book to the Branch\n"
					+ "3)Quit to previous");
			
			String keyboard = in.readLine();
			int operation = Integer.parseInt(keyboard);
			
			if (operation == 1) {
				System.out.print("Please enter new branch name or enter N/A for no change:");
				String newName = in.readLine();
				System.out.print("Please enter new branch address or enter N/A for no change:");
				String newAddress = in.readLine();
				
				if (!newName.equals("N/A")) {
					branch.setName(newName);
				}
				
				if (!newAddress.equals("N/A")) {
					branch.setAddress(newAddress);
				}
				this.updateLibrary(branch);
			}
			else if (operation == 2) {
				option2(branch);
			}
			else {
				break;
			}
		}
	}
	
	private void option2(Branch branch) throws SQLException, IOException {
		while(true) {
			System.out.println("Pick the Book you want to add copies of, to your branch: ");
			
			List<Book> books = this.getAllBooks();
			int count = 1;
			for (Book bk : books) {
				System.out.println(count+") "+bk.getTitle());
				count++;
			}
			System.out.println(count+") Quit to cancel operation ");
			
			String keyboard = in.readLine();
			int operation = Integer.parseInt(keyboard);
			
			if (operation == count) {
				break;
			}
			else {
				Book book = books.get(operation-1);
				System.out.println("Existing number of copies:");
				keyboard = in.readLine();
				int numOfCopies = Integer.parseInt(keyboard);
				
				BookCopies copy = new BookCopies();
				copy.setBook(book);
				copy.setBranch(branch);
				copy.setNoOfCopies(numOfCopies);
				
				this.updateBookCopies(copy);
				//bookCopiesDAO.insert(copy);;
				break;
			}
		}
	}
}
