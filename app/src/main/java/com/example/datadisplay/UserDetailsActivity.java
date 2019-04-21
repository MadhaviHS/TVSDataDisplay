package com.example.datadisplay;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailsActivity extends AppCompatActivity {
    public static final int IMAGE_CAPTURE_REQUEST_CODE = 101;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.eId)
    TextView eId;
    @BindView(R.id.salary)
    TextView salary;
    @BindView(R.id.realImage)
    ImageView realImage;
    @BindView(R.id.thumbnailImage)
    ImageView thumbnailImage;
    @BindView(R.id.reTake)
    Button reTake;
    @BindView(R.id.textapToTakePhoto)
    TextView textapToTakePhoto;

    private EmployeeDTO employeeDTO;
    private String orientString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        employeeDTO = getIntent().getParcelableExtra("Employee");
        if (employeeDTO != null)
            initView(employeeDTO);
        realImage.setEnabled(false);
    }

    private void initView(EmployeeDTO employeeDTO) {
        name.setText(employeeDTO.getName());
        designation.setText(employeeDTO.getDesignation());
        city.setText(employeeDTO.getCity());
        eId.setText(employeeDTO.getEid());
        salary.setText(employeeDTO.getSalary());
    }

    @OnClick(R.id.thumbnailImage)
    public void captureImage() {
        Intent i = new Intent(this, ImageCaptureActivity.class);
        startActivityForResult(i, IMAGE_CAPTURE_REQUEST_CODE);
    }

    @OnClick(R.id.reTake)
    public void reTakeClick() {
        createAlertDialog("Are you sure, you want to recapture Image?");
    }

    private void createAlertDialog(String s) {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        String title = "Retake Image";
        ForegroundColorSpan foregroundColorSpan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            foregroundColorSpan = new ForegroundColorSpan(getApplicationContext().getColor(R.color.primaryColorBlue2));
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(title);
        spannableStringBuilder.setSpan(
                foregroundColorSpan,
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        builder.setTitle(spannableStringBuilder)
                .setMessage(s)
                .setPositiveButton("yes", (dialog, which) -> {
                    removeImage();

                })
                .setNegativeButton("no", (dialog, which) -> dialog.dismiss())
                .show();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void removeImage() {
        realImage.setImageBitmap(null);
        thumbnailImage.setVisibility(View.VISIBLE);
        textapToTakePhoto.setVisibility(View.VISIBLE);
        reTake.setVisibility(View.GONE);
        captureImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
                setImageThumbnail(realImage, data);
                reTake.setVisibility(View.VISIBLE);
                thumbnailImage.setVisibility(View.GONE);
                textapToTakePhoto.setVisibility(View.GONE);
            }
        }
    }

    private void setImageThumbnail(ImageView realImage, Intent data) {
        File imageFile = (File) data.getExtras().get("outputImage");
        if (imageFile == null) {
            Toast.makeText(this, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        Integer requiredSize = 1024;
        Bitmap bitmapImage = decodeFile(imageFile, requiredSize);
        realImage.setImageBitmap(bitmapImage);
       /* try(FileOutputStream fOut = new FileOutputStream(imageFile);) {
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error writing image to file", e.getCause());
        }*/
    }

    private Bitmap decodeFile(File imageFile, Integer requiredSize) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(imageFile), null, o);

            // Find the correct scale value. It should be the power of 2.
            int REQUIRED_SIZE = requiredSize;
            /*if(requiredSize != null && requiredSize > 0) {
                REQUIRED_SIZE = requiredSize;
            }*/
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            try {
                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // decode with inSampleSize
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, o2);
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
}
