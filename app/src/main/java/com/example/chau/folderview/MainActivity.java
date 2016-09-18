package com.example.chau.folderview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


public class MainActivity extends Activity implements View.OnClickListener {
    public final static String EXTRA_MESSAGE = "MESSAGE";

    private static final int REQUEST_PICK_FILE = 1;

    private TextView filePath;
    private Button Browse;
    private File selectedFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePath = (TextView)findViewById(R.id.file_path);
        Browse = (Button)findViewById(R.id.browse);
        Browse.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.browse:
                Intent intent = new Intent(this, FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            switch(requestCode) {
                case REQUEST_PICK_FILE:
                    if(data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {
                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        filePath.setText(selectedFile.getPath());

                        String extension = selectedFile.getPath().substring(selectedFile.getPath().lastIndexOf(".") + 1, selectedFile.getPath().length());
                        if(extension.equals("jpg")){
                            Log.d("Linksebd",selectedFile.getPath());
                            sendMessage(selectedFile.getPath());
                        }
                        //show img
                    }
                    break;
            }
        }
    }

    public void sendMessage(String link) {
        Intent intent = new Intent(this, FullscreenActivity.class);
        ///EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, link);
        startActivity(intent);
    }
}
