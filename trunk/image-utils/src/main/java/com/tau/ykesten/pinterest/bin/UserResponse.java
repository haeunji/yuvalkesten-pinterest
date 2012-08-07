package com.tau.ykesten.pinterest.bin;

import java.util.List;


public class UserResponse {
	private List<Board> boards;
	private String status;
	private User user;

	public List<Board> getBoards() {
		return this.boards;
	}

	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
