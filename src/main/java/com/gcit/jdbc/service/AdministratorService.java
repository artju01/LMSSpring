package com.gcit.jdbc.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.jdbc.dao.AuthorDAO;
import com.gcit.jdbc.dao.BookDAO;
import com.gcit.jdbc.dao.Book_CopiesDAO;
import com.gcit.jdbc.dao.Book_LoansDAO;
import com.gcit.jdbc.dao.BorrowerDAO;
import com.gcit.jdbc.dao.BranchDAO;
import com.gcit.jdbc.dao.GenreDAO;
import com.gcit.jdbc.dao.PublisherDAO;
import com.gcit.jdbc.entity.Author;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;
import com.gcit.jdbc.entity.Branch;
import com.gcit.jdbc.entity.Genre;
import com.gcit.jdbc.entity.Publisher;


@Service
public class AdministratorService {
	@Autowired
	BookDAO bookDAO;
	@Autowired
	AuthorDAO authorDAO;
	@Autowired
	GenreDAO genreDAO;
	@Autowired
	PublisherDAO pubDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	BorrowerDAO borrowDAO;
	@Autowired
	Book_CopiesDAO bookCopiesDAO;
	@Autowired
	Book_LoansDAO bookLoansDAO;
	
	public List<Book> getAllBooks(int pageNo) throws SQLException {
		if(pageNo <= 0)
			pageNo = 1;
		
		bookDAO.setPageNo(pageNo);
		List<Book> books = bookDAO.readAll();
	
		for(Book b : books) {
			assignBookData(b);
		}
		
		return books;
	}
 	
	public Book getBook (int bookId) throws SQLException {
		Book bk = bookDAO.readOne(bookId);
		assignBookData(bk);
		return bk;
	}
	
	private void assignBookData (Book bk) throws SQLException {
		bk.setAuthors(authorDAO.readAllByBook(bk));
		bk.setGenres(genreDAO.readGenreByBook(bk));
		bk.setPublisher(pubDAO.readOneByBook(bk));
		bk.setCopies(bookCopiesDAO.readAllByBook(bk));
		bk.setLoans(bookLoansDAO.readAllByBook(bk));
	}
	
	@Transactional
	public void addBook(Book bk) throws SQLException {
		/*List<Book>books = bookDAO.readAll();
		for (Book book : books) {
			if (bk.getTitle() == book.getTitle()) {
				List<Author> authors = authorDAO.readAllByBook(book);
				HashSet<String> authorName = new HashSet<String>();
				for (Author author : authors) {
					authorName.add(author.getAuthorName());
				}
				
				List<Author> addedAuthor = bk.getAuthors();
				HashSet<String> authorName2 = new HashSet<String>();
				for (Author author : addedAuthor) {
					authorName2.add(author.getAuthorName());
				}
				
				if (authorName.containsAll(authorName2)) {
					//duplicate
					System.out.println("This book is already in the record");
					return;
				}
			}
		}*/
		
		//Add
		try {
			bookDAO.insert(bk);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Transactional
	public void deleteBook(Book bk) throws SQLException {
		try {
			bookDAO.delete(bk);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
	@Transactional
	public void updateBook(Book bk) throws SQLException {
		try {
			bookDAO.update(bk);
		}
		catch (SQLException e) {
			System.out.println(e);
		}	
	}

	public int getBookCount() throws SQLException {
		return bookDAO.readBookCount();
	}
	
	public List<Book> searchBooksWithPage(String searchText, int pageNo) throws SQLException {
		List<Book> books = bookDAO.readAllByNameWithPage(searchText,pageNo);
		for (Book bk:books) {
			assignBookData(bk);
		}
		
		return books;
	}
	
	public List<Author> getAllAuthors() throws SQLException {
		return authorDAO.readAllNoLimit();
	}
	
	public List<Author> getAllAuthors(int pageNo) throws SQLException {
		if(pageNo <= 0)
			pageNo = 1;
		
		authorDAO.setPageNo(pageNo);
		List<Author> authors = authorDAO.readAll();
		
		return authors;
	}
	
	public int getAuthorCount() throws SQLException {
		return authorDAO.readAuthorCount();
	}
	
	@Transactional
	public void addAuthor(Author author) throws SQLException {
		try {
			authorDAO.insert(author);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		
	}

	public List<Author> searchAuthors(String searchText) throws SQLException {
		List<Author> authors = authorDAO.readAllByName(searchText);
		return authors;
	}
	
	public List<Author> searchAuthorsWithPage(String searchText, int pageNo) throws SQLException {
		List<Author> authors = authorDAO.readAllByNameWithPage(searchText,pageNo);
		return authors;
	}
 	
	@Transactional
	public void updateAuthor(Author author) throws SQLException {
		try {
			authorDAO.update(author);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void updateAuthorByName(String exName, String newName) throws SQLException {
		Author author = authorDAO.readOneByName(exName);
		if (author != null) {
			author.setAuthorName(newName);
			authorDAO.update(author);
		}
	}
	
	@Transactional
	public void deleteAuthor(Author author) throws SQLException {
		try {
			authorDAO.delete(author);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void addPublisher(Publisher pub) throws SQLException {
		try {
			pubDAO.insert(pub);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void updatePublisher(Publisher pub) throws SQLException
	{
		try {
			pubDAO.update(pub);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void deletePublisher(Publisher pub) throws SQLException {
		try {
			pubDAO.delete(pub);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public int getPublisherCount() throws SQLException {
		return pubDAO.readPublisherCount();
	}
	
	public List<Publisher> getAllPublisher(int pageNo) throws SQLException {
		if(pageNo <= 0)
			pageNo = 1;
		pubDAO.setPageNo(pageNo);
		List<Publisher> pubs = pubDAO.readAll();
		
		return pubs;
	}
	
	public List<Publisher> getAllPublisher() throws SQLException {
		List<Publisher> pubs = pubDAO.readAllNoLimit();
		for (Publisher pub : pubs) {
			pub.setBooks(bookDAO.readAllByPublisher(pub));
		}
		return pubs;
	}
	
	public List<Publisher> searchPublishers(String searchText) throws SQLException {
		List<Publisher> pubs = pubDAO.readAllByName(searchText);
		return pubs;
	}
	
	public List<Publisher> searchPublishersWithPage(String searchText, int pageNo) throws SQLException {
		List<Publisher> pubs = pubDAO.readAllByNameWithPage(searchText,pageNo);
		return pubs;
	}
	
	public List<Branch> getAllBranches(int pageNo) throws SQLException {
		if(pageNo <= 0)
			pageNo = 1;
		
		branchDAO.setPageNo(pageNo);
		List<Branch> branches = branchDAO.readAll();
		
		return branches;
	}
	
	public List<Branch> getAllBranches() throws SQLException {
		List<Branch> branches = branchDAO.readAllNoLimit();
		return branches;
	}
	
	@Transactional
	public void addBranch(Branch branch) throws SQLException {
		try {
			branchDAO.insert(branch);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void updateBranch(Branch branch) throws SQLException {
		try {
			branchDAO.update(branch);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void deleteBranch(Branch branch) throws SQLException {
		try {
			branchDAO.delete(branch);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public int getBranchCount() throws SQLException {
		return branchDAO.readBranchCount();
	}	
	
	public List<Branch> searchBranchesWithPage(String searchText, int pageNo) throws SQLException {
		List<Branch> branches = branchDAO.readAllByNameWithPage(searchText,pageNo);
		return branches;
	}
	
	public List<Borrower> getAllBorrower(int pageNo) throws SQLException {
		if(pageNo <= 0)
			pageNo = 1;
		
		borrowDAO.setPageNo(pageNo);
		List<Borrower> borrowers = borrowDAO.readAll();
		
		return borrowers;
	}
	
	public List<Borrower> getAllBorrower() throws SQLException {
		List<Borrower> borrowers = borrowDAO.readAllNoLimit();
		return borrowers;
	}
	
	@Transactional
	public void addBorrower(Borrower borrow) throws SQLException {
		try {
			borrowDAO.insert(borrow);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void updateBorrower(Borrower borrow) throws SQLException {
		try {
			borrowDAO.update(borrow);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	@Transactional
	public void deleteBorrower(Borrower borrow) throws SQLException {
		try {
			borrowDAO.delete(borrow);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public int getBorrowerCount() throws SQLException {
		return borrowDAO.readBorrowerCount();
	}

	
	public List<Borrower> searchBorrowersWithPage(String searchText, int pageNo) throws SQLException {
		List<Borrower> borrowers = borrowDAO.readAllByNameWithPage(searchText,pageNo);
		return borrowers;
	}
	
	@Transactional
	public void updateBookLoans(BookLoans loan) throws SQLException {
		try {
			bookLoansDAO.update(loan);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public int getUnreturnBookLoansCount() throws SQLException {
		return bookLoansDAO.readUnreturnBookLoansCount();
	}
	
	public List<BookLoans> getAllBookLoans(int pageNo) throws SQLException {
		if(pageNo <= 0)
			pageNo = 1;
		bookLoansDAO.setPageNo(pageNo);
		List<BookLoans> bookLoans = bookLoansDAO.readAll();
		return bookLoans;
	}
	
	public List<BookLoans> getAllUnreturnBookLoans(int pageNo) throws SQLException {
		if(pageNo <= 0)
			pageNo = 1;
		bookLoansDAO.setPageNo(pageNo);
		List<BookLoans> bookLoans = bookLoansDAO.readAllUnreturn();
		return bookLoans;
	}
	
	@Transactional
	public void updateBookLoansDueDate(BookLoans bookloan) throws SQLException {
		bookLoansDAO.updateDueDate(bookloan);
	}
	
	public List<Genre> getAllGenres() throws SQLException {
		return genreDAO.readAll();
	}

	public int getGenreCount() throws SQLException {
		return genreDAO.readGenreCount();
	}
	
	public List<Genre> searchGenres(String searchText) throws SQLException {
		List<Genre> gens = genreDAO.readAllByName(searchText);
		return gens;
	}
}
