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

	Bitmap baseImage;
	int santaImageResourceId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_overlay_activity);


		baseImage = getBaseImage();

		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		ImageView santaImageView = (ImageView) findViewById(R.id.santaImageView);
		santaImageResourceId = R.drawable.smiley;

		santaImageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), santaImageResourceId));

		imageView.setImageBitmap(baseImage);
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
		int startingX;
		int startingY;
		boolean touchStateScale = false;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				startingX = (int) event.getX();
				startingY = (int) event.getY();

				((ImageView) v).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, startingX, startingY));
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				int scaleX = 20;
				int scaleY = 20;
				if(touchStateScale == true){
					scaleX = (int) (event.getX(0) - event.getX(1));
					scaleY = (int) (event.getY(0) - event.getY(1));
				}
				int endingX = (int) event.getX();
				int endingY = (int) event.getY();

				((ImageView) v).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, endingX, endingY, scaleX, scaleY));
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
}