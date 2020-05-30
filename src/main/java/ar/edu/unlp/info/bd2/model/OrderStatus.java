package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

@BsonDiscriminator
public abstract class OrderStatus implements PersistentObject {
	@BsonId
	protected ObjectId objectId;
	protected Date date;
//    protected Order order;
	
//	public Order getOrder() {
//		return order;
//	}
//
//	public void setOrder(Order order) {
//		this.order = order;
//	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	public OrderStatus() {
		
	}
	
	public String getStatus() {
		return null;
	}
	
	public void cancel(Order o) {
		
	}

	public void deliver(Order o) {
			
	}
	
	public void send(Order o) {
		
	}

	public void finish(Order o) {
		
	}

	public Boolean canCancel(Order o) {
		return false;
	}
	
	public Boolean canFinish(Order o) {
		return false;
	}
	
	public Boolean canDeliver(Order o) {
		return false;
	}

	public void send(Order order2, Date date2) {
		// TODO Auto-generated method stub
		
	}

	public void cancel(Order order2, Date date2) {
		// TODO Auto-generated method stub
		
	}

	public void finish(Order order2, Date date2) {
		// TODO Auto-generated method stub
		
	}
	
}