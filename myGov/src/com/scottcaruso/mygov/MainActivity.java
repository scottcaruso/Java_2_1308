/* Scott Caruso
 * Java 1 - 1307
 * Week 4 Project
 */
package com.scottcaruso.mygov;

import org.json.JSONObject;

import com.scottcaruso.datafunctions.DataRetrievalService;
import com.scottcaruso.datafunctions.RetrieveDataFromSunlightLabs;
import com.scottcaruso.datafunctions.SaveFavoritesLocally;
import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;
import com.scottcaruso.utilities.Connection_Verification;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static Context currentContext;
	public static JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_screen);
        
        //Allows the context to be passed across classes.
        setCurrentContext(MainActivity.this);
        
        //Create interface elements from the XML files
        final EditText zipEntry = (EditText) findViewById(R.id.zipcodeentry);
        Button searchForPolsButton = (Button) findViewById(R.id.dosearchnow);
        
        searchForPolsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Verify network connection before attempting to connect to network.
				Boolean connected = Connection_Verification.areWeConnected(MainActivity.this);
				if (connected)
				{
					String enteredZip = zipEntry.getText().toString();
					//RetrieveDataFromSunlightLabs.retrieveData("http://congress.api.sunlightfoundation.com/legislators/locate?zip="+enteredZip+"&apikey=eab4e1dfef1e467b8a25ed1eab0f7544");
					Handler retrievalHandler = new Handler()
					{

						@Override
						public void handleMessage(Message msg) 
						{
							if (msg.arg1 == RESULT_OK)
							{
								try {
									jsonResponse = (JSONObject) msg.obj;
								} catch (Exception e) {
									Log.e("Error","There was a problem retrieving the json Response.");
								}
								if (jsonResponse == null)
								{
									Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "You have entered an invalid zip code. Please try again.", Toast.LENGTH_LONG);
									toast.show();
								} else
								{
									DisplayPoliticianResults.showPoliticiansInMainView(jsonResponse, false);
								}
							}
						}
						
					};
					
					Messenger apiMessenger = new Messenger(retrievalHandler);
					
					Intent startDataService = new Intent(MainActivity.this, DataRetrievalService.class);
					startDataService.putExtra(DataRetrievalService.MESSENGER_KEY, apiMessenger);
					startDataService.putExtra(DataRetrievalService.ZIP_KEY,enteredZip);
					startService(startDataService);
					
					//Toast toast = Toast.makeText(MainActivity.this, "Retrieving data...", Toast.LENGTH_SHORT);
					//toast.show();

				
				} else
				{
					Toast toast = Toast.makeText(MainActivity.this, "There is no connection to the internet available. Please try again later, or view saved politicians.", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
        
        //Click this button to retrieve politicians saved to local storage.
        final Button retrieveSavedPols = (Button) findViewById(R.id.retrievefavorites);
        retrieveSavedPols.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String savedData;
				try {
					savedData = SaveFavoritesLocally.getSavedPols();
					JSONObject savedDataObject = new JSONObject(savedData);
					DisplayPoliticianResults.showPoliticiansInMainView(savedDataObject, true);
				} catch (Exception e) {
					Toast toast = Toast.makeText(MainActivity.this, "There are no politicians saved to storage.", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
        
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
    
}
