package com.piercestudio.santabomb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class PhotoOverlayActivity extends Activity
{
	private String TAG = "photooverlayactivity TAG";

	Bitmap baseImage;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_overlay_activity);


		baseImage = getBaseImage();

		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setImageBitmap(baseImage);
//		imageView.setImageBitmap(drawSantaOnImage(baseImage, R.drawable.santa1));
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

	private Bitmap drawSantaOnImage(Bitmap bitmap, int drawableId){
		Bitmap overlayBitmap = BitmapFactory.decodeResource(this.getResources(), drawableId);
		Canvas canvas = new Canvas(bitmap);
//		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawBitmap(overlayBitmap, 0, 0, null);
		return bitmap;
	}

	private BitmapDrawable getBitmapDrawable(){
		return new BitmapDrawable(getResources(), getBaseImage());
	}
}