package com.rssapi;
import java.net.UnknownHostException;

import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
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


@Path("/feed")
public class FeedManagement {
	@Path("/")
	@POST
	@Produces("application/json")
	public Response createFeed(@QueryParam("title") String title,
				   @QueryParam("link") String link,
				   @QueryParam("description") String description,
				   @QueryParam("language") String language,
				   @QueryParam("copyright") String copyright,
				   @QueryParam("pubdate") String pubdate,
				   @QueryParam("token") String token) throws JSONException {
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
			DBCollection feedDb = db.getCollection("feed");
			

			// create feed favoris
			BasicDBObject feed = new BasicDBObject();
			feed.put("title", title);
			feed.put("link", link);
			feed.put("description", description);
			feed.put("language", language);
			feed.put("copyright", copyright);
			feed.put("pubdate", pubdate);
			feed.put("id_user", id);

			DBCursor cursorFeed = feedDb.find(feed);
			if (cursorFeed.count() == 0) {
			    feedDb.save(feed);
			    jsonObject.put("message", "feed successfully saved");
			    jsonObject.put("status", 200);
			    jsonObject.put("response", JSONObject.NULL);
			} else {
			    jsonObject.put("message", "feed already saved");
			    jsonObject.put("status", 200);
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
	public Response getFeed(@QueryParam("token") String token) throws JSONException {
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
			DBCollection feedDb = db.getCollection("feed");
			
		  
			// create feed favoris
			BasicDBObject feed = new BasicDBObject();
			feed.put("id_user", id);
			
			DBCursor cursorFeed = feedDb.find(feed);
			JSONArray array = new JSONArray();
			
			while (cursorFeed.hasNext()) {
			    JSONObject find = new JSONObject();
			    DBObject cursorObj = cursorFeed.next();
			    find.put("title", cursorObj.get("title"));
			    find.put("id", cursorObj.get("_id"));
			    find.put("link", cursorObj.get("link"));
			    find.put("language", cursorObj.get("language"));
			    find.put("copyright", cursorObj.get("copyright"));
			    find.put("pubdate", cursorObj.get("pubdate"));
			    find.put("description", cursorObj.get("description"));
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
    public Response getFeed(@QueryParam("token") String token, @QueryParam("id") String id) throws JSONException {
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
		DBCollection feedDb = db.getCollection("feed");
		
		
			// create feed favoris

		BasicDBObject feed = new BasicDBObject();

		feed.put("_id", new ObjectId(id));
		
	        DBCursor cursorFeed = feedDb.find(feed);

		if (cursorFeed.count() == 0) {

		    jsonObject.put("message", "unknown feed id");
		    jsonObject.put("status",  400);
		    jsonObject.put("response", JSONObject.NULL);
		} else {
		    DBObject test = cursorFeed.next();
		    
		    feedDb.remove(test);
		    jsonObject.put("message", "feed remove successfully");
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
