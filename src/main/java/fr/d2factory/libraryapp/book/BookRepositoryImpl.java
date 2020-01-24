package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.d2factory.libraryapp.member.Member;

/**
 * The book repository emulates a database via 2 HashMaps
 */

public class BookRepositoryImpl implements BookRepository {
    
	private Map<Long, Book> availableBooks = new HashMap<>();
    private Map<Long, Borrow> borrowedBooks = new HashMap<>();

    @Override
	public void addBooks(List<Book> books){
        books.stream().forEach(book->availableBooks.put(book.getIsbn().getIsbnCode(), book));
    }

    @Override
	public Book findBook(long isbnCode) {
      	return availableBooks.get(isbnCode);
    }
    
	@Override
	public void saveBookBorrow(Book book, Member member, LocalDate borrowedAt) {
            borrowedBooks.put(book.getIsbn().getIsbnCode(), new Borrow(book, borrowedAt, member));
    }
    
    @Override
	public void removeBookBorrow(Book book, Member member) {
    	borrowedBooks.remove(book.getIsbn().getIsbnCode());  
    }
  
	@Override
	public List<Borrow> getBooksBorrowedByMember(Member member) {
        return borrowedBooks.values().stream().filter(map->map.getMember() == member).collect(Collectors.toList());
    }
	
    @Override
	public LocalDate findBorrowedBookDate(Book book) {
        Borrow borrow = borrowedBooks.get(book.getIsbn().getIsbnCode());
        return borrow != null ? borrow.getBorrowAt() : null;
    }
}
