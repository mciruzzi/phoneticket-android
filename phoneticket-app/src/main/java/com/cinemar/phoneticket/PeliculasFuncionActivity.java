package com.cinemar.phoneticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cinemar.phoneticket.films.DownloadImageTask;
import com.cinemar.phoneticket.films.ExpandableShowListAdapter;
import com.cinemar.phoneticket.films.FilmsClientAPI;
import com.cinemar.phoneticket.model.Film;
import com.cinemar.phoneticket.model.ItemOperation;
import com.cinemar.phoneticket.model.Show;
import com.cinemar.phoneticket.model.Theatre;
import com.cinemar.phoneticket.model.prices.PriceInfo;
import com.cinemar.phoneticket.reserveandbuy.OperationConstants;
import com.cinemar.phoneticket.reserveandbuy.ReserveBuyAPI;
import com.cinemar.phoneticket.reserveandbuy.ReserveRequest;
import com.cinemar.phoneticket.reserveandbuy.ReserveResponseHandler;
import com.cinemar.phoneticket.reserveandbuy.ReserveResponseHandler.Fields;
import com.cinemar.phoneticket.reserveandbuy.ReserveResponseHandler.PerformReserveListener;
import com.cinemar.phoneticket.theaters.TheatresClientAPI;
import com.cinemar.phoneticket.util.AppCommunicator;
import com.cinemar.phoneticket.util.NotificationUtil;
import com.cinemar.phoneticket.viewcontrollers.SeatQuantityPickerFragment;
import com.cinemar.phoneticket.viewcontrollers.SeatQuantityPickerFragment.NoticeDialogListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PeliculasFuncionActivity extends AbstractApiConsumerActivity
		implements NoticeDialogListener,PerformReserveListener {

	private static final int TRANSACTION_REQUEST_CODE = 9999;
	public static final int TRANSACTION_OK = 9998;
	public static final int TRANSACTION_SEATS_PROBLEM = 9997;
	private Film mFilm;
	private String theatreId;
	private Show selectedShow = null;
	private ExpandableShowListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader = new ArrayList<String>();
	private HashMap<String, List<Show>> listDataChild = new HashMap<String, List<Show>>();
	private ImageView mYoutubeImage;
	private AppCommunicator sharer;
	private SeatQuantityPickerFragment picker;
	private PriceInfo priceInfo;
	private boolean isReserveSelected;

	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharer = new AppCommunicator(this);
		setContentView(R.layout.activity_peliculas_funcion);
		picker = new SeatQuantityPickerFragment();

		title = getIntent().getStringExtra("filmTitle");

		mFilm = new Film(getIntent().getStringExtra("filmId"), title,
				getIntent().getStringExtra("filmSinopsis"), getIntent()
						.getStringExtra("filmYouTubeTrailer"), getIntent()
						.getStringExtra("filmCoverUrl"), getIntent()
						.getStringExtra("filmDirector"), getIntent()
						.getStringExtra("filmAudienceRating"), getIntent()
						.getStringExtra("filmCast"), getIntent()
						.getStringExtra("filmGenre"), getIntent()
						.getStringExtra("filmShareUrl"));

		theatreId = getIntent().getStringExtra("theatreId");

		setTitle(mFilm.getTitle());

		// ** Important to get in order to use the showProgress method**//
		mMainView = findViewById(R.id.funciones_main_view);
		mStatusView = findViewById(R.id.funciones_status);
		mStatusMessageView = (TextView) findViewById(R.id.funciones_status_message);

		mYoutubeImage = (ImageView) findViewById(R.id.youtubeImage);
		if (mFilm.getYouTubeTrailerURL() != null
				&& !mFilm.getYouTubeTrailerURL().isEmpty()) {
			mYoutubeImage.setClickable(true);
			mYoutubeImage.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mFilm.getYouTubeTrailerURL()));
					startActivityForResult(intent, TRANSACTION_REQUEST_CODE);
				}

			});
		} else {
			mYoutubeImage.setVisibility(View.GONE);
		}

		TextView genreText = (TextView) findViewById(R.id.genreText);
		genreText.setText("Género: " + mFilm.getGenre());

		TextView castText = (TextView) findViewById(R.id.castText);
		castText.setText("Actores: " + mFilm.getCast());

		TextView idSinopsisText = (TextView) findViewById(R.id.sinopsisText);
		idSinopsisText.setText("Sinopsis: " + mFilm.getSynopsis());

		ImageView coverView = (ImageView) findViewById(R.id.filmCoverImage);
		new DownloadImageTask(coverView).execute(mFilm.getCoverURL());

		Button comprarButton = (Button) findViewById(R.id.comprarButton);
		comprarButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (selectedShow == null) {
					showSimpleAlert(getResources().getString(
							R.string.no_selected_show));
					return;
				}
				
				if (!validateLogin())
					return;
				
				if (selectedShow.isNumbered())
					goToSeatSelectionActivity(false);
				else{
					displaySeatsPicker();
					isReserveSelected = false;
				}
			}

		});

		Button reservarButton = (Button) findViewById(R.id.reservarButton);
		reservarButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (selectedShow == null) {
					showSimpleAlert(getResources().getString(
							R.string.no_selected_show));
					return;
				}
				if (!validateLogin())
					return;
						
				if (selectedShow.isNumbered())
					goToSeatSelectionActivity(true);
				else{
					displaySeatsPicker();
					isReserveSelected = true;
				}
			}
		});

		ImageView fbButtonView = (ImageView) findViewById(R.id.facebookImage);
		fbButtonView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent shareIntent = sharer.getFacebookIntent(mFilm
						.getShareURL());
				if (shareIntent == null) {
						showSimpleAlert(getString(R.string.missingApplication));
					return;
				}

				startActivity(Intent.createChooser(shareIntent, "Share..."));
			}
		});

		ImageView twButtonView = (ImageView) findViewById(R.id.twitterImage);
		twButtonView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent shareIntent = sharer.getTwitterIntent(
						"Me gusta esta peli: ", mFilm.getShareURL());

				if (shareIntent == null) {
					showSimpleAlert(getString(R.string.missingApplication));
					return;
				}
				startActivity(Intent.createChooser(shareIntent, "Share..."));
			}
		});

		this.getFunciones();
	}

	private void displaySeatsPicker() {
		//picker.displayPicker();
		picker.show(getSupportFragmentManager(), "SeatQuantityPickerFragment");
	}

	private void getFunciones() {
		mStatusMessageView.setText(R.string.funciones_progress_getting);
		showProgress(true);

		FilmsClientAPI api = new FilmsClientAPI();
		if (theatreId == null) { // Funciones sin filtrado
			api.getFunciones(mFilm, responseHandler);
		} else {
			api.getFuncionesEnComplejo(mFilm.getId(), theatreId, responseHandler);
		}
	}

	private void prepareFuncionesList() {
		expListView = (ExpandableListView) findViewById(R.id.funcionesList);

		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<Show>>();

		for (Theatre cinema : mFilm.getCinemas()) {
			// TODO Formate funcion date properly
			String key = cinema.getName() + "\n" + cinema.getAddress();
			listDataHeader.add(key);
			listDataChild.put(key, cinema.getShows());
		}

		listAdapter = new ExpandableShowListAdapter(this, listDataHeader, listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);

		expListView.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				selectedShow = listDataChild.get(
						listDataHeader.get(groupPosition)).get(childPosition);

				Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : " + selectedShow.getShowId(), Toast.LENGTH_SHORT).show();

				return false;
			}
		});
	}

	JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject film) {
			Log.i("Funciones Activity", "Funciones Recibidas");
			Log.i("Funciones Activity", "Funciones" + film + "recibida");

			mFilm.clearFunciones();
			try {
				mFilm.addFunciones(film);
			} catch (JSONException e) {
				this.onFailure(e, "Error while parsing funciones JSON object");
			}
		}

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			showSimpleAlert(arg1);
		};

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			Log.i("Funciones Activity", "Failure pidiendo funciones");
			if (errorResponse != null) {
				showSimpleAlert(errorResponse.optString("error"));
			} else {
				showSimpleAlert(e.getMessage());
			}
		}

		@Override
		public void onFinish() {
			showProgress(false);
			prepareFuncionesList();
		}

	};
	
	private boolean validateLogin() {
		SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
		if (settings.getString("email", null)==null){
			//showSimpleAlert(getResources().getString(R.string.must_be_logged));
			Toast toast = Toast.makeText(this, getString(R.string.must_be_logged), 2000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setAction(LoginActivity.SIGNIN_ACTION);
			startActivityForResult(intent, MainMyAccountActivity.REQUEST_LOGIN);
			
			return false;
		}
		else {
			return true;
		}
		
		
	}

	public void onSeatsCountSelected(DialogInterface dialog,final int seatsCount) {

		if (isReserveSelected) {
			ReserveRequest reserve = new ReserveRequest();
			SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
			reserve.setEmail(settings.getString("email", null));
			reserve.setShowId(selectedShow.getShowId());
			reserve.setSeatsCount(seatsCount);

			ReserveBuyAPI api = new ReserveBuyAPI();
			ReserveResponseHandler reserveResponseHandler = new ReserveResponseHandler(this);
			api.performUnNumberedReserve(reserve, reserveResponseHandler);

		} else {
			// Hack para conseguir la informacion de precios
			// (que es una paja porque si no paso por la seleccion de asientos
			// cague)

			TheatresClientAPI api = new TheatresClientAPI();
			api.getShowSeats(selectedShow.getShowId(),
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject roomInfo) {
							try {
								priceInfo = new PriceInfo(roomInfo);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFinish() {
							goToTicketSelectionActivity(seatsCount);
						}
					});
		}

	}

	private void goToSeatSelectionActivity(boolean isReserve) {

		Intent intent = new Intent(this, SelectSeatsActivity.class);
		intent.putExtra("showId", selectedShow.getShowId());
		intent.putExtra("title", title);
		// intent.putExtra("maxSelections",value); //Si se quisiese limitar la
		// cant de entradas se hace mediante este parametro
		intent.putExtra("isReserve", isReserve);
		startActivityForResult(intent,TRANSACTION_REQUEST_CODE);
	}

	private void goToTicketSelectionActivity(int seatsCount) {

		Intent intent = new Intent(this, SelectTicketsActivity.class);
		intent.putExtra("showId", selectedShow.getShowId());
		intent.putExtra("seatsCount", seatsCount);
		intent.putExtra("priceInfo", priceInfo);
		intent.putExtra("isReserve", false);

		startActivityForResult(intent, TRANSACTION_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TRANSACTION_REQUEST_CODE) {
			switch (resultCode) {
			case SelectTicketsActivity.TRANSACTION_OK:
				finish();
				break;

			case SelectTicketsActivity.TRANSACTION_SEATS_PROBLEM:
				showSimpleAlert("No quedan suficiente cantidad de asientos.\nPor favor, seleccione una cantidad menor.", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						displaySeatsPicker();
					}
				});
				break;

			case SelectTicketsActivity.TRANSACTION_SHOW_PROBLEM:
				getFunciones();
				break;

			default:
				break;
			}
		}
	}

	public void onReserveOk(String msg,JSONObject result) {

		setResult(PeliculasFuncionActivity.TRANSACTION_OK);

		Intent intent = new Intent(this, ReserveShowActivity.class);
		ItemOperation item ;
		try {
			item = new ItemOperation(result);
			intent.putExtra(OperationConstants.TITLE, item.getTitle());
			intent.putExtra(OperationConstants.CINEMA, item.getCinema());
			intent.putExtra(OperationConstants.DATE, item.getDateToString());
			intent.putExtra(OperationConstants.SEATING, item.getSeatingToString());
			intent.putExtra(OperationConstants.TICKETS_TYPE, item.getTicketsType());
			intent.putExtra(OperationConstants.CODE, item.getId()); // para las reservas el id es el cod.
			intent.putExtra(OperationConstants.SHARE_URL, item.getShareUrl());
			intent.putExtra(OperationConstants.SCHEDULABLE_DATE, item.getDate().getTime());
			intent.putExtra(OperationConstants.NEW_OPERATION, true);
			intent.putExtra(OperationConstants.ID_SHOW, item.getIdShow());

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

	public void onErrorWhenReserving(String msg) {
		showSimpleAlert(msg);
	}

	public void onValidationError(Fields field, String msg) {
		switch (field) {
		case seats:
			// Shouldn't happend (here we only select by seats_count)
			showSimpleAlert("Los asientos seleccionados ya no se encuentran disponibles.\nPor favor, seleccione nuevamente.");
			break;
		case seats_count:
			showSimpleAlert("No quedan suficiente cantidad de asientos.\nPor favor, seleccione una cantidad menor.", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					displaySeatsPicker();
				}
			});
			break;
		default:
			break;
		}
	}
}
