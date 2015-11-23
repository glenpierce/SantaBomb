package com.piercestudio.santabomb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class PhotoOverlayActivity extends Activity
{
	private String TAG = "photooverlayactivity TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_overlay_activity);


		Bitmap baseImage = getBaseImage();

		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setImageBitmap(baseImage);
	}

	private Bitmap getBaseImage(){
		if(getIntent().hasExtra("filePath")){
			return BitmapFactory.decodeFile(getIntent().getStringExtra("filePath"));
		} else {
			return getBitMapFromUri(getIntent().getData());
		}
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
