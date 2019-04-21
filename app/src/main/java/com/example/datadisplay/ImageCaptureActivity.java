package com.example.datadisplay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCaptureActivity extends AppCompatActivity {

    public final static int EXTERNAL_STORAGE_PERMISSION_REQUEST = 100;
    public static final int CAMERA_CODE = 101;

    private File outPutFile;
    private File tempFile;
    private Uri mImageCaptureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        requestExternalStoragePermission();
    }

    private void requestExternalStoragePermission() {

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST);
        } else {
            outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "image" + ".jpg");
            selectImageOption();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
//            mImageCaptureUri = data.getData();
//            System.out.println("Gallery Image URI : "+mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CAMERA_CODE && resultCode != RESULT_OK) {
            if (outPutFile != null) {
                outPutFile.delete();
            }
            Intent i = new Intent();
            setResult(RESULT_CANCELED, i);
            finish();
        } else {
            finish();
        }
    }

    private void CropingIMG() {
        if (mImageCaptureUri != null) {
            Intent i = new Intent();
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPutFile));
                try {
                    InputStream iStream = getContentResolver().openInputStream(mImageCaptureUri);
                    byte[] inputData = getBytes(iStream);
                    bos.write(inputData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            i.putExtra("outputImage", outPutFile);
            setResult(RESULT_OK, i);
            if (tempFile != null) {
                tempFile.delete();
            }
        } else {
            Toast.makeText(this, "Error capturing image.Please retry", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "image" + ".jpg");
                    selectImageOption();
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    private void selectImageOption() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        //mImageCaptureUri = Uri.fromFile(tempFile);
        mImageCaptureUri = FileProvider.getUriForFile(ImageCaptureActivity.this, getApplicationContext().getPackageName() + ".provider", tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_CODE);
    }
}
