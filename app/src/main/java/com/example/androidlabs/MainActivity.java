package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        Button myBtn = findViewById(R.id.myBtn);

        myBtn.setOnClickListener(e-> Toast.makeText(this,R.string.toast_message,Toast.LENGTH_LONG).show());

        CheckBox myCheck = findViewById(R.id.myCheck);

        myCheck.setOnCheckedChangeListener((cn, b)->{
            String str=b?this.getString(R.string.checkOn):this.getString(R.string.checkOff);
            Snackbar snackbar = Snackbar.make(myCheck, str, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, click -> myCheck.setChecked(!b));
            snackbar.show();

        });

        Switch mySwitch = findViewById(R.id.mySwitch);

        mySwitch.setOnCheckedChangeListener((cn,b)->{
            String str = b? this.getString(R.string.switchIsOn):this.getString(R.string.switchIsOff);
            Snackbar snackbar = Snackbar.make(myCheck, str, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, click -> mySwitch.setChecked(!b));
            snackbar.show();
        });

    }
}