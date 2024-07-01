package com.sajiman.filehandling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String state;
    private int storageCheckPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
        state = Environment.getExternalStorageState();
        storageCheckPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void initControls() {
        findViewById(R.id.btnWriteToFile).setOnClickListener(this);
        findViewById(R.id.btnReadFromFile).setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 309:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    externalStoragePublic();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 309);
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWriteToFile:
                if (storageCheckPermission == PackageManager.PERMISSION_GRANTED) {
                    externalStoragePublic();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 309);
                }
                externalStoragePrivate();
                internalStorage();
                break;

            case R.id.btnReadFromFile:
                startActivity(new Intent(MainActivity.this, DisplayActivity.class));
                break;
        }
    }

    /*
     * Internal Storage
     */
    private void internalStorage() {
        File internalBasePath = getFilesDir();
        Log.e(MainActivity.class.getSimpleName(), " Internal File Path = =  " + internalBasePath.getAbsolutePath());
        File internalFileTextPath = new File(internalBasePath, "Internal Content");
        if (!internalFileTextPath.exists()) {
            internalFileTextPath.mkdir();
        }
        Log.e(MainActivity.class.getSimpleName(), " Internal File Text Path = =  " + internalFileTextPath.getAbsolutePath());
        File internalFile = new File(internalFileTextPath, "internal_text_file.txt");
        try {
            internalFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(internalFile, true);
            fos.write("Internal Content of the text file".getBytes());
            fos.flush();
            fos.close();
            Toast.makeText(MainActivity.this, "Successfully written", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Failed to write in a internalStorage file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /*
     * External File (Private)
     */
    private void externalStoragePrivate() {
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File privateBasePath = getExternalFilesDir(null);
            Log.e(MainActivity.class.getSimpleName(), "private Base Path " + privateBasePath.getAbsolutePath() + "\n");
            File privateTexFilePath = new File(privateBasePath, "FileHandlePrivate");
            if (!privateTexFilePath.exists()) {
                privateTexFilePath.mkdirs();
            }
            Log.e(MainActivity.class.getSimpleName(), "private txt file path " + privateTexFilePath.getAbsolutePath() + "\n");
            File privateTextFile = new File(privateTexFilePath, "private_text_file.txt");
            try {
                privateTextFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(privateTextFile, true);
                fileOutputStream.write("Sajan private is my hobby;".getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity.this, "Failed to write in a external private file", Toast.LENGTH_SHORT).show();
            Log.e(MainActivity.class.getSimpleName(), "Internal Storage s not ready");
        }
    }

    /*
     * External Storage  (Public)
     */
    private void externalStoragePublic() {
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            Log.e(MainActivity.class.getSimpleName(), "Base Path ==  " + basePath.getAbsolutePath() + "\n");
            File appDirectory = new File(basePath, "FileHandling");
            if (!appDirectory.exists()) {
                appDirectory.mkdir();
            }

            File textFile = new File(appDirectory, "intro.txt");
            Log.e(this.getLocalClassName(), "text file path == =  " + textFile.getAbsolutePath() + "\n");
            try {
                textFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(textFile, true);
                fos.write("Sajan;".getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Failed ot write into the file public external", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Log.e(MainActivity.class.getSimpleName(), "External storage is not ready" + "\n");
        }
    }
}