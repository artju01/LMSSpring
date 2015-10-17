package com.gcit.jdbc.entity;

import java.util.List;

public class Branch {

	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	private int branchId;
	private String name;
	private String address;
	
	public List<BookLoans> getLoans() {
		return loans;
	}
	public void setLoans(List<BookLoans> loans) {
		this.loans = loans;
	}
	public List<BookCopies> getCopies() {
		return copies;
	}
	public void setCopies(List<BookCopies> copies) {
		this.copies = copies;
	}
	private List<BookLoans> loans;
	private List<BookCopies> copies;
}
