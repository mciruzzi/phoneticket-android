package com.cinemar.phoneticket.viewcontrollers;

import java.util.ArrayList;
import java.util.List;

import com.cinemar.phoneticket.R;
import com.cinemar.phoneticket.model.prices.Promotion;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TicketItemViewController {

	Spinner spinner;
	TextView description, title, subtotal;
	Context context;
	Promotion promotion; // If it belongs to a promotion some behavior is
							// delegated in it

	public TicketItemViewController(LinearLayout layout, Context ctx,
			Promotion promo) {
		title = (TextView) layout.findViewById(R.id.promoTitle);
		description = (TextView) layout.findViewById(R.id.promoDescription);
		subtotal = (TextView) layout.findViewById(R.id.promoSubtotal);
		spinner = (Spinner) layout.findViewById(R.id.promoSpinner);
		context = ctx;
		promotion = promo;

		if (promotion != null) {
			this.setTitle(promotion.getName());
			this.setDescription(promotion.getName()); // TODO Change for description
			this.setSubtotal(new Double(0));
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

	public int getSelectedAmount() {
		//
		return 0;
	}

	public void updateTicketsView(int maxTicketsAllowed) {
		this.setSpinnerOptions(maxTicketsAllowed);

	}

	private void setSpinnerOptions(int maxTicketsAllowed) {
		List<Integer> options = new ArrayList<Integer>();
		options.add(0); // default Option

		if (promotion != null) {
			for (int i = 1; i * promotion.getSeatsNeeded() <= maxTicketsAllowed; i++) {
				options.add(promotion.getSeatsNeeded() * i);
			}
		} else {
			// default behaviour (one promo == one item) ==> Child and Adults
			// Tickets
			for (int i = 1; i <= maxTicketsAllowed; i++) {
				options.add(i);
			}
		}

		ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(context,
				android.R.layout.simple_spinner_item, options);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

}
