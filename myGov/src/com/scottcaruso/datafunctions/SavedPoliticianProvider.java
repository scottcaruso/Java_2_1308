package com.scottcaruso.datafunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.BaseColumns;

public class SavedPoliticianProvider extends ContentProvider{

	public static final String AUTHORITY = "com.scottcaruso.myGov.savedpoliticianprovider";
	
	public static class PoliticianData implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/politicians");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.scaruso.mygov.politician";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.scaruso.mygov.politician";
		
		public static final String NAME_COLUMN = "name";
		public static final String PARTY_COLUMN = "party";
		public static final String STATE_COLUMN = "state";
		public static final String TERM_COLUMN = "term";
		public static final String TWITTER_COLUMN = "twitter";
		public static final String WEBSITE_COLUMN = "website";
		
		public static final String[] PROJECTION = { "_id", NAME_COLUMN,PARTY_COLUMN,STATE_COLUMN,TERM_COLUMN,TWITTER_COLUMN,WEBSITE_COLUMN};
	
		private PoliticianData() {};
	}
	
	public static final int POLS = 1;
	public static final int POLS_ID = 2;
	
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static 
	{
		uriMatcher.addURI(AUTHORITY, "politicians/",POLS);
		uriMatcher.addURI(AUTHORITY,"politicians/",POLS_ID);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		
		switch (uriMatcher.match(uri)) 
		{
		case POLS:
			return PoliticianData.CONTENT_TYPE;
			
		case POLS_ID:
			return PoliticianData.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		MatrixCursor result = new MatrixCursor(PoliticianData.PROJECTION);
		String JSONString = SaveFavoritesLocally.getSavedPols();
		JSONObject masterObject = null;
		JSONArray polArray = null;
		JSONObject polObject = null;
		
		try {
			masterObject = new JSONObject(JSONString);
			polArray = masterObject.getJSONArray("Politicians");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (polArray == null)
		{
			return result;
		}
		
		switch (uriMatcher.match(uri)) 
		{
		case POLS:
			for (int x = 0; x < polArray.length(); x++)
			{
				try {
					polObject = polArray.getJSONObject(x);
					result.addRow(new Object[] { x + 1, polObject.get("Name"), polObject.get("Party"),polObject.get("State"),polObject.get("Term Start"),polObject.get("Twitter"),polObject.get("Website")		
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		case POLS_ID:
		
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
