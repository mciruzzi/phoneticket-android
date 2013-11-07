package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cinemar.phoneticket.model.ItemOperation;
import com.cinemar.phoneticket.model.prices.PriceInfo;
import com.cinemar.phoneticket.model.prices.Promotion;
import com.cinemar.phoneticket.reserveandbuy.BuyResponseHandler;
import com.cinemar.phoneticket.reserveandbuy.BuyResponseHandler.PerformBuyListener;
import com.cinemar.phoneticket.reserveandbuy.OperationConstants;
import com.cinemar.phoneticket.reserveandbuy.PurchaseRequest;
import com.cinemar.phoneticket.reserveandbuy.ReserveBuyAPI;
import com.cinemar.phoneticket.util.CreditCardValidatorUtil;
import com.cinemar.phoneticket.util.NotificationUtil;
import com.cinemar.phoneticket.viewcontrollers.AdultsTicketItemViewController;
import com.cinemar.phoneticket.viewcontrollers.ChildrenTicketItemViewController;
import com.cinemar.phoneticket.viewcontrollers.TicketItemViewController;

public class SelectTicketsActivity extends AbstractApiConsumerActivity implements PerformBuyListener{

	public static final int TRANSACTION_OK = 9998;
	public static final int TRANSACTION_SEATS_PROBLEM = 9997;
	public static final int TRANSACTION_SHOW_PROBLEM = 9996;

	private String showId;
	private String reserveId;
	private final Set<String> selectedSeats = new HashSet<String>();
	private int seatsCount;
	private boolean isBuy, isReserve, isNumbered;
	private PriceInfo priceInfo;
	private int selectedTickets = 0;

	// Views
	RadioButton compraRadioButton, reservaRadioButton;
	EditText editNumeroDeTarjeta, editTitular, editCodigoSeg,
			editFechaVencimiento;
	TextView ticketsTotal,cantTickets;
	TicketItemViewController adultsTicketsItem,childrenTicketsItem,selectedPromoItem=null;

	List<TicketItemViewController> promosItems = new ArrayList<TicketItemViewController>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tickets);

		setTitle(getString(R.string.seleccioneTiposEntrada));

		showId = getIntent().getStringExtra(OperationConstants.ID_SHOW);
		reserveId = getIntent().getStringExtra(OperationConstants.ID_RESERVE);
		
		//contempla casos que pueden venir de una seleccion de asientos o de la cant de asientos
		if (getIntent().hasExtra("selectedSeats")){
			selectedSeats.addAll(getIntent().getStringArrayListExtra("selectedSeats"));
			seatsCount = selectedSeats.size();
			isNumbered = true;
		}
		else{
			seatsCount = getIntent().getIntExtra("seatsCount",0);
			isNumbered = false;
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
		childrenTicketsItem.setDescription("Entrada de niño");


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
		boolean promoSelected = false;// para dejar elejir solo 1 promo (no acumulables)
		selectedPromoItem=null;

		//recoletamos la cantidad de tickets ya seleccionados
		for (TicketItemViewController promoItem : promosItems){
			selectedTickets += promoItem.getSelectedAmount();
			if (promoItem.getSelectedAmount()>0){
				promoSelected = true;
				selectedPromoItem = promoItem;
			}

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
		resetErrors();
		
		if ( !CreditCardValidatorUtil.validate(this,editNumeroDeTarjeta,editTitular, editCodigoSeg,editFechaVencimiento))
			return; //validate before performing purchase
		
		PurchaseRequest purchaseRequest = new PurchaseRequest();
		SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
		purchaseRequest.setEmail(settings.getString("email", null));
		purchaseRequest.setShowId(showId);
		purchaseRequest.setKidsCount(childrenTicketsItem.getSelectedAmount());
		if (selectedPromoItem!=null){
			purchaseRequest.setPromotionId(selectedPromoItem.getPromotionId());
			if (selectedPromoItem.isValidatedByCode())
			purchaseRequest.setPromotionCode(selectedPromoItem.getPromotionCode());
		}
		if (isNumbered) {
			purchaseRequest.setSeats(selectedSeats);
		} else {
			purchaseRequest.setSeatsCount(seatsCount);
		}

		if (reserveId != null)
			purchaseRequest.setReservationId(reserveId);

		purchaseRequest.setCardNumber(editNumeroDeTarjeta.getText().toString());
		purchaseRequest.setCardOwner(editTitular.getText().toString());
		purchaseRequest.setCardVerification(Integer.parseInt(editCodigoSeg.getText().toString()));

		purchaseRequest.setNumbered(isNumbered);

		ReserveBuyAPI api = new ReserveBuyAPI();
		BuyResponseHandler buyResponseHandler = new BuyResponseHandler(this);

		api.performBuy(this, purchaseRequest, buyResponseHandler);

	}

	private void resetErrors() {
		this.editNumeroDeTarjeta.setError(null);
		this.editFechaVencimiento.setError(null);
		this.editNumeroDeTarjeta.setError(null);
		this.editTitular.setError(null);		
	}

	public void onBuyOk(String msg,JSONObject result) {

		setResult(TRANSACTION_OK);

		Intent intent = new Intent(this, BuyShowActivity.class);
		ItemOperation item ;
		try {
			item = new ItemOperation(result);
			intent.putExtra(OperationConstants.TITLE, item.getTitle());
			intent.putExtra(OperationConstants.CINEMA, item.getCinema());
			intent.putExtra(OperationConstants.DATE, item.getDateToString());
			intent.putExtra(OperationConstants.SEATING, item.getSeatingToString());
			intent.putExtra(OperationConstants.TICKETS_TYPE, item.getTicketsType());
			intent.putExtra(OperationConstants.CODE, item.getCode());
			intent.putExtra(OperationConstants.SHARE_URL, item.getShareUrl());
			intent.putExtra(OperationConstants.SCHEDULABLE_DATE, item.getDate().getTime());
			intent.putExtra(OperationConstants.NEW_OPERATION, true);

			final Intent intentFinal = intent;

			NotificationUtil.showSimpleAlert("", msg, this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { //para que espere a que el usuario toque la pantalla, sino salta un error en la consola
					startActivity(intentFinal);
					finish();
				}
			});

		} catch (JSONException e) {
			this.showSimpleAlert("Error parseando compra respuesta");
		}

	}

	public void onErrorWhenBuying(String msg) {
		showSimpleAlert(msg);
	}

	public void onValidationError(BuyResponseHandler.Fields field, String error) {
		switch (field) {
		case promotion_code:
			// Add error to all promotions, no current way to know wich one is
			// the problem
			for (TicketItemViewController promoItem : promosItems) {
				promoItem.addErrorOnPromotionCode(error);
			}
			break;

		case card_number:
			editNumeroDeTarjeta.setError(error);
			editNumeroDeTarjeta.requestFocus();
			break;

		case seats:
			setResult(TRANSACTION_SEATS_PROBLEM);
			finish();
			break;

		case seats_count:
			setResult(TRANSACTION_SEATS_PROBLEM);
			finish();
			break;

		case payment:
			// Payment sale con mensaje de pago rechazado (no corresponde ningún
			// campo específico)
			showSimpleAlert(error);
			break;

		case show_id:
			showSimpleAlert(error, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					setResult(TRANSACTION_SHOW_PROBLEM);
					finish();
				}
			});
			break;

		default:
			showSimpleAlert(error);
			break;
		}
	}



}
