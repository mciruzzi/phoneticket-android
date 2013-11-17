package com.cinemar.phoneticket.reserveandbuy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupOperation {

	private String name;
	private List<OperationView> items;

	public GroupOperation(String title) {
		this.name = title;
		this.items = new ArrayList<OperationView>();
	}

	public String getName() {
		return name;
	}

	public void addItem(OperationView item) {
		items.add(item);
	}

	public List<OperationView> getItems() {
		return items;
	}

	public void sortOperation() {
		Collections.sort(items, new Comparator<OperationView>()
		{
			public int compare(OperationView operation1, OperationView operation2) {
				return operation1.getItem().getDate().compareTo(operation2.getItem().getDate());
			}
		});
	}
} 