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
import com.gcit.jdbc.entity.Borrower;
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
	
	@RequestMapping(value = "/listAuthors/{pageNo}/{searchText}", method = {
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
}
