package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
import javax.persistence.*;

//import java.util.ArrayList; // import the ArrayList class
@Entity
@Table(name="products")
public class Product {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="name")
	private String name;
	@Column(name="supplier")
	private Supplier supplier;
	@Column(name="price")
	private Float price;
	@Column(name="weight")
	private Float weight;
	private ArrayList<Price> prices = new ArrayList<>();
	
	public Product (String name, Float price, Float weight, Supplier supplier) {
		this.name=name;
		this.price=price;
		this.weight=weight;
		this.supplier=supplier;
	}
	
	public Product() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Price> getPrices() {
		return prices;
	}
	public void setPrices(ArrayList<Price> prices) {
		this.prices = prices;
	}

	
	// HISTORIAL	private ArrayList<String> historic = new ArrayList<String>();
 
}
