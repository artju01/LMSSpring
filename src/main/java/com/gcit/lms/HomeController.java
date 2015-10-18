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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcit.jdbc.entity.Author;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;
import com.gcit.jdbc.entity.Branch;
import com.gcit.jdbc.entity.Genre;
import com.gcit.jdbc.entity.Publisher;
import com.gcit.jdbc.service.AdministratorService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	AdministratorService adminService;

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	//////////////////////Author/////////////////////////////////
	
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addAuthor(@RequestBody Author author,
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
	public @ResponseBody String editAuthor(@RequestBody Author author,
			Locale locale, Model model) {
		try {
			adminService.updateAuthor(author);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString("Edit Author successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return "Authors delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.POST , consumes = "application/json")
	public @ResponseBody String deleteAuthor(@RequestBody Author author,
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
	public @ResponseBody String listAuthors(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Author> authors = adminService.getAllAuthors(pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(authors);
		} catch (Exception e) {
			e.printStackTrace();
			return "Authors get failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/countAuthors", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String countAuthors() {
		try {
			int count = adminService.getAuthorCount();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(count);
		} catch (Exception e) {
			e.printStackTrace();
			return "countAuthors get failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/searchAuthorsWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String searchAuthorsWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Author> authors = adminService.searchAuthorsWithPage(searchText, pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(authors);
		} catch (Exception e) {
			e.printStackTrace();
			return "Authors search failed. Reason: " + e.getMessage();
		}
	}
	
	//////////////////////Borrower/////////////////////////////////
	
	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addBorrower(@RequestBody Borrower borrow,
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
	public @ResponseBody String editBorrower(@RequestBody Borrower borrow,
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
	public @ResponseBody String deleteBorrower(@RequestBody Borrower borrow,
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
	public @ResponseBody String listBorrowers(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Borrower> borrow = adminService.getAllBorrower(pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(borrow);
		} catch (Exception e) {
			e.printStackTrace();
			return "Borrowers get failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/countBorrowers", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String countBorrowrs() {
		try {
			int count = adminService.getBorrowerCount();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(count);
		} catch (Exception e) {
			e.printStackTrace();
			return "countBorrowrs get failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/searchBorrowersWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String searchBorrowersWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Borrower> borrows = adminService.searchBorrowersWithPage(searchText, pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(borrows);
		} catch (Exception e) {
			e.printStackTrace();
			return "Authors search failed. Reason: " + e.getMessage();
		}
	}
	
	//////////////////////Branch/////////////////////////////////
	
	@RequestMapping(value = "/addBranch", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addBranch(@RequestBody Branch branch,
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
	public @ResponseBody String editBranch(@RequestBody Branch branch,
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
	public @ResponseBody String deleteBorrower(@RequestBody Branch branch,
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
	public @ResponseBody String listBranches(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Branch> branches = adminService.getAllBranches(pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(branches);
		} catch (Exception e) {
			e.printStackTrace();
			return "Branches get failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/countBranches", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String countBranches() {
		try {
			int count = adminService.getBranchCount();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(count);
		} catch (Exception e) {
			e.printStackTrace();
			return "countBranches get failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/searchBranchesWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String searchBranchesWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Branch> branches = adminService.searchBranchesWithPage(searchText, pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(branches);
		} catch (Exception e) {
			e.printStackTrace();
			return "Branches search failed. Reason: " + e.getMessage();
		}
	}
	
	//////////////////////Book/////////////////////////////////

	@RequestMapping(value = "/addBook", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addBook(@RequestBody Book book,
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
	public @ResponseBody String editBook(@RequestBody Book book,
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
	public @ResponseBody String deleteBook(@RequestBody Book book,
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
	public @ResponseBody String listBooks(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Book> books = adminService.getAllBooks(pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(books);
		} catch (Exception e) {
			e.printStackTrace();
			return "Books get failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/countBooks", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String countBooks() {
		try {
			int count = adminService.getBookCount();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(count);
		} catch (Exception e) {
			e.printStackTrace();
			return "countBooks get failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/searchBooksWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String searchBooksWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Book> books = adminService.searchBooksWithPage(searchText, pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(books);
		} catch (Exception e) {
			e.printStackTrace();
			return "Books search failed. Reason: " + e.getMessage();
		}
	}

	//////////////////////Publisher/////////////////////////////////

	@RequestMapping(value = "/addPublisher", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addPublisher(@RequestBody Publisher pub,
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
	public @ResponseBody String editPublisher(@RequestBody Publisher pub,
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
	public @ResponseBody String deletePublisher(@RequestBody Publisher pub,
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
	public @ResponseBody String listPublishers(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Publisher> pubs = adminService.getAllPublisher(pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(pubs);
		} catch (Exception e) {
			e.printStackTrace();
			return "Publishers get failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/countPublishers", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String countPublishers() {
		try {
			int count = adminService.getPublisherCount();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(count);
		} catch (Exception e) {
			e.printStackTrace();
			return "countPublishers get failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/searchPublishersWithPage/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String searchPublishersWithPage(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<Publisher> pubs = adminService.searchPublishersWithPage(searchText, pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(pubs);
		} catch (Exception e) {
			e.printStackTrace();
			return "Publishers search failed. Reason: " + e.getMessage();
		}
	}
	
	//////////////////////Book_Loans/////////////////////////////////
	@RequestMapping(value = "/editBookLoan", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String editBookLoan(@RequestBody BookLoans bookloan,
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
	public @ResponseBody String listBookLoans(
			@PathVariable(value = "pageNo") Integer pageNo) {
		try {
			if (pageNo == null)
				pageNo = 1;
			List<BookLoans> bookloans = adminService.getAllUnreturnBookLoans(pageNo);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(bookloans);
		} catch (Exception e) {
			e.printStackTrace();
			return "BookLoans get failed. Reason: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/countBookloans", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String countBookloans() {
		try {
			int count = adminService.getUnreturnBookLoansCount();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(count);
		} catch (Exception e) {
			e.printStackTrace();
			return "countBookLoanss get failed. Reason: " + e.getMessage();
		}
	}
	
	//////////////////////Genre/////////////////////////////////
	@RequestMapping(value = "/listGenres", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String listBookLoans( ) {
		try {
			List<Genre> gen = adminService.getAllGenres();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(gen);
		} catch (Exception e) {
			e.printStackTrace();
			return "BookLoans get failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/searchGenres/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String searchGenresWithPage(@PathVariable(value = "searchText") String searchText) {
		try {
			List<Genre> gens = adminService.searchGenres(searchText);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(gens);
		} catch (Exception e) {
			e.printStackTrace();
			return "Publishers search failed. Reason: " + e.getMessage();
		}
	}
}
