package ar.edu.unlp.info.bd2.model;

public class Order {
	private Product product;
	private Integer cant;
	private OrderStatus state;
	
	
	
	public Order(Product p, Integer c) {   //constructor
		cant = c;
		product = p;
		state = new Pending();
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
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
}
