package edu.sjsu.cmpe.library.repository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import javax.jms.JMSException;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Book.Status;

public class Asynchrouns_Msg_Receiver implements MessageListener {
	LibraryServiceConfiguration config;
	BookRepositoryInterface bookRepository;
	Book newbook=new Book();
	public Asynchrouns_Msg_Receiver(LibraryServiceConfiguration config, BookRepositoryInterface bookRepository2)
			throws JMSException {
		this.config = config;
		this.bookRepository = bookRepository2;
		// messgListener();
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 *  
	 */
	public void onMessage(Message message) {
		TextMessage messg = (TextMessage) message;
		try {
			System.out.println("Received message:::: " + messg.getText());
		} catch (JMSException ex) {
			ex.printStackTrace();
		}
	}

	public void messgListener() throws JMSException, MalformedURLException {
		System.out.println("Inside message listener");
		ArrayList<String> receivedBooks = new ArrayList<String>();

		long isbn_number;
		String bookTitle;
		String bookCategory;
		URL webUrl;
		Book receivedBookItems;
		String user = "admin";
		String password = "password";
		String host = "54.193.56.218";
		int port = Integer.parseInt("61613");

		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);

		while (true) {

			Connection connection = factory.createConnection(user, password);

			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Destination dest = new StompJmsDestination(
					config.getStompTopicName());

			MessageConsumer consumer = session.createConsumer(dest);
			System.currentTimeMillis();
			while (true) {
				Message messg = consumer.receive(500);
				if (messg == null)
					break;
				if (messg instanceof TextMessage) {
					String body = ((TextMessage) messg).getText();
					System.out.println("Message received =  " + body);
					receivedBooks.add(body);
				} else {
					System.out.println("Unexpected message " + messg.getClass());
				}
			}
			connection.close();
			if(!receivedBooks.isEmpty()){
			for (String books : receivedBooks) 
			{
				isbn_number = Long.parseLong(books.split(":\"")[0]);
				bookTitle = books.split(":\"")[1].replaceAll("^\"|\"$", "");
				bookCategory = books.split(":\"")[2].replaceAll("^\"|\"$", "");
				String str = books.split(":\"")[3];// .replaceAll("^\"|\"$",// "");//url
				str = str.substring(0,str.length()-1);
				webUrl=new URL(str);
				receivedBookItems = bookRepository.getBookByISBN(isbn_number);
				System.out.println(webUrl);

				if(receivedBookItems.getIsbn()==0){
					System.out.println("Adding a new book");
					receivedBookItems.setIsbn(isbn_number);
					receivedBookItems.setCategory(bookCategory);
					receivedBookItems.setCoverimage(webUrl);
					receivedBookItems.setStatus(Status.available);
					receivedBookItems.setTitle(bookTitle);
					bookRepository.add(receivedBookItems);
					System.out.println("Received new book is  "+ receivedBookItems.toString());
				}
				else{//if (receivedBookItems.getStatus().equals(Status.lost)) {
					receivedBookItems.setStatus(Status.available);
				}
				
			}
			receivedBooks.clear();
		}

	}
	}
}