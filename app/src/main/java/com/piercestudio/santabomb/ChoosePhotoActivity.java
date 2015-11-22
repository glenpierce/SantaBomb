package com.piercestudio.santabomb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChoosePhotoActivity extends AppCompatActivity
{
	private String TAG = "choosephotoactivity TAG";

	static final int REQUEST_CODE_SELECT_PHOTO = 0;
	static final int REQUEST_CODE_TAKE_PHOTO = 1;
	String mCurrentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_photo_activity);

		Button selectPictureButton = (Button) findViewById(R.id.selectPictureButton);
		selectPictureButton.setOnClickListener(selectPicture);

		Button takePictureButton = (Button) findViewById(R.id.takePictureButton);
		takePictureButton.setOnClickListener(takePicture);

	}

	View.OnClickListener selectPicture = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_SELECT_PHOTO);
		}
	};

	View.OnClickListener takePicture = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			dispatchTakePictureIntent();
		}
	};

	private void dispatchTakePictureIntent()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			File photoFile = null;
			photoFile = createImageFile();
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
			}
		}
	}

	private File createImageFile()
	{
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = "JPEG_" + timeStamp;
			File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File image = new File(storageDir, imageFileName + ".jpg");
			Log.i(TAG, image.getAbsolutePath());

			return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
				Uri imageUri = (Uri) data.getExtras().get(MediaStore.EXTRA_OUTPUT);
				startPhotoOverlayActivity(imageUri);
			}else if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
				Uri imageUri = (Uri) data.getData();
				startPhotoOverlayActivity(imageUri);
			}
		}
	}

	private void startPhotoOverlayActivity(Uri imageUri){
		Intent intent = new Intent(this, PhotoOverlayActivity.class);
		intent.setData(imageUri);
		startActivity(intent);
	}
}