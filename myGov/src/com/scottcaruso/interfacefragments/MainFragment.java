package com.scottcaruso.interfacefragments;

import com.scottcaruso.datafunctions.SavedPoliticianProvider.PoliticianData;
import com.scottcaruso.mygov.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MainFragment extends Fragment {

	private MainListener listener;
	
	public interface MainListener
	{
		public void onPolsSearch(EditText zipEntry);
		public void getSavedPols(Cursor cursor);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.main_screen, container);
		
        final EditText zipEntry = (EditText) view.findViewById(R.id.zipcodeentry);
		final Button searchForPolsButton = (Button) view.findViewById(R.id.dosearchnow);
		searchForPolsButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Build the onClick method, Handler, Intent, etc. See the method below!
					listener.onPolsSearch(zipEntry);
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchForPolsButton.getWindowToken(), 0);
					Log.i("Info","Hiding the keyboard");
				}
			});
		
        //Click this button to retrieve politicians saved to local storage.
        final Button retrieveSavedPols = (Button) view.findViewById(R.id.retrievefavorites);
        retrieveSavedPols.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//String savedData; //This has been commented out because it is only used when accessing data directly.
				Log.i("Info","Fetching ALL data via URI.");
				Uri uri = PoliticianData.CONTENT_URI;
				Cursor thisCursor = getActivity().getContentResolver().query(uri, PoliticianData.PROJECTION, null, null, null);
				listener.getSavedPols(thisCursor);
				}
			});
        
        //Create interface elements from the XML files
        final Spinner queryChoice = (Spinner) view.findViewById(R.id.spinner1);
        final Button queryButton = (Button) view.findViewById(R.id.partyquery);
	        
	        //Handle a spinner click
	        queryButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				Log.i("Info","Query clicked");
				if (queryChoice.getSelectedItem().toString().equals("Republicans"))
				{
					Log.i("Info","Fetching Republicans via URI");
					Uri uri = PoliticianData.REPUBLICAN_URI;
					Cursor thisCursor = getActivity().getContentResolver().query(uri, PoliticianData.PROJECTION, null, null, null);
					listener.getSavedPols(thisCursor);
				} else if (queryChoice.getSelectedItem().toString().equals("Democrats"))
				{
					Log.i("Info","Fetching Democrats via URI");
					Uri uri = PoliticianData.DEMOCRAT_URI;
					Cursor thisCursor = getActivity().getContentResolver().query(uri, PoliticianData.PROJECTION, null, null, null);
					listener.getSavedPols(thisCursor);
				} else
				{
					Toast toast = Toast.makeText(getActivity(), "There was an error making this query.", Toast.LENGTH_LONG);
					toast.show();
				}
				
			}
		});
		 
		return view;
	};

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try 
		{
			listener = (MainListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement the Listener interface.");
		}
	}
}
