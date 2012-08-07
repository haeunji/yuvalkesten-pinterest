package com.tau.ykesten.pinterest.bin;

import java.util.List;

public class User {
	private String about;
	private String facebook_link;
	private String full_name;
	private String id;
	private String image_large_url;
	private String image_url;
	private boolean is_following;
	private String location;
	private boolean publish_timeline;
	private Stats stats;
	private String twitter_link;
	private String username;
	private String website;
	private List<Board> boards;

	public String getAbout() {
		return this.about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getFacebook_link() {
		return this.facebook_link;
	}

	public void setFacebook_link(String facebook_link) {
		this.facebook_link = facebook_link;
	}

	public String getFull_name() {
		return this.full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage_large_url() {
		return this.image_large_url;
	}

	public void setImage_large_url(String image_large_url) {
		this.image_large_url = image_large_url;
	}

	public String getImage_url() {
		return this.image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public boolean getIs_following() {
		return this.is_following;
	}

	public void setIs_following(boolean is_following) {
		this.is_following = is_following;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean getPublish_timeline() {
		return this.publish_timeline;
	}

	public void setPublish_timeline(boolean publish_timeline) {
		this.publish_timeline = publish_timeline;
	}

	public Stats getStats() {
		return this.stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public String getTwitter_link() {
		return this.twitter_link;
	}

	public void setTwitter_link(String twitter_link) {
		this.twitter_link = twitter_link;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<Board> getBoards() {
		return boards;
	}

	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
