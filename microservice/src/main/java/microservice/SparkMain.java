package microservice;
import static spark.Spark.get;

import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import java.util.List;

import com.google.gson.Gson;

import static spark.Spark.port;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;  
import java.sql.DriverManager;

public class SparkMain   {
	
	
	 private  Connection connect() {  
	        // SQLite connection string  
		 String url = "jdbc:sqlite:C:/sqlite/gui/SQLiteStudio/microservicedb";  
	        Connection conn = null;  
	        try {  
	            conn = DriverManager.getConnection(url); 
	            System.out.printf("COnnect Success");
	        } catch (SQLException e) {  
	            System.out.println(e.getMessage());  
	        }  
	        return conn;  
	    }  
	 
	 public void insert() {  
		 String sql = "INSERT INTO products(id) VALUES(450)";   
	   
	        try{  
	            Connection conn = this.connect();  
	            PreparedStatement pstmt = conn.prepareStatement(sql);  
//	            pstmt.setString(1, name);  
//	            pstmt.setDouble(2, capacity);  
	            pstmt.executeUpdate();  
	        } catch (SQLException e) {  
	            System.out.println(e.getMessage());  
	        }  
	    }  
	public static void main(String[] args) {
		
		 SparkMain app = new SparkMain();
		 app.insert();
	
		
//		int x = 500;
//		
//		
//		 try {
//			 Connection conn = this.connect();  
//			PreparedStatement pstmt = conn.prepareStatement(sql);  
////			 pstmt.setInt(1, x);
//			 pstmt.executeUpdate();  
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//		
//		sparkMethods methods = new sparkMethods();
//		 
//		 //configure port
//		 port(8087);
//		 
//		 get("/hello", (req, res) -> "Hello, world!");
//		 
//		 get("/hello/:name", (req,res)->{
//		            return "Hello, "+ req.params(":name") + "!!";
//		        });
//		 post("/users/add", (req,res)-> {
//			 
//			 res.type("application/json");
//			 
//			 User user = new Gson().fromJson(req.body(), User.class);
//			 methods.addUser(user);
//			 
//			 return new Gson().toJson(user);
//			});
//			get("/users", (req,res)-> {
//				 res.type("application/json");
//				 return new Gson().toJson(methods.getUsers());
//				});
//			
//			put("/users/edit/:id", (req,res) -> {
//				 res.type("application/json");
//				 User ue = new Gson().fromJson(req.body(), User.class);
//				 
//				 User searchedUser = methods.getUserById(ue.getId());
//				 if(searchedUser != null) {
//				 methods.editUser(ue);
//				 return new Gson().toJson(ue);
//				 } else {
//				 return new Gson().toJson("User not found or error in edit");
//				 }
//				 
//				});
//			delete("/users/delete/:id", (req,res) -> {
//				 res.type("application/json");
//				methods.deleteUserById(req.params(":id"));
//				 return new Gson().toJson("User deleted");
//				});

		 }

	
		
}
