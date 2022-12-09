package microservice;
import static spark.Spark.post;
import static spark.Spark.put;
import com.google.gson.Gson;
import static spark.Spark.port;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.DriverManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OrderMain2   {
	
	 public static String catalogIP_Port = "192.168.249.179:8078"; // ip and port for  catalog2 microservice
	 public static String orderIP_Port1 = "192.168.249.196:8088"; // ip and port for  order1 microservice

	
	//Connect to sqlite order database
	 private  Connection connect() {  
		// SQLite connection string to the order database 
		 String url = "jdbc:sqlite:orderdb";  
	        Connection conn = null;  
	        try {  
	            conn = DriverManager.getConnection(url); 
	            System.out.printf("COnnect Success");
	        } catch (SQLException e) {  
	            System.out.println(e.getMessage());  
	        }  
	        return conn;  
	    }  
	 

	 Connection conn = this.connect(); // create just one connection to the database
	 
	
	 
	 public static String resultStr = "";//contain the message if there is to respond to user
	 
	 
     //Method to convert ResultSet to JSONArray (Also Add resultStr to the Array)
	 public static JSONArray convert(ResultSet resultSet) throws Exception {

		 
		    JSONArray jsonArray = new JSONArray();
		 
		    while (resultSet.next()) {
		 
		        int columns = resultSet.getMetaData().getColumnCount();
		        JSONObject obj = new JSONObject();
		 
		        for (int i = 0; i < columns; i++)
		            obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
		     
		        
		        jsonArray.put(obj);
		       System.out.println(jsonArray);
		    }
		    if(!resultStr.equals("")) {
	        	jsonArray.put(resultStr);
	        	
		        
	        }
		    return jsonArray;
		}
	
	 
	 //Method to Insert a new record into the order database when the purchase is Success
	 public  String orderUpdate(int id,Timestamp p) throws NumberFormatException, JSONException, Exception {  
		 String sql = "INSERT INTO orders(id, Time) VALUES(?,?)";   //sql query
		 PreparedStatement pstmt= conn.prepareStatement(sql);  
    	 pstmt.setInt(1, id);
    	 pstmt.setString(2,p.toString());
         pstmt.executeUpdate();
         
         return "Insert Sccuess";
      
	   
	 }
	 
	
	 
	 public static void main(String[] args) {
		
		 OrderMain2 app = new OrderMain2(); //create an instance of the class
		
		 
		 
		 

		 port(8089); //start the microservice at port 8088
		 

		 
		//when it receive a post http request for an id it checks if there is in the stock 
		// and make the purchase also add the record (id,timestamp) to the order database 
		 post("/purchase/:id", (req,res) -> {
			
			 res.type("application/json");
			 
			 //send a get method to catalog database to see the remaining quantity from this book
			  URL url = new URL("http://"+catalogIP_Port+"/purchase/" + req.params(":id").replaceAll(" ", "%20")); 

			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 
			 //read the respond
			 BufferedReader in = new BufferedReader(
			 new InputStreamReader(con.getInputStream()));
			 String inputLine;
			 StringBuffer content = new StringBuffer();
			 while ((inputLine = in.readLine()) != null) {
		     content.append(inputLine);
			 }
			 in.close();
			 System.out.println(content); 
			 
			 //return not found or sold out if it is correct 
			 if(content.toString().equals("-1")) { 
				 return new Gson().toJson("Not Found");
			
			 }else if (content.toString().equals("0")) {
				 return new Gson().toJson("Sold Out");
			 }else { //if it is found and not sold out continue
				
				 //send a put method to catalog database to update the remaining quantity from this book (quantity -1)
				 URL url2 = new URL("http://"+catalogIP_Port+"/purchase/" + req.params(":id").replaceAll(" ", "%20"));
					
				 con = (HttpURLConnection) url2.openConnection();
				 con.setDoOutput(true);
				 con.setRequestMethod("PUT");
				 
				
				 // put in the message (quantity -1)
				 OutputStreamWriter out = new OutputStreamWriter(
						    con.getOutputStream());
				        int w = Integer.parseInt(content.toString());
				 
						out.write("" + (w-1));
						out.close();
						
				 //read the response
				 BufferedReader in2 = new BufferedReader(
						  new InputStreamReader(con.getInputStream()));
						String inputLine2;
						StringBuffer content2 = new StringBuffer();
						while ((inputLine2 = in2.readLine()) != null) {
						    content2.append(inputLine2);
						}
						in2.close();
						System.out.println(content2);
						
						
						
						 URL url3 = new URL("http://"+orderIP_Port1+"/orderUpdate/" + req.params(":id").replaceAll(" ", "%20"));
							
						 con = (HttpURLConnection) url3.openConnection();
						 con.setDoOutput(true);
						 con.setRequestMethod("PUT");
						 
						 BufferedReader in3 = new BufferedReader(
								  new InputStreamReader(con.getInputStream()));
								String inputLine3;
								StringBuffer content3 = new StringBuffer();
								while ((inputLine3 = in3.readLine()) != null) {
								    content3.append(inputLine3);
								}
								in3.close();
								System.out.println(content3);
						 
						
						
						if(!("").contentEquals(content3)) {
							 return content2;
						}
						 return new Gson().toJson("ERROR happend");
			 }
			 
		
			 
			 
			
		 });
		 
		 //to make db consistant
		 put("/orderUpdate/:id", (req,res) -> {
			 res.type("application/json");
			 return app.orderUpdate(Integer.parseInt(req.params(":id")),Timestamp.valueOf(req.body()));
		 });
		 



		 }

	
		
}
