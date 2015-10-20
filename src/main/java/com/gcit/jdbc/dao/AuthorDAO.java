package com.gcit.jdbc.dao;

import java.util.ArrayList;
import java.util.List;

import com.gcit.jdbc.entity.Author;
import com.gcit.jdbc.entity.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AuthorDAO extends BaseDAO implements ResultSetExtractor<List<Author>> {
	
	public AuthorDAO(JdbcTemplate conn) {
		super(conn);
	}
	
	public void insert(Author auth) throws SQLException {
		template.update("insert into tbl_author (authorName) values (?)",
				new Object[] { auth.getAuthorName() });
	}
	
	public void update(Author auth) throws SQLException {
		template.update("update tbl_author set authorName = ? where authorId = ?",
				new Object[] { auth.getAuthorName(), auth.getAuthorId() });
	}

	public void delete(Author auth) throws SQLException {
		template.update("delete from tbl_book_authors where authorId = ?",
				new Object[] { auth.getAuthorId() });
		
		template.update("delete from tbl_author where authorId = ?",
				new Object[] { auth.getAuthorId() });
		
	}
	
	public int readAuthorCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_author", Integer.class);
	}
	
	public Author readOne(int authorId) throws SQLException {
		List<Author> authors = template.query(
				"select * from tbl_author where authorId = ?",
				new Object[] { authorId }, this);
		if (authors != null && authors.size() > 0) {
			return authors.get(0);
		} else {
			return null;
		}
	}
	
	public Author readOneByName(String name) throws SQLException {
		List<Author> authors = template.query(
				"select * from tbl_author where authorName = ?",
				new Object[] { name }, this);
		if (authors != null && authors.size() > 0) {
			return authors.get(0);
		} else {
			return null;
		}
	}

	public List<Author> readAll() throws SQLException {
		return template.query(setPageLimits("select * from tbl_author"), this);
	}

	public List<Author> readAllByBook(Book bk) throws SQLException {
		return template.query(
				"select * from tbl_author where authorId in (select authorId from tbl_book_authors where bookId = ?)",
				new Object[] { bk.getBookId() }, this);
	}
	
	public List<Author> readAllByName(String authorName) throws SQLException {
		String searchText = '%'+authorName+'%';
		return template.query("select * from tbl_author where authorName like ?", new Object[] { searchText }, this);
	}
	
	public List<Author> readAllByNameWithPage(String authorName, int pageNo) throws SQLException {
		String searchText = '%'+authorName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_author");
		query = "select * from ("+query+") as t1 where t1.authorName like ?";
		return template.query(query, new Object[] { searchText }, this);
	}
	
	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		List<Author> authors = new ArrayList<Author>();
		while (rs.next()) {
			Author auth = new Author();
			auth.setAuthorId(rs.getInt("authorId"));
			auth.setAuthorName(rs.getString("authorName"));
			authors.add(auth);
		}

		return authors;
	}

}
