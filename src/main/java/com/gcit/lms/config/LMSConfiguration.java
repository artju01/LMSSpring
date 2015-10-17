package com.gcit.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcit.jdbc.dao.AuthorDAO;
import com.gcit.jdbc.dao.BookDAO;
import com.gcit.jdbc.dao.Book_CopiesDAO;
import com.gcit.jdbc.dao.Book_LoansDAO;
import com.gcit.jdbc.dao.BorrowerDAO;
import com.gcit.jdbc.dao.BranchDAO;
import com.gcit.jdbc.dao.GenreDAO;
import com.gcit.jdbc.dao.PublisherDAO;
import com.gcit.jdbc.service.AdministratorService;

@Configuration
public class LMSConfiguration {
	
	@Bean
	public AdministratorService admin() {
		return new AdministratorService();
	}

	@Bean
	public BookDAO bookDAO() {
		return new BookDAO();
	}
	
	@Bean
	public BranchDAO branchDAO() {
		return new BranchDAO();
	}
	
	@Bean
	public AuthorDAO authorDAO() {
		return new AuthorDAO();
	}
	
	@Bean
	public PublisherDAO pubDAO() {
		return new PublisherDAO();
	}
	
	@Bean
	public BorrowerDAO borrowDAO() {
		return new BorrowerDAO();
	}
	
	@Bean
	public GenreDAO genreDAO() {
		return new GenreDAO();
	}
	
	@Bean
	public Book_CopiesDAO bookCopiesDAO() {
		return new Book_CopiesDAO();
	}
	
	@Bean Book_LoansDAO bookLoansDAO() {
		return new Book_LoansDAO();
	}
}
