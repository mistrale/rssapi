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

@Path("/getRSSstream")
public class StreamController {

	@Path("/")
	@GET
	@Produces("application/json")
	public Response getStream(@QueryParam("stream") String stream)throws JSONException {
		try {
			MongoClient mongo = new MongoClient( "localhost" , 27017 );
			List<String> dbs = mongo.getDatabaseNames();
			for(String db : dbs){
				System.out.println(db);
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		JSONObject jsonObject = new JSONObject();
	
	    try {
	    	    RSSFeedParser parser = new RSSFeedParser(stream);
	    	Feed feed = parser.readFeed();
	    	
	    	    jsonObject.put("feed", feed.toJson());
	    	    jsonObject.put("message", "ok");
	    	    jsonObject.put("status", 200);
	    } catch (Exception e) {
	    	jsonObject.put("feed", JSONObject.NULL);
	    	jsonObject.put("message", "invalid stream");
	    	jsonObject.put("status", 403);
	    }
	    
	    return Response.status(200).entity(jsonObject.toString()).build();
 	}
}
