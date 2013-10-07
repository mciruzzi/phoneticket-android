package com.cinemar.phoneticket.reserveandbuy;

import java.util.ArrayList;
import java.util.List;

public class GroupOperation {

	  private String name;
	  private List<OperationView> items = new ArrayList<OperationView>();

	  public GroupOperation(String title) {
	    this.name = title;
	  }
	  
	  public String getName() {
		  return name;
	  }
	  
	  public void addItem(OperationView item) {
		  
		  items.add(item);
	  }
	  
	  public  List<OperationView> getItems() {
		  
		  return items;
	  }
} 