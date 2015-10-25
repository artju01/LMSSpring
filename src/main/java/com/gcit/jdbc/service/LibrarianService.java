package com.gcit.jdbc.service;

//TODO  handle more than 45 char  try +catch


import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.jdbc.dao.BookDAO;
import com.gcit.jdbc.dao.BranchDAO;
import com.gcit.jdbc.dao.Book_CopiesDAO;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.Branch;

@Service
public class LibrarianService {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	BookDAO bookDAO;
	@Autowired
	Book_CopiesDAO bookCopiesDAO;
	
	
	public LibrarianService () {
		
	}
	
	@Transactional
	public void updateLibrary(Branch branch) throws SQLException {
		try {
			branchDAO.update(branch);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void updateBookCopies(BookCopies copy) {
		try {
			bookCopiesDAO.update(copy);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void addBookCopies(BookCopies copy) {
		try {
			bookCopiesDAO.insert(copy);
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
	
}
