package com.cinemar.phoneticket.authentication;

import java.io.IOException;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.cinemar.phoneticket.model.User;
import com.google.gson.Gson;

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

	// Variables para manipular los controles de la UI

	@Override
	public User login(String user, String Password) {

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
	public User signup(String user, String Password) {
		// TODO Auto-generated method stub
		return new User("A","B");
	}
	
	/**
     * Metodo que recibe una cadena JSON y la convierte en una lista
     * de objetos AndroidOS para despues cargarlos en la lista
     * @param strJson (String) Cadena JSON
     */
    private User parseUser(String strJson){
		//se crea el objeto que ayuda deserealizar la cadena JSON
		gson = new Gson();
		
 		//Deserealizamos la cadena JSON para que se convertida
		return gson.fromJson(strJson, User.class);	
 
	}

}
