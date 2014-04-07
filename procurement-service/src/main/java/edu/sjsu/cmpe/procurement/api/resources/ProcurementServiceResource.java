package edu.sjsu.cmpe.procurement.api.resources;
import java.util.Arrays;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.*;

public class ProcurementServiceResource {

	private Client client;
	public ProcurementServiceResource()
	{}
	public ProcurementServiceResource(Client client)
	{
		this.client=client;
	}
	void doPostServer()
	{
		try{
			client=Client.create();
			
			WebResource webResource=client.resource("http://54.193.56.218:9000/orders");
			String msg="{\"singer\":\"Metallica\",\"title\":\"Fade To Black\"}";
			ClientResponse response=webResource.type("application/json").post(ClientResponse.class,msg);
			
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
		}
		
			
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	void doHttpGet(){
	try {
		client = Client.create();
		WebResource webResource=client.resource("http://54.193.56.218:9000/orders/15566");
		ClientResponse response = webResource.accept("application/json")
				.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		String output = response.getEntity(String.class);
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		System.out.println("\n\n\n");
		JSONObject obj=new JSONObject(output);
		JSONArray shipping=obj.getJSONArray("shipped_books");
		int n=shipping.length();
		
		for(int i=0;i<n;i++)
		{
			
			JSONObject getbooks=shipping.getJSONObject(i);
			System.out.println("isbn is "+getbooks.getLong("isbn"));
			System.out.println("title is "+getbooks.getString("title"));
			System.out.println("category is "+getbooks.getString("category"));
			System.out.println("coverimage is "+getbooks.getString("coverimage"));
			
		}
		

	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
}
