package com.scottcaruso.datafunctions;

import org.json.JSONObject;

import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;
import com.scottcaruso.mygov.MainActivity;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class DataRetrievalService extends IntentService{

	public DataRetrievalService() 
	{
		super("DataRetrievalService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{		
		String dataResponse = RetrieveDataFromSunlightLabs.retrieveData(urlString);
		JSONObject parsedObject = TurnStringIntoJSONObject.createMasterObject(dataResponse);
		//Handle if the user has entered an unknown/invalid zip code.
		if (parsedObject == null)
		{
			Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "You have entered an invalid zip code. Please try again.", Toast.LENGTH_LONG);
			toast.show();
		} else
		{
			DisplayPoliticianResults.showPoliticiansInMainView(parsedObject, false);
		}
	}

}
