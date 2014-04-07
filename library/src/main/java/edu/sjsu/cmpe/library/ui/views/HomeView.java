package edu.sjsu.cmpe.library.ui.views;

import java.util.ArrayList;
import java.util.List;

import com.yammer.dropwizard.views.View;

import edu.sjsu.cmpe.library.domain.Book;

public class HomeView extends View {
    private final List<Book> books;
    public HomeView(List<Book> books) {
              super("home.mustache");
              this.books = new ArrayList<Book>();
              for (Book book : books) {
            	  Book book1 = new Book();
            	  book1.setCategory(book1.getCategory());
            	  book1.setCoverimage(book1.getCoverimage());
            	  book1.setIsbn(book1.getIsbn());
            	  book1.setStatus(book1.getStatus());
            	  System.out.println(book1.getStatus().toString()+"status of book");
            	  book1.setTitle(book1.getTitle());
            	  book1.setLost(book1.getStatus());   
            	  this.books.add(book1);
              }
              
    }
    public List<Book> getBooks() {
              return books;
    }
}