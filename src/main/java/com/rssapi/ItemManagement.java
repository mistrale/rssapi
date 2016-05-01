package com.rssapi;
import java.net.UnknownHostException;

import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;


@Path("/item")
public class ItemManagement {
	@Path("/")
	@POST
	@Produces("application/json")
	public Response createFeed(@QueryParam("title") String title,
				   @QueryParam("link") String link,
				   @QueryParam("description") String description,
				   @QueryParam("author") String author,
				   @QueryParam("guid") String guid,
				   @QueryParam("state") String state,
				   @QueryParam("token") String token) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		int status = 200;

		if (link.isEmpty() || title.isEmpty() || description.isEmpty() || author.isEmpty()
				|| guid.isEmpty() || state.isEmpty()) {
					jsonObject.put("message", "field cannot be blank saved");
					jsonObject.put("response", JSONObject.NULL);
					jsonObject.put("status", 400);
					return Response.status(status).entity(jsonObject.toString()).build();
				}

		try {
		    MongoClient mongo = new MongoClient( "localhost" , 27017 );
		    DB db = mongo.getDB( "rssapidatabase" );

		    DBCollection collection = db.getCollection("user");
		    BasicDBObject user = new BasicDBObject();
		    user.put("token", token);

		    DBCursor cursor = collection.find(user);
		    if (cursor.count() != 0) {
			DBObject	obj = cursor.next();
			Object id = obj.get("_id");
			System.out.println(id);

			// check existance of feed in db
			DBCollection itemDb = db.getCollection("item");


			// create feed favoris
			BasicDBObject item = new BasicDBObject();
		        item.put("title", title);
		        item.put("link", link);
		        item.put("description", description);
		        item.put("author", author);
		        item.put("guid", guid);
			item.put("state", state);
		        item.put("id_user", id);

			DBCursor cursorItem = itemDb.find(item);
			if (cursorItem.count() == 0) {
			    itemDb.save(item);
			    jsonObject.put("message", "item successfully saved");
			    jsonObject.put("status", 200);
			    jsonObject.put("response", JSONObject.NULL);
			} else {
			    jsonObject.put("message", "item already saved");
			    jsonObject.put("status", 400);
			    jsonObject.put("response", JSONObject.NULL);
			    status = 400;
			}
		    } else {
			jsonObject.put("message", "wrong token");
			jsonObject.put("status", 400);
			jsonObject.put("response", JSONObject.NULL);
			status = 400;
		    }
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			jsonObject.put("message", "fail to connect to DB");
			jsonObject.put("status", 400);
			jsonObject.put("response", JSONObject.NULL);
			status = 400;
		}

	    return Response.status(status).entity(jsonObject.toString()).build();
	}

    	@Path("/")
	@GET
	@Produces("application/json")
	public Response getItem(@QueryParam("token") String token) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		int status = 200;

		try {
		    MongoClient mongo = new MongoClient( "localhost" , 27017 );
		    DB db = mongo.getDB( "rssapidatabase" );

		    DBCollection collection = db.getCollection("user");
		    BasicDBObject user = new BasicDBObject();
		    user.put("token", token);

		    DBCursor cursor = collection.find(user);
		    if (cursor.count() != 0) {
			DBObject	obj = cursor.next();
			Object id = obj.get("_id");
			System.out.println(id);

			// check existance of feed in db
			DBCollection itemDb = db.getCollection("item");


			// create feed favoris
			BasicDBObject item = new BasicDBObject();
			item.put("id_user", id);

			DBCursor cursorItem = itemDb.find(item);
			JSONArray array = new JSONArray();

			while (cursorItem.hasNext()) {
			    JSONObject find = new JSONObject();
			    DBObject cursorObj = cursorItem.next();
			    find.put("title", cursorObj.get("title"));
			    find.put("id", cursorObj.get("_id"));
			    find.put("link", cursorObj.get("link"));
			    find.put("author", cursorObj.get("author"));
			    find.put("guid", cursorObj.get("guid"));
			    find.put("description", cursorObj.get("description"));
			    find.put("state", cursorObj.get("state"));
			    array.put(find);
			    System.out.println(cursorObj);
			}
			jsonObject.put("message", "ok");
			jsonObject.put("status", 200);
			jsonObject.put("response", array);


		    } else {
			jsonObject.put("message", "wrong token");
			jsonObject.put("status", 400);
			jsonObject.put("response", JSONObject.NULL);
			status = 400;
		    }
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			jsonObject.put("message", "fail to connect to DB");
			jsonObject.put("status", 400);
			jsonObject.put("response", JSONObject.NULL);
			status = 400;
		}

	    return Response.status(status).entity(jsonObject.toString()).build();
	}

    @Path("/")
    @DELETE
    @Produces("application/json")
    public Response getItem(@QueryParam("token") String token, @QueryParam("id") String id) throws JSONException {
	JSONObject jsonObject = new JSONObject();
	int status = 200;

	try {
	    MongoClient mongo = new MongoClient( "localhost" , 27017 );
	    DB db = mongo.getDB( "rssapidatabase" );

	    DBCollection collection = db.getCollection("user");
	    BasicDBObject user = new BasicDBObject();
	    user.put("token", token);

	    DBCursor cursor = collection.find(user);
	    if (cursor.count() != 0) {
		// check existance of feed in db
		DBCollection itemDb = db.getCollection("item");


			// create feed favoris

		BasicDBObject item = new BasicDBObject();

		item.put("_id", new ObjectId(id));

	        DBCursor cursorItem = itemDb.find(item);

		if (cursorItem.count() == 0) {

		    jsonObject.put("message", "unknown item id");
		    jsonObject.put("status",  400);
				status = 400;
		    jsonObject.put("response", JSONObject.NULL);
		} else {
		    DBObject test = cursorItem.next();

		    itemDb.remove(test);
		    jsonObject.put("message", "item remove successfully");
		    jsonObject.put("status", 200);
		    jsonObject.put("response", JSONObject.NULL);
		}

	    } else {
		jsonObject.put("message", "wrong token");
		jsonObject.put("status", 400);
		jsonObject.put("response", JSONObject.NULL);
		status = 400;
	    }
	} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
	    e1.printStackTrace();
	    jsonObject.put("message", "fail to connect to DB");
	    jsonObject.put("status", 400);
	    jsonObject.put("response", JSONObject.NULL);
	    status = 400;
	}

	return Response.status(status).entity(jsonObject.toString()).build();
    }

    @Path("/")
    @PUT
    @Produces("application/json")
    public Response getItem(@QueryParam("token") String token, @QueryParam("id") String id,
			    @QueryParam("state") String state) throws JSONException {
	JSONObject jsonObject = new JSONObject();
	int status = 200;

	try {
	    MongoClient mongo = new MongoClient( "localhost" , 27017 );
	    DB db = mongo.getDB( "rssapidatabase" );

	    DBCollection collection = db.getCollection("user");
	    BasicDBObject user = new BasicDBObject();
	    user.put("token", token);

	    DBCursor cursor = collection.find(user);
	    if (cursor.count() != 0) {
		// check existance of feed in db
		DBCollection itemDb = db.getCollection("item");


			// create feed favoris

		BasicDBObject item = new BasicDBObject();

		item.put("_id", new ObjectId(id));

	        DBCursor cursorItem = itemDb.find(item);

		if (cursorItem.count() == 0) {
				status = 400;
		    jsonObject.put("message", "unknown item id");
		    jsonObject.put("status",  400);
		    jsonObject.put("response", JSONObject.NULL);
		} else {
		    DBObject test = cursorItem.next();

		    BasicDBObject searchQuery = new BasicDBObject().append("$set", new BasicDBObject().append("state", state));

		    itemDb.update(item, searchQuery);
		    jsonObject.put("message", "item save successfully");
		    jsonObject.put("status", 200);
		    jsonObject.put("response", JSONObject.NULL);
		}

	    } else {
		jsonObject.put("message", "wrong token");
		jsonObject.put("status", 400);
		jsonObject.put("response", JSONObject.NULL);
		status = 400;
	    }
	} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
	    e1.printStackTrace();
	    jsonObject.put("message", "fail to connect to DB");
	    jsonObject.put("status", 400);
	    jsonObject.put("response", JSONObject.NULL);
	    status = 400;
	}

	return Response.status(status).entity(jsonObject.toString()).build();
    }

}
