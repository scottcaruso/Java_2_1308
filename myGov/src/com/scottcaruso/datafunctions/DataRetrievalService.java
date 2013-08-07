package com.scottcaruso.datafunctions;

import org.json.JSONObject;

import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;
import com.scottcaruso.mygov.MainActivity;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class DataRetrievalService extends IntentService{

	public static final String MESSENGER_KEY = "messenger";
	public static final String ZIP_KEY = "URL";
	
	public DataRetrievalService() 
	{
		super("DataRetrievalService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{		
		Log.i("onHandleIntent","Service started");
		
		Bundle extras = intent.getExtras();
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		String zipString = (String) extras.get(ZIP_KEY);
		String dataResponse = RetrieveDataFromSunlightLabs.retrieveData("http://congress.api.sunlightfoundation.com/legislators/locate?zip="+zipString+"&apikey=eab4e1dfef1e467b8a25ed1eab0f7544");
		JSONObject parsedObject = TurnStringIntoJSONObject.createMasterObject(dataResponse);

		//Handle if the user has entered an unknown/invalid zip code.
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		message.obj = parsedObject;
		
		try 
		{
			messenger.send(message);
		} catch (RemoteException e) {
			Log.e("messenger","There was a problem sending the message.");
		}
	}
}
