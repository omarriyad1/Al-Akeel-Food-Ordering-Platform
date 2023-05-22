package application.models;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Receipt {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	private double totalrecieptvalue;
	
	private String restaurantname="AlAkeel Food Servives";
	
	private String date;

	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public double getTotalrecieptvalue() {
		return totalrecieptvalue;
	}
	public void setTotalrecieptvalue(double totalrecieptvalue) {
		this.totalrecieptvalue = totalrecieptvalue;
	}
	
	public String getRestaurantname() {
		return restaurantname;
	}
	public void setRestaurantname(String restaurantname) {
		this.restaurantname = restaurantname;
	}
	public String getDate() {
		return date;
	}
	public void setDate() {
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        date = currentDateTime.format(formatter);
	
	}
	
	
}
