package application.webservices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import application.models.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.security.*;


@Stateless
@Path("/")
@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
public class AlAkeelService {
	@PersistenceContext
    private EntityManager EM;
	
	
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/User")
	  @PermitAll
	public User User(User Cal) {
			
			EM.persist(Cal);
			return Cal;
		}
	  
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Runner")
	  @PermitAll
	public Runner Runner(Runner Cal) {
			
			EM.persist(Cal);
			return Cal;
		}
	  
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/RestOwner")
	  @RolesAllowed("restaurantOwner")

	public RestOwner CreateRestaurantOwner(RestOwner Cal) {
			EM.persist(Cal);
			return Cal;
		}
	  
	  
	  //Done
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Meal")
	  //@RolesAllowed("restaurantOwner")
	public Meal CreateMeal(Meal Cal) {
			
			EM.persist(Cal);
			return Cal;
		}
	  
	  
	  
	//Setting Receipt
	  public Orders getReceipt(Orders O) {
			Receipt R=new Receipt();
			R.getRestaurantname();
			R.setDate();
			R.getDate();
			R.setTotalrecieptvalue(O.getTotalPrice()+O.getRunner().getDeliveryFees());
			R.getTotalrecieptvalue();
			O.setReceipt(R);
			return O;
		}
	
	  //DONE
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Orders")
	public type2  Orders(type T){
		  List <Meal>OrderList=new ArrayList<>();
		  List <Meal>AllMeals=new ArrayList<>();
		  int RID = 0;
		
		  Query query=EM.createQuery("SELECT e from Meal e ");
		  AllMeals=query.getResultList(); 
		  
		  
		  
 		  double TotalPrice = 0;
 	
		  	for (Meal i : AllMeals)
			 {
				for (Integer j: T.L )
				 {
					 if (j==i.getID())
					 {
						 OrderList.add(i);
						 TotalPrice+=i.getPrice();	
						 RID=i.getRestaurantId();
					 }
				 }
			 }
			//Assign Free Order to a runner 
		  	List<Runner>Run=new ArrayList<>();
		  	Runner R=new Runner ();
		  	Query query2=EM.createQuery("SELECT r from Runner r where r.available = true");
		    Run= query2.getResultList(); 
		    
		    for (Runner x : Run)
			 {
				 if (x.isAvailable())
				 {
					 R=x;
					 R.setAvailable(false);
					 
					 EM.merge(R);
					 break;
				 }
			 }
		    
		    

		  	Orders MyOrder=new Orders();
		  	MyOrder.setMeals(OrderList);
		  	MyOrder.setTotalPrice(TotalPrice);
		  	MyOrder.setRestaurantId(RID);
		  	MyOrder.setRunner(R);
			Orders MyOrder1=getReceipt(MyOrder);
			
			
			EM.persist(MyOrder1);
		
			type2 ty=new type2(MyOrder1.getReceipt(),MyOrder1.getMeals());
			return ty ;
		}
	  

	 
	  //DONE
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Restaurant")
	public Restaurant CreateRestaurant(Restaurant Cal) {
			
			EM.persist(Cal);
			return Cal;
		}
	  
	  	
	  //Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/List")
	public List<Restaurant> ListRestaurants() {
		  
		  return EM.createQuery("SELECT Res FROM Restaurant Res ", Restaurant.class).getResultList();
					
	  }
	  
	  
	  
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Details/{id}")
	public Object GetRestaurantDetailsById(@PathParam("id") int id ) {
		  List<Restaurant>M=new ArrayList<>();
		  
		  Query query=EM.createQuery("SELECT e from Restaurant e where e.ID =:sal");
		  query.setParameter("sal", id);
		  return query.getSingleResult(); 
		 	 
			
	  }
	  
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Menu/{id}")
	public List<Meal> GetRestaurantMenu(@PathParam("id") int id ) {
		  
		  Query query=EM.createQuery("SELECT e from Meal e where e.restaurantId =:sal");
		  query.setParameter("sal", id);
		  return query.getResultList(); 
		 	 
			
	  }
	    
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Report/{id}")
	public Object GetReportForRestaurantById(@PathParam("id") int id ) {
		   
		  
		  
		  Query query=EM.createQuery("SELECT e from Restaurant e where e.ID =:sal");
		  query.setParameter("sal", id);
		  return query.getResultList(); 
		  
		  
		  
		 
		 	 
			
	  }
	  
	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/EditMenu/{id}")
	public String EditMenu(@PathParam("id")int id ) {
		   
		  
		  
		  Query query=EM.createQuery("SELECT e from Meal e where e.ID =:sal");
		  query.setParameter("sal", id);
		  List<Meal> R=query.getResultList(); 
		  
		  for (Meal i:R)
		  {
			  if (i.getID()==id)
			  {
				  EM.remove(i);
				  break;
			  }
		  }
		  String Message="Sucessfully Deleted Item with ID :"+id+" Menu";
		 return Message;
		 	 
			
	  }
	  
	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/EditOrder/{id}")
	public String EditOrder(@PathParam("id")int id ) {
		   
		  
		  Query query=EM.createQuery("SELECT e from Orders e where e.ID =:sal");
		  query.setParameter("sal", id);
		  List<Orders> R=query.getResultList();
		  
		  
		  
		  for (Orders i:R)
		  {
			  if (i.getID()==id)
			  {
				  i.setCanceled();
				  EM.merge(i);

				  break;
			  }
		  }
		 String Message="Sucessfully Cancelled Order with ID :"+id+" Menu";
		 return Message;
		 	 
			
	  }
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/StatusOfRunner/{id}")
	public Object GetStatusOfRunner(@PathParam("id") int id ) {
		  
		  Query query=EM.createQuery("SELECT e from Runner e where e.ID =:sal");
		  query.setParameter("sal", id);
		  return query.getResultList(); 
		 	 
			
	  }
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/OrdersOfRunner/{id}")
	public Object OrdersOfRunner(@PathParam("id") int id ) {
		  
		  Query query=EM.createQuery("SELECT e from Runner e where e.ID =:sal");
		  query.setParameter("sal", id);
		  return query.getResultList(); 
		 	 
			
	  }
	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/EditOrderRemoveMeal/{id}/{MID}")
	public Orders EditOrder(@PathParam("id")int id ,@PathParam("MID")int MID ) {
		   
		  
		  Query query=EM.createQuery("SELECT e from Orders e where e.ID =:sal");
		  query.setParameter("sal", id);
		  List<Orders> R=query.getResultList(); 
		  List<Meal> OrderedMeals=query.getResultList(); 
		  
		  for (Orders i:R)
		  {
			  if (i.getID()==id)
			  {
				  OrderedMeals= i.getMeals();
				  for (Meal m:OrderedMeals)
				  {
					  if(m.getID()==MID) {
						  if (i.getStatus()=="PREPARING")
						  {
							  OrderedMeals.remove(m);
							  EM.merge(i);
							  return i;
						  }
					  }
				  }
			  }
		  }
		  return null;
		 
		 	 
			
	  }
	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/EditOrderAddMeal/{id}/{MID}")
	public Orders EditOrderAddMeal(@PathParam("id")int id ,@PathParam("MID")int MID ) {
		   
		  
		  Query query=EM.createQuery("SELECT e from Orders e where e.ID =:sal");
		  query.setParameter("sal", id);
		  List<Orders> R=query.getResultList();
		  
		  List<Meal> OrderedMeals=query.getResultList(); 
		  
		  Query query2=EM.createQuery("SELECT e from Meal e where e.ID =:sall");
		  query2.setParameter("sall", MID);
		  List<Meal> AllMeals=query2.getResultList();
		  
		  for (Orders i:R)
		  {
			  if (i.getID()==id)
			  {
				  for (Meal m:AllMeals)
				  {
					  if(m.getID()==MID) {
							  OrderedMeals.add(m);
							  EM.merge(i);
							  return i;
						  
					  }
				  }
			  }
		  }
		  return null;
		 
		 	 
			
	  }
	//Done
	  @SuppressWarnings("unchecked")
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/ListAllOrdersForRest/{id}")
	public List<type2> ListAllOrdersForRestaurantById(@PathParam("id")int id  ) {
		  List<type2>Orders=new ArrayList<>();
		  List<Orders>Orderss=new ArrayList<>();
		  
		  Query query=EM.createQuery("SELECT e from Orders e where e.restaurantId = :sal");
		  query.setParameter("sal", id);
		  Orderss=query.getResultList();
		  
		  for (Orders i:Orderss)
		  {
			  type2 t=new type2(i.getReceipt(),i.getMeals());
			  Orders.add(t);
		  }
		  
		  return Orders;
		  
		  
		  
		  
		  	
	  }
	  	
	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/AssignDelivered/{RID}/{id}")
	public String MarkOrderAsDelivered(@PathParam("id")int id,@PathParam("RID")int RID  ) {
		   
		  
		  /*Query query = EM.createQuery("UPDATE Orders m SET m.status = DELIVERED where m.ID = :mealId");
		  query.setParameter("mealId", id);	*/
		//Assign Order as delivered 
		  	List<Orders>Order=new ArrayList<>();
		  	Orders R=new Orders ();
		  	Query query=EM.createQuery("SELECT r from Orders r where r.ID = :id");
		  	query.setParameter("id", id);
		    Order= query.getResultList(); 
		    
		    for (Orders x : Order)
			 {
				 if (x.getID()==id)
				 {
					 R=x;
					 R.setDelivered();
					 EM.merge(R);
					 break;
				 }
			 }
		  
		 
		//Assign Runner as available
		  	List<Runner>Run=new ArrayList<>();
		  	Runner Ru=new Runner ();
		  	Query query2=EM.createQuery("SELECT r from Runner r where r.ID = :RID");
		  	query2.setParameter("RID", RID);
		    Run= query2.getResultList(); 
		    
		    for (Runner x : Run)
			 {
				 if (x.getID()==RID)
				 {
					 Ru=x;
					 Ru.setAvailable(true);
					 
					 EM.merge(Ru);
					 break;
				 }
			 }
		  
		  //Assign earns and delivered ordered for a restaurant
		    List<Restaurant>Rest=new ArrayList<>();
		  	Restaurant Rs=new Restaurant ();
		  	Query query4=EM.createQuery("SELECT r from Restaurant r where r.ID = :id");
		  	query4.setParameter("id", R.getRestaurantId());
		    Rest= query4.getResultList(); 
		    
		    for (Restaurant x : Rest)
			 {
				 if (x.getID()==R.getID())
				 {
					 Rs=x;
					 Rs.setNumberOfCompletedOrders();
					 Rs.setTotalEarns(R.getTotalPrice());
					 
					 EM.merge(Rs);
					 break;
				 }
			 }
		  
		
		  
		  return "Delivered Successfully";
	  }
	

}
