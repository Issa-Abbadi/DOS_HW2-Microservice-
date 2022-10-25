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
	
	

    public static String catalogIP_Port = "192.168.1.107:8077";
    public static String orderIP_Port = "192.168.1.248:8088";

	public static void main(String[] args) {
		
		 FrontMain app = new FrontMain();
		
		 
		 


		 port(8084);
		 
		 get("/search/:topic", (req, res) -> {
			 res.type("application/json");
		     URL url = new URL("http://"+ catalogIP_Port +"/search/"+req.params(":topic").replaceAll(" ", "%20"));
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
			   
			  URL url = new URL("http://"+ catalogIP_Port +"/info/" + req.params(":id").replaceAll(" ", "%20"));
			
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
		 
		 
		 post("/purchase/:id", (req,res) -> {
			
			 res.type("application/json");
			 
			   
			  URL url = new URL("http://"+orderIP_Port+"/purchase/" + req.params(":id").replaceAll(" ", "%20"));
			
				 HttpURLConnection con = (HttpURLConnection) url.openConnection();
				 con.setRequestMethod("POST");
				 
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



		 }

	
		
}
