package com.example.btandroid_4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editorText= (EditText) findViewById(R.id.text);
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        editorText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN&& keyCode==keyEvent.KEYCODE_ENTER){
                    String message=editorText.getText().toString();
                    ad.setMessage(message);
                    ad.show();
                    return true;
                }
                return false;
            }
        });
    }
}