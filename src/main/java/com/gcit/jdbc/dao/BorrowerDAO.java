package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.BookLoans;
import com.gcit.jdbc.entity.Borrower;

public class BorrowerDAO extends BaseDAO {

	public void insert(Borrower borrow) throws SQLException {
		save("insert into tbl_borrower (name, address, phone) values (?,?,?)",
				new Object[] { borrow.getName(), borrow.getAddress(), borrow.getPhone() });
	}
	
	public void update(Borrower borrow) throws SQLException {
		save("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[] { borrow.getName(), borrow.getAddress(), borrow.getPhone(), borrow.getCardNo() });
		
		save("delete from tbl_book_loans where cardNo = ?", 
				new Object[] { borrow.getCardNo() });
		
		if (borrow.getLoans() != null) {
			for (BookLoans loan : borrow.getLoans()) {
				if (loan.getBorrower() != null && loan.getBook() != null && loan.getBranch() != null) {
					save("insert into tbl_book_loans (bookId, branchId, cardNo, dteOut, dueDate, dateIn)", 
							new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo(),
							loan.getDateOut(), loan.getDueDate(), loan.getDateIn()});	
				}
			}
		}
	}

	public void delete(Borrower borrow) throws SQLException {
		save("delete from tbl_book_loans where cardNo = ?",
				new Object[] { borrow.getCardNo()});
		
		save("delete from tbl_borrower where cardNo = ?",
				new Object[] { borrow.getCardNo()});
	}

	public int readBorrowerCount() throws SQLException {
		return readCount("select count(*) from tbl_borrower");
	}
	
	@SuppressWarnings("unchecked")
	public Borrower readOne(int cardNo) throws SQLException {
		List<Borrower> borrows = (List<Borrower>) read("select * from tbl_borrower where cardNo = ?"
				, new Object[] {cardNo});
		if (borrows != null && borrows.size() > 0) {
			return borrows.get(0);
		}
		else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Borrower> readAll() throws SQLException {
		return (List<Borrower>) read(setPageLimits("select * from tbl_borrower"), null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Borrower> readByBook(Book bk) throws SQLException {
		List<Borrower> read = (List<Borrower>) read("select * from tbl_borrower where cardNo in (select cardNo from table_book_loans where bookId = ?)"
				, new Object[] {bk.getBookId()} );
		return read;
	}

	@SuppressWarnings("unchecked")
	public List<Borrower> readAllByNameWithPage(String branchName, int pageNo) throws SQLException {
		String searchText = '%'+branchName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_borrower");
		query = "select * from ("+query+") as t1 where name like ?";
		return (List<Borrower>) read(query, new Object[] { searchText });
	}
	
	@Override
	protected List<Borrower> convertResult(ResultSet rs) throws SQLException {
		List<Borrower> borrows = new ArrayList<Borrower>();
		while (rs.next()) {
			Borrower borrow = new Borrower();
			borrow.setCardNo(rs.getInt("cardNo"));
			borrow.setName(rs.getString("name"));
			borrow.setAddress(rs.getString("address"));
			borrow.setPhone(rs.getString("phone"));
			borrows.add(borrow);
		}
		
		conn.close();
		
		return borrows;
	}


}
