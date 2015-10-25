package com.gcit.lms.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gcit.jdbc.dao.AuthorDAO;
import com.gcit.jdbc.dao.BookDAO;
import com.gcit.jdbc.dao.Book_CopiesDAO;
import com.gcit.jdbc.dao.Book_LoansDAO;
import com.gcit.jdbc.dao.BorrowerDAO;
import com.gcit.jdbc.dao.BranchDAO;
import com.gcit.jdbc.dao.GenreDAO;
import com.gcit.jdbc.dao.PublisherDAO;
import com.gcit.jdbc.service.AdministratorService;
import com.gcit.jdbc.service.BorrowerService;
import com.gcit.jdbc.service.LibrarianService;

@Configuration
@EnableTransactionManagement
public class LMSConfiguration {
	
	@Bean
	public AdministratorService admin() {
		return new AdministratorService();
	}
	
	@Bean
	public LibrarianService librarian() {
		return new LibrarianService();
	}
	
	@Bean
	public BorrowerService borrower() {
		return new BorrowerService();
	}

	@Bean
	public BookDAO bookDAO() {
		return new BookDAO(template());
	}
	
	@Bean
	public BranchDAO branchDAO() {
		return new BranchDAO(template());
	}
	
	@Bean
	public AuthorDAO authorDAO() {
		return new AuthorDAO(template());
	}
	
	@Bean
	public PublisherDAO pubDAO() {
		return new PublisherDAO(template());
	}
	
	@Bean
	public BorrowerDAO borrowDAO() {
		return new BorrowerDAO(template());
	}
	
	@Bean
	public GenreDAO genreDAO() {
		return new GenreDAO(template());
	}
	
	@Bean
	public Book_CopiesDAO bookCopiesDAO() {
		return new Book_CopiesDAO(template());
	}
	
	@Bean Book_LoansDAO bookLoansDAO() {
		return new Book_LoansDAO(template());
	}
	
	@Bean
	public PlatformTransactionManager txManager() {
		PlatformTransactionManager tx = new DataSourceTransactionManager(datasource());
		return tx;
	}
	
	@Bean
	public JdbcTemplate template() {
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(datasource());
		return template;
	}
	
	@Bean
	public BasicDataSource datasource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/library");
		ds.setUsername("root");
		ds.setPassword("");
		
		return ds;
	}
}
