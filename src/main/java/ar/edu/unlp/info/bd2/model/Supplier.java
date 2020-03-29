package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
//import java.util.Date;

//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="supplier")
public class Supplier {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="name")
	private String name;
	@Column(name="cuil")
	private String cuil;
	@Column(name="address")
	private String address;
	@Column(name="coordx")
	private Float coordX;
	@Column(name="coordY")
	private Float coordY;
	//@OneToMany(mappedBy="supplier")
	private ArrayList<Product> products;
	/*
	 * cascade = CascadeType.ALL, fetch = FetchType.LAZY
	 * @JoinColumn(name = "supplier", referencedColumnName = "supplier_id")
	 * orphanRemoval
	 * */
	
	public Supplier() {

	}
	
	public Supplier(String n,String c,String a,Float x,Float y) {   //constructor
		id=1;
		name = n;
		cuil = c;
		address = a;
		coordX = x;
		coordY = y;
		products=new ArrayList<Product>();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCuil() {
		return cuil;
	}
	public void setCuil(String cuil) {
		this.cuil = cuil;
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

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}



}
