package application.models;





import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Restaurant {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	private String name;
	private int ownerId;
	private int numberOfCompletedOrders=0;
	private double totalEarns=0;
	
	public int getNumberOfCompletedOrders() {
		return numberOfCompletedOrders;
	}

	public void setNumberOfCompletedOrders() {
		this.numberOfCompletedOrders ++;
	}

	public double getTotalEarns() {
		return totalEarns;
	}

	public void setTotalEarns(double Earn) {
		this.totalEarns+=Earn;
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

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

}
