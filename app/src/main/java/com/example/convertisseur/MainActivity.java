package com.example.convertisseur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    EditText murl;
    Button mdownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialiser les view
        murl = findViewById(R.id.url);
        mdownload = findViewById(R.id.download);



        // handle button click
        mdownload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // if OS is Marshmellow or above, handle runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied, request it
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show pop for runtime permission
                        requestPermissions(permission, PERMISSION_STORAGE_CODE);

                    } else {
                        startDownloading();
                        murl.setText("");
                    }

                } else {
                    startDownloading();
                    murl.setText("");
                }
            }
        });
    }

    private void startDownloading() {
        //get url from edit text
        String url = murl.getText().toString().trim();

        //create download request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //allow types of network to download files
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file ...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ System.currentTimeMillis());

        //get download
        DownloadManager maneger= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        maneger.enqueue(request);
    }



    //handle permission result
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE: {
                if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownloading();
                }else{
                    Toast.makeText(this, "Permission denied..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}