package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView mScannerView;
    String scannedURL;
    PackageManager mPackageManager;
    String TAG = "ScanCodeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView =  new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void handleResult(Result result) {
        scannedURL = result.getText();
        Log.d(TAG, "Scanned URL = " + scannedURL);
        startWebPage(scannedURL);
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Paused");
        mScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed");
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    private void startWebPage(String scannedURL){
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(scannedURL));
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    mPackageManager = getPackageManager();
    String appChooser = getResources().getString(R.string.app_chooser);
    Intent chooser = Intent.createChooser(intent, appChooser);
    chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if(intent.resolveActivity(mPackageManager)!=null)
        getApplicationContext().startActivity(chooser);
    }
}
