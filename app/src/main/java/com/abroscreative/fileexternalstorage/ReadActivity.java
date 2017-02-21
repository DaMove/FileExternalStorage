package com.abroscreative.fileexternalstorage;

import android.app.AlertDialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadActivity extends AppCompatActivity {
    EditText mEtFileName;
    TextView mTvDisplayContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        mEtFileName = (EditText) findViewById(R.id.etFileTitle);
        mTvDisplayContent = (TextView) findViewById(R.id.tvFileContent);


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
                    .setMessage("Please remember to declare the uses permission for WRITE EXTERNAL STORAGE")
                    .create().show();
        }
    }

    public void loadExternalCache(View view) throws IOException {
       String fileName =  mEtFileName.getText().toString();
        File externalCacheDir = getExternalCacheDir();

        readFromFile(externalCacheDir, fileName);

    }

    private void readFromFile(File directory, String fileName ) throws IOException {
        File fileToRead = new File(directory, fileName+".txt");
        FileInputStream fis = new FileInputStream(fileToRead);
        int dataRead = -1;
        StringBuilder sb = new StringBuilder();
        while ((dataRead = fis.read())!=-1) {
            sb.append((char)dataRead);
        }

        mTvDisplayContent.setText(sb.toString());
        Toast.makeText(this, fileToRead.getName()+" successfully read from "+directory, Toast.LENGTH_SHORT).show();
        fis.close();
    }

    public void loadPrivateExternal(View view) throws IOException {
        String fileName =  mEtFileName.getText().toString();
        File privateExternalDir = getExternalFilesDir("my_private_folder");

        readFromFile(privateExternalDir, fileName);
    }

    public void loadPublicExternal(View view) throws IOException {

        String fileName =  mEtFileName.getText().toString();
        File publicExternalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        readFromFile(publicExternalDir, fileName);
    }

    public void reset(View view) {
        mTvDisplayContent.setText("");
        mEtFileName.setText("");

    }
}
