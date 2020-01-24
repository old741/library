package fr.d2factory.libraryapp.book;

/**
 * A simple representation of a book
 */
public class Book {
 
	private String title;
	private String author;
	private ISBN isbn;

    public Book() {}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public ISBN getIsbn() {
		return isbn;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setIsbn(ISBN isbn) {
		this.isbn = isbn;
	}
    
    
}
