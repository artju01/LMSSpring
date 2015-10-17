package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.jdbc.entity.Author;
import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Genre;
import com.gcit.jdbc.entity.Publisher;

public class BookDAO extends BaseDAO {

	public void insert(Book book) throws SQLException {
		if (book.getPublisher() != null) {
			int bookId = saveWithId(
					"insert into tbl_book (title, pubId) values (?,?)",
					new Object[] { book.getTitle(),
							book.getPublisher().getPublisherId() });
			
			if (book.getAuthors() != null) {
				for (Author auth : book.getAuthors()) {
					save("insert into tbl_book_authors (bookId, authorId) values (?,?)",
							new Object[] { bookId, auth.getAuthorId() });
				}
			}
			
			if (book.getGenres() != null) {
				for (Genre genre : book.getGenres()) {
					save("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
							new Object[] { bookId, genre.getGenreId() });
				}
			}
			
			if (book.getCopies() != null) {
				for (BookCopies copy : book.getCopies()) {
					if (copy.getBranch() != null) {
						save ("insert into tbl_book_copies (bookId, branchId, noOfCopies)",
								new Object[] {book.getBookId(), copy.getBranch().getBranchId(), copy.getNoOfCopies()});
					}
				}
			}
			
		}
	}

	public void update(Book book) throws SQLException {
		
		if (book.getPublisher() != null) {
			save("update tbl_book  set title = ?, pubId = ? where bookId = ?",
					new Object[] { book.getTitle(), book.getPublisher().getPublisherId(), book.getBookId() });
		}
		
		save("delete from tbl_book_authors where bookId = ?",
					new Object[] { book.getBookId() });
		if (book.getAuthors() != null) {
			for (Author auth : book.getAuthors()) {
				save("insert into tbl_book_authors (bookId, authorId) values (?,?)",
						new Object[] { book.getBookId(), auth.getAuthorId() });
			}
		}

		
		save("delete from tbl_book_genres where bookId = ?",
				new Object[] { book.getBookId() });
		if (book.getGenres() != null) {
			for (Genre genre : book.getGenres()) {
				save("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
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
		save("delete from tbl_book_authors where bookId = ?",
				new Object[] { book.getBookId()});
		
		save("delete from tbl_book_genres where bookId = ?",
				new Object[] { book.getBookId()});
		
		save("delete from tbl_book_copies where bookId = ?",
				new Object[] { book.getBookId()});
		
		save("delete from tbl_book_loans where bookId = ?",
				new Object[] { book.getBookId()});
		
		save("delete from tbl_book where bookId = ?",
				new Object[] { book.getBookId()});
	}
	
	public int readBookCount() throws SQLException {
		return readCount("select count(*) from tbl_book");
	}

	@SuppressWarnings("unchecked")
	public Book readOne(int bookId) throws SQLException {
		List<Book> books = (List<Book>) read(
					"select * from tbl_book where bookId = ?",
					new Object[] { bookId });
		if (books != null && books.size() > 0) {
			return books.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Book> readAll() throws SQLException {
			return (List<Book>) read(setPageLimits("select * from tbl_book"), null);
	}
	 
	@SuppressWarnings("unchecked")
	public List<Book> readAllByAuthor(Author auth) throws SQLException {
		return (List<Book>) read(
					"select * from tbl_book where bookId in (select bookId from tbl_book_authors where authorId = ?)",
					new Object[] { auth.getAuthorId() });
	}
	
	@SuppressWarnings("unchecked")
	public List<Book> readAllByPublisher(Publisher pub) throws SQLException {
		return (List<Book>) read( "select * from tbl_book where pubId = ?",
					new Object[] { pub.getPublisherId() });
	}
	
	@SuppressWarnings("unchecked")
	public List<Book> readAllByGenres(Genre gen) throws SQLException {
		return (List<Book>) read("select * from tbl_book where bookId = in (select bookId from tbl_book_genres where genre_id = ?)",
					new Object[] { gen.getGenreId() });
	}
	
	@SuppressWarnings("unchecked")
	public List<Book> readAllByNameWithPage(String branchName, int pageNo) throws SQLException {
		String searchText = '%'+branchName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_book");
		query = "select * from ("+query+") as t1 where title like ?";
		return (List<Book>) read(query, new Object[] { searchText });
	}

	@Override
	protected List<Book> convertResult(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<Book>();
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			books.add(book);
		}
		
		conn.close();
		
		return books;
	}

}
