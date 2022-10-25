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


public class OrderMain   {
	
	
	 private  Connection connect() {  
	        // SQLite connection string  
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
	 
  

	 
	 
	 Connection conn = this.connect();
	 public ResultSet purchase(int id) throws NumberFormatException, JSONException, Exception {  
		 String sql = "SELECT quantity FROM  products WHERE id = ?";   
		 
	   
	        try{  
	              
	            PreparedStatement pstmt = conn.prepareStatement(sql);  
	            pstmt.setInt(1, id);  
	            ResultSet res =  pstmt.executeQuery();
	          
	            if(res.getInt(1) > 0) {
	            
	            	 String sql2 = "UPDATE products SET quantity = ? WHERE id = ?"; 
	            	 PreparedStatement pstmt2 = conn.prepareStatement(sql2);  
	            	 pstmt2.setInt(1,  res.getInt(1)-1);
	 	            pstmt2.setInt(2, id);  
	 	              pstmt2.executeUpdate();
	 	             res =  pstmt.executeQuery();
	 	            resultStr = "Remaining";
	 	            return  res;
	            }else  if(!res.next() ) {
	            	resultStr = "Not Found";
	            	return res;
	            }else{
	            	System.out.println("Sold out");
	            	resultStr = "Sold Out";
	            	return res;
	            }
	        } catch (SQLException e) {  
	        	
	            System.out.println(e.getMessage());  
	        }
	        resultStr = "Not Found";
			return null;  
	    } 
	 
	 public static String resultStr = "";
	 
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
	
	 
	 
	 public  void orderUpdate(int id) throws NumberFormatException, JSONException, Exception {  
		 String sql = "INSERT INTO orders(id, Time) VALUES(?,?)";   
		 PreparedStatement pstmt= conn.prepareStatement(sql);  
    	 pstmt.setInt(1, id);
    	 Timestamp p = new java.sql.Timestamp(new java.util.Date().getTime());
    	 System.out.println(p.toString());
    	 pstmt.setString(2,p.toString());
         pstmt.executeUpdate();
      
	   
	 }
	 
	 public static String catalogIP_Port = "192.168.1.107:8077";
	 
	 public static void main(String[] args) {
		
		 OrderMain app = new OrderMain();
		
		 
		 
		 

		 port(8088);
		 

		 
		 
		 post("/purchase/:id", (req,res) -> {
			
			 res.type("application/json");
			 
			  URL url = new URL("http://"+catalogIP_Port+"/purchase/" + req.params(":id").replaceAll(" ", "%20"));

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
			 
			 if(content.toString().equals("-1")) {
				 return new Gson().toJson("Not Found");
			
			 }else if (content.toString().equals("0")) {
				 return new Gson().toJson("Sold Out");
			 }else {
				
				 URL url2 = new URL("http://"+catalogIP_Port+"/purchase/" + req.params(":id").replaceAll(" ", "%20"));
					
				 con = (HttpURLConnection) url2.openConnection();
				 con.setDoOutput(true);
				 con.setRequestMethod("PUT");
				 
				
				 
				 OutputStreamWriter out = new OutputStreamWriter(
						    con.getOutputStream());
				        int w = Integer.parseInt(content.toString());
				 
						out.write("" + (w-1));
						out.close();
						
				 BufferedReader in2 = new BufferedReader(
						  new InputStreamReader(con.getInputStream()));
						String inputLine2;
						StringBuffer content2 = new StringBuffer();
						while ((inputLine2 = in2.readLine()) != null) {
						    content2.append(inputLine2);
						}
						in2.close();
						System.out.println(content2);
						app.orderUpdate(Integer.parseInt(req.params(":id")));
						 return content2;
			 }
			 
		
			 
			 
			
		 });



		 }

	
		
}
