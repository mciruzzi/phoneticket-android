package com.cinemar.phoneticket;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class LoginWebServiceTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

	@SuppressWarnings("deprecation")
	public LoginWebServiceTest() {
        super("com.cinemar.phoneticket", MainMenuActivity.class);
    }
	
    @SuppressWarnings({ "unchecked", "unused" })
	private <T extends Activity> T startActivitySync(Class<T> clazz) {
        Intent intent = new Intent(getInstrumentation().getTargetContext(), clazz);
        intent.setFlags(intent.getFlags() | FLAG_ACTIVITY_NEW_TASK);
        return ((T) getInstrumentation().startActivitySync(intent));
    }
//
    public void testLoginWebService() throws Throwable {
//    	startActivitySync(LoginActivity.class);
//		final AsyncHttpClient client = new AsyncHttpClient();
//		final CountDownLatch signal = new CountDownLatch(1);
//		final StringBuilder strBuilder = new StringBuilder();
//		
//		runTestOnUiThread(new Runnable() { // THIS IS THE KEY TO SUCCESS
//			public void run() {
//				client.post(
//						"http://phoneticket-stg.herokuapp.com/api/users/sessions.json?email=snipperme@gmail.com&password=123456",
//						new AsyncHttpResponseHandler() {
//							@Override
//							public void onStart() {
//								Log.i("START", "start");
//								Log.i("START", "start");
//							}
//
//							@Override
//							public void onSuccess(String response) {
//								Log.i("OK", response);
//								Log.i("OK", response);
//								strBuilder.append(response);
//
//							}
//
//							@Override
//							public void onFailure(Throwable e, String response) {
//								Log.i("ERROR", response);
//								Log.i("ERROR", response);
//							}
//
//							@Override
//							public void onFinish() {
//								Log.i("FINISHA", "finish");
//								Log.i("FINISHA", "finish");
//								signal.countDown();
//							}
//						});
//			}
//		});//
//		Log.i("A", "Comenzado el test");
//		try {
//			signal.await(30, TimeUnit.SECONDS); // wait for callback
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		assertEquals(strBuilder.toString(),"{\"email\":\"snipperme@gmail.com\"}");
//		
//		//se crea el objeto que ayuda deserealizar la cadena JSON
//		Gson gson = new Gson();
		
 		//Deserealizamos la cadena JSON para que se convertida
//		assertEquals(user.getEmail(),"snipperme@gmail.com");
	}
}