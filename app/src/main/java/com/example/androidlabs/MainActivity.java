package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);

        EditText emailText = findViewById(R.id.emailText);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(e->
                {
                    Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                    goToProfile.putExtra("EMAIL", emailText.getText().toString());
                    startActivity(goToProfile);
                }
                );
        prefs = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String emailSaved = prefs.getString("email","");
        emailText.setText(emailSaved);

    }


    @Override
    protected void onPause() {
        super.onPause();
        prefs = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        EditText emailText = findViewById(R.id.emailText);
        saveSharedPrefs(emailText.getText().toString());
    }

    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", stringToSave);
        editor.commit();
    }

}