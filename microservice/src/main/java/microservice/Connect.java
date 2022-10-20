package microservice;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;  
   
public class Connect {  
     /** 
     * Connect to a sample database 
     */  
    public static Connection conn() {  
        Connection conn = null;  
        try {  
            // db parameters  
            String url = "jdbc:sqlite:C:/sqlite/gui/SQLiteStudio/microservicedb.db";  
            // create a connection to the database  
            conn = DriverManager.getConnection(url);  
              
            System.out.println("Connection to SQLite has been established.");  
            return conn;
              
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } finally {  
            try {  
                if (conn != null) {  
                    conn.close();  
                }  
            } catch (SQLException ex) {  
                System.out.println(ex.getMessage());  
            }  
        }
        return conn;
    }  
 
}  