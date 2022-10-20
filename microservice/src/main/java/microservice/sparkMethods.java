package microservice;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import microservice.User;
import java.util.ArrayList;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  


public class sparkMethods implements UserService{
	
	
	//Local datastore
	private static Map<String, User> users = new HashMap<String, User>();
	
	public sparkMethods() {
		users.put("101", new User("101","Pavan Solapure","pavan.solapure@gmail.com"));
		users.put("102", new User("102","Aadya Solapure","aadya.solapure@gmail.com"));
		users.put("103", new User("103","Aaarna Solapure","aarna.solapure@gmail.com"));
		users.put("104", new User("104","Shilpa Solapure","shilpa.solapure@gmail.com"));
	}
	
	@Override
	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	@Override
	public void editUser(User user) {
		users.replace(user.getId(), user);
	}

	@Override
	public User getUserById(String id) {
		return users.get(id);
	}

	@Override
	public void deleteUserById(String id) {
		users.remove(id);		
	}

	@Override
	public List<User> getUsers() {
		return new ArrayList<User>(users.values());
	}

}
