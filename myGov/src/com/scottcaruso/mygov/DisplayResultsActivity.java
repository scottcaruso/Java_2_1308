package com.scottcaruso.mygov;

import org.json.JSONObject;

import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;

import android.app.Activity;
import android.os.Bundle;

public class DisplayResultsActivity extends Activity {
	
	JSONObject pols;
	Boolean favorites;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		this.setContentView(R.layout.politician_display);
		Bundle passedData = getIntent().getExtras();
		pols = (JSONObject) passedData.get("politicians");
		favorites = (Boolean) passedData.getBoolean("favorites");
		DisplayPoliticianResults.showPoliticiansInMainView(pols, favorites);
	}
}
