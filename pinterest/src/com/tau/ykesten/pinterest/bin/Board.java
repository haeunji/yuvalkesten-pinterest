
package com.tau.ykesten.pinterest.bin;

import java.util.List;


public class Board{
   	private String category;
   	private List<User> collaborators;
   	private String description;
   	private String id;
   	private boolean is_following;
   	private String name;
   	private Stats stats;
   	private String url;
   	private User user;
   	private List<Pin> pins;

 	public String getCategory(){
		return this.category;
	}
	public void setCategory(String category){
		this.category = category;
	}
 	public List<User> getCollaborators(){
		return this.collaborators;
	}
	public void setCollaborators(List<User> collaborators){
		this.collaborators = collaborators;
	}
 	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public boolean getIs_following(){
		return this.is_following;
	}
	public void setIs_following(boolean is_following){
		this.is_following = is_following;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public Stats getStats(){
		return this.stats;
	}
	public void setStats(Stats stats){
		this.stats = stats;
	}
 	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
 	public User getUser(){
		return this.user;
	}
	public void setUser(User user){
		this.user = user;
	}
	public List<Pin> getPins() {
		return pins;
	}
	public void setPins(List<Pin> pins) {
		this.pins = pins;
	}
	
}
