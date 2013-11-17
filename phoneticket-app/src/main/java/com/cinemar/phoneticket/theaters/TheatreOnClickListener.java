package com.cinemar.phoneticket.theaters;

import android.view.View;
import android.view.View.OnClickListener;

import com.cinemar.phoneticket.model.Theatre;

public class TheatreOnClickListener implements OnClickListener {
	
	protected Theatre theatre;

	public TheatreOnClickListener(Theatre theatre) {
		this.theatre = theatre;
	}
	
	public void onClick(View v) {		
		
	}

}
