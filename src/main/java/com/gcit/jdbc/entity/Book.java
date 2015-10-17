package com.gcit.jdbc.entity;

import java.util.List;

public class Book {
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public List<Genre> getGenres() {
		return genres;
	}
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public List<BookCopies> getCopies() {
		return copies;
	}
	public void setCopies(List<BookCopies> copies) {
		this.copies = copies;
	}
	public List<BookLoans> getLoans() {
		return loans;
	}
	public void setLoans(List<BookLoans> loans) {
		this.loans = loans;
	}
	private int bookId;
	private String title;
	private List<Author> authors;
	private List<Genre> genres;
	private Publisher publisher;
	private List<BookCopies> copies;
	private List<BookLoans> loans;

}
