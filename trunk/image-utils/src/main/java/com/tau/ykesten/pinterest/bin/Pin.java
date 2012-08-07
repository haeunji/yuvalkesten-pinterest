package com.tau.ykesten.pinterest.bin;

import java.io.Serializable;
import java.util.List;

public class Pin implements Serializable{
	
	private static final long serialVersionUID = 5582426350977153914L;
	
	private Board board;
	private List<Comment> comments;
	private Counts counts;
	private String created_at;
	private String description;
	private String domain;
	private String id;
	private Images images;
	private boolean is_repin;
	private boolean is_video;
	private Sizes sizes;
	private String source;
	private User user;
	private User via_user;

	public Board getBoard() {
		return this.board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Counts getCounts() {
		return this.counts;
	}

	public void setCounts(Counts counts) {
		this.counts = counts;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Images getImages() {
		return this.images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public boolean getIs_repin() {
		return this.is_repin;
	}

	public void setIs_repin(boolean is_repin) {
		this.is_repin = is_repin;
	}

	public boolean getIs_video() {
		return this.is_video;
	}

	public void setIs_video(boolean is_video) {
		this.is_video = is_video;
	}

	public Sizes getSizes() {
		return this.sizes;
	}

	public void setSizes(Sizes sizes) {
		this.sizes = sizes;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getVia_user() {
		return this.via_user;
	}

	public void setVia_user(User via_user) {
		this.via_user = via_user;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pin other = (Pin) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
