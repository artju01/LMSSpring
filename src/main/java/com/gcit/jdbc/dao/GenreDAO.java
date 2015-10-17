package com.gcit.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.jdbc.entity.Book;
import com.gcit.jdbc.entity.Genre;


public class GenreDAO extends BaseDAO{

	public void insert(Genre gen) throws SQLException {
		save("insert into tbl_genre (genre_name) values (?)",
				new Object[] { gen.getGenreName() });
	}
	
	public void update(Genre gen) throws SQLException {
		save("update tbl_genre set genre_name = ? where genre_id = ?",
				new Object[] { gen.getGenreName(), gen.getGenreId() });
	}
	
	public void delete(Genre gen) throws SQLException {
		save("delete from tbl_book_genre where genre_id = ?",
				new Object[] { gen.getGenreId() });
		
		save("delete from tbl_genre where genre_id = ?",
				new Object[] { gen.getGenreId() });
	}

	public int readGenreCount() throws SQLException {
		return readCount("select count(*) from tbl_genre");
	}
	
	@SuppressWarnings("unchecked")
	public Genre readOne(int genreId) throws SQLException {
		List<Genre> gens = (List<Genre>) read(
				"select * from tbl_genre where genre_id = ?",
				new Object[] { genreId });
		if (gens != null && gens.size() > 0) {
			return gens.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Genre> readAll() throws SQLException {
		return (List<Genre>) read("select * from tbl_genre", null);
	}
	

	@SuppressWarnings("unchecked")
	public List<Genre> readGenreByBook(Book book) throws SQLException {
		List<Genre> gens = (List<Genre>) read ("select * from tbl_genre where genre_id in (select genre_id from tbl_book_genres where bookId = ?)", 
				new Object[] {book.getBookId()});
		return gens;
	}
	
	@SuppressWarnings("unchecked")
	public List<Genre> readAllByName(String genreName) throws SQLException {
		String searchText = '%'+genreName+'%';
		return (List<Genre>) read("select * from tbl_genre where genre_name like ?", new Object[] { searchText });
	}

	@Override
	protected List<Genre> convertResult(ResultSet rs) throws SQLException {
		List<Genre> gens = new ArrayList<Genre>();
		while (rs.next()) {
			Genre gen = new Genre();
			gen.setGenreId(rs.getInt("genre_Id"));
			gen.setGenreName(rs.getString("genre_name"));
			gens.add(gen);
		}
		
		conn.close();
		
		return gens;
	}

}
