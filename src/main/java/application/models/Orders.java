package application.models;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class Orders {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID ;
	private int restaurantId;
	
	public int getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<Meal>meals=new ArrayList<>();
	 
	private String status="PREPARING";
	private double totalPrice=0;
	
	@ManyToOne
	private Runner runner;
	
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "receipt", unique = true, updatable = false)
	private Receipt receipt;	
	
	public Receipt getReceipt() {
		return receipt;
	}
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double Price) {
		this.totalPrice += Price;
	}
	public Runner getRunner() {
		return runner;
	}
	public void setRunner(Runner runner) {
		this.runner = runner;
	}
	
	public String getStatus() {
		return status;
	}
	public void setDelivered() {
		this.status = "DELIVERED";
	}
	public void setCanceled() {
		this.status = "CANCELED";
	}
	public List<Meal> getMeals() {
		return meals;
	}
	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}
	
	
	
}
