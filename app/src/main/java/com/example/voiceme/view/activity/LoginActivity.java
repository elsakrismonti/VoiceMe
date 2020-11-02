package com.example.voiceme.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.voiceme.R;

public class LoginActivity extends AppCompatActivity {
    private EditText eTPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize() {
        eTPhoneNumber = findViewById(R.id.eTPhoneNumber);
    }

    public void loginAction(View view) {
        Intent i = new Intent(this, VerificationActivity.class);
        i.putExtra("phoneNumber",eTPhoneNumber.getText().toString());
        startActivity(i);

    }

}