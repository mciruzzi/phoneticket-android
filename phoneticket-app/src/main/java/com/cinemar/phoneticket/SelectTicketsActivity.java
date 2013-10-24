package com.cinemar.phoneticket;

import java.util.HashSet;
import java.util.Set;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SelectTicketsActivity extends AbstractApiConsumerActivity {
	String showId;
	Set<String> selectedSeats = new HashSet<String>();	
	boolean compra,reserva;
	
	//Views
	RadioButton compraRadioButton,reservaRadioButton;	
	EditText editNumeroDeTarjeta,editTitular,editCodigoSeg,editFechaVencimiento;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tickets);

		setTitle(getString(R.string.seleccioneTiposEntrada));
		
		showId = getIntent().getStringExtra("showId");
		selectedSeats.addAll(getIntent().getStringArrayListExtra("selectedSeats"));

		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.salaView);
		mStatusView = findViewById(R.id.sala_status);
		mStatusMessageView = (TextView) findViewById(R.id.sala_status_message);		
		
		getUIItems();
		showTicketTypes();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_tickets, menu);
		return true;
	}
	
	private void showTicketTypes() {
		// TODO consultar a la api de promociones por los tipos de entrada y mostrarla
		// tener en cuenta que el maximo numero de entradas no puede superar el maximo de asientos
		// seleccionados
	}

	private void getUIItems() {
		compraRadioButton = (RadioButton)findViewById(R.id.compraRadioButton);
		reservaRadioButton = (RadioButton)findViewById(R.id.reservaRadioButton);
		editNumeroDeTarjeta =  (EditText) findViewById(R.id.numeroDeTarjeta);
		editTitular =   (EditText) findViewById(R.id.nombreDeltitular);
		editCodigoSeg =  (EditText) findViewById(R.id.codigoDeSeguridad);
		editFechaVencimiento =  (EditText) findViewById(R.id.fechaVencimiento);
		
	}
	
	public void reservaSelected(View view){
		reserva = true;
		compra = false;
		compraRadioButton.setChecked(compra);
		reservaRadioButton.setChecked(reserva);
		editNumeroDeTarjeta.setVisibility(View.GONE);
		editTitular.setVisibility(View.GONE);
		editCodigoSeg.setVisibility(View.GONE);
		editFechaVencimiento.setVisibility(View.GONE);
	}
	
	public void compraSelected(View view){
		compra = true;
		reserva = false;
		compraRadioButton.setChecked(compra);
		reservaRadioButton.setChecked(reserva);		
		
		editNumeroDeTarjeta.setVisibility(View.VISIBLE);
		editTitular.setVisibility(View.VISIBLE);
		editCodigoSeg.setVisibility(View.VISIBLE);
		editFechaVencimiento.setVisibility(View.VISIBLE);
				
	}
	
	public void onClickDone(View view){
		//TODO validar/chequear datos de compra/reserva y hacer call a la api
		//de compras/reservas para registrala
	}

	

}
