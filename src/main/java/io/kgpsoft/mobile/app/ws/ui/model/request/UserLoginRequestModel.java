package io.kgpsoft.mobile.app.ws.ui.model.request;

public class UserLoginRequestModel {
	
	private String email;
	private String password;
	
	public UserLoginRequestModel(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public UserLoginRequestModel() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
