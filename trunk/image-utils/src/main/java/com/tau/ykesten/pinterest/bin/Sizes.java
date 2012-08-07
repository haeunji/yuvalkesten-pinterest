package com.tau.ykesten.pinterest.bin;

public class Sizes {

	private Size mobile;
	private Size closeup;
	private Size thumbnail;
	private Size board;

	public Size getMobile() {
		return mobile;
	}

	public void setMobile(Size mobile) {
		this.mobile = mobile;
	}

	public Size getCloseup() {
		return closeup;
	}

	public void setCloseup(Size closeup) {
		this.closeup = closeup;
	}

	public Size getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Size thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Size getBoard() {
		return board;
	}

	public void setBoard(Size board) {
		this.board = board;
	}

	private static class Size {
		private Integer width;
		private Integer height;

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		public Integer getHeight() {
			return height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}

	}
}
