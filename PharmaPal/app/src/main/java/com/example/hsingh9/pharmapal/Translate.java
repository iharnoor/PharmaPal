package com.example.hsingh9.pharmapal;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class Translate extends AppCompatActivity {
    TextToSpeech t1;
    EditText ed1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        ed1 = findViewById(R.id.editText);
        b1 = findViewById(R.id.button);

        ed1.setText("\"Excedrin\": la aspirina / paracetamol / cafeína es un medicamento combinado para el tratamiento del dolor, especialmente el dolor de cabeza por tensión y la migraña");
        ed1.setText("\"Excedrin\"：阿司匹林/对乙酰氨基酚/咖啡因是一种用于治疗疼痛，特别是紧张性头痛和偏头痛的组合药物。");
        ed1.setText("\"एक्सेड्रिन\": एस्पिरिन / पैरासिटामोल / कैफीन दर्द के इलाज के लिए एक संयोजन दवा है, विशेष रूप से तनाव सिरदर्द और माइग्रेन।");

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale locSpanish = new Locale("spa", "MEX");
//                    t1.setLanguage(locSpanish);
//                    t1.setLanguage(Locale.CHINA);
//                    Hindi
                    t1.setLanguage(Locale.forLanguageTag("hin"));
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = ed1.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
