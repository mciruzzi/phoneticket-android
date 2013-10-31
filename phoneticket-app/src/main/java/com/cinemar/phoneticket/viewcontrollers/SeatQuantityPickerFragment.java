package com.cinemar.phoneticket.viewcontrollers;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import com.cinemar.phoneticket.R;
import com.cinemar.phoneticket.model.Show;
import com.cinemar.phoneticket.model.prices.PriceInfo;
import com.cinemar.phoneticket.theaters.TheatresClientAPI;
import com.loopj.android.http.JsonHttpResponseHandler;

//In Charge of displaying a pop up an selecting the number of seats to reserve/buy
// (In cases of un-numbered shows) 
public class SeatQuantityPickerFragment extends DialogFragment {

	private static final int MAX_SEATS = 20;
	private NumberPicker numberPicker;		
	NoticeDialogListener mListener;


	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View pickerView = inflater.inflate(R.layout.seats_quatity_picker, null);
		numberPicker = (NumberPicker) pickerView.findViewById(R.id.seatPicker);
		builder.setView(pickerView);

		// Buttons
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {						
						mListener.onSeatsCountSelected(dialog,numberPicker.getValue());
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

		builder.setTitle(getString(R.string.pick_quantity_seats));
		

		String[] nums = new String[MAX_SEATS];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(i+1);

		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(MAX_SEATS);
		numberPicker.setWrapSelectorWheel(false);
		numberPicker.setDisplayedValues(nums);
		numberPicker.setValue(1);

		return builder.create();					
	}
	
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
 
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
    public interface NoticeDialogListener {
        public void onSeatsCountSelected(DialogInterface dialog,int seatsCount);        
    }


}
