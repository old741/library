package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.Borrow;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.resident.Resident;
import fr.d2factory.libraryapp.member.student.Student;
/**
 * The library class is in charge of stocking the books and managing the return delays and members
 *
 * The books are available via the {@link fr.d2factory.libraryapp.book.BookRepository}
 */
public class LibraryImpl implements Library {

	private static final int DAYS_30 = 30;
	private static final int DAYS_60 = 60;
	private static final String MESSAGE_EXCEPTION = "Member must return book to borrow";

	private BookRepository bookRepository;
	
	public LibraryImpl(BookRepository bookRepository) {
	        super();
	        this.bookRepository = bookRepository;
	    }

	 public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
	        Book book = bookRepository.findBook(isbnCode);
	        List<Borrow> borrowedBook = bookRepository.getBooksBorrowedByMember(member);
	        
	        if (borrowedBook != null) {
	            if (borrowedBook.stream().anyMatch(borrow-> { 
	                if (member instanceof Student) {
	                    if (ChronoUnit.DAYS.between(borrow.getBorrowAt(), borrowedAt) > DAYS_30) {
	                        return true;
	                    }
	                }

	                if (member instanceof Resident) {
	                    if (ChronoUnit.DAYS.between(borrow.getBorrowAt(), borrowedAt) > DAYS_60) {
	                        return true;
	                    }
	                }

	                return false;
	            })) {
	                throw new HasLateBooksException(MESSAGE_EXCEPTION);
	            }
	        }
	        if (book != null) {
	            LocalDate date = bookRepository.findBorrowedBookDate(book);
	            if (date == null) {
	                bookRepository.saveBookBorrow(book, member, borrowedAt);
	                return book;
	            }
	        }

	        return null;
	    }

	@Override
    public void returnBook(Book book, Member member, LocalDate returnedAt) {
        LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
        long borrowDays = ChronoUnit.DAYS.between(borrowedAt, returnedAt);
        member.payBook(borrowDays);
        bookRepository.removeBookBorrow(book, member);
    }

}
