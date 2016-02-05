package com.example.pongwiiapp.slidingtablayout;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Tab1 extends Fragment implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void handleResult(Result rawResult) {


        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep06);
        mediaPlayer.start();


        Toast.makeText(getActivity(), "รหัสที่ Scan ได้มาคือ = " + rawResult.getContents() +
                ", โดยการแปลงจาก = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

}