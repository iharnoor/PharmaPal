package com.example.hsingh9.pharmapal;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static com.example.hsingh9.pharmapal.MainActivity.t1;

public class AudioActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        String output;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                output = null;
            } else {
                output = extras.getString("output");
            }
        } else {
            output = (String) savedInstanceState.getSerializable("output");
        }

        Toast.makeText(this, "Output: " + output, Toast.LENGTH_LONG).show();

        String toSpeak = output;
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
}
