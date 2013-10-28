package com.cinemar.phoneticket.viewcontrollers;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cinemar.phoneticket.SelectTicketsActivity;
import com.cinemar.phoneticket.model.prices.Promotion;


public class SingleTicketItemViewController extends TicketItemViewController {
	
	Double unitPrice;

	public SingleTicketItemViewController(LinearLayout layout,	SelectTicketsActivity ctx, Double price) {
		//it does not have any promotion, it just a ticket value entry
		super(layout, ctx, null);
		unitPrice = price;
	}

	@Override
	protected List<Integer> getOptions(int maxTicketsAllowed) {
		List<Integer> options = new ArrayList<Integer>();
		options.add(0); 
		
		for (int i = 1; i <= maxTicketsAllowed+selectedAmount; i++) {
			options.add(i);
		}	
		return options;
	}
	
	@Override
	public double getSubtotal() {
		return selectedAmount * unitPrice;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)		
		selectedAmount = (Integer) parent.getItemAtPosition(pos);
		context.updateValues();
    }
	
}
