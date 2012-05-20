
package com.tau.ykesten.pinterest.bin;

import java.util.List;

public class BoardResponse{
   	private Board board;
   	private Number generated_at;
   	private Pagination pagination;
   	private List<Pin> pins;
   	private String status;

 	public Board getBoard(){
		return this.board;
	}
	public void setBoard(Board board){
		this.board = board;
	}
 	public Number getGenerated_at(){
		return this.generated_at;
	}
	public void setGenerated_at(Number generated_at){
		this.generated_at = generated_at;
	}
 	public Pagination getPagination(){
		return this.pagination;
	}
	public void setPagination(Pagination pagination){
		this.pagination = pagination;
	}
 	public List<Pin> getPins(){
		return this.pins;
	}
	public void setPins(List<Pin> pins){
		this.pins = pins;
	}
 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
}
