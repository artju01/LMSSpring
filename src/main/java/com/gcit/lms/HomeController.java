package com.gcit.lms;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcit.jdbc.entity.Author;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;
import com.gcit.jdbc.entity.Branch;
import com.gcit.jdbc.entity.Genre;
import com.gcit.jdbc.entity.Publisher;
import com.gcit.jdbc.service.AdministratorService;
import com.gcit.jdbc.service.BorrowerService;
import com.gcit.jdbc.service.LibrarianService;

/**
 * Handles requests for the application home page.
 */
@RestController
public class HomeController {
	
	@Autowired
	AdministratorService adminService;
	@Autowired
	LibrarianService libService;
	@Autowired
	BorrowerService borrowService;
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	//////////////////////Author/////////////////////////////////
	
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST, consumes = "application/json")
	public  String addAuthor(@RequestBody Author author,
			Locale locale, Model model) {
		try {
			adminService.addAuthor(author);
			return "Author added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Author add failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/editAuthor", method = RequestMethod.POST , consumes = "application/json")
	public String editAuthor(@RequestBody Author author,
			Locale locale, Model model) {
		try {
			System.out.println(author);
			adminService.updateAuthor(author);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString("Edit Author successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return "Authors delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.POST , consumes = "application/json")
	public  String deleteAuthor(@RequestBody Author author,
			Locale locale, Model model) {
		try {
			adminService.deleteAuthor(author);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString("Delete Author successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return "Authors delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listAuthors/{pageNo}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public  List<Author> listAuthors(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null) {
				pageNo = 1;
				return adminService.getAllAuthors(pageNo);
			}
			else if (pageNo == -1) {
				return adminService.getAllAuthors();
			}
			else {
				return adminService.getAllAuthors(pageNo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/countAuthors", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public  int countAuthors() {
		try {
			return adminService.getAuthorCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@RequestMapping(value = "/searchAuthorsWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Author> searchAuthorsWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null) {
				pageNo = 1;
				return adminService.searchAuthorsWithPage(searchText, pageNo);
			}
			else if (pageNo == -1) {
				return adminService.searchAuthors(searchText);
			}
			else {
				return adminService.searchAuthorsWithPage(searchText, pageNo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//////////////////////Borrower/////////////////////////////////
	
	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST, consumes = "application/json")
	public  String addBorrower(@RequestBody Borrower borrow,
			Locale locale, Model model) {
		try {
			adminService.addBorrower(borrow);
			return "Borrower added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Borrower add failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/editBorrower", method = RequestMethod.POST, consumes = "application/json")
	public String editBorrower(@RequestBody Borrower borrow,
			Locale locale, Model model) {
		try {
			adminService.updateBorrower(borrow);
			return "Borrower edited succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Borrower edit failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deleteBorrower", method = RequestMethod.POST, consumes = "application/json")
	public String deleteBorrower(@RequestBody Borrower borrow,
			Locale locale, Model model) {
		
		try {
			adminService.deleteBorrower(borrow);
			return "Borrower deleted succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Borrower delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listBorrowers/{pageNo}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Borrower> listBorrowers(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null) {
				pageNo = 1;
				return adminService.getAllBorrower(pageNo);
			}
			else if (pageNo == -1) {
				return adminService.getAllBorrower();
			}
			else {
				return adminService.getAllBorrower(pageNo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/countBorrowers", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public int countBorrowrs() {
		try {
			return adminService.getBorrowerCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@RequestMapping(value = "/searchBorrowersWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public  List<Borrower> searchBorrowersWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			return adminService.searchBorrowersWithPage(searchText, pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//////////////////////Branch/////////////////////////////////
	
	@RequestMapping(value = "/addBranch", method = RequestMethod.POST, consumes = "application/json")
	public String addBranch(@RequestBody Branch branch,
			Locale locale, Model model) {
		try {
			adminService.addBranch(branch);
			return "Branch added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Branch add failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/editBranch", method = RequestMethod.POST, consumes = "application/json")
	public String editBranch(@RequestBody Branch branch,
			Locale locale, Model model) {
		try {
			adminService.updateBranch(branch);
			return "Branch edited succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Branch edit failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deleteBranch", method = RequestMethod.POST, consumes = "application/json")
	public String deleteBorrower(@RequestBody Branch branch,
			Locale locale, Model model) {
		
		try {
			adminService.deleteBranch(branch);
			return "Branch deleted succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Branch delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listBranches/{pageNo}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Branch> listBranches(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null) {
				pageNo = 1;
				return adminService.getAllBranches(pageNo);
			}
			else if (pageNo == -1) {
				return adminService.getAllBranches();
			}
			else {
				return adminService.getAllBranches(pageNo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/countBranches", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public int countBranches() {
		try {
			return adminService.getBranchCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@RequestMapping(value = "/searchBranchesWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Branch> searchBranchesWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			return adminService.searchBranchesWithPage(searchText, pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//////////////////////Book/////////////////////////////////

	@RequestMapping(value = "/addBook", method = RequestMethod.POST, consumes = "application/json")
	public String addBook(@RequestBody Book book,
			Locale locale, Model model) {
		try {
			adminService.addBook(book);
			return "Book added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Book add failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/editBook", method = RequestMethod.POST, consumes = "application/json")
	public String editBook(@RequestBody Book book,
			Locale locale, Model model) {
		try {
			adminService.updateBook(book);
			return "Book edited succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Book edit failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/deleteBook", method = RequestMethod.POST, consumes = "application/json")
	public String deleteBook(@RequestBody Book book,
			Locale locale, Model model) {

		try {
			adminService.deleteBook(book);
			return "Book deleted succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Book delete failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/listBooks/{pageNo}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Book> listBooks(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			return adminService.getAllBooks(pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/countBooks", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public int countBooks() {
		try {
			return adminService.getBookCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@RequestMapping(value = "/searchBooksWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Book> searchBooksWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			return adminService.searchBooksWithPage(searchText, pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//////////////////////Publisher/////////////////////////////////

	@RequestMapping(value = "/addPublisher", method = RequestMethod.POST, consumes = "application/json")
	public String addPublisher(@RequestBody Publisher pub,
			Locale locale, Model model) {
		try {
			adminService.addPublisher(pub);
			return "Publisher added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Book add failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/editPublisher", method = RequestMethod.POST, consumes = "application/json")
	public String editPublisher(@RequestBody Publisher pub,
			Locale locale, Model model) {
		try {
			adminService.updatePublisher(pub);
			return "Publisher edited succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Publisher edit failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/deletePublisher", method = RequestMethod.POST, consumes = "application/json")
	public String deletePublisher(@RequestBody Publisher pub,
			Locale locale, Model model) {

		try {
			adminService.deletePublisher(pub);
			return "Publisher deleted succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Publisher delete failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/listPublishers/{pageNo}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Publisher> listPublishers(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null) {
				pageNo = 1;
				return adminService.getAllPublisher(pageNo);
			}
			else if (pageNo == -1) {
				return adminService.getAllPublisher();
			}
			else {
				return adminService.getAllPublisher(pageNo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/countPublishers", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public int countPublishers() {
		try {
			return adminService.getPublisherCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@RequestMapping(value = "/searchPublishersWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Publisher> searchPublishersWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			return adminService.searchPublishersWithPage(searchText, pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//////////////////////Book_Loans/////////////////////////////////
	@RequestMapping(value = "/editBookLoan", method = RequestMethod.POST, consumes = "application/json")
	public String editBookLoan(@RequestBody BookLoans bookloan,
			Locale locale, Model model) {
		try {
			adminService.updateBookLoans(bookloan);
			return "BookLoans edited succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "BookLoans edit failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listBookLoans/{pageNo}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<BookLoans> listBookLoans(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			return adminService.getAllUnreturnBookLoans(pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/countBookloans", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public int countBookloans() {
		try {
			return adminService.getUnreturnBookLoansCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//////////////////////Genre/////////////////////////////////
	@RequestMapping(value = "/listGenres", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Genre> listGenres( ) {
		try {
			return adminService.getAllGenres();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/searchGenres/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<Genre> searchGenresWithPage(@PathVariable(value = "searchText") String searchText) {
		try {
			return adminService.searchGenres(searchText);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//////////////////////BookCopies/////////////////////////////////
	@RequestMapping(value = "/listBookCopies/{branchId}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<BookCopies> searchGenresWithPage(@PathVariable(value = "branchId") int branchId) {
		try {
			return libService.getBookCopiesByBranch(branchId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateBookCopy", method = RequestMethod.POST, consumes = "application/json")
	public String updateBookCopy(@RequestBody BookCopies copy,
			Locale locale, Model model) {
		try {
			libService.updateBookCopies(copy);
			return "BookCopy update succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "BookCopy update failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/addBookCopy", method = RequestMethod.POST, consumes = "application/json")
	public String addBookCopy(@RequestBody BookCopies copy,
			Locale locale, Model model) {
		try {
			libService.addBookCopies(copy);
			return "BookCopy add succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "BookCopy add failed. Reason: " + e.getMessage();
		}
	}
	
	//////////////////////for Borrower/////////////////////////////////
	@RequestMapping(value = "/listBookLoansForBorrower", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public List<BookLoans> listBookLoansForBorrower(@RequestBody Borrower borrower) {
		try {
			return borrowService.getAllBookLoansByBorrower(borrower);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/addBookLoan", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public String addBookLoan(@RequestBody BookLoans bookloan) {
		try {
			borrowService.checkOutBook(bookloan);
			return "CheckOut add succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "CheckOut failed. Reason: " + e.getMessage();
		}
	}
}
