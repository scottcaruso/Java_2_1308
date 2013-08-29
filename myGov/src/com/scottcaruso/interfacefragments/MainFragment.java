package com.scottcaruso.interfacefragments;

import com.scottcaruso.mygov.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {

	private MainListener listener;
	
	public interface MainListener
	{
		public void onPolsSearch(EditText zipEntry);
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
