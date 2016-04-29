package com.rssapi;
import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClient;

import com.models.Feed;
import com.models.User;
import com.utils.RSSFeedParser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;


@Path("/getRSSstream")
public class StreamController {

	@Path("/")
	@GET
	@Produces("application/json")
	public Response getStream(@QueryParam("stream") String stream,
				  @QueryParam("token") String token)throws JSONException {
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
		    RSSFeedParser parser = new RSSFeedParser(stream);
		    Feed feed = parser.readFeed();
		    
		    jsonObject.put("feed", feed.toJson());
		    jsonObject.put("message", "ok");
		    jsonObject.put("status", 200);
 		
		} else {
		    jsonObject.put("message", "wrong token");
		    jsonObject.put("status", 400);
		    jsonObject.put("response", JSONObject.NULL);
		    status = 400;
		}       		
	    } catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		jsonObject.put("message", "invalid stream");
		jsonObject.put("status", 400);
		jsonObject.put("response", JSONObject.NULL);
		status = 400;
	    }
	
	    return Response.status(status).entity(jsonObject.toString()).build();

 	}
}
