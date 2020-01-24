package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.Borrow;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.resident.Resident;
import fr.d2factory.libraryapp.member.student.Student;
import fr.d2factory.libraryapp.member.student.StudentFirstYear;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
	
	private static final int DAYS_5 = 5;
	private static final int DAYS_15 = 15;
	private static final int DAYS_25 = 25;
	private static final int DAYS_30 = 30;
	private static final int DAYS_60 = 60;
	
	private static final String MESSAGE_EXCEPTION = "Member must return book to borrow";

	private static final int BOOK_INDEX_1 = 0;
	private static final int BOOK_INDEX_2 = 1;
	
	private static final long ISBN_BOOK_2 = 3326456467846L;
	private static final long ISBN_BOOK_1 = 46578964513L;
	
	private static final float TEN_CENTS = 0.10f; 
	private static final float TWENTY_CENTS = 0.20f; 
	
	@Mock
	private BookRepository bookRepositoryMock;
    private Library library;
	private static List<Book> books;


    @BeforeEach
    void setup() throws JsonParseException, JsonMappingException, IOException {
    	MockitoAnnotations.initMocks(this);
        ObjectMapper mapper = new ObjectMapper();
        File booksJson = new File("src/test/resources/books.json");
        bookRepositoryMock.addBooks(books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {
        }));
        library = new LibraryImpl(bookRepositoryMock);
    }
    
    @Test
    void student_can_borrow_a_book_if_book_is_available(){
    	
    	// Configure mock
    	Member student = new Student();
    	Book book = books.get(BOOK_INDEX_1);
    	when(bookRepositoryMock.findBook(ISBN_BOOK_1)).thenReturn(book);
        LocalDate date = LocalDate.of(2020,1,2);
       
        // Perform the test
        Book result = library.borrowBook(ISBN_BOOK_1, student, date);
        
        // Junit asserts
        Assertions.assertNotNull(result);
        Mockito.verify(bookRepositoryMock).saveBookBorrow(book, student, date);
        Assertions.assertEquals(book, result);
    }
    
    @Test
    void student_first_year_can_borrow_a_book_if_book_is_available(){
    	
    	// Configure mock
    	Member studentFirstYear = new StudentFirstYear();
    	Book book = books.get(BOOK_INDEX_1);
       	when(bookRepositoryMock.findBook(ISBN_BOOK_1)).thenReturn(book);
        LocalDate date = LocalDate.of(2020,1,2);
        
        // Perform the test
        Book result = library.borrowBook(ISBN_BOOK_1, studentFirstYear, date);
        
        // Junit asserts
        Mockito.verify(bookRepositoryMock).saveBookBorrow(book, studentFirstYear, date);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(book, result);
    }
    
    @Test
    void resident_can_borrow_a_book_if_book_is_available(){
    	
    	// Configure mock
    	Member resident = new Resident();
    	Book book = books.get(BOOK_INDEX_1);
    	when(bookRepositoryMock.findBook(ISBN_BOOK_1)).thenReturn(book);
        LocalDate date = LocalDate.of(2020,1,2);
        
        // Perform the test
        Book result = library.borrowBook(ISBN_BOOK_1, resident, date);
        
        // Junit asserts
        Mockito.verify(bookRepositoryMock).saveBookBorrow(book, resident, date);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(book, result);
    }

    @Test
    void borrowed_book_is_no_longer_available_for_student(){
    	
    	// Configure mock
    	Member student = new Student();
    	Book book = books.get(BOOK_INDEX_1);
    	LocalDate date = LocalDate.of(2020,1,2);
        when(bookRepositoryMock.findBook(ISBN_BOOK_1)).thenReturn(book);
    	when(bookRepositoryMock.findBorrowedBookDate(book)).thenReturn(date);
    	
    	// Perform the test
    	Book bookMustNull = library.borrowBook(ISBN_BOOK_1, student, date);
    	
    	// Junit asserts
    	Mockito.verify(bookRepositoryMock,never()).saveBookBorrow(book, student, date);
        Assertions.assertNull(bookMustNull);
    }

    @Test
    void borrowed_book_is_no_longer_available_for_student_first_year(){
    	
    	// Configure mock
    	Member studentFirstYear = new StudentFirstYear();
    	Book book = books.get(BOOK_INDEX_2);
        LocalDate date = LocalDate.of(2020,1,2);
        when(bookRepositoryMock.findBook(ISBN_BOOK_2)).thenReturn(book);
    	when(bookRepositoryMock.findBorrowedBookDate(book)).thenReturn(date);
        
        // Perform the test
        Book bookMustNull = library.borrowBook(ISBN_BOOK_2, studentFirstYear, date);
        
        // Junit asserts
        Mockito.verify(bookRepositoryMock,never()).saveBookBorrow(book, studentFirstYear, date);
        Assertions.assertNull(bookMustNull);
    }
    
    @Test
    void borrowed_book_is_no_longer_available_for_resident(){
    	
    	// Configure mock
     	Member resident = new Resident();
     	Book book = books.get(BOOK_INDEX_2);
        LocalDate date = LocalDate.of(2020,1,2);
        when(bookRepositoryMock.findBook(ISBN_BOOK_2)).thenReturn(book);
      	when(bookRepositoryMock.findBorrowedBookDate(book)).thenReturn(date);
        
        // Perform the test
        Book bookMustNull = library.borrowBook(ISBN_BOOK_2, resident, date);
        
        // Junit asserts
        Mockito.verify(bookRepositoryMock,never()).saveBookBorrow(book, resident, date);
        Assertions.assertNull(bookMustNull);
    }
    
	
    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){

    	// Configure mock
    	Member resident = new Resident();
     	Book book = books.get(BOOK_INDEX_2);
        float initialWallet = 100;
        resident.setWallet(initialWallet);
        LocalDate borrowDate = LocalDate.of(2020,1,2);
        LocalDate returnDate = borrowDate.plusDays(DAYS_25);
        when(bookRepositoryMock.findBorrowedBookDate(book)).thenReturn(borrowDate);
        
        // Perform the test
        library.returnBook(book, resident, returnDate);
        
        // Junit asserts
        Mockito.verify(bookRepositoryMock).removeBookBorrow(book, resident);
        Assertions.assertEquals(initialWallet - DAYS_25 * TEN_CENTS, resident.getWallet());
    }

    @Test
    void students_pay_10_cents_the_first_30days(){
    	
    	 // Configure mock
    	 Member student = new Student();
      	 Book book = books.get(BOOK_INDEX_2);
         float initialWallet = 100;
         student.setWallet(initialWallet);
         LocalDate borrowDate = LocalDate.of(2020,1,1);
         LocalDate returnDate = borrowDate.plusDays(DAYS_30);
    	 when(bookRepositoryMock.findBorrowedBookDate(book)).thenReturn(borrowDate);
    	 
    	 // Perform the test
         library.returnBook(book, student, returnDate);
         
         // Junit asserts
         Mockito.verify(bookRepositoryMock).removeBookBorrow(book, student);
         Assertions.assertEquals(initialWallet - DAYS_30 * TEN_CENTS, student.getWallet());
    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days(){
    	
    	// Configure mock
    	 Member studentFirstYear = new StudentFirstYear();
    	 Book book = books.get(BOOK_INDEX_2);
         float initialWallet = 100;
         studentFirstYear.setWallet(initialWallet);
         LocalDate borrowDate = LocalDate.of(2020,1,1);
         LocalDate returnDate = borrowDate.plusDays(DAYS_25);
    	 when(bookRepositoryMock.findBorrowedBookDate(book)).thenReturn(borrowDate);

    	 // Perform the test
         library.returnBook(book, studentFirstYear, returnDate);

         // Junit asserts
         Mockito.verify(bookRepositoryMock).removeBookBorrow(book, studentFirstYear);
         Assertions.assertEquals(initialWallet - (DAYS_25-DAYS_15) *TEN_CENTS, studentFirstYear.getWallet());
    }
   
    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
      	 
    	 // Configure mock
    	 Member resident = new Resident();
    	 Book book = books.get(BOOK_INDEX_2);
         float initialWallet = 100;
         resident.setWallet(initialWallet);
         LocalDate borrowDate = LocalDate.of(2020,1,1);
         LocalDate returnDate = borrowDate.plusDays(DAYS_60+DAYS_5);
    	 when(bookRepositoryMock.findBorrowedBookDate(book)).thenReturn(borrowDate);

         // Perform the test
         library.returnBook(book, resident, returnDate);
         
         // Junit asserts
         Assertions.assertEquals(initialWallet - (DAYS_60 * TEN_CENTS + DAYS_5 * TWENTY_CENTS), resident.getWallet());
    }

    @Test
    void resident_cannot_borrow_book_if_they_have_late_books(){
    	
    	// Configure mock
        Member resident = new Resident();
        Book borrowBookByMember = books.get(BOOK_INDEX_1);
        LocalDate borrowDate = LocalDate.of(2020,1,1);
        LocalDate now = borrowDate.plusDays(DAYS_60+DAYS_5);
        Borrow borrowByMember = new Borrow(borrowBookByMember, borrowDate, resident);
      	when(bookRepositoryMock.getBooksBorrowedByMember(resident)).thenReturn(new ArrayList<Borrow>(){{add(borrowByMember);}});
      	
      	// Perform the test
      	Exception exc = assertThrows(HasLateBooksException.class, ()->{
            library.borrowBook(ISBN_BOOK_2, resident, now);
        });
        String actualMessage = exc.getMessage();
        
        // Junit asserts
        Assertions.assertTrue(actualMessage.contains(MESSAGE_EXCEPTION));
    }
    
    @Test
    void student_cannot_borrow_book_if_they_have_late_books(){
    	
    	// Configure mock
        Member student = new Student();
        Book borrowBookByMember = books.get(BOOK_INDEX_1);
        LocalDate borrowDate = LocalDate.of(2020,1,1);
        Borrow borrowByMember = new Borrow(borrowBookByMember, borrowDate, student);
        LocalDate now = borrowDate.plusDays(DAYS_30+DAYS_5);
      	when(bookRepositoryMock.getBooksBorrowedByMember(student)).thenReturn(new ArrayList<Borrow>(){{add(borrowByMember);}});
      	
      	// Perform the test
        Exception exc = assertThrows(HasLateBooksException.class, ()->{
            library.borrowBook(ISBN_BOOK_2, student, now);
        });
        String actualMessage = exc.getMessage();
        
        // Junit asserts
        Assertions.assertTrue(actualMessage.contains(MESSAGE_EXCEPTION));
    }
    
    @Test
    void students_in_1st_year_cannot_borrow_book_if_they_have_late_books(){
    	
    	// Configure mock
    	Member studentFirstYear = new StudentFirstYear();
        Book borrowBookByMember = books.get(BOOK_INDEX_1);
        LocalDate borrowDate = LocalDate.of(2020,1,1);
        Borrow borrowByMember = new Borrow(borrowBookByMember, borrowDate, studentFirstYear);
        LocalDate now = borrowDate.plusDays(DAYS_30+DAYS_5);
      	when(bookRepositoryMock.getBooksBorrowedByMember(studentFirstYear)).thenReturn(new ArrayList<Borrow>(){{add(borrowByMember);}});

        // Perform the test
        Exception exc = assertThrows(HasLateBooksException.class, ()->{
            library.borrowBook(ISBN_BOOK_2, studentFirstYear, now);
        });
        String actualMessage = exc.getMessage();
        
        // Junit asserts
        Assertions.assertTrue(actualMessage.contains(MESSAGE_EXCEPTION));
    }
}
