package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

public class Price implements PersistentObject {
	@BsonProperty("objectId")
	@BsonId
	private ObjectId objectId;
	private Date startDate;
	private Date finishDate;
	private Float price;
	
	public Price() {
		
	}
	

	public Price(Date start, Date finish, Float price){
		this.startDate=start;
		this.finishDate=finish;
		this.price=price;
		
	}

	public Price(Date start, Float price){
		this.startDate=start;
		this.price=price;
		
	}
	
	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
}
