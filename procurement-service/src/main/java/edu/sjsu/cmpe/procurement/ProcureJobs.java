package edu.sjsu.cmpe.procurement;

import java.util.ArrayList;


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.message.StompJmsMessage;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;



@Every("5s")
public class ProcureJobs extends Job{
	@Override
	public void doJob() {
		//get post consumer and publisher
		//1 consumer
		//2 post
		//3 get
		//4 publisher
		System.out.println("Job started");
		
		ProcureJobs pj =new ProcureJobs();
		try{
			System.out.println("Creating job for consumer");
			//taking jobs the from consumer process
			String str_consumer = pj.consumer();
			if(str_consumer!=null)
			{
				pj.domsgpost(str_consumer);
			}
			ArrayList<String>books=pj.getBooksFromPulisher();
			pj.publisher(books);
		}
		catch(Exception e){e.printStackTrace();}
    }

		
	public String consumer() throws JMSException{

		String userName ="admin";
		String password ="password";
		String host = "54.193.56.218";
		int port = Integer.parseInt("61613");
		System.out.println("executing consumer process");
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);
		Connection conn = factory.createConnection(userName, password);
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination dest = new StompJmsDestination("/queue/15566.book.orders");
		MessageConsumer consumer = session.createConsumer(dest);
		System.out.println("Waiting for messages from " + "/queue/15566.book.orders" + "...");
		String temp1="";
		while(true) {
		    Message msg = consumer.receive(5000);
		    if(msg==null)
		    	break;
		    
		    if( msg instanceof  TextMessage ) {
			String body = ((TextMessage) msg).getText();
			
			System.out.println("Received message = " + body);
			
			String temp=body;
			String isbn=temp.split(":")[1];
			
			temp1=temp1+isbn+",";
			System.out.println("temp 1 is "+temp1);
			if( "SHUTDOWN".equals(body)) {
			    break;
			}

		    } else if (msg instanceof StompJmsMessage) {
			StompJmsMessage smsg = ((StompJmsMessage) msg);
			String body = smsg.getFrame().contentAsString();
			if ("SHUTDOWN".equals(body)) {
			    break;
			}
			System.out.println("Received message = " + body);

		    } else {
			System.out.println("Unexpected message type: "+msg.getClass());
		    }
		}
		conn.close();
		String jsonIsbn="";
		if(temp1==""){
			return null;
		}

		jsonIsbn=temp1;
		System.out.println("jsonisbn "+jsonIsbn);
		jsonIsbn=temp1.substring(0,temp1.length()-1);
		System.out.println("jsonisbn is "+jsonIsbn);
		return jsonIsbn;
	    }

	
	void domsgpost(String str)
	{
		try {
	
			Client client = Client.create();
			WebResource webResource = client
					.resource("http://54.193.56.218:9000/orders");
			String input="{\"id\" :\"15566\",\"order_book_isbns\":["+str+"]}";
			ClientResponse response = webResource.type("application/json")
					.post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Connection Failed : HTTP error code : "
						+ response.getStatus());
			}
		
			String output = response.getEntity(String.class);
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    	public ArrayList<String> getBooksFromPulisher() throws JSONException
    	{
    		ArrayList<String> library1=new ArrayList<String>();
    		
    			Client client = Client.create();
    			WebResource webResource=client.resource("http://54.193.56.218:9000/orders/15566");
    			ClientResponse response = webResource.accept("application/json")
    					.get(ClientResponse.class);
    			if (response.getStatus() != 200) {
    				throw new RuntimeException("Failed : HTTP error code : "
    						+ response.getStatus());
    			}
    			String output = response.getEntity(String.class);
    			System.out.println("Output received from Server .... \n");
    			System.out.println(output);
    			JSONObject obj=new JSONObject(output);
    			JSONArray shipping=obj.getJSONArray("shipped_books");
    			int n=shipping.length();
    			
    			for(int i=0;i<n;i++)
    			{
    				JSONObject getbooks=shipping.getJSONObject(i);
    				System.out.println("Isbn is "+getbooks.getLong("isbn"));
    				System.out.println("Title is "+getbooks.getString("title"));
    				System.out.println("Category is "+getbooks.getString("category"));
    				System.out.println("Cover image is "+getbooks.getString("coverimage"));
    				String str=""+getbooks.getLong("isbn")+":\""+getbooks.getString("title")+ "\""+":\""+getbooks.getString("category")+"\""+":\""+getbooks.getString("coverimage")+"\"";
    				System.out.println(str);
    				library1.add(str);
    			}
    		return library1;
    	}
    	

    	public void publisher(ArrayList<String>books) throws JMSException
    	{
    		String userName = "admin";
    		String password = "password";
    		String host = "54.193.56.218";
    		int port = Integer.parseInt("61613");
    		String destination_a = "/topic/15566.book.all";
    		String destination_b="/topic/15566.book.computer";

    		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
    		factory.setBrokerURI("tcp://" + host + ":" + port);

    		Connection conn = factory.createConnection(userName, password);
    		conn.start();
    		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    		
    		Destination destlibrary1_a = new StompJmsDestination(destination_a);
    		MessageProducer producer_a = session.createProducer(destlibrary1_a);
    		
    		Destination destlibrary1_b = new StompJmsDestination(destination_b);
    		MessageProducer producer_b = session.createProducer(destlibrary1_b);
    		
    		String info;
    		for(int i=0;i<books.size();i++)
    		{
    			info=books.get(i);
    			TextMessage msg=session.createTextMessage(info);
        		msg.setLongProperty("id", System.currentTimeMillis());
    			producer_a.send(msg);
				System.out.println("Message sent for producer A"+msg);
    			if(info.split(":")[2].equals("\"computer\""))
    			{
    				producer_b.send(msg);
    				System.out.println("Message sent for producer B"+msg);
    			}
    		}
    		conn.close();
    	    }
    	}

