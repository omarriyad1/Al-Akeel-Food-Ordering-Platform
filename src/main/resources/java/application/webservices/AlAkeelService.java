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
	private User CurrentUser=new User();
	
	
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/SignUp")
	  @PermitAll
	public User SignUp(User Cal) {
		  
			
			EM.persist(Cal);
			return Cal;
		}
	  @GET
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/SignIn")
	  @RolesAllowed("customer")
	public String SignIn(Credentials Cal) {
		  List<User>Users=new ArrayList<>();
		  Query query=EM.createQuery("SELECT e from User e where e.ID = :id");
		  query.setParameter("id", Cal.getID());
		  Users=query.getResultList(); 
			
		  if (Cal.getID()==Users.get(0).getID() && Cal.getPassword().equals(Users.get(0).getPassword()))
			{
				CurrentUser=Users.get(0);
				return "SignIn Sucessfully";
			}
				
				
			return "Invalid Credentials";
			
			
		}
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Runner")
	  //@PermitAll
	public Runner Runner(Runner Cal) {
			
			EM.persist(Cal);
			return Cal;
		}
	  
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/RestOwner")
	  //@RolesAllowed("restaurantOwner")
	public RestOwner CreateRestaurantOwner(RestOwner Cal) {
			EM.persist(Cal);
			return Cal;
		}
	  
	  
	  //Done
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Meal")
	  @RolesAllowed("restaurantOwner")
	public Meal CreateMeal(Meal Cal) {
			String Role="restaurantOwner";
			List<Restaurant>Restaurants=new ArrayList<>();
			
			
			Query query2=EM.createQuery("SELECT r from Restaurant r where r.ID = :id");
			query2.setParameter("id", Cal.getRestaurantId());
			Restaurants= query2.getResultList(); 
			
		
			
			if (CurrentUser.getRole().equals(Role) && Restaurants.get(0).getOwnerId()==CurrentUser.getID())
			{
				EM.persist(Cal);
				return Cal;
			}
			return null;
			
			
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
	  //@RolesAllowed("customer")
	public type2  Orders(type T){
		  String Role="customer";
		  
		  if (CurrentUser.getRole().equals(Role))
		  {
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
			  	MyOrder.setCustomerId(CurrentUser.getID());
				Orders MyOrder1=new Orders();
				MyOrder1=getReceipt(MyOrder);
				
				
				
				EM.persist(MyOrder1);
			
				type2 ty=new type2(MyOrder1.getReceipt(),MyOrder1.getMeals());
				return ty ;
		  }
		  return null;
		}
	  

	 
	  //DONE
	  @POST
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Restaurant")
	//@RolesAllowed("restaurantOwner")
	public Restaurant CreateRestaurant(Restaurant Cal) {
			String Role="restaurantOwner";
			if (CurrentUser.getRole().equals(Role) && Cal.getOwnerId()==CurrentUser.getID())
			{
				EM.persist(Cal);
				return Cal;
			}
			return null;
		}
	  
	  	
	  //Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/List")
	//@RolesAllowed("customer")
	public List<Restaurant> ListRestaurants() {
		  String Role="customer";
			if (CurrentUser.getRole().equals(Role) )
			{
				return EM.createQuery("SELECT Res FROM Restaurant Res ", Restaurant.class).getResultList();
			}
			return null;
		  
					
	  }
	  
	  
	  
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Details/{id}")
	//@RolesAllowed("restaurantOwner")

	public Object GetRestaurantDetailsById(@PathParam("id") int id ) {
		  List<Restaurant>M=new ArrayList<>();
		  
		  String Role="restaurantOwner";
			if (CurrentUser.getRole().equals(Role)  )
			{
				  Query query=EM.createQuery("SELECT e from Restaurant e where e.ID =:sal");
				  query.setParameter("sal", id);
				  M=query.getResultList();
				  if (M.get(0).getOwnerId()==CurrentUser.getID()) {
					  return query.getSingleResult();  
				  }
			}
			return null;
		  
		  
		  
		
		 	 
			
	  }
	  
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Menu/{id}")
	//@RolesAllowed("customer")

	public List<Meal> GetRestaurantMenu(@PathParam("id") int id ) {
		  if (CurrentUser.getRole()!=null)
		  {
			  Query query=EM.createQuery("SELECT e from Meal e where e.restaurantId =:sal");
			  query.setParameter("sal", id);
			  return query.getResultList(); 
			 	 
		  }
		  return null;
			
	  }
	    
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/Report/{id}")
	//@RolesAllowed("restaurantOwner")
	public Object GetReportForRestaurantById(@PathParam("id") int id ) {
		  List<Restaurant>M=new ArrayList<>();
		  
		  String Role="restaurantOwner";
			if (CurrentUser.getRole().equals(Role)  )
			{
				  Query query=EM.createQuery("SELECT e from Restaurant e where e.ID =:sal");
				  query.setParameter("sal", id);
				  M=query.getResultList();
				  if (M.get(0).getOwnerId()==CurrentUser.getID()) {
					  return query.getSingleResult();  
				  }
			}
			return null;
	  
		 
	  }
	  
	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/EditMenu/{id}")
	//@RolesAllowed("restaurantOwner")
	public String EditMenu(@PathParam("id")int id ) {
		  
		  

		  String Role="restaurantOwner";
			if (CurrentUser.getRole().equals(Role)  )
			{
				List<Restaurant>M=new ArrayList<>();
				  Query query=EM.createQuery("SELECT e from Restaurant e where e.ID =:sal");
				  query.setParameter("sal", id);
				  M=query.getResultList();
				  if (M.get(0).getOwnerId()==CurrentUser.getID()) {
					  Query query2=EM.createQuery("SELECT e from Meal e where e.ID =:sal");
					  query2.setParameter("sal", id);
					  List<Meal> R=query2.getResultList(); 
					  
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
			}
			return null;
		  
		  
		  
			
	  }
	  
	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/EditOrder/{id}")
	//@RolesAllowed("customer")
	public String EditOrder(@PathParam("id")int id ) {

		  String Role = "customer";
		  if (CurrentUser.getRole().equals(Role)) {
			  Query query = EM.createQuery("SELECT e from Orders e where e.ID =:sal");
			  query.setParameter("sal", id);
			  List<Orders> R = query.getResultList();


			  for (Orders i : R) {
				  if (i.getID() == id) {
					  i.setCanceled();
					  EM.merge(i);

					  break;
				  }
			  }
			  String Message = "Sucessfully Cancelled Order with ID :" + id + " Menu";
			  return Message;
		  }

return null;
	  }
	//Done
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/StatusOfRunner/{id}")
	//@RolesAllowed("restaurantOwner","runner")
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
	//@RolesAllowed("restaurantOwner","runner")
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
	//@RolesAllowed("customer")
	public Orders EditOrder(@PathParam("id")int id ,@PathParam("MID")int MID ) {

		  String Role="customer";

		  if (CurrentUser.getRole().equals(Role))
		  {
			  List<Orders> R=new ArrayList<>();
			  Query query=EM.createQuery("SELECT e from Orders e where e.ID = :sal");
			  query.setParameter("sal", id);
			  R=query.getResultList();

			  List<Meal> OrderedMeals=new ArrayList<>();
			  OrderedMeals=R.get(0).getMeals();
			  
			  

			  for (Orders i:R)
			  {
				  if (i.getID()==id)
				  {
					  for (Meal m:OrderedMeals)
					  {
						  if(m.getID()==MID ) {
							  OrderedMeals.remove(m);
							  i.setMeals(OrderedMeals);
							  i.setTotalPrice(-m.getPrice());
							  Receipt d = i.getReceipt();
							  d.setTotalrecieptvalue(i.getReceipt().getTotalrecieptvalue() - m.getPrice());
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
	//@RolesAllowed("customer")
	public Orders EditOrderAddMeal(@PathParam("id")int id ,@PathParam("MID")int MID ) {
		   String Role="customer";

		   if (CurrentUser.getRole().equals(Role))
		   {
			   Query query=EM.createQuery("SELECT e from Orders e where e.ID = :sal");
			   query.setParameter("sal", id);
			   List<Orders> R=query.getResultList();

			   List<Meal> OrderedMeals=query.getResultList();

			   Query query2=EM.createQuery("SELECT e from Meal e where e.ID = :sall");
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
							   i.setTotalPrice(m.getPrice());
							   Receipt d=i.getReceipt();
							   i.setMeals(OrderedMeals);
							   d.setTotalrecieptvalue(i.getReceipt().getTotalrecieptvalue()+m.getPrice());
							   i.setReceipt(d);
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
	  @SuppressWarnings("unchecked")
	  @GET
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/ListAllOrdersForRest/{id}")
	//@RolesAllowed("restaurantOwner")
	public List<type2> ListAllOrdersForRestaurantById(@PathParam("id")int id  ) {

		  String Role = "restaurantOwner";
		  if (CurrentUser.getRole().equals(Role)) {

			  List<type2> Orders = new ArrayList<>();
			  List<Orders> Orderss = new ArrayList<>();

			  Query query = EM.createQuery("SELECT e from Orders e where e.restaurantId = :sal");
			  query.setParameter("sal", id);
			  Orderss = query.getResultList();

			  for (Orders i : Orderss) {
				  type2 t = new type2(i.getReceipt(), i.getMeals());
				  Orders.add(t);
			  }

			  return Orders;
		  }
		  return null;
	  }


	//Done
	  @PUT
	  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	  @Path("/AssignDelivered/{RID}/{id}")
	//@RolesAllowed("runner")
	public String MarkOrderAsDelivered(@PathParam("id")int id,@PathParam("RID")int RID  ) {

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
					 Ru.getDeliveryFees();
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
