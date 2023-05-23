package application.models;


public class Credentials {
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;
	public void setID(int iD) {
		this.id = iD;
	}
	public int id;
	public int getID() {
		return id;
	}

}
