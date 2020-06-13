package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

public class DBliveryMongoRepository {

    @Autowired private MongoClient client;


    public void saveAssociation(PersistentObject source, PersistentObject destination, String associationName) {
        Association association = new Association(source.getObjectId(), destination.getObjectId());
        this.getDb()
                .getCollection(associationName, Association.class)
                .insertOne(association);
    }

    public MongoDatabase getDb() {
        return this.client.getDatabase("bd2_grupo2");
    }

    public <T extends PersistentObject> List<T> getAssociatedObjects(
            PersistentObject source, Class<T> objectClass, String association, String destCollection) {
        AggregateIterable<T> iterable =
                this.getDb()
                        .getCollection(association, objectClass)
                        .aggregate(
                                Arrays.asList(
                                        match(eq("source", source.getObjectId())),
                                        lookup(destCollection, "destination", "_id", "_matches"),
                                        unwind("$_matches"),
                                        replaceRoot("$_matches")));
        Stream<T> stream =
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }
    
    public <T extends PersistentObject> List<T> getObjectsAssociatedWith(
            ObjectId objectId, Class<T> objectClass, String association, String destCollection) {
        AggregateIterable<T> iterable =
                this.getDb()
                        .getCollection(association, objectClass)
                        .aggregate(
                                Arrays.asList(
                                        match(eq("destination", objectId)),
                                        lookup(destCollection, "source", "_id", "_matches"),
                                        unwind("$_matches"),
                                        replaceRoot("$_matches")));
        Stream<T> stream =
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }

    
    
    /* COMIENZO DE METODOS CREATE */

    //Intento de hacer que todos los create usen el mismo metodo
    //REVISAR PROBLEMAS DE USAR PersistentObject.class
    public void save(PersistentObject obj, String collectionName) {
        this.getDb()
                .getCollection(collectionName, PersistentObject.class)
                .insertOne(obj);
    }

	public User createUser(User u) {
		MongoCollection<User> userCollection = this.getDb().getCollection("users",User.class);
	    userCollection.insertOne(u);
	    return u;
	}

	public Supplier createSupplier(Supplier s) {
		MongoCollection<Supplier> supplierCollection = this.getDb().getCollection("suppliers",Supplier.class);
		supplierCollection.insertOne(s);
	    return s;
	}

	public Product createProduct(Supplier s) {
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers",Supplier.class);
		suppliersCollection.replaceOne(eq("_id", s.getObjectId()),s);
		return null;
	}

	public Order createOrder(Order o) {
		MongoCollection<Order> orderCollection = this.getDb().getCollection("orders",Order.class);
		orderCollection.insertOne(o);
	    return o;
	}

	public void refreshOrder(Order o) {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders",Order.class);
		ordersCollection.replaceOne(eq("_id", o.getObjectId()),o);
	}

	public void refreshSupplier(Supplier p) {
		MongoCollection<Supplier> ordersCollection = this.getDb().getCollection("suppliers",Supplier.class);
		ordersCollection.replaceOne(eq("_id", p.getObjectId()),p);
	}

	public void updateProductInRows(Product pr,Date sd) {
		MongoCollection<Row> rowsCollection = this.getDb().getCollection("rows",Row.class);

		//Filtrar renglones
		BasicDBObject filter = new BasicDBObject();
		filter.append("product._id", pr.getObjectId());
		BasicDBObject options = new BasicDBObject();
		options.append("multi", true);
		BasicDBObject exist = new BasicDBObject();
		exist.append("$exists", false);
		BasicDBObject aux = new BasicDBObject();
		aux.append("price.finishDate", exist);
		List<Bson> arrayFilter= new ArrayList<Bson>();
		arrayFilter.add(aux);
		BasicDBObject updateFields2 = new BasicDBObject();
		updateFields2.append("product.prices.$[price].finishDate", sd);
		BasicDBObject setQuery2 = new BasicDBObject();
		setQuery2.append("$set", updateFields2);
		UpdateOptions uo=new UpdateOptions();
		uo.arrayFilters(arrayFilter);		
		
		//Cambia price, lastPrice y pushea el nuevo precio en el historial
		BasicDBObject newPrice = new BasicDBObject();
		newPrice.append("price", pr.getPrice());
		newPrice.append("startDate", sd);		
		BasicDBObject pushAttr = new BasicDBObject();
		pushAttr.append("product.prices", newPrice);		
		BasicDBObject updateFields = new BasicDBObject();
		updateFields.append("product.price", pr.getPrice());
		updateFields.append("product.lastPrice.price", pr.getPrice());
		updateFields.append("product.lastPrice.startDate", sd);
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", updateFields);
		setQuery.append("$push", pushAttr);
		
		rowsCollection.updateMany(filter, setQuery2,uo);
		rowsCollection.updateMany(eq("product._id",pr.getObjectId()), setQuery);		
	}
	
	/* FIN DE METODOS CREATE */



	/* COMIENZO DE METODOS FIND */
	
	public List<Product> findProductsByName(String name) {
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers", Supplier.class); 		
		BasicDBObject filter = new BasicDBObject(new BasicDBObject("products",new BasicDBObject("$elemMatch", new BasicDBObject("name", new BasicDBObject("$regex",name))))); 		
		FindIterable<Supplier> itr = suppliersCollection.find(filter);
		List<Product> products = new ArrayList<Product>();
		for(Supplier s : itr) {
			for(Product p : s.getProducts()) {
				if(p.getName().matches(".*"+name+".*")) {
					products.add(p);
				}
			}
		}
		return products;
	}

	public Optional<User> findUserById(ObjectId id) {
		MongoCollection<User> usersCollection = this.getDb().getCollection("users", User.class);
		User u = usersCollection.find(eq("_id", id)).first();
		Optional<User> ou = Optional.ofNullable(u);
		return ou;
	}
	
	public Optional<Supplier> findSupplierById(ObjectId id) {
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers", Supplier.class);
		Supplier sup = suppliersCollection.find(eq("_id", id)).first();
		Optional<Supplier> su = Optional.ofNullable(sup);
		return su;
	}

	public Optional<User> findUserByUsername(String username) {
		MongoCollection<User> usersCollection = this.getDb().getCollection("users", User.class);
		User u = usersCollection.find(eq("username", username)).first();
		Optional<User> ou = Optional.ofNullable(u);
		return ou;
	}

	public Optional<User> findUserByEmail(String email) {
		MongoCollection<User> usersCollection = this.getDb().getCollection("users", User.class);
		User u = usersCollection.find(eq("email", email)).first();
		Optional<User> ou = Optional.ofNullable(u);
		return ou;
	}

	public Optional<Order> findOrderById(ObjectId order) {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class);
		Order o = ordersCollection.find(eq("_id", order)).first();
		List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
		o.setProducts(lr);
		Optional<Order> oo = Optional.ofNullable(o);
		return oo;
	}

	public Optional<Supplier> findSupplierOfProduct(ObjectId id) { 		
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers", Supplier.class); 		
		BasicDBObject filter = new BasicDBObject(new BasicDBObject("products",new BasicDBObject("$elemMatch", new BasicDBObject("_id", id)))); 		
		Supplier s = suppliersCollection.find(filter).first();
		Optional<Supplier> os = Optional.ofNullable(s);
		return os; 	
	}
	
	public List<Order> findPendingOrders() {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class); 		
		BasicDBObject filter = new BasicDBObject("statusActual.status", "Pending"); 		
		FindIterable<Order> itr = ordersCollection.find(filter);
		List<Order> orders = new ArrayList<Order>();
		for(Order o : itr) {
			List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
			o.setProducts(lr);
			orders.add(o);
		}
		return orders;
	}
	
	public List<Order> findSentOrders() {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class); 		
		BasicDBObject filter = new BasicDBObject("statusActual.status", "Sent"); 		
		System.out.print(filter);
		FindIterable<Order> itr = ordersCollection.find(filter);
		List<Order> orders = new ArrayList<Order>();
		for(Order o : itr) {
			List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
			o.setProducts(lr);
			orders.add(o);
		}
		return orders;
	}
	
	public List<Order> findOrdersMadeByUser(String username){
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class); 		
		BasicDBObject filter = new BasicDBObject("client.username", username); 		
		FindIterable<Order> itr = ordersCollection.find(filter);
		List<Order> orders = new ArrayList<Order>();
		for(Order o : itr) {
			List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
			o.setProducts(lr);
			orders.add(o);
		}
		return orders;
	}

	public List<Order> findOrderNearPlazaMoreno(){
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class);
		BasicDBObject geoNearParams = new BasicDBObject();
	    double[] coordinates = {-34.921236,-57.954571};
	    BasicDBObject near=new BasicDBObject();
	    near.append("type", "Point");
	    near.append("coordinates", coordinates);
	    geoNearParams.append("near", near);
	    geoNearParams.append("spherical", "true");
	    geoNearParams.append("maxDistance", 400);
	    geoNearParams.append("distanceField", "dist");
	    BasicDBObject geoNear = new BasicDBObject("$geoNear", geoNearParams);
	    AggregateIterable<Order> itr = ordersCollection.aggregate(Arrays.asList(geoNear));
	    List<Order> orders=new ArrayList<Order>();
		for(Order o : itr) {
			List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
			o.setProducts(lr);
			orders.add(o);
		}
	    return orders;
	}

	public List<Product> findProductsOnePrice() {
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers", Supplier.class); 		
		
		BasicDBObject filter = new BasicDBObject("products.prices",new BasicDBObject("$size", 1)); 		
		System.out.println(filter);
		FindIterable<Supplier> itr = suppliersCollection.find(filter);
		List<Product> products = new ArrayList<Product>();
		for(Supplier s : itr) {
			for(Product p : s.getProducts()) {
				if(p.getPrices().size()==1) {
					products.add(p);
				}
			}
		}
		return products;
	}

	public List<Order> findDeliveredOrdersForUser(String username) {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class); 		
		BasicDBObject filter = new BasicDBObject();
		filter.append("statusActual.status", "Delivered");
		filter.append("client.username", username);
		FindIterable<Order> itr = ordersCollection.find(filter);
		List<Order> orders = new ArrayList<Order>();
		for(Order o : itr) {
			List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
			o.setProducts(lr);
			orders.add(o);
		}
		return orders;
	}

	public Product findProductWithMaxWeigth() {
		MongoCollection<Product> suppliersCollection = this.getDb().getCollection("suppliers", Product.class);
		BasicDBObject filter = new BasicDBObject("$unwind","$products");
		BasicDBObject weigth = new BasicDBObject("$sort",new BasicDBObject("products.weight",-1));
		BasicDBObject project = new BasicDBObject();
		project.append("name", "$products.name");
//		project.append("_id", "$products.objectId");
		project.append("_id", "$products._id");
		project.append("price", "$products.price");
		project.append("weight", "$products.weight");
		project.append("prices", "$products.prices");
		List<BasicDBObject> aggrFilter = new ArrayList<BasicDBObject>();
		aggrFilter.add(filter);
		aggrFilter.add(weigth);
		aggrFilter.add(new BasicDBObject("$project",project));
		System.out.println(aggrFilter);
		Product prod = suppliersCollection.aggregate(aggrFilter).first();
		return prod;
	}

	public List<Product> findSoldProductsOn(Date day) {
//		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
//		String strDate = sm.format(day);
//		BasicDBObject filter = new BasicDBObject("dateOfOrder", new BasicDBObject("$regex",strDate));
//		System.out.println(filter);
	    MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class); 		
		FindIterable<Order> itr = ordersCollection.find(eq("dateOfOrder", day));
		
		List<Product> prods = new ArrayList<Product>();
		for(Order o : itr) {
			List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
			for(Row r : lr) {
				prods.add(r.getProduct());
//				System.out.println(r.getProduct().getName());
			}
		}

		return prods;
	}
	
	public Product findProductById(ObjectId id) {
		MongoCollection<Product> suppliersCollection = this.getDb().getCollection("suppliers", Product.class);
		BasicDBObject filter = new BasicDBObject("$unwind","$products");
		BasicDBObject aux = new BasicDBObject("products._id",id);
		BasicDBObject match = new BasicDBObject("$match",aux);
		BasicDBObject project = new BasicDBObject();
		project.append("name", "$products.name");
		project.append("_id", "$products._id");
		project.append("price", "$products.price");
		project.append("weight", "$products.weight");
		project.append("prices", "$products.prices");
		List<BasicDBObject> aggrFilter = new ArrayList<BasicDBObject>();
		aggrFilter.add(filter);
		aggrFilter.add(match);
		aggrFilter.add(new BasicDBObject("$project",project));
		Product prod = suppliersCollection.aggregate(aggrFilter).first();
		return prod;
	}
	
	public List<Order> findDeliveredInPeriod(Date startDate, Date endDate){
		MongoCollection<Order> orderCollection = this.getDb().getCollection("orders", Order.class);
		BasicDBObject filter = new BasicDBObject("$gte", startDate);
		filter.append("$lt", endDate);
		BasicDBObject filterDate = new BasicDBObject("dateOfOrder",filter);
		filterDate.append("statusActual.status", "Delivered");
		FindIterable<Order> itr = orderCollection.find(filterDate);
		List<Order> ords = new ArrayList<Order>();
		for(Order o : itr) {
			List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
			o.setProducts(lr);
			ords.add(o);
		}
		return ords;
	}

	public List<Supplier> findTopNSuppliersInSentOrders(int n) {
			AggregateIterable<Supplier> itr = this.getDb().getCollection("suppliers", Supplier.class).aggregate( Arrays.asList(
				lookup("rows","products._id", "product._id", "row"),
				unwind("$row"),
				lookup("order_rows", "row._id", "destination", "order_id"),
				lookup("orders", "order_id.source", "_id", "order"),
				match(eq("order.actualStatus.status", "Sent")),
				group("$_id", Accumulators.sum("total", "$row.cant"), Accumulators.first("name", "$name"),  Accumulators.first( "address",  "$address"), Accumulators.first( "coordX",  "$coordX"), Accumulators.first( "coordY",  "$coordY"), Accumulators.first( "cuil", "$cuil"), Accumulators.first( "products", "$products")),
				sort(Sorts.descending("total")),
				limit(n)
				));
			List<Supplier> sups = new ArrayList<Supplier>();
			for (Supplier s : itr) {
				sups.add(s);
			}
		return sups;
	}

	public Product findBestSellingProduct() {
		return this.getDb().getCollection("rows", Product.class).aggregate(
				Arrays.asList(
				group("$product._id", Accumulators.sum("total", "$cant"), Accumulators.first("name", "$product.name"),  Accumulators.first( "prices",  "$product.prices"), Accumulators.first( "price",  "$product.price"), Accumulators.first( "weight",  "$product.weight")),
				sort(Sorts.descending("total")))).first();
	}
	
	
	/* FIN DE METODOS FIND */

}
