package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cinemar.phoneticket.model.prices.PriceInfo;
import com.cinemar.phoneticket.model.prices.Promotion;
import com.cinemar.phoneticket.reserveandbuy.BuyResponseHandler.PerformBuyListener;
import com.cinemar.phoneticket.reserveandbuy.ReserveBuyAPI;
import com.cinemar.phoneticket.reserveandbuy.ReserveRequest;
import com.cinemar.phoneticket.viewcontrollers.AdultsTicketItemViewController;
import com.cinemar.phoneticket.viewcontrollers.ChildrenTicketItemViewController;
import com.cinemar.phoneticket.viewcontrollers.TicketItemViewController;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SelectTicketsActivity extends AbstractApiConsumerActivity implements PerformBuyListener{
	private String showId;
	private Set<String> selectedSeats = new HashSet<String>();
	private int seatsCount;
	private boolean isBuy, isReserve;
	private PriceInfo priceInfo;
	private int selectedTickets = 0;

	// Views
	RadioButton compraRadioButton, reservaRadioButton;
	EditText editNumeroDeTarjeta, editTitular, editCodigoSeg,
			editFechaVencimiento;
	TextView ticketsTotal,cantTickets;
	TicketItemViewController adultsTicketsItem;
	TicketItemViewController childrenTicketsItem;
	List<TicketItemViewController> promosItems = new ArrayList<TicketItemViewController>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tickets);

		setTitle(getString(R.string.seleccioneTiposEntrada));

		showId = getIntent().getStringExtra("showId");
		//isReserve = getIntent().getBooleanExtra("isReserve", false);
		//contempla casos que pueden venir de una seleccion de asientos o de la cant de asientos
		if (getIntent().hasExtra("selectedSeats")){
			selectedSeats.addAll(getIntent().getStringArrayListExtra("selectedSeats"));
			seatsCount = selectedSeats.size();
		}
		else{
			seatsCount = getIntent().getIntExtra("seatsCount",0);
		}
			
		priceInfo = (PriceInfo) getIntent().getSerializableExtra("priceInfo");

		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.salaView);
		mStatusView = findViewById(R.id.sala_status);
		mStatusMessageView = (TextView) findViewById(R.id.sala_status_message);

		getUIItems();		
		displayPromos();
		updateValues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_tickets, menu);
		return true;
	}

	private void getUIItems() {
		compraRadioButton = (RadioButton) findViewById(R.id.compraRadioButton);
		reservaRadioButton = (RadioButton) findViewById(R.id.reservaRadioButton);
		editNumeroDeTarjeta = (EditText) findViewById(R.id.numeroDeTarjeta);
		editTitular = (EditText) findViewById(R.id.nombreDeltitular);
		editCodigoSeg = (EditText) findViewById(R.id.codigoDeSeguridad);
		editFechaVencimiento = (EditText) findViewById(R.id.fechaVencimiento);
		ticketsTotal = (TextView) findViewById(R.id.ticketsTotal);
		cantTickets = (TextView) findViewById(R.id.cantTickets);
		cantTickets.setText("Seleccione sus "+ seatsCount + " tickets:"+ '\n'+ "* Las promociones no son acumulables");
		
		adultsTicketsItem = new AdultsTicketItemViewController(
				(LinearLayout) findViewById(R.id.adultsTicketsLayout),this,priceInfo);
		adultsTicketsItem.setTitle(getString(R.string.adult_ticket));
		adultsTicketsItem.setDescription("Entrada de adulto");
			
		
		childrenTicketsItem = new ChildrenTicketItemViewController(
				(LinearLayout) findViewById(R.id.childrenTicketsLayout),this,priceInfo);
		childrenTicketsItem.setTitle(getString(R.string.children_ticket));
		childrenTicketsItem.setDescription("Entrada de ninio");
		
	    
	}

	public void reservaSelected(View view) {
		isReserve = true;
		isBuy = false;
		compraRadioButton.setChecked(isBuy);
		reservaRadioButton.setChecked(isReserve);
		editNumeroDeTarjeta.setVisibility(View.GONE);
		editTitular.setVisibility(View.GONE);
		editCodigoSeg.setVisibility(View.GONE);
		editFechaVencimiento.setVisibility(View.GONE);
	}

	public void compraSelected(View view) {
		isBuy = true;
		isReserve = false;
		compraRadioButton.setChecked(isBuy);
		reservaRadioButton.setChecked(isReserve);

		editNumeroDeTarjeta.setVisibility(View.VISIBLE);
		editTitular.setVisibility(View.VISIBLE);
		editCodigoSeg.setVisibility(View.VISIBLE);
		editFechaVencimiento.setVisibility(View.VISIBLE);
	}

	public PriceInfo getPriceInfo(){
		return priceInfo;
	}
	
	private void displayPromos() {		
		LinearLayout ticketsItemContainer = (LinearLayout) findViewById(R.id.ticketItemContainer);
		
		LayoutInflater inflater = LayoutInflater.from(getBaseContext());
		
		//No son necesarios ahora que son tratados distintos
//		LinearLayout adultsItem = (LinearLayout)inflater.inflate(R.layout.tickets_item_layout, null);
//		adultsTicketsItem = new SingleTicketItemViewController(
//				adultsItem,this,priceInfo.getAdultPrice());
//		adultsTicketsItem.setTitle(getString(R.string.adult_ticket));
//		adultsTicketsItem.setDescription("Entrada de adulto");				
//		 
//		LinearLayout childrenItem = (LinearLayout)inflater.inflate(R.layout.tickets_item_layout, null);
//		childrenTicketsItem = new SingleTicketItemViewController(
//				childrenItem,this,priceInfo.getChildPrice());
//		childrenTicketsItem.setTitle(getString(R.string.children_ticket));
//		childrenTicketsItem.setDescription("Entrada de ninio");
//		
//		promosItems.add(adultsTicketsItem);
//		promosItems.add(childrenTicketsItem);
//		ticketsItemContainer.addView(adultsItem);
//		ticketsItemContainer.addView(childrenItem);
		
		for (Promotion promo : priceInfo.getPromotions()){
			LinearLayout promoItem = (LinearLayout)inflater.inflate(R.layout.tickets_item_layout, null);
			
			promosItems.add(new TicketItemViewController(promoItem, this,promo));		
			ticketsItemContainer.addView(promoItem);
		}
	}
	
	public void updateValues(){
		selectedTickets = 0;
		double total = 0;
		boolean promoSelected = false; // para dejar elejir solo 1 promo (no acumulables)
		
		//recoletamos la cantidad de tickets ya seleccionados
		for (TicketItemViewController promoItem : promosItems){
			selectedTickets += promoItem.getSelectedAmount();
			if (selectedTickets>0) promoSelected = true;
		}
		
		selectedTickets += childrenTicketsItem.getSelectedAmount();
	
		//tickets que faltan seleccionar
		int maxTicketsAllowed = seatsCount - selectedTickets;
		
		//Hago update de los valores que puedo escojer en cada item/spinner	
		for (TicketItemViewController promoItem :promosItems){
			promoItem.updateTicketsView(maxTicketsAllowed, promoSelected);			
			total += promoItem.getSubtotal();
		}	
		
		total += (childrenTicketsItem.getSubtotal() + adultsTicketsItem.getSubtotal());
		adultsTicketsItem.updateTicketsView(maxTicketsAllowed,promoSelected);
		childrenTicketsItem.updateTicketsView(maxTicketsAllowed,promoSelected);
		
		//Expreso el total a pagar al momento
		ticketsTotal.setText("Total: $"+ Double.valueOf(total));
		
	}
	
	public void onClickDone(View view) {
		// TODO validar/chequear datos de compra/ y hacer call a la api
		// de compras para registrala
		// Nota : Si es reserva jamas llega a seleccionar tipo de entradas

		if(isBuy){
			
		}
	}

	public void onBuyOk(String msg) {
		showSimpleAlert(msg);
		
	}

	public void onErrorWhenBuying(String msg) {
		showSimpleAlert(msg);
		
	}
	
	

	


}
