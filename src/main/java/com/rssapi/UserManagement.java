package com.rssapi;
import java.net.UnknownHostException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;


@Path("/user")
public class UserManagement {
	@Path("/")
	@POST
	@Produces("application/json")
	public Response createUser(@QueryParam("username") String username,
								@QueryParam("password") String password)throws JSONException {
		JSONObject jsonObject = new JSONObject();
		int status = 200;

		try {
			MongoClient mongo = new MongoClient( "localhost" , 27017 );
	        DB db = mongo.getDB( "rssapidatabase" );

			DBCollection collection = db.getCollection("user");
			BasicDBObject user = new BasicDBObject();
			user.put("username", username);

			DBCursor cursor = collection.find(user);
			if (cursor.count() == 0) {
				user.put("password", password);
				user.put("token", "null");
				collection.save(user);

				BasicDBObject item = new BasicDBObject();
				item.put("username", username);
				item.put("id_user", (collection.find(user).next().get("_id")).toString());
				System.out.println( (ObjectId)collection.find(user).next().get("_id"));
				jsonObject.put("message", item);
				jsonObject.put("status", 200);
				jsonObject.put("response", JSONObject.NULL);
			} else {
				jsonObject.put("message", "username already exists");
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
	public Response deleteUser(@QueryParam("token") String token) throws JSONException {
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
				collection.remove(cursor.next());
				jsonObject.put("message", "user remove successfully");
				jsonObject.put("status", 200);
				jsonObject.put("response", JSONObject.NULL);
			}
			else {
				jsonObject.put("message", "unknown user id");
				jsonObject.put("status",  400);
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
