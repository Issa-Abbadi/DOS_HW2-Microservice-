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


public class CatalogMain   {
	
	
	 private  Connection connect() {  
	        // SQLite connection string  
		 String url = "jdbc:sqlite:catalogdb";  
	        Connection conn = null;  
	        try {  
	            conn = DriverManager.getConnection(url); 
	            System.out.printf("COnnect Success");
	        } catch (SQLException e) {  
	            System.out.println(e.getMessage());  
	        }  
	        return conn;  
	    }  
	 
  
	
	 public ResultSet search(String topic) {  
		 String sql = "SELECT id,title FROM  products WHERE topic = ?";   
	   
	        try{  
	            Connection conn = this.connect();  
	            PreparedStatement pstmt = conn.prepareStatement(sql);  
	            pstmt.setString(1, topic);   
	            ResultSet res =  pstmt.executeQuery();
	            	
	           
	            
	            if(!res.isBeforeFirst()) {
	            	 resultStr = "Not Found";
	            	 return res;
	            }
	            resultStr = "";
	            return res;
	        } catch (SQLException e) {  
	            System.out.println(e.getMessage());  
	        }
	        resultStr = "Not Found";
			return null;  
	    } 
	 
	 public ResultSet info(int id) {  
		 String sql = "SELECT title,price,quantity FROM  products WHERE id = ?";   
	   
	        try{  
	            Connection conn = this.connect();  
	            PreparedStatement pstmt = conn.prepareStatement(sql);  
	            pstmt.setInt(1, id);  
	            ResultSet res =  pstmt.executeQuery();
	          
	           
	            
	            if(!res.isBeforeFirst()) {
	            	 resultStr = "Not Found";
	            	 return res;
	            }else {
	            resultStr = "";
	           
	            return res;
	            }
	        } catch (SQLException e) {  
	        
	            System.out.println(e.getMessage());  
	        }
	        resultStr = "Not Found";
			return null;  
	    } 
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
	 
	 
	 
	 
	 public static String resultStr = "";
	public static void main(String[] args) {
		
		 CatalogMain app = new CatalogMain();
		
		 
		 


		 port(8077);
		 
		 get("/search/:topic", (req, res) -> {
			 res.type("application/json");
			   
			 return app.convert(app.search(req.params(":topic")));
		 });
		 
		 get("/info/:id", (req, res) -> {
			 res.type("application/json");
			   
			 return app.convert(app.info(Integer.parseInt(req.params(":id"))));
		 });
		 


		 }

	
		
}
