package com.scottcaruso.interfacefragments;

import org.json.JSONObject;

import com.scottcaruso.datafunctions.TurnStringIntoJSONObject;
import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;
import com.scottcaruso.mygov.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DisplayResultsFragment extends Fragment {

	String pols;
	Boolean favorites;
	JSONObject responseObject;

	
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
		//There is some moderately sophisticated but highly volatile code (particularly around the spinner) that has always been housed in this separate "Display Politician Results" class that I created. I felt that it would be counter-intuitive to the project to break this apart for relatively minimal gain, therefore it remains and is simply called by the fragment.
		DisplayPoliticianResults.showPoliticiansInDisplay(responseObject, favorites);
	}
	
}
