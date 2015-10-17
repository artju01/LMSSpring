package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookCopies;
import com.gcit.jdbc.entity.Branch;



public class Book_CopiesDAO extends BaseDAO{
	
	public void insert(BookCopies copy) throws SQLException {
		if (copy.getBook() != null && copy.getBranch() != null) {
			save("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?,?,?)",
					new Object[] { copy.getBook().getBookId(), copy.getBranch().getBranchId(), copy.getNoOfCopies() });
		}
	}
	
	public void update(BookCopies copy) throws SQLException {
		if (copy.getBook() != null && copy.getBranch() != null) {
			save("update tbl_book_copies set bookId = ?, branchId = ?, noOfCopies = ? where bookId = ? and branchId = ?",
					new Object[] { copy.getBook().getBookId(), copy.getBranch().getBranchId(), copy.getNoOfCopies(),
					copy.getBook().getBookId(), copy.getBranch().getBranchId()});
		}
	}

	public void delete(BookCopies copy) throws SQLException {
		if (copy.getBook() != null && copy.getBranch() != null) {
			save("delete from tbl_book_copies where bookId = ? and branchId = ?",
					new Object[] { copy.getBook().getBookId(), copy.getBranch().getBranchId() });
		}
		
	}

	@SuppressWarnings("unchecked")
	public BookCopies readOne(int bookId, int branchId) throws SQLException {
		List<BookCopies> bookcopies = (List<BookCopies>) read(
				"select * from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[] { bookId, branchId });
		if (bookcopies != null && bookcopies.size() > 0) {
			return bookcopies.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BookCopies> readAll() throws SQLException {
		List<BookCopies> read = (List<BookCopies>) read("select * from tbl_book_copies", null);
		return read;
	}

	@SuppressWarnings("unchecked")
	public List<BookCopies> readAllByBook(Book bk) throws SQLException {
		return (List<BookCopies>) read(
				"select * from tbl_book_copies where bookId = ?",
				new Object[] { bk.getBookId() });
	}
	
	@SuppressWarnings("unchecked")
	public List<BookCopies> readAllByBranch(Branch branch) throws SQLException {
		return (List<BookCopies>) read(
				"select * from tbl_book_copies where branchId = ?",
				new Object[] { branch.getBranchId() });
	}
	
	@SuppressWarnings("unchecked")
	public List<BookCopies> readAllByBranchWithAtLeastOneCopy(Branch branch) throws SQLException {
		return (List<BookCopies>) read(
				"select * from tbl_book_copies where branchId = ? and noOfCopies > 0",
				new Object[] { branch.getBranchId() });
	}
	
	public int readBookCopiesCount() throws SQLException {
		return readCount("select count(*) from tbl_book_copies");
	}
	
	@Override
	protected List<BookCopies> convertResult(ResultSet rs) throws SQLException {
		List<BookCopies> copies = new ArrayList<BookCopies>();
		BookDAO bookDAO = new BookDAO();
		BranchDAO branchDAO = new BranchDAO();
		while (rs.next()) {
			BookCopies copy = new BookCopies();
			copy.setBook(bookDAO.readOne(rs.getInt("bookId")));
			copy.setBranch(branchDAO.readOne(rs.getInt("branchId")));
			copy.setNoOfCopies(rs.getInt("noOfCopies"));
			copies.add(copy);
		}
		
		conn.close();
		
		return copies;
	}

}
