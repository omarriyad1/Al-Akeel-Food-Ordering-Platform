package application.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




@Entity
public class Runner {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	private String name;
	private double deliveryFees;
	private boolean available=true;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDeliveryFees() {
		return deliveryFees;
	}
	public void setDeliveryFees(double deliveryFees) {
		this.deliveryFees = deliveryFees;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		available = available;
	}
	
}
