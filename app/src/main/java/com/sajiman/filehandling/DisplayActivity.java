package com.sajiman.filehandling;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvData;
    private String state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        intControls();
    }

    private void intControls() {
        mTvData = findViewById(R.id.tvData);
        findViewById(R.id.btnReadExternalStoragePublic).setOnClickListener(this);
        findViewById(R.id.btnReadExternalStoragePrivate).setOnClickListener(this);
        findViewById(R.id.btnReadInternalStorage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReadExternalStoragePublic:
                state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    File appDirectory = new File(basePath, "FileHandling");
                    File textFile = new File(appDirectory, "intro.txt");
                    StringBuilder response = readFile(textFile);
                    mTvData.setText(response.toString());
                }
                break;

            case R.id.btnReadExternalStoragePrivate:
                state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File basePath = getExternalFilesDir(null);
                    File appDirectory = new File(basePath, "FileHandlePrivate");
                    File privateFile = new File(appDirectory, "private_text_file.txt");
                    StringBuilder response = readFile(privateFile);
                    mTvData.setText(response.toString());
                }
                break;

            case R.id.btnReadInternalStorage:
                File internalBasePath = getFilesDir();
                File internalFileTextPath = new File(internalBasePath, "Internal Content");
                Log.e(MainActivity.class.getSimpleName(), " Internal File Text Path = =  " + internalFileTextPath.getAbsolutePath());
                File internalFile = new File(internalFileTextPath, "internal_text_file.txt");
                StringBuilder response = readFile(internalFile);
                mTvData.setText(response.toString());
                break;
        }
    }

    private StringBuilder readFile(File textFile) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            String line;
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Log.e(MainActivity.class.getSimpleName(), "Content == == = =" + builder + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
