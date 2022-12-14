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
	    
    public static String catalogIP_Port1 = "192.168.249.76:8077"; // ip and port for  catalog1 microservice
    public static String orderIP_Port1 = "192.168.249.196:8088"; // ip and port for  order1 microservice
    
    public static String catalogIP_Port2 = "192.168.249.179:8078"; // ip and port for  catalog2 microservice
    public static String orderIP_Port2 = "192.168.249.242:8089"; // ip and port for  order2 microservice
    
    public static Queue<String> catalogQueue = new LinkedList<>();//catalog Queue to switch between them
    public static Queue<String> orderQueue = new LinkedList<>(); //order Queue to switch between them
    
  


	public static void main(String[] args) {
		
		//add IPs to Queues
		catalogQueue.add(catalogIP_Port1);
		catalogQueue.add(catalogIP_Port2);
		
		orderQueue.add(orderIP_Port1);
		orderQueue.add(orderIP_Port2);
		
		
		//create a cache of size 5
		Cache<URL,StringBuffer> cache = new Cache<URL,StringBuffer>(5);
		
		 port(8084); //start the microservice at port 8084
		 
		 
		 //when it receive a get http request for an topic check if it is in the cache if not 
		 //forword it to the catalog microservice and return the response to the client
		 
		 get("/search/:topic", (req, res) -> {
			 
			
			 
			 res.type("application/json");
			 
			 URL cacheUrl = new URL("http://"+"/search/" + req.params(":topic").replaceAll(" ", "%20"));
		     
		     if(cache.get(cacheUrl) != null) {
		    	 System.out.println("In cache" + cache.get(cacheUrl));
		    	 return cache.get(cacheUrl);
		     }
		     //if not in the cache continue
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
		 
		 // when it receive a get http request for an id  check if it is in the cache if not 
		 // forword it to the catalog microservice and return the response to the client
		 get("/info/:id", (req, res) -> {
			 res.type("application/json");
			 
			
			   
			  URL cacheUrl = new URL("http://"+"/info/" + req.params(":id").replaceAll(" ", "%20"));
			
			 
			    if(cache.get(cacheUrl) != null) {
			    	 System.out.println("In cache" + cache.get(cacheUrl));
			    	 return cache.get(cacheUrl);
			     }
			    //if not in the cache continue
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
			
			 //switch between the order microservices
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
				 if(content.toString().contains("Remaining")) {
					 URL cacheUrl = new URL("http://"+"/info/" + req.params(":id").replaceAll(" ", "%20"));
					 cache.delete(cacheUrl);
				 }
				 		

				   
				 return  content;
		 });



		 }

	
		
}
