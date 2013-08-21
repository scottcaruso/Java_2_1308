package com.scottcaruso.mygov;

import org.json.JSONObject;

import com.scottcaruso.datafunctions.TurnStringIntoJSONObject;
import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class DisplayResultsActivity extends Activity {
	
	String pols;
	Boolean favorites;
	static Context displayContext;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setDisplayContext(DisplayResultsActivity.this);
		this.setContentView(R.layout.politician_display);
		Bundle passedData = getIntent().getExtras();
		pols = (String) passedData.get("response");
		favorites = (Boolean) passedData.getBoolean("favorites");
		JSONObject responseObject = TurnStringIntoJSONObject.createMasterObject(pols);
		DisplayPoliticianResults.showPoliticiansInDisplay(responseObject, favorites);
		
	}
	
	public static Context getDisplayContext() {
		return displayContext;
	}

	public static void setDisplayContext(Context displayContext) {
		DisplayResultsActivity.displayContext = displayContext;
	}
}
