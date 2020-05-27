package ar.edu.unlp.info.bd2.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

public class Row implements PersistentObject {
	@BsonId
	private ObjectId objectId;
	private Long cant;
	private Product product;
//PORQUE NUNCA SE USABA?!
	private Order order;

	public Row(Product product, Long quantity, Order order) {
		this.cant=quantity;
		this.product=product;
		this.order=order;
	}
	
	public Row() {
		
	}
	
	public Long getCant() {
		return cant;
	}

	public void setCant(Long cant) {
		this.cant = cant;
	}
	
	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
