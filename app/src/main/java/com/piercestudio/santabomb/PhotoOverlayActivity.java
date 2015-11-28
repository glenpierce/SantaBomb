package com.piercestudio.santabomb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoOverlayActivity extends Activity
{

	private String TAG = "photooverlayactivity TAG";

	int x = 0;
	int y = 0;
	int scaleSensitivity = 10;
	int scale = scaleSensitivity;

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
		santaImageResourceId = R.drawable.santa2;

		imageView.setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, 0, 0));
		imageView.setOnTouchListener(moveSantaListener);

		Button sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setOnClickListener(sendOnClickListener);

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

	private Bitmap drawSantaOnImage(Bitmap bitmap, int drawableId, int x, int y, int scale){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inMutable = true;
		Bitmap overlayBitmap = BitmapFactory.decodeResource(this.getResources(), drawableId, opt);
//		This is causing an out of memory error, scaling is costing us too much memory
		overlayBitmap = scaleBitmap(overlayBitmap, scale);
		Bitmap bitmapToReturn = Bitmap.createBitmap(bitmap);
		Canvas canvas = new Canvas(bitmapToReturn);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawBitmap(overlayBitmap, x, y, null);
		return bitmapToReturn;
	}

	private Bitmap scaleBitmap(Bitmap bitmap, int scale){
		int startingHeight = bitmap.getHeight();
		int startingWidth = bitmap.getWidth();
		int endingHeight = startingHeight * scale/scaleSensitivity;
		int endingWidth = startingWidth * scale/scaleSensitivity;
		bitmap = Bitmap.createScaledBitmap(bitmap, endingWidth, endingHeight, false);
		return bitmap;
	}

	private BitmapDrawable getBitmapDrawable(){
		return new BitmapDrawable(getResources(), getBaseImage());
	}

	View.OnTouchListener moveSantaListener = new View.OnTouchListener() {
		boolean touchStateScale = false;
		int startingX, startingY;
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			int fingerDistance = 0;

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				startingX = (int) event.getX();
				startingY = (int) event.getY();
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				if(touchStateScale == true){
					if(fingerDistance == 0) {
						fingerDistance = (int) (event.getX(0) - event.getX(1));
					} else {
						int newFingerDistance = (int) (event.getX(0) - event.getX(1));
						if(newFingerDistance > fingerDistance){
							scale++;
							Log.i(TAG, "scale++");
						}
						if(newFingerDistance < fingerDistance){
							scale--;
							Log.i(TAG, "scale--");
						}
						fingerDistance = newFingerDistance;
					}
					((ImageView) v).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				} else {
					int endingX = (int) event.getX();
					int endingY = (int) event.getY();

					x = x + (endingX - startingX);
					y = y + (endingY - startingY);

					((ImageView) v).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				}
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
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
			case "santa2":
				santaImageResourceId = R.drawable.santa2;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
			case "santa3":
				santaImageResourceId = R.drawable.santa3;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
			case "santa4":
				santaImageResourceId = R.drawable.santa4;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
			case "santa5":
				santaImageResourceId = R.drawable.santa5;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
			case "santa6":
				santaImageResourceId = R.drawable.santa6;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
			case "santa7":
				santaImageResourceId = R.drawable.santa7;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
			case "santa8":
				santaImageResourceId = R.drawable.santa8;
				((ImageView) imageView).setImageBitmap(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));
				break;
		}
	}

	View.OnClickListener sendOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");

			Uri uri = getImageUri(drawSantaOnImage(baseImage, santaImageResourceId, x, y, scale));

			share.putExtra(Intent.EXTRA_STREAM, uri);

			startActivity(Intent.createChooser(share, "Share!"));
		}
	};

	public Uri getImageUri(Bitmap bitmap) {
		File file = new File(getApplication().getFilesDir(), "Santa" + ".jpeg");
		try {
			FileOutputStream out = getApplication().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e){
			Log.i(TAG, "getImageUri Exception");
		}
		String realPath = file.getAbsolutePath();
		File f = new File(realPath);
		return Uri.fromFile(f);
	}
}