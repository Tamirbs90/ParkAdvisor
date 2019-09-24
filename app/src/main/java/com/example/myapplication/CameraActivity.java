package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.monscanner.ScanActivity;
import com.example.monscanner.ScanConstants;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CameraActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 7;
    private TextView tvHello;
    private ImageView imageView;
    private AppLogic appLogic;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        StringBuilder welcomeSb = new StringBuilder();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = (ImageView)findViewById(R.id.ImageView);
        tvHello = (TextView)findViewById(R.id.helloText);
        appLogic= AppLogic.getInstance(getApplicationContext());
        String username = appLogic.getUsername().toLowerCase();
        String cap = username.substring(0, 1).toUpperCase() + username.substring(1);
        welcomeSb.append(cap);
        welcomeSb.append(", \n");
        welcomeSb.append(getResources().getString(R.string.welcome));
        tvHello.setText(welcomeSb.toString());
    }


    public void openGallery(View view)
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else
        {
            startScan(ScanConstants.OPEN_GALLERY);
        }

    }


    public void openCamera(View view)
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        else {
            startScan(ScanConstants.OPEN_CAMERA);
        }
    }

    private void startScan(int preference)
    {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_CODE)
            {
                try
                {
                    assert data != null;
                    Uri imageUri = Objects.requireNonNull(data.getExtras()).getParcelable(ScanActivity.SCAN_RESULT);
                    assert imageUri != null;
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap scannedImage = BitmapFactory.decodeStream(imageStream);
                    getContentResolver().delete(imageUri, null, null);
                    appLogic.matchUserImageToDbImages(scannedImage);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                finally {
                    Intent startIntent = new Intent(getApplicationContext(), DetailsActivity.class);
                    startActivity(startIntent);
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
