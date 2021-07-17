package com.example.cam_ex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    Button mButtonPic;
    Uri photoUri;

    public static final int REQUEST_IMAGE_CAPTURE = 5;


    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
          //  Manifest.permission.MANAGE_EXTERNAL_STORAGE

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkStoragePermissions();
        mButtonPic = findViewById(R.id.btn_pic);
        mButtonPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePhoto();
            }
        });
    }


    private void takePhoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this
                        , "com.example.cam_ex.FileProvider",
                        photoFile);


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }


    }

/*

    public static File createFolder(String folderName) {
        File myFolder = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!myFolder.exists()) {
            if (myFolder.mkdirs()) {
                return myFolder;
            } else {
                Log.e(TAG , "Directory not created");
                return null;

            }
        }
        return myFolder;
    }

    public static File createFile(String folderName, String fileName) {
        return new File(Objects.requireNonNull(createFolder(folderName)).getPath(), fileName);
    }
*/

    private File createImageFile() {
        File image = null;
        try {
            String file_name = String.valueOf(Calendar.getInstance().getTime().getTime());
         //   File folder = new File(Environment.getExternalStorageDirectory(), "fara");
        //    File folder = new File("sdcard/fara");
            File folder =Environment.
                    getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS);

            if (!folder.exists()) {


                try {
                    if (folder.mkdirs()) {
                        Log.e(TAG, "create");
                    } else {
                        Log.e(TAG, "not create");
                    }
                }catch (Exception e){
                    Log.e(TAG , "error" + e.getMessage().toString());

                }


            }
            image = new File(folder, file_name + ".jpg");

            return image;
        } catch (Exception e) {
            Log.i(TAG, "createImageFile: " + e.getMessage())
            ;
            return null;
        }


    }

    private void checkStoragePermissions() {
        int permissionWrite = ActivityCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ActivityCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE);
    /*    int permissionManage = ActivityCompat.checkSelfPermission
                (this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);*/



        if (permissionWrite != PackageManager.PERMISSION_GRANTED
                || permissionRead != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(
                    this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE
            );
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String l = photoUri.toString();
            Log.d(TAG, "onActivityResult: " + l);
        }
    }
}