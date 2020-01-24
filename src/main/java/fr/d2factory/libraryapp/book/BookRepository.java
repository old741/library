package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.List;

import fr.d2factory.libraryapp.member.Member;
/**
 * The book repository emulates a database
 */
public interface BookRepository {
	/**
	 * Add a list of books to the library
	 * @param books
	 */
	void addBooks(List<Book> books);
	/**
	 * Retrieve book by isbnCode
	 * @param isbnCode
	 * @return
	 */
	Book findBook(long isbnCode);
	/**
	 * Register a book for a member
	 * @param book
	 * @param member
	 * @param borrowedAt
	 */
	void saveBookBorrow(Book book, Member member, LocalDate borrowedAt);
	/**
	 * Remove a book borrow for a member
	 * @param book
	 * @param member
	 */
	void removeBookBorrow(Book book, Member member);
	/**
	 * Return a loan list by member
	 * @param member
	 * @return
	 */
	List<Borrow> getBooksBorrowedByMember(Member member);
	/**
	 * Return the borrow date of book
	 * @param book
	 * @return
	 */
	LocalDate findBorrowedBookDate(Book book);

}