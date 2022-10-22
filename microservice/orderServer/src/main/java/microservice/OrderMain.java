package microservice;
import static spark.Spark.get;

import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import java.util.List;

import com.google.gson.Gson;

import spark.Response;

import static spark.Spark.port;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  
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
	 
  

	 
	 
	 
	 public ResultSet purchase(int id) throws NumberFormatException, JSONException, Exception {  
		 String sql = "SELECT quantity FROM  products WHERE id = ?";   
		 
	   
	        try{  
	            Connection conn = this.connect();  
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
	public static void main(String[] args) {
		
		 OrderMain app = new OrderMain();
		
		 
		 


		 port(8088);
		 

		 
		 
		 put("/purchase/:id", (req,res) -> {
			
			 res.type("application/json");
			 
			 return app.convert(app.purchase(Integer.parseInt(req.params(":id"))));
		 });



		 }

	
		
}
