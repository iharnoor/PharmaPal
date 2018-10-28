package com.example.hsingh9.pharmapal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class FirstActivity extends AppCompatActivity {

    private Button btnEnglish;
    private Button btnSpanish;
    private Button btnChinese;
    private Button btnHindi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        btnEnglish = findViewById(R.id.btnEnglish);
        btnChinese = findViewById(R.id.btnChinese);
        btnHindi = findViewById(R.id.btnHindi);
        btnSpanish = findViewById(R.id.btnSpanish);

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.LANGUAGE = Utils.ENGLISH;
                Utils.LOCALE = Locale.US;
                launchMainActivity();
            }
        });

        btnChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.LANGUAGE = Utils.CHINESE;
                Utils.LOCALE = Locale.CHINA;
                launchMainActivity();
            }
        });

        btnSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.LANGUAGE = Utils.SPANISH;
                Utils.LOCALE = new Locale("spa", "MEX");
                launchMainActivity();
            }
        });

        btnHindi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Utils.LANGUAGE = Utils.HINDI;
                Utils.LOCALE = Locale.forLanguageTag("hin");
                launchMainActivity();
            }
        });
    }

    public void launchMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
