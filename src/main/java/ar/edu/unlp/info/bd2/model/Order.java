package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

public class Order implements PersistentObject {
	@BsonId
	private ObjectId objectId;
	private User deliveryUser;
	private User client;
	private Date dateOfOrder;
	private String address;
	private Float coordX;
	private Float coordY;
	private Point position;
	private OrderStatus statusActual;
	private List<Row> products = new ArrayList<>();
	private List<OrderStatus> status = new ArrayList<>();
	
	public Order(){
		
	}

	public Order(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		this.dateOfOrder=dateOfOrder;
		this.address=address;
		this.coordX=coordX;
		this.coordY=coordY;
		this.client=client;
		this.objectId= new ObjectId();
		Position pos = new Position(coordX, coordY);
		this.setPosition(new Point(pos));
		Pending p = new Pending(this,dateOfOrder);
		this.status.add(p);
		this.statusActual = p;
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

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
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

	public void setProducts(List<Row> products) {
		this.products = products;
	}

	public List<OrderStatus> getStatus() {
		return status;
	}

	public void setStatus(ArrayList<OrderStatus> s) {
		this.status=s;
	}
	
	public void addStatus(OrderStatus status) {
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

	//@BsonIgnore
	public OrderStatus getActualStatus() {
		OrderStatus act= this.getStatus().get(0);
//		System.out.println(act.getObjectId());
//		for (OrderStatus os :this.getStatus()) {
//			if(os.getDate().after(act.getDate())) act=os;
//		}
		for (OrderStatus os :this.getStatus()) {
			if(os.getObjectId().compareTo(act.getObjectId())>0) act=os;
//			System.out.println(os.getObjectId());
		}
		return act;
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

	public Float getAmount() {
		Float amount=0F;
		for (Row r : this.getProducts()) {
			amount= amount+ (r.getProduct().getPriceInDate(this.getDateOfOrder())*r.getCant());
        }
		return amount;
	}

	public void send(User deliveryUser2, Date date) {
		if (this.canDeliver()) {
			this.setDeliveryUser(deliveryUser2);
			this.getActualStatus().send(this,date);	
		}
	}

	public void cancel(Date date) {
		if (this.canCancel()) {
			this.getActualStatus().cancel(this,date);	
		}
	}

	public void finish(Date date) {
		if (this.canFinish()) {
			this.getActualStatus().finish(this,date);
		}
	}

	public OrderStatus getStatusActual() {
		return statusActual;
	}

	public void setStatusActual(OrderStatus statusActual) {
		this.statusActual = statusActual;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

}
