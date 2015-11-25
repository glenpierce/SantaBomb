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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class PhotoOverlayActivity extends Activity
{

	private String TAG = "photooverlayactivity TAG";

	int x = 0;
	int y = 0;
	int scaleWidth = 20;
	int scaleHeight = 20;

	int touchOffset = 200;

	ImageView imageView;
	Bitmap baseImage;
	int santaImageResourceId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_overlay_activity);

		baseImage = getBaseImage();

		imageView = (ImageView) findViewById(R.id.imageView);
		santaImageResourceId = R.drawable.santa1;

		imageView.setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, 0, 0));
		imageView.setOnTouchListener(moveSantaListener);

	}

	private Bitmap getBaseImage(){
		if(getIntent().hasExtra("filePath")){
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inMutable = true;
			return BitmapFactory.decodeFile(getIntent().getStringExtra("filePath"), opt);
		} else {
			return getBitMapFromUri(getIntent().getData());
		}
	}

	private Bitmap getBitMapFromUri(Uri imageUri){
		try {
			return MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri).copy(Bitmap.Config.ARGB_8888, true);
		}catch (IOException ex) {
			Log.i(TAG, "error retrieving file");
		}
		return null;
	}

	private Bitmap drawSantaOnImage(Bitmap bitmap, int drawableId, int x, int y){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inMutable = true;
		Bitmap overlayBitmap = BitmapFactory.decodeResource(this.getResources(), drawableId, opt);
		Bitmap bitmapToReturn = Bitmap.createBitmap(bitmap);
		Canvas canvas = new Canvas(bitmapToReturn);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawBitmap(overlayBitmap, x, y, null);
		return bitmapToReturn;
	}

	private Bitmap drawSantaOnImage(Bitmap bitmap, int drawableId, int x, int y, int height, int width){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inMutable = true;
		Bitmap overlayBitmap = BitmapFactory.decodeResource(this.getResources(), drawableId, opt);
//		This is causing an out of memory error, scaling is costing us too much memory
//		overlayBitmap = scaleBitmap(overlayBitmap, height, width);
		Bitmap bitmapToReturn = Bitmap.createBitmap(bitmap);
		Canvas canvas = new Canvas(bitmapToReturn);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawBitmap(overlayBitmap, x, y, null);
		return bitmapToReturn;
	}

	private Bitmap scaleBitmap(Bitmap bitmap, int height, int width){
		int startingHeight = bitmap.getHeight();
		int startingWidth = bitmap.getWidth();
		int endingHeight = startingHeight * height/20;
		int endingWidth = startingWidth * width/20;
		bitmap = Bitmap.createScaledBitmap(bitmap, endingWidth, endingHeight, false);
		return bitmap;
	}

	private BitmapDrawable getBitmapDrawable(){
		return new BitmapDrawable(getResources(), getBaseImage());
	}

	View.OnTouchListener moveSantaListener = new View.OnTouchListener() {
		boolean touchStateScale = false;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				x = (int) event.getX() - touchOffset;
				y = (int) event.getY() - touchOffset;

				((ImageView) v).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y));
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				if(touchStateScale == true){
					scaleWidth = (int) (event.getX(0) - event.getX(1));
					scaleHeight = (int) (event.getY(0) - event.getY(1));
				}
				x = (int) event.getX() - touchOffset;
				y = (int) event.getY() - touchOffset;

				((ImageView) v).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
			}
			if(event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
				Log.i(TAG, "second pointer detected");
				touchStateScale = true;
			}
			if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
				Log.i(TAG, "second pointer lost");
				touchStateScale = false;
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				touchStateScale = false;
				return false;
			}
			return true;
		}
	};

	public void onSantaClick(View v) {
		switch(v.getTag().toString()){
			case "santa1":
				santaImageResourceId = R.drawable.santa1;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
			case "santa2":
				santaImageResourceId = R.drawable.santa2;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
			case "santa3":
				santaImageResourceId = R.drawable.santa3;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
			case "santa4":
				santaImageResourceId = R.drawable.santa4;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
			case "santa5":
				santaImageResourceId = R.drawable.santa5;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
			case "santa6":
				santaImageResourceId = R.drawable.santa6;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
			case "santa7":
				santaImageResourceId = R.drawable.santa7;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
			case "santa8":
				santaImageResourceId = R.drawable.santa8;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scaleHeight, scaleWidth));
				break;
		}
	}
}