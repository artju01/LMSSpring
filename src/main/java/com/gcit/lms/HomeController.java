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

	@RequestMapping(value = "/listAuthors/{pageNo}/{searchText}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public @ResponseBody String listAuthors(
			@PathVariable(value = "pageNo") Integer pageNo,
			@PathVariable(value = "searchText") String searchText) {
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

	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addAuthor(@RequestBody Author author,
			Locale locale, Model model) {
		System.out.println("here");
		try {
			adminService.addAuthor(author);
			return "Author added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Author add failed. Reason: " + e.getMessage();
		}
	}
}
