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
	private int NumOfDeliverys=0;	
	
	
	
	public int getCounter() {
		return NumOfDeliverys/2;
	}
	public void setCounter(int NumOfDeliverys) {
		this.NumOfDeliverys = NumOfDeliverys;
	}
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
		NumOfDeliverys++;
		return deliveryFees;
	}
	public void setDeliveryFees(double deliveryFees) {
		this.deliveryFees = deliveryFees;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}
