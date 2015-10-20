package com.gcit.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.jdbc.entity.Author;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.Genre;
import com.gcit.jdbc.entity.Publisher;

public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>>{

	public BookDAO(JdbcTemplate conn) {
		super(conn);
	}

	public void insert(final Book book) throws SQLException {
		if (book.getPublisher() != null) {
			KeyHolder holder = new GeneratedKeyHolder();
			template.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(
							"insert into tbl_book (title, pubId) values (?,?)",
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, book.getTitle());
					ps.setInt(2, book.getPublisher().getPublisherId());
					return ps;
				}
			}, holder);

			int bookId = holder.getKey().intValue();
			
			if (book.getAuthors() != null) {
				for (Author auth : book.getAuthors()) {
					template.update("insert into tbl_book_authors (bookId, authorId) values (?,?)",
							new Object[] { bookId, auth.getAuthorId() });
				}
			}
			
			if (book.getGenres() != null) {
				for (Genre genre : book.getGenres()) {
					template.update("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
							new Object[] { bookId, genre.getGenreId() });
				}
			}
			
			if (book.getCopies() != null) {
				for (BookCopies copy : book.getCopies()) {
					if (copy.getBranch() != null) {
						template.update ("insert into tbl_book_copies (bookId, branchId, noOfCopies)",
								new Object[] {book.getBookId(), copy.getBranch().getBranchId(), copy.getNoOfCopies()});
					}
				}
			}
			
		}
	}

	public void update(Book book) throws SQLException {
		
		if (book.getPublisher() != null) {
			template.update("update tbl_book  set title = ?, pubId = ? where bookId = ?",
					new Object[] { book.getTitle(), book.getPublisher().getPublisherId(), book.getBookId() });
		}	
		
		template.update("delete from tbl_book_authors where bookId = ?",
					new Object[] { book.getBookId() });
		if (book.getAuthors() != null) {
			for (Author auth : book.getAuthors()) {
				template.update("insert into tbl_book_authors (bookId, authorId) values (?,?)",
						new Object[] { book.getBookId(), auth.getAuthorId() });
			}
		}

		
		template.update("delete from tbl_book_genres where bookId = ?",
				new Object[] { book.getBookId() });
		if (book.getGenres() != null) {
			for (Genre genre : book.getGenres()) {
				template.update("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
						new Object[] { book.getBookId(), genre.getGenreId() });
			}
		}
		
		
		/*
		save("delete from tbl_book_copies where bookId = ?",
				new Object[] { book.getBookId() });
		for (BookCopies copy : book.getCopies()) {
			if (copy.getBook() != null && copy.getBranch() != null) {
				save("insert into tbl_book_copies (bookId, branchId) values (?,?)",
						new Object[] { copy.getBook().getBookId(), copy.getBranch().getBranchId() });
			}
		}

		save("delete from tbl_book_loans where bookId = ?",
				new Object[] { book.getBookId() });
		for (BookLoans loan : book.getLoans()) {
			if (loan.getBranch() != null && loan.getBorrower() != null) {
				save("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, "
						+ "dueDate, dateIn) values (?,?,?,?,?,?)",
						new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), 
								loan.getBorrower().getCardNo(), loan.getDateOut(), loan.getDueDate(), 
								loan.getDateIn() });
			}
		}*/
		
		
	}

	public void delete(Book book) throws SQLException {
		template.update("delete from tbl_book_authors where bookId = ?",
				new Object[] { book.getBookId()});
		
		template.update("delete from tbl_book_genres where bookId = ?",
				new Object[] { book.getBookId()});
		
		template.update("delete from tbl_book_copies where bookId = ?",
				new Object[] { book.getBookId()});
		
		template.update("delete from tbl_book_loans where bookId = ?",
				new Object[] { book.getBookId()});
		
		template.update("delete from tbl_book where bookId = ?",
				new Object[] { book.getBookId()});
	}
	
	public int readBookCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_book", Integer.class);
	}

	
	public Book readOne(int bookId) throws SQLException {
		List<Book> books = template.query(
					"select * from tbl_book where bookId = ?",
					new Object[] { bookId }, this);
		if (books != null && books.size() > 0) {
			return books.get(0);
		} else {
			return null;
		}
	}

	
	public List<Book> readAll() throws SQLException {
			return template.query(setPageLimits("select * from tbl_book"), this);
	}
	 
	
	public List<Book> readAllByAuthor(Author auth) throws SQLException {
		return template.query(
					"select * from tbl_book where bookId in (select bookId from tbl_book_authors where authorId = ?)",
					new Object[] { auth.getAuthorId() }, this);
	}
	
	
	public List<Book> readAllByPublisher(Publisher pub) throws SQLException {
		return template.query( "select * from tbl_book where pubId = ?",
					new Object[] { pub.getPublisherId() }, this);
	}
	
	
	public List<Book> readAllByGenres(Genre gen) throws SQLException {
		return template.query("select * from tbl_book where bookId = in (select bookId from tbl_book_genres where genre_id = ?)",
					new Object[] { gen.getGenreId() }, this);
	}
	
	public List<Book> readAllByNameWithPage(String branchName, int pageNo) throws SQLException {
		String searchText = '%'+branchName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_book");
		query = "select * from ("+query+") as t1 where title like ?";
		return template.query(query, new Object[] { searchText }, this);
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Book> books = new ArrayList<Book>();
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			books.add(book);
		}
	
		return books;
	}

}
