/* Scott Caruso
 * Java II 1308
 * Week 3 Assignment
 */
package com.scottcaruso.mygov;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scottcaruso.datafunctions.DataRetrievalService;
import com.scottcaruso.datafunctions.SavedPoliticianProvider;
import com.scottcaruso.interfacefragments.MainFragment.MainListener;
import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;
import com.scottcaruso.utilities.Connection_Verification;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements MainListener {
	
	static Context currentContext;
	public static JSONObject jsonResponse;
	public static String response;
	static Spinner queryChoice;
	public static Cursor thisCursor;

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	Log.i("Info","No state change detected. Loading normally.");
       	DisplayPoliticianResults.setViewingDisplay(false);
        
        this.setContentView(R.layout.mainfrag);
        Log.i("Info","Created Main Menu elements based on XML files.");
        
        //Allows the context to be passed across classes.
        setCurrentContext(MainActivity.this);

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == 0) {
        Bundle result = data.getExtras();
        String polName = result.getString("lastpolname");
        Boolean didOrientationChange = result.getBoolean("orientationchanged");
        if (didOrientationChange == true)
        {
        	this.onCreate(result);
        }
        if (polName != null)
        {
        	final TextView lastPolName = (TextView) findViewById(R.id.returnPolName);
        	lastPolName.setVisibility(0);
        	lastPolName.setText("Last Politician Viewed: " + polName);
        }
      }
    }
	
	public static void setCurrentContext (Context context)
	{
		currentContext = context;
	}
	
	public static Context getCurrentContext ()
	{
		return currentContext;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	//Because there is so much going on here - and because we need to recreate all of this when the Back button is pressed.
	
	public static void buildClicker(EditText zipEntry)
	{
		Log.i("Info","Attached onClick method to Zip Code entry button.");
		Boolean connected = Connection_Verification.areWeConnected(getCurrentContext());
		if (connected)
		{
			Log.i("Info","Connection established.");
			String enteredZip = zipEntry.getText().toString();
			Handler retrievalHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg) 
				{
					if (msg.arg1 == RESULT_OK)
					{
						try {
							response = (String) msg.obj;
						} catch (Exception e) {
							Log.e("Error","There was a problem retrieving the json Response.");
						}
						String nullResponse = "{\"results\":[],\"count\":0}";
						if (response.equals(nullResponse))
						{
							Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "You have entered an invalid zip code. Please try again.", Toast.LENGTH_LONG);
							toast.show();
						} else
						{
							//DisplayPoliticianResults.showPoliticiansInMainView(jsonResponse, false);
							Intent nextActivity = new Intent(MainActivity.getCurrentContext(),DisplayResultsActivity.class);
							nextActivity.putExtra("favorites", false);
							nextActivity.putExtra("response", response);
							Activity currentActivity = (Activity) getCurrentContext();
							currentActivity.startActivityForResult(nextActivity, 0);
						}
					}
				}
			};
			Log.i("Info","Handler created.");
			
			Messenger apiMessenger = new Messenger(retrievalHandler);
			
			Intent startDataService = new Intent(getCurrentContext(), DataRetrievalService.class);
			Log.i("Info","Intent to start data service started.");
			startDataService.putExtra(DataRetrievalService.MESSENGER_KEY, apiMessenger);
			startDataService.putExtra(DataRetrievalService.ZIP_KEY,enteredZip);
			Log.i("Info","Put Messenger and Zip Code into Data Service intent.");
			getCurrentContext().startService(startDataService);
		
		} else
		{
			Log.i("Info","No internet connection found.");
			Toast toast = Toast.makeText(getCurrentContext(), "There is no connection to the internet available. Please try again later, or view saved politicians.", Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	public static void turnCursorIntoDisplay(Cursor dataCursor)
	{
		JSONObject masterObject = new JSONObject();
		JSONArray arrayOfPols = new JSONArray();
		JSONObject thisObject = null;
		
		Log.i("Info","Parsing cursor to pull data into display.");
		if (dataCursor != null) 
		{
			try 
			{
				if (dataCursor.moveToFirst())
				{
					   while(!dataCursor.isAfterLast())
					   {
						  thisObject = new JSONObject(dataCursor.getString(dataCursor.getColumnIndex("polObject")));
					      arrayOfPols.put(thisObject);
					      dataCursor.moveToNext();
					   }
						masterObject.put("Politicians", arrayOfPols);
						try 
						{
							String newObject = masterObject.toString();
							Intent nextActivity = new Intent(MainActivity.getCurrentContext(),DisplayResultsActivity.class);
							nextActivity.putExtra("favorites", true);
							nextActivity.putExtra("response", newObject);
							Activity currentActivity = (Activity) getCurrentContext();
							currentActivity.startActivityForResult(nextActivity, 0);
							dataCursor.close();
						}  catch (Exception e) {
							Toast toast = Toast.makeText(getCurrentContext(), "There are no politicians saved to storage.", Toast.LENGTH_LONG);
							toast.show();
						}
				} else
				{
					if (SavedPoliticianProvider.JSONString != null)
					{
						Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "There are no politicians saved to storage.", Toast.LENGTH_LONG);
						toast.show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else
		{
			Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "There are no saved politicians to view.", Toast.LENGTH_LONG);
			toast.show();
		}
	}

	@Override
	public void onPolsSearch(EditText zipEntry) {
		buildClicker(zipEntry);
	}

	@Override
	public void getSavedPols(Cursor cursor) {
		turnCursorIntoDisplay(cursor);
	}
    
}
