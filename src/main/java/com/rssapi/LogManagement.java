package com.rssapi;

import java.net.UnknownHostException;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import com.models.User;

@Path("/connection")
public class LogManagement {

	@Path("/login")
	@POST
	@Produces("application/json")
	public Response login(@QueryParam("username") String username,
								@QueryParam("password") String password)throws JSONException {
		int status = 200;
		JSONObject jsonObject = new JSONObject();

		MongoClient mongo;
		try {
			mongo = new MongoClient( "localhost" , 27017 );
	        DB db = mongo.getDB( "rssapidatabase" );
	        
			DBCollection collection = db.getCollection("user");
			BasicDBObject user = new BasicDBObject();
			user.put("username", username);
			user.put("password", password);
			
			DBCursor cursor = collection.find(user);
			if (cursor.count() == 0) {
				jsonObject.put("message", "unknown username or wrong password");
				jsonObject.put("status", 400);
				jsonObject.put("response", JSONObject.NULL);
				status = 400;
			} else {
				String uuid = UUID.randomUUID().toString();
				while (cursor.hasNext()) {
					DBObject updateDoc = cursor.next();
					BasicDBObject newDoc = new BasicDBObject();
					newDoc.append("$set", new BasicDBObject().append("token", uuid));
					
					collection.update(updateDoc, newDoc);
				}
				cursor = collection.find(new BasicDBObject().append("username", username));
				System.out.println(cursor.next().toString());
				User userResponse = new User();
				userResponse.setPassword(password);
				userResponse.setToken(uuid);
				userResponse.setUsername(username);
				jsonObject.put("message", "login success");
				jsonObject.put("status", 200);
				jsonObject.put("response", userResponse.toJson());
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return Response.status(status).entity(jsonObject.toString()).build();
	}
	
	@Path("/logout")
	@POST
	@Produces("application/json")
	public Response logout(@QueryParam("token") String token)throws JSONException {
		int status = 200;
		JSONObject jsonObject = new JSONObject();

		MongoClient mongo;
		try {
			mongo = new MongoClient( "localhost" , 27017 );
	        DB db = mongo.getDB( "rssapidatabase" );
	        
			DBCollection collection = db.getCollection("user");
			BasicDBObject user = new BasicDBObject();
			user.put("token", token);
			
			DBCursor cursor = collection.find(user);
			if (cursor.count() == 0) {
				jsonObject.put("message", "wrong token");
				jsonObject.put("status", 400);
				jsonObject.put("response", JSONObject.NULL);
				status = 400;
			} else {
				while (cursor.hasNext()) {
					DBObject updateDoc = cursor.next();
					BasicDBObject newDoc = new BasicDBObject();
					newDoc.append("$set", new BasicDBObject().append("token", "null"));
					
					collection.update(updateDoc, newDoc);
				}
				jsonObject.put("message", "logout success");
				jsonObject.put("status", 200);
				jsonObject.put("response", JSONObject.NULL);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return Response.status(status).entity(jsonObject.toString()).build();
	}
}
