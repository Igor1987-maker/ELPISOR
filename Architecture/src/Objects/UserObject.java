package Objects;

public class UserObject extends BasicObject {
	private String userName;
	private String password;
	private String firstName;
	private String lastname;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}
