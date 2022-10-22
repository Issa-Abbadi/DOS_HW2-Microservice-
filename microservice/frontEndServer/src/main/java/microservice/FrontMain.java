package microservice;
import static spark.Spark.get;

import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import java.util.List;

import com.google.gson.Gson;

import spark.Response;

import static spark.Spark.port;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  
import java.sql.DriverManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FrontMain   {
	
	
	 private  Connection connect() {  
	        // SQLite connection string  
		 String url = "jdbc:sqlite:microservicedb";  
	        Connection conn = null;  
	        try {  
	            conn = DriverManager.getConnection(url); 
	            System.out.printf("COnnect Success");
	        } catch (SQLException e) {  
	            System.out.println(e.getMessage());  
	        }  
	        return conn;  
	    }  
	 
  

	public static void main(String[] args) {
		
		 FrontMain app = new FrontMain();
		
		 
		 


		 port(8084);
		 
		 get("/search/:topic", (req, res) -> {
			 res.type("application/json");
		     URL url = new URL("http://localhost:8077/search/"+req.params(":topic").replaceAll(" ", "%20"));
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 
			 int status = con.getResponseCode();
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
		 
		 get("/info/:id", (req, res) -> {
			 res.type("application/json");
			   
			  URL url = new URL("http://localhost:8077/info/" + req.params(":id").replaceAll(" ", "%20"));
			
				 HttpURLConnection con = (HttpURLConnection) url.openConnection();
				 con.setRequestMethod("GET");
				 
				 int status = con.getResponseCode();
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
		 
		 
		 put("/purchase/:id", (req,res) -> {
			
			 res.type("application/json");
			 
			   
			  URL url = new URL("http://localhost:8088/purchase/" + req.params(":id").replaceAll(" ", "%20"));
			
				 HttpURLConnection con = (HttpURLConnection) url.openConnection();
				 con.setRequestMethod("PUT");
				 
				 int status = con.getResponseCode();
				 BufferedReader in = new BufferedReader(
				 new InputStreamReader(con.getInputStream()));
				 String inputLine;
				 StringBuffer content = new StringBuffer();
				 while ((inputLine = in.readLine()) != null) {
			     content.append(inputLine);
				 }
				 in.close();
				 System.out.println(content);
						
//						 URL url2 = new URL("http://localhost:8088/purchase/" + req.params(":id").replaceAll(" ", "%20"));
//							
//						 HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
//						 con2.setRequestMethod("PUT");
//						 
//						 int status2 = con2.getResponseCode();
//						 BufferedReader in2 = new BufferedReader(
//								  new InputStreamReader(con2.getInputStream()));
//								String inputLine2;
//								StringBuffer content2 = new StringBuffer();
//								while ((inputLine2 = in2.readLine()) != null) {
//								    content2.append(inputLine2);
//								}
//								in2.close();
//								System.out.println(content2);
				   
				 return  content;
		 });



		 }

	
		
}
