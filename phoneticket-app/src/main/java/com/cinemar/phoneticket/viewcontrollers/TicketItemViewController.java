package com.cinemar.phoneticket.viewcontrollers;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cinemar.phoneticket.R;
import com.cinemar.phoneticket.SelectTicketsActivity;
import com.cinemar.phoneticket.model.prices.Promotion;

// The default View Controller, corresponde con los items de promociones (ninios y adultos se modelan heredando de este)
public class TicketItemViewController implements OnItemSelectedListener {

	Spinner spinner;
	Integer selectedAmount;
	TextView description, title, subtotal;
	EditText promoCode;



	SelectTicketsActivity context;
	Promotion promotion; // If it belongs to a promotion some behavior is
							// delegated in it

	public TicketItemViewController(LinearLayout layout, SelectTicketsActivity ctx,
			Promotion promo) {
		title = (TextView) layout.findViewById(R.id.promoTitle);
		description = (TextView) layout.findViewById(R.id.promoDescription);
		subtotal = (TextView) layout.findViewById(R.id.promoSubtotal);
		spinner = (Spinner) layout.findViewById(R.id.promoSpinner);
		promoCode = (EditText) layout.findViewById(R.id.codeText);		
		spinner.setOnItemSelectedListener(this);
		context = ctx;
		promotion = promo;
		
		this.setSubtotal(new Double(0));
		selectedAmount = 0;

		if (promotion != null) {
			this.setTitle(promotion.getName());
			this.setDescription(promotion.getName()); // TODO Change for description			
		}
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setDescription(String desc) {
		this.description.setText(desc);
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal.setText(subtotal.toString());
	}
	
	public Integer getSelectedAmount() {
		return selectedAmount;
	}
	
	public double getSubtotal() {
		return promotion.getPrice(selectedAmount, context.getPriceInfo());
	}

	public void updateTicketsView(int maxTicketsAllowed, boolean promoSelected) {
		//Si hay otra promo seleccionada y no soy yo no puedo ser seleccionada
		if (promoSelected && selectedAmount == 0)
			maxTicketsAllowed = 0;

		
		this.setSpinnerOptions(maxTicketsAllowed);
		setSubtotal(getSubtotal());
	}

	protected void setSpinnerOptions(int maxTicketsAllowed) {
		if (spinner.getAdapter() == null){
			ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(context,
				android.R.layout.simple_spinner_item, getOptions(maxTicketsAllowed));		
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter);
		}
		else {
			ArrayAdapter<Integer> dataAdapter = (ArrayAdapter<Integer>) spinner.getAdapter();
			dataAdapter.clear();
			dataAdapter.addAll(getOptions(maxTicketsAllowed));
		}
		
	}

	protected List<Integer> getOptions(int maxTicketsAllowed) {
		List<Integer> options = new ArrayList<Integer>();
		options.add(0); // default Option
		
		for (int i = 1; i * promotion.getSeatsNeeded() <= maxTicketsAllowed+selectedAmount; i++) {
			options.add(promotion.getSeatsNeeded() * i);
		}
	
		return options;
	}
	
	
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)		
		selectedAmount = (Integer) parent.getItemAtPosition(pos);
		if (selectedAmount > 0 && promotion.getValidationType().equals(Promotion.CODE))
			promoCode.setVisibility(View.VISIBLE);
		else
			promoCode.setVisibility(View.GONE);
		context.updateValues();
    }


	public void onNothingSelected(AdapterView<?> arg0) {
		this.setSubtotal(new Double(0));		
	}

}
