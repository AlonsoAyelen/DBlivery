package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;

public class Supplier {

	private String name;
	private String cuil;
	private String address;
	private Float coordX;
	private Float coordY;
	private ArrayList<Product> products = new ArrayList<>();
	
	
	public Supplier(String n,String c,String a,Float x,Float y) {   //constructor
		name = n;
		cuil = c;
		address = a;
		coordX = x;
		coordY = y;
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
