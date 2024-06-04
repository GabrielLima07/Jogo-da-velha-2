package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jogo_da_velha_2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GameResultWin extends AppCompatActivity {
    Button popupBTN;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result_win);
        popupBTN = findViewById(R.id.popupBTN);

        mDialog = new Dialog(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWin();
            }
        });
    }

    private void showPopUpWin() {
        mDialog.setContentView(R.layout.popupwin);
        Button buttonX = mDialog.findViewById(R.id.buttonx);
        Button buttonShare = mDialog.findViewById(R.id.buttonCompartilhar);
        Button buttonContinue = mDialog.findViewById(R.id.buttonContinue);
        Intent i = new Intent(this, MainActivity.class);

        buttonContinue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(i);
            }
        });
        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    private void takeScreenshot() {
        View rootView = getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        saveScreenshot(bitmap);
    }

    private void saveScreenshot(Bitmap bitmap) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Screenshots";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
        File file = new File(dir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            Toast.makeText(GameResultWin.this, "Screenshot saved: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            // Share the screenshot
            shareScreenshot(file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(GameResultWin.this, "Error saving screenshot", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareScreenshot(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }
}
