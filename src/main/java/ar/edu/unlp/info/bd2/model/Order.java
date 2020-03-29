package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;

public class Order {
	//private Product product;
	private long id;
	private User deliveryUser;
	private User client;
	private OrderStatus state;
	private ArrayList<Row> products = new ArrayList<>();
	private ArrayList<OrderStatus> status = new ArrayList<>();
	
	public Order(){
		
	}
	
	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	public User getDeliveryUser() {
		return deliveryUser;
	}

	public void setDeliveryUser(User deliveryUser) {
		this.deliveryUser = deliveryUser;
	}

	private Integer cant;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	
	public Order(Product p, Integer c) {   //constructor
		cant = c;
		//product = p;
		state = new Pending();
	}
	
//	public Product getProduct() {
//		return product;
//	}
//	public void setProduct(Product product) {
//		this.product = product;
//	}
	public Integer getCant() {
		return cant;
	}
	public void setCant(Integer cant) {
		this.cant = cant;
	}
	public OrderStatus getState() {
		return state;
	}
	public void setState(OrderStatus state) {
		this.state = state;
	}

	public ArrayList<Row> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Row> products) {
		this.products = products;
	}

	public ArrayList<OrderStatus> getStatus() {
		return status;
	}

	public void setStatus(ArrayList<OrderStatus> status) {
		this.status = status;
	}
}
