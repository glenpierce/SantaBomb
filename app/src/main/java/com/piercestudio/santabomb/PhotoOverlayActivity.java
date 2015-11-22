package com.piercestudio.santabomb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

public class PhotoOverlayActivity extends Activity
{
	private String TAG = "photooverlayactivity TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_overlay_activity);


	}

	private Bitmap getBitMapFromUri(Uri imageUri){
		try {
			return MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
		}catch (IOException ex) {
			Log.i(TAG, "error retrieving file");
		}
		return null;
	}
}
