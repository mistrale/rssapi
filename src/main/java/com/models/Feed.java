package com.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Stores an RSS feed
 */
public class Feed {

  final String title;
  final String link;
  final String description;
  final String language;
  final String copyright;
  final String pubDate;

  final List<FeedMessage> entries = new ArrayList<FeedMessage>();

  public Feed(String title, String link, String description, String language,
      String copyright, String pubDate) {
    this.title = title;
    this.link = link;
    this.description = description;
    this.language = language;
    this.copyright = copyright;
    this.pubDate = pubDate;
  }

  public List<FeedMessage> getMessages() {
    return entries;
  }

  public String getTitle() {
    return title;
  }

  public String getLink() {
    return link;
  }

  public String getDescription() {
    return description;
  }

  public String getLanguage() {
    return language;
  }

  public String getCopyright() {
    return copyright;
  }

  public String getPubDate() {
    return pubDate;
  }

  @Override
  public String toString() {
    return "Feed [copyright=" + copyright + ", description=" + description
        + ", language=" + language + ", link=" + link + ", pubDate="
        + pubDate + ", title=" + title + "]";
  }
  
  public JSONObject toJson() {
	  JSONObject obj = new JSONObject();
	  
	  obj.put("title", title);
	  obj.put("link", link);
	  obj.put("description", description);
	  obj.put("language", language);
	  obj.put("copyright", copyright);
	  obj.put("Publication", pubDate);
	  
	  JSONArray array = new JSONArray();
	  for (int i = 0; i < entries.size(); i++) {
		  JSONObject msg = new JSONObject();
		  msg.put("title", entries.get(i).getTitle());
		  msg.put("description", entries.get(i).getDescription());
		  msg.put("link", entries.get(i).getLink());
		  msg.put("author", entries.get(i).getAuthor());
		  msg.put("guid", entries.get(i).getGuid());
		  array.put(msg);
	  }
	  obj.put("feedMessage", array);
	  return obj;
  }
} 
