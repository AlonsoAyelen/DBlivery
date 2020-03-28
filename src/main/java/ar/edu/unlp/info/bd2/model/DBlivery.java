package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;

public class DBlivery {
	private long id;
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Supplier> suppliers = new ArrayList<>();
	

	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	public ArrayList<Supplier> getSuppliers() {
		return suppliers;
	}
	public void setSuppliers(ArrayList<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
