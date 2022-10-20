package microservice;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class User {
	
	 
	public User(String string, String string2, String string3) {
		id = string;
		name = string2;
	email = string3;
	}
	private String id;
	 private String name;
	 private String email;
	public String getId() {
	
		return id;
	}
	

}
