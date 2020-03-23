package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;

public class DBlivery {
	private ArrayList<Users> users = new ArrayList<>();
	private ArrayList<Supplier> suppliers = new ArrayList<>();
	private ArrayList<DeliveryMan> deliveryMans = new ArrayList<>();
	
	public ArrayList<Users> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<Users> users) {
		this.users = users;
	}
	public ArrayList<Supplier> getSuppliers() {
		return suppliers;
	}
	public void setSuppliers(ArrayList<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
	public ArrayList<DeliveryMan> getDeliveryMans() {
		return deliveryMans;
	}
	public void setDeliveryMans(ArrayList<DeliveryMan> deliveryMans) {
		this.deliveryMans = deliveryMans;
	}
}