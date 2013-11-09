package com.cinemar.phoneticket.util;

import com.cinemar.phoneticket.R;

import android.text.TextUtils;
import android.widget.EditText;
import android.content.Context;

public class CreditCardValidatorUtil {
	
	static public boolean validate(Context ctx,EditText editNumeroDeTarjeta,EditText editTitular, EditText editCodigoSeg,
			EditText editFechaVencimiento) {
		boolean result = true;
		String numTarjeta = editNumeroDeTarjeta.getText().toString();
		String titular = editTitular.getText().toString();
		String codigoSeguridad = editCodigoSeg.getText().toString();
		String fechaVencimiento = editFechaVencimiento.getText().toString();
		
		if (TextUtils.isEmpty(numTarjeta)) {
			editNumeroDeTarjeta.setError(ctx.getString(R.string.error_field_required));	
			editNumeroDeTarjeta.requestFocus();
			result = false;			
		}
		
		if (numTarjeta.length()!= 16){
			editNumeroDeTarjeta.setError(ctx.getString(R.string.error_card_number_16digits));	
			editNumeroDeTarjeta.requestFocus();
			result = false;	
		}
		
		if (TextUtils.isEmpty(codigoSeguridad)) {
			editCodigoSeg.setError(ctx.getString(R.string.error_field_required));	
			editCodigoSeg.requestFocus();
			result = false;			
		}
		
		if (codigoSeguridad.length()!= 3){
			editCodigoSeg.setError(ctx.getString(R.string.error_card_seg_3digits));	
			editCodigoSeg.requestFocus();
			result = false;	
		}
		
		if (TextUtils.isEmpty(titular)) {
			editTitular.setError(ctx.getString(R.string.error_field_required));		
			editTitular.requestFocus();
			result = false;			
		}
		
		if (TextUtils.isEmpty(fechaVencimiento)) {
			editFechaVencimiento.setError(ctx.getString(R.string.error_field_required));	
			editFechaVencimiento.requestFocus();
			result = false;			
		}
		
		if (fechaVencimiento.length()!= 6){
			editFechaVencimiento.setError(ctx.getString(R.string.error_card_date_format));	
			editFechaVencimiento.requestFocus();
			result = false;	
		}

		return result;
	}
	
	
}
