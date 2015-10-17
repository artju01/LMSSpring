package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.Publisher;

public class PublisherDAO extends BaseDAO {

	public void insert(Publisher pub) throws SQLException {
		save("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?,?,?)",
				new Object[] { pub.getPublisherName(), pub.getAddress(), pub.getPhone() });
	}

	public void update(Publisher pub) throws SQLException {
		save("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[] { pub.getPublisherName(), pub.getAddress(), pub.getPhone(), pub.getPublisherId() });
	}

	public void delete(Publisher pub) throws SQLException {
		save("delete from tbl_publisher where publisherId = ?",
				new Object[] { pub.getPublisherId() });
	}
	
	public int readPublisherCount() throws SQLException {
		return readCount("select count(*) from tbl_publisher");
	}

	@SuppressWarnings("unchecked")
	public Publisher readOne(int publisherId) throws SQLException {
		List<Publisher> publishers = (List<Publisher>) read(
				"select * from tbl_publisher where publisherId = ?",
				new Object[] { publisherId });
		if (publishers != null && publishers.size() > 0) {
			return publishers.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Publisher readOneByBook(Book bk) throws SQLException {
		List<Publisher> publishers = (List<Publisher>) read(
				"select * from tbl_publisher where publisherId = (select pubId from tbl_book where bookId = ?)",
				new Object[] { bk.getBookId() });
		if (publishers != null && publishers.size() > 0) {
			return publishers.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Publisher> readAll() throws SQLException {
		return (List<Publisher>) read(setPageLimits("select * from tbl_publisher"), null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Publisher> readAllByName(String publisherName) throws SQLException {
		String searchText = '%'+publisherName+'%';
		return (List<Publisher>) read("select * from tbl_publisher where publisherName like ?", new Object[] { searchText });
	}
	
	@SuppressWarnings("unchecked")
	public List<Publisher> readAllByNameWithPage(String pubName, int pageNo) throws SQLException {
		String searchText = '%'+pubName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_publisher");
		query = "select * from ("+query+") as t1 where publisherName like ?";
		return (List<Publisher>) read(query, new Object[] { searchText });
	}
	

	@Override
	protected List<Publisher> convertResult(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<Publisher>();
		while (rs.next()) {
			Publisher pb = new Publisher();
			pb.setPublisherId(rs.getInt("publisherId"));
			pb.setPublisherName(rs.getString("publisherName"));
			pb.setAddress(rs.getString("publisherAddress"));
			pb.setPhone(rs.getString("publisherPhone"));
			publishers.add(pb);
		}

		conn.close();
		
		return publishers;
	}

}