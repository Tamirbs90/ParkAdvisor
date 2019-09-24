package com.example.monscanner;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ScanActivity extends AppCompatActivity implements ComponentCallbacks2 {

    private final String TAG = "ScanActivityDebug";

    public static final String SCAN_RESULT = "scan_result";
    private boolean onEditionFragment;
    static {
        System.loadLibrary("opencv_java4");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_layout);
        handleIntentPreference();
        onEditionFragment = false;
    }

    private void handleIntentPreference() {
        int preference = getPreferenceContent();
        if (preference == ScanConstants.OPEN_CAMERA) {
            openCamera();
        } else if (preference == ScanConstants.OPEN_GALLERY) {
            openMediaContent();
        }
    }

    protected int getPreferenceContent() {
        return getIntent().getIntExtra(ScanConstants.OPEN_INTENT_PREFERENCE, 0);
    }

    public void openMediaContent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, ScanConstants.PICKFILE_REQUEST_CODE);
    }

    public void openCamera() {
        Intent intent = new Intent(this, CameraBackendActivity.class);
        startActivityForResult(intent, ScanConstants.START_CAMERA_REQUEST_CODE);
    }

    void clearTempImages() {
        try {
            File tempFolder = new File(ScanConstants.IMAGE_PATH);
            for (File f : tempFolder.listFiles())
                if (!f.delete()){}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            try {
                switch (requestCode) {
                    case ScanConstants.START_CAMERA_REQUEST_CODE:
                        Uri imageUri = Objects.requireNonNull(data.getExtras()).getParcelable(ScanConstants.PHOTO_RESULT);
                        bitmap = getBitmap(imageUri);
                        break;

                    case ScanConstants.PICKFILE_REQUEST_CODE:
                        bitmap = getBitmap(data.getData());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
        if (bitmap != null) {
            postImagePick(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void postImagePick(Bitmap bitmap) {
        Uri uri = getUri(bitmap);
        bitmap.recycle();
        onBitmapSelect(uri);
    }

    public void onBitmapSelect(Uri uri) {
        ShapeValidationFragment fragment = new ShapeValidationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScanConstants.SELECTED_BITMAP, uri);
        fragment.setArguments(bundle);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(ShapeValidationFragment.class.toString());
        fragmentTransaction.commit();
    }

    private Bitmap getBitmap(Uri selectedimg) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        AssetFileDescriptor fileDescriptor;
        fileDescriptor = getContentResolver().openAssetFileDescriptor(selectedimg, "r");
        assert fileDescriptor != null;
        return BitmapFactory.decodeFileDescriptor(
        fileDescriptor.getFileDescriptor(), null, options);
    }

    public static Uri getUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap,mat);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
        Imgcodecs.imwrite(ScanConstants.IMAGE_PATH+"/originale.jpg",mat);
        return Uri.parse(ScanConstants.IMAGE_PATH+"/originale.jpg");
    }

    public void onScanFinish(Uri uri) {
        EditionFragment fragment = new EditionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScanConstants.SCANNED_RESULT, uri);
        fragment.setArguments(bundle);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(EditionFragment.class.toString());
        fragmentTransaction.commit();
        onEditionFragment = true;
    }

    Bitmap scaledBitmap(Bitmap bitmap, int width, int height) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    @Override
    public void onTrimMemory(int level) {
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.little_memory))
                        .setMessage(getResources().getString(R.string.little_memory_desc))
                        .create()
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed(){
        if (!onEditionFragment)
            finish();
        else {
            super.onBackPressed();
            onEditionFragment = false;
        }
    }
}

