package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import fr.d2factory.libraryapp.member.Member;

public class Borrow {
	
	private Book book;
	private  LocalDate borrowAt;
	private Member member;
	
	public Borrow(Book book, LocalDate borrowAt, Member member) {
		this.book = book;
		this.borrowAt = borrowAt;
		this.member = member;
	}

	public Book getBook() {
		return book;
	}

	public LocalDate getBorrowAt() {
		return borrowAt;
	}

	public Member getMember() {
		return member;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setBorrowAt(LocalDate borrowAt) {
		this.borrowAt = borrowAt;
	}

	public void setMember(Member member) {
		this.member = member;
	}   
}