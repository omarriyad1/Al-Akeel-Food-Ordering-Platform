package application.models;

import java.util.ArrayList;
import java.util.List;

public class type2 {
	public Receipt Receipt=new Receipt();
	public List<Meal>Meals=new ArrayList<>();
	
	public type2(Receipt r,List<Meal>Mo)
	{
		this.Receipt=r;
		this.Meals=Mo;
	}
	

}
