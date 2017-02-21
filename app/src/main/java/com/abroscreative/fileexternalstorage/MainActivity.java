package com.abroscreative.fileexternalstorage;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText mFileNameEt, mContentEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFileNameEt = (EditText) findViewById(R.id.etFileName);
        mContentEt = (EditText) findViewById(R.id.etContent);
    }

    public void cacheFile(View view) throws IOException {
        File externalCacheDir = getExternalCacheDir();
        String fileName = mFileNameEt.getText().toString();
        String content = mContentEt.getText().toString();

        writeToFile(externalCacheDir, fileName, content);
    }

    private void writeToFile(File directory, String fileName, String content) throws IOException {
        File fileToSave = new File(directory, fileName+".txt");
        FileOutputStream fos = new FileOutputStream(fileToSave);
        fos.write(content.getBytes());
        Toast.makeText(this, fileToSave.getName()+" successfully saved to "+directory, Toast.LENGTH_SHORT).show();
        fos.close();
    }

    public void goToReadActivity(View view) {
        startActivity(new Intent(this, ReadActivity.class));
    }

    public void savePublic(View view) throws IOException {
        File publicExternalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = mFileNameEt.getText().toString();
        String content = mContentEt.getText().toString();
        writeToFile(publicExternalDir, fileName, content);
    }

    public void savePrivate(View view) throws IOException {
        File privateExternalDir = getExternalFilesDir("my_private_folder");
        String fileName = mFileNameEt.getText().toString();
        String content = mContentEt.getText().toString();

        writeToFile(privateExternalDir, fileName, content);
    }

    boolean externalStorageAvailable;
    boolean isWritable;

    @Override
    protected void onStart() {
        super.onStart();

        //Before beginning check if there really is an SD card on device
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            externalStorageAvailable =true;
            isWritable = true;
        }else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            externalStorageAvailable =true;
            isWritable = false;
        }else{
            externalStorageAvailable = isWritable=false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (externalStorageAvailable==false) {
            new AlertDialog.Builder(this)
                    .setTitle("Unavailable SD Card")
                    .setMessage("There is no external storage space on this device")
                    .create().show();
        }

        if(externalStorageAvailable == true && isWritable==true){
            new AlertDialog.Builder(this)
                    .setTitle("Available SD Card and  Writable too")
                    .setNeutralButton("OK",null)
                    .setMessage("Please remember to declare the uses permission for WRITE EXTERNAL STORAGE")
                    .create().show();
        }
    }
}
