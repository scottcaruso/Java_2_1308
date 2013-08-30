package com.scottcaruso.interfacefragments;

import org.json.JSONObject;

import com.scottcaruso.datafunctions.TurnStringIntoJSONObject;
import com.scottcaruso.interfacefragments.MainFragment.MainListener;
import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;
import com.scottcaruso.mygov.DisplayResultsActivity;
import com.scottcaruso.mygov.R;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DisplayResultsFragment extends Fragment {

	String pols;
	Boolean favorites;
	JSONObject responseObject;
	
	private ResultsListener listener;
	
	public interface ResultsListener
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle passedData = getActivity().getIntent().getExtras();
		pols = (String) passedData.get("response");
		favorites = (Boolean) passedData.getBoolean("favorites");
		responseObject = TurnStringIntoJSONObject.createMasterObject(pols,favorites);	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.politician_display, container);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		DisplayPoliticianResults.showPoliticiansInDisplay(responseObject, favorites);
	}
	
}
