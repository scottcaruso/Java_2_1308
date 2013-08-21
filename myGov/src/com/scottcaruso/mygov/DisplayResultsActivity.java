package com.scottcaruso.mygov;

import org.json.JSONObject;

import com.scottcaruso.datafunctions.TurnStringIntoJSONObject;
import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

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
	
	@Override
	public void finish() {
		final Spinner polName = (Spinner) findViewById(com.scottcaruso.mygov.R.id.politicianName);
		String name = polName.getSelectedItem().toString();
	    Intent data = new Intent();
	    data.putExtra("lastpolname", name);
	    setResult(RESULT_OK, data);
	    super.finish();
	}
	
	public static Context getDisplayContext() {
		return displayContext;
	}

	public static void setDisplayContext(Context displayContext) {
		DisplayResultsActivity.displayContext = displayContext;
	}
}
