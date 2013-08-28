package com.scottcaruso.interfacefragments;

import com.scottcaruso.mygov.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.main_screen, container);
		return view;
	}
}
