package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class Order {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	//@Column(name="dalivery_user")
	@ManyToOne()
	private User deliveryUser;
	@ManyToOne()
	private User client;
	@Column(name="date")
	private Date dateOfOrder;
	@Column(name="address")
	private String address;
	@Column(name="coordx")
	private Float coordX;
	@Column(name="coordy")
	private Float coordY;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Row> products = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderStatus> status = new ArrayList<>();
	
	public Order(){
		
	}

	public Order(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		this.dateOfOrder=dateOfOrder;
		this.address=address;
		this.coordX=coordX;
		this.coordY=coordY;
		this.client=client;
		Pending p = new Pending();
		this.status.add(p);
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(Date dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public List<Row> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Row> products) {
		this.products = products;
	}

	public List<OrderStatus> getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status.add(status);
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getCoordX() {
		return coordX;
	}

	public void setCoordX(Float coordX) {
		this.coordX = coordX;
	}

	public Float getCoordY() {
		return coordY;
	}

	public void setCoordY(Float coordY) {
		this.coordY = coordY;
	}

	public void addProduct(Row r) {
		this.getProducts().add(r);
	}

	public boolean canCancel() {
		return this.getActualStatus().canCancel(this);
	}
	
	public boolean canFinish() {
		return this.getActualStatus().canFinish(this);
	}
	
	public boolean canDeliver() {
		return this.getActualStatus().canDeliver(this);
	}
	
	public OrderStatus getActualStatus() {
		return this.getStatus().get(this.getStatus().size() - 1 );
	}
	
	public void send(User deilvery) {
		if (this.canDeliver()) {
			this.setDeliveryUser(deilvery);
			this.getActualStatus().send(this);	
		}
	}
	
	public void cancel() {
		if (this.canCancel()) {
			this.getActualStatus().cancel(this);	
		}
	}
	
	public void finish() {
		if (this.canFinish()) {
			this.getActualStatus().finish(this);	
		}
	}

}
