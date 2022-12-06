package microservice;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



// This is the FrontEnd microservice Class
public class FrontMain   {
	
	
	
    public static String catalogIP_Port = "localhost:8077"; // ip and port for  catalog microservice
    public static String orderIP_Port = "localhost:8088"; // ip and port for  order microservice

	public static void main(String[] args) {
		
		Cache<URL,StringBuffer> cache = new Cache<URL,StringBuffer>(5);
		
		 port(8084); //start the microservice at localhost at port 8084
		 
		 
		 //when it receive a get http request for an topic forword it to the catalog microservice 
		 //And return the response to the client
		 get("/search/:topic", (req, res) -> {
			 res.type("application/json");
			 
			
		     URL url = new URL("http://"+ catalogIP_Port +"/search/"+req.params(":topic").replaceAll(" ", "%20"));
		     if(cache.get(url) != null) {
		    	 System.out.println("In cache" + cache.get(url));
		    	 return cache.get(url);
		     }
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
			 cache.put(url, content);
			 return  content;
		 });
		 
		 //when it receive a get http request for an id forword it to the catalog microservice 
		 //And return the response to the client
		 get("/info/:id", (req, res) -> {
			 res.type("application/json");
			   
			  URL url = new URL("http://"+ catalogIP_Port +"/info/" + req.params(":id").replaceAll(" ", "%20"));
			    if(cache.get(url) != null) {
			    	 System.out.println("In cache" + cache.get(url));
			    	 return cache.get(url);
			     }
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
			     cache.put(url, content);
				 return  content;
		 });
		 
		 //when it receive a post http request for an id forword it to the order microservice 
		 //And return the response to the client
		 post("/purchase/:id", (req,res) -> {
			
			 res.type("application/json");
			 
			   
			  URL url = new URL("http://"+orderIP_Port+"/purchase/" + req.params(":id").replaceAll(" ", "%20"));
			
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
