package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.Branch;



public class Book_CopiesDAO extends BaseDAO implements ResultSetExtractor<List<BookCopies>>{
	
	public Book_CopiesDAO(JdbcTemplate conn) {
		super(conn);
	}

	@Autowired
	BookDAO bookDAO;
	@Autowired
	BranchDAO branchDAO;
	
	public void insert(BookCopies copy) throws SQLException {
		if (copy.getBook() != null && copy.getBranch() != null) {
			template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?,?,?)",
					new Object[] { copy.getBook().getBookId(), copy.getBranch().getBranchId(), copy.getNoOfCopies() });
		}
	}
	
	public void update(BookCopies copy) throws SQLException {
		if (copy.getBook() != null && copy.getBranch() != null) {
			template.update("update tbl_book_copies set bookId = ?, branchId = ?, noOfCopies = ? where bookId = ? and branchId = ?",
					new Object[] { copy.getBook().getBookId(), copy.getBranch().getBranchId(), copy.getNoOfCopies(),
					copy.getBook().getBookId(), copy.getBranch().getBranchId()});
		}
	}

	public void delete(BookCopies copy) throws SQLException {
		if (copy.getBook() != null && copy.getBranch() != null) {
			template.update("delete from tbl_book_copies where bookId = ? and branchId = ?",
					new Object[] { copy.getBook().getBookId(), copy.getBranch().getBranchId() });
		}
		
	}

	public BookCopies readOne(int bookId, int branchId) throws SQLException {
		List<BookCopies> bookcopies = template.query(
				"select * from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[] { bookId, branchId }, this);
		if (bookcopies != null && bookcopies.size() > 0) {
			return bookcopies.get(0);
		} else {
			return null;
		}
	}

	public List<BookCopies> readAll() throws SQLException {
		List<BookCopies> read = template.query("select * from tbl_book_copies", this);
		return read;
	}
	 
	public List<BookCopies> readAllByBook(Book bk) throws SQLException {
		return template.query(
				"select * from tbl_book_copies where bookId = ?",
				new Object[] { bk.getBookId() }, this);
	}
	
	public List<BookCopies> readAllByBranch(Branch branch) throws SQLException {
		return template.query(
				"select * from tbl_book_copies where branchId = ?",
				new Object[] { branch.getBranchId() }, this);
	}
	
	public List<BookCopies> readAllByBranchWithAtLeastOneCopy(Branch branch) throws SQLException {
		return template.query(
				"select * from tbl_book_copies where branchId = ? and noOfCopies > 0",
				new Object[] { branch.getBranchId() }, this);
	}
	
	public int readBookCopiesCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_book_copies", Integer.class);
	}
	
	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		List<BookCopies> copies = new ArrayList<BookCopies>();	
		while (rs.next()) {
			BookCopies copy = new BookCopies();
			copy.setBook(bookDAO.readOne(rs.getInt("bookId")));
			copy.setBranch(branchDAO.readOne(rs.getInt("branchId")));
			copy.setNoOfCopies(rs.getInt("noOfCopies"));
			copies.add(copy);
		}

		return copies;
	}
}
