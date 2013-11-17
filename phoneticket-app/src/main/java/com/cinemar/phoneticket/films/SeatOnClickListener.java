package com.cinemar.phoneticket.films;

import java.util.LinkedList;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cinemar.phoneticket.R;
import com.cinemar.phoneticket.model.Room.Seat;
import com.cinemar.phoneticket.model.SeatStatus;

public class SeatOnClickListener implements OnClickListener {
	protected Seat seat;
	private LinkedList<Seat> selectedSeats;
	private int maxSelected;
	Map<String, ImageView> seatsImages;
	ImageView thisSeatImage;
	boolean selected;
	boolean selectable;

	public SeatOnClickListener(Seat seatModel, Map<String, ImageView> seatsMap, boolean selectable, LinkedList<Seat> seatsSelected, int maxSelected) {
		this.seat = seatModel;
		this.seatsImages = seatsMap;
		this.thisSeatImage = seatsImages.get(seatModel.getId());
		//this.selected = false;
		this.selectable = selectable;
		this.maxSelected= maxSelected;
		this.selectedSeats=seatsSelected;
	}

	public void onClick(View v) {
		if (seat.getStatus().equals(SeatStatus.RESERVED) ){
			thisSeatImage.setImageResource(R.drawable.seat_available);
			selectedSeats.remove(seat);
			//selected = false;			
			seat.setStatus(SeatStatus.AVAILABLE);			
		}
		else if (selectable){			
			if (selectedSeats.size() >= maxSelected){
				Seat noMore = selectedSeats.removeFirst();
				noMore.setStatus(SeatStatus.AVAILABLE);
				ImageView noMoreImage = seatsImages.get(noMore.getId());
				noMoreImage.setImageResource(R.drawable.seat_available);	
			}
			thisSeatImage.setImageResource(R.drawable.seat_selected);
			selectedSeats.addLast(seat);
			//selected = true;
			seat.setStatus(SeatStatus.RESERVED);			
		}
			
		
	}
}
