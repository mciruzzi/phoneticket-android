package com.cinemar.phoneticket.viewcontrollers;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cinemar.phoneticket.SelectTicketsActivity;
import com.cinemar.phoneticket.model.prices.PriceInfo;



public class AdultsTicketItemViewController extends TicketItemViewController {
	
	Double unitPrice;

	public AdultsTicketItemViewController(LinearLayout layout,	SelectTicketsActivity ctx, PriceInfo price) {
		//it does not have any promotion, it just a ticket value entry
		super(layout, ctx, null);
		unitPrice = price.getAdultPrice();		
	}

	@Override
	protected List<Integer> getOptions(int maxTicketsAllowed) {
		// Los ticket de adulto por defecto se comen toda la cantidad de asientos disponibles
		List<Integer> options = new ArrayList<Integer>();
		options.add(maxTicketsAllowed);
		selectedAmount = maxTicketsAllowed;

		return options;
	}
	
	@Override
	public double getSubtotal() {
		return selectedAmount * unitPrice;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		selectedAmount = (Integer) parent.getItemAtPosition(pos);
		context.updateValues();
    }
	
	
	@Override
	public void updateTicketsView(int maxTicketsAllowed, boolean promoSelected) {
		//Idem entradas de ninios
		this.setSpinnerOptions(maxTicketsAllowed);
		setSubtotal(getSubtotal());
	}
	

	

	


	
}
