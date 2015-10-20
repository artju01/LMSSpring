package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.Genre;


public class GenreDAO extends BaseDAO implements ResultSetExtractor<List<Genre>>{

	public GenreDAO(JdbcTemplate conn) {
		super(conn);
	}

	public void insert(Genre gen) throws SQLException {
		template.update("insert into tbl_genre (genre_name) values (?)",
				new Object[] { gen.getGenreName() });
	}
	
	public void update(Genre gen) throws SQLException {
		template.update("update tbl_genre set genre_name = ? where genre_id = ?",
				new Object[] { gen.getGenreName(), gen.getGenreId() });
	}
	
	public void delete(Genre gen) throws SQLException {
		template.update("delete from tbl_book_genre where genre_id = ?",
				new Object[] { gen.getGenreId() });
		
		template.update("delete from tbl_genre where genre_id = ?",
				new Object[] { gen.getGenreId() });
	}

	public int readGenreCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_genre", Integer.class);
	}
	
	public Genre readOne(int genreId) throws SQLException {
		List<Genre> gens = template.query(
				"select * from tbl_genre where genre_id = ?",
				new Object[] { genreId }, this);
		if (gens != null && gens.size() > 0) {
			return gens.get(0);
		} else {
			return null;
		}
	}

	public List<Genre> readAll() throws SQLException {
		return template.query("select * from tbl_genre", this);
	}
	
	public List<Genre> readGenreByBook(Book book) throws SQLException {
		List<Genre> gens = template.query ("select * from tbl_genre where genre_id in (select genre_id from tbl_book_genres where bookId = ?)", 
				new Object[] {book.getBookId()}, this);
		return gens;
	}
	
	public List<Genre> readAllByName(String genreName) throws SQLException {
		String searchText = '%'+genreName+'%';
		return template.query("select * from tbl_genre where genre_name like ?", new Object[] { searchText }, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Genre> gens = new ArrayList<Genre>();
		while (rs.next()) {
			Genre gen = new Genre();
			gen.setGenreId(rs.getInt("genre_Id"));
			gen.setGenreName(rs.getString("genre_name"));
			gens.add(gen);
		}

		return gens;
	}

}
