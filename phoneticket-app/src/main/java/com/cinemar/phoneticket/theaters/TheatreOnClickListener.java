package com.cinemar.phoneticket.theaters;

import com.cinemar.phoneticket.model.Theatre;

import android.view.View;
import android.view.View.OnClickListener;

public class TheatreOnClickListener implements OnClickListener {
	
	protected Theatre theatre;

	public TheatreOnClickListener(Theatre theatre) {
		this.theatre = theatre;
	}
	
	public void onClick(View v) {		
		
	}

}
