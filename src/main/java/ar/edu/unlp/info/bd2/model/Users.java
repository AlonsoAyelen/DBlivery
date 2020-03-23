package ar.edu.unlp.info.bd2.model;

import java.util.Date;

public class Users {
	private String username;
	private String password;
	private String name;
	private String email;
	private Date birth;
	
	public Users(String u,String p,String n,String e,Date b) {   //constructor
		username = u;
		password = p;
		name = n;
		email = e;
		birth = b;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
}