package com.cinemar.phoneticket.films;

import java.util.Map;

import com.cinemar.phoneticket.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SeatOnClickListener implements OnClickListener {
	protected String seatId;
	Map<String, ImageView> seatsImages;
	ImageView thisSeatImage;
	boolean selected;
	boolean selectable;

	public SeatOnClickListener(String seatId, Map<String, ImageView> seatsMap, boolean selectable) {
		this.seatId = seatId;
		this.seatsImages = seatsMap;
		this.thisSeatImage = seatsImages.get(seatId);
		this.selected = false;
		this.selectable = selectable;
	}

	public void onClick(View v) {
		if (selected ){
			thisSeatImage.setImageResource(R.drawable.seat_available);
			selected = false;
		}
		else if (selectable){
			thisSeatImage.setImageResource(R.drawable.seat_selected);
			selected = true;
		}
			
		
	}
}
