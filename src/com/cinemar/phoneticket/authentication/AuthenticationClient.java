package com.cinemar.phoneticket.authentication;

import java.io.IOException;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.cinemar.phoneticket.model.User;
import com.google.gson.Gson;
import com.loopj.android.http.*;


public class AuthenticationClient implements AuthenticationService {

	// Constantes para la invocacion del web service
	private static final String NAMESPACE = "http://ws.cdyne.com/WeatherWS/";
	private static String URL = "http://wsf.cdyne.com/WeatherWS/Weather.asmx";
	private static final String METHOD_NAME = "GetWeatherInformation";
	private static final String SOAP_ACTION = "http://ws.cdyne.com/WeatherWS/GetWeatherInformation";
	
	// Declaracion de variables para consumir el web service
	private SoapObject request = null;
	private SoapSerializationEnvelope envelope = null;
	private SoapObject resultsRequestSOAP = null;

	// Declaracion de variables para serealziar y deserealizar
	// objetos y cadenas JSON
	Gson gson;

	@Override
	public User loginSOAP(String user, String Password) {

		request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		// Se crea un objeto SoapSerializationEnvelope para serealizar la peticion SOAP y permitir viajar el mensaje por la nube
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true; // se asigna true para el caso de que el WS sea de dotNet
				
		// Se envuelve la peticion soap
		envelope.setOutputSoapObject(request);
		
		// Objeto que representa el modelo de transporte
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			// Hace la llamada al ws
			transporte.call(SOAP_ACTION, envelope);
			// Se crea un objeto SoapPrimitive y se obtiene la respuesta de la peticion
			resultsRequestSOAP = (SoapObject) envelope.getResponse();
			Log.i("myApp", resultsRequestSOAP.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		// Almacenamos el resultado en un String ya que lo que represa el ws es una cadena json
		String strJSON = resultsRequestSOAP.toString();

		/*return parseUser(strJSON);*/ return new User("A","B");
		
	}

	@Override
	public User loginAsync(String user, String password) {
		
		RequestParams params = new RequestParams();
		params.put("email", user);
		params.put("password", "password");
		Log.i("AuthenticationClient","Request Por Iniciar");

		
		RestClient.post("/api/users/sessions", params, new JsonHttpResponseHandler() {
			@Override public void onStart() {
				Log.i("AuthenticationClient","Request Iniciado");				
			}
			@Override public void onFailure(Throwable arg0, JSONObject arg1) {
				Log.i("AuthenticationClient","Failure papa");
			}
			@Override public void onFailure(Throwable arg0, JSONArray arg1) {
				Log.i("AuthenticationClient","Failure List papa");
			}		
			@Override public void onSuccess(JSONArray arg0) {
				Log.i("AuthenticationClient","Succes List papa");
			};
			
            @Override            
            public void onSuccess(JSONObject  response) {
                String emailDelChavon = null;
                Log.i("AuthenticationClient","EYYYYYY");
				try {					
					emailDelChavon = response.getString("email");
					Log.i("AuthenticationClient","email del chavon " + emailDelChavon);
					
				} catch (JSONException e) {					
					e.printStackTrace();
				}                        
            }
            @Override 
            public void onFinish(){
            	Log.i("AuthenticationClient","Request Finalizado");
            }
        });

		
		
		return new User("login","succesful");
		
	}
	@Override
	public User login (String user, String password){
	
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost("http://phoneticket-stg.herokuapp.com/api/users/sessions");
		try {
		    JSONObject data = new JSONObject();
	        data.put("email", "snipperme@gmail.com");
	        data.put("password", "123456");	        
	        
	        StringEntity se = new StringEntity(data.toString());

		    httppost.setEntity(se);
		    
		    httppost.setHeader("Accept", "application/json");
		    httppost.setHeader("Content-type", "application/json");			
		       		    
		    // send the variable and value, in other words post, to the URL
		    HttpResponse response = httpclient.execute(httppost);
		    Log.i("RESPONSE",response.toString());
	    } catch (ClientProtocolException e) {
	    	Log.i("PROTOCOL","A");
	    } catch (IOException e) {
		    Log.i("IO","S");
	    } catch (JSONException e) {
			Log.i("JSON","J");
		}
		
		return new User("login","succesful");
		
	}
	
	
	@Override
	public User signup(String user, String Password) {
		// TODO Auto-generated method stub
		return new User("A","B");
	}
	
	/**
     * Metodo que recibe una cadena JSON y la convierte en un @User
     * @param strJson (String) Cadena JSON
     */
    private User parseUser(String strJson){
		//se crea el objeto que ayuda deserealizar la cadena JSON
		gson = new Gson();
		
 		//Deserealizamos la cadena JSON para que se convertida
		return gson.fromJson(strJson, User.class);	
 
	}

}
