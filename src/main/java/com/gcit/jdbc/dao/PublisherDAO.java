package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.Publisher;

public class PublisherDAO extends BaseDAO implements ResultSetExtractor<List<Publisher>> {

	public PublisherDAO(JdbcTemplate conn) {
		super(conn);
	}

	public void insert(Publisher pub) throws SQLException {
		template.update("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?,?,?)",
				new Object[] { pub.getPublisherName(), pub.getAddress(), pub.getPhone() });
	}

	public void update(Publisher pub) throws SQLException {
		template.update("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[] { pub.getPublisherName(), pub.getAddress(), pub.getPhone(), pub.getPublisherId() });
	}

	public void delete(Publisher pub) throws SQLException {
		template.update("delete from tbl_publisher where publisherId = ?",
				new Object[] { pub.getPublisherId() });
	}
	
	public int readPublisherCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_publisher", Integer.class);
	}

	public Publisher readOne(int publisherId) throws SQLException {
		List<Publisher> publishers = template.query(
				"select * from tbl_publisher where publisherId = ?",
				new Object[] { publisherId }, this);
		if (publishers != null && publishers.size() > 0) {
			return publishers.get(0);
		} else {
			return null;
		}
	}

	public Publisher readOneByBook(Book bk) throws SQLException {
		List<Publisher> publishers = template.query(
				"select * from tbl_publisher where publisherId = (select pubId from tbl_book where bookId = ?)",
				new Object[] { bk.getBookId() }, this);
		if (publishers != null && publishers.size() > 0) {
			return publishers.get(0);
		} else {
			return null;
		}
	}
	
	public List<Publisher> readAllNoLimit() throws SQLException {
		return template.query("select * from tbl_publisher", this);
	}

	public List<Publisher> readAll() throws SQLException {
		return template.query(setPageLimits("select * from tbl_publisher"), this);
	}
	
	public List<Publisher> readAllByName(String publisherName) throws SQLException {
		String searchText = '%'+publisherName+'%';
		return template.query("select * from tbl_publisher where publisherName like ?", new Object[] { searchText }, this);
	}
	
	public List<Publisher> readAllByNameWithPage(String pubName, int pageNo) throws SQLException {
		String searchText = '%'+pubName+'%';
		this.setPageNo(pageNo);
		String query = setPageLimits("select * from tbl_publisher");
		query = "select * from ("+query+") as t1 where publisherName like ?";
		return template.query(query, new Object[] { searchText }, this);
	}
	

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Publisher> publishers = new ArrayList<Publisher>();
		while (rs.next()) {
			Publisher pb = new Publisher();
			pb.setPublisherId(rs.getInt("publisherId"));
			pb.setPublisherName(rs.getString("publisherName"));
			pb.setAddress(rs.getString("publisherAddress"));
			pb.setPhone(rs.getString("publisherPhone"));
			publishers.add(pb);
		}
		
		return publishers;
	}

}