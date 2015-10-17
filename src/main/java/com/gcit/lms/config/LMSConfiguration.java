package com.gcit.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcit.jdbc.dao.AuthorDAO;
import com.gcit.jdbc.dao.BookDAO;
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
	public AuthorDAO authorDAO() {
		return new AuthorDAO();
	}
	@Bean
	public PublisherDAO pubDAO() {
		return new PublisherDAO();
	}
}
