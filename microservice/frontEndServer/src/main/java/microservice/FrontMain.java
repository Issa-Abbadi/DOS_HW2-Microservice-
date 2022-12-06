package microservice;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;



// This is the FrontEnd microservice Class
public class FrontMain   {
	
	
	public static String catalogIP_Port = ""; 
	public static String orderIP_Port = "";
	    
    public static String catalogIP_Port1 = "localhost:8077"; // ip and port for  catalog microservice
    public static String orderIP_Port1 = "localhost:8088"; // ip and port for  order microservice
    
    public static String catalogIP_Port2 = "localhost:8078"; // ip and port for  catalog2 microservice
    public static String orderIP_Port2 = "localhost:8089"; // ip and port for  order2 microservice
    public static Queue<String> catalogQueue = new LinkedList<>();
    public static Queue<String> orderQueue = new LinkedList<>();
    
  


	public static void main(String[] args) {
		
		catalogQueue.add(catalogIP_Port1);
		catalogQueue.add(catalogIP_Port2);
		
		orderQueue.add(orderIP_Port1);
		orderQueue.add(orderIP_Port2);
		
		
		
		Cache<URL,StringBuffer> cache = new Cache<URL,StringBuffer>(5);
		
		 port(8084); //start the microservice at localhost at port 8084
		 
		 
		 //when it receive a get http request for an topic forword it to the catalog microservice 
		 //And return the response to the client
		 get("/search/:topic", (req, res) -> {
			 
			
			 
			 res.type("application/json");
			 
			 URL cacheUrl = new URL("http://"+"/info/" + req.params(":id").replaceAll(" ", "%20"));
		     
		     if(cache.get(cacheUrl) != null) {
		    	 System.out.println("In cache" + cache.get(cacheUrl));
		    	 return cache.get(cacheUrl);
		     }
		     catalogIP_Port = catalogQueue.remove();
			 catalogQueue.add(catalogIP_Port);
			 URL url = new URL("http://"+ catalogIP_Port +"/search/"+req.params(":topic").replaceAll(" ", "%20"));
			 
		     System.out.println(url);
		     
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 
			
			 BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
					System.out.println(content);
			 cache.put(cacheUrl, content);
			 return  content;
		 });
		 
		 //when it receive a get http request for an id forword it to the catalog microservice 
		 //And return the response to the client
		 get("/info/:id", (req, res) -> {
			 res.type("application/json");
			 
			
			   
			  URL cacheUrl = new URL("http://"+"/info/" + req.params(":id").replaceAll(" ", "%20"));
			
			 
			    if(cache.get(cacheUrl) != null) {
			    	 System.out.println("In cache" + cache.get(cacheUrl));
			    	 return cache.get(cacheUrl);
			     }
			    catalogIP_Port = catalogQueue.remove();
				 catalogQueue.add(catalogIP_Port);
				  URL url = new URL("http://"+ catalogIP_Port +"/info/" + req.params(":id").replaceAll(" ", "%20"));
			    System.out.println(url);
				 HttpURLConnection con = (HttpURLConnection) url.openConnection();
				 con.setRequestMethod("GET");
				 
				
				 BufferedReader in = new BufferedReader(
						  new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer content = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
						    content.append(inputLine);
						}
						in.close();
						System.out.println(content);
			     cache.put(cacheUrl, content);
				 return  content;
		 });
		 
		 //when it receive a post http request for an id forword it to the order microservice 
		 //And return the response to the client
		 post("/purchase/:id", (req,res) -> {
			
			 res.type("application/json");
			 
			 orderIP_Port = orderQueue.remove();
			 orderQueue.add(orderIP_Port);
			   
			 
			  URL url = new URL("http://"+orderIP_Port+"/purchase/" + req.params(":id").replaceAll(" ", "%20"));
			  System.out.println(url);
				 HttpURLConnection con = (HttpURLConnection) url.openConnection();
				 con.setRequestMethod("POST");
				 
				
				 BufferedReader in = new BufferedReader(
				 new InputStreamReader(con.getInputStream()));
				 String inputLine;
				 StringBuffer content = new StringBuffer();
				 while ((inputLine = in.readLine()) != null) {
			     content.append(inputLine);
				 }
				 in.close();
				 System.out.println(content);
						

				   
				 return  content;
		 });



		 }

	
		
}
