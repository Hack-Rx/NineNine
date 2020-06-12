package com.example.ninenine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Personalization extends AppCompatActivity {
    EditText perCalories;
    EditText perFoodItem;
    static  String pc; static  String pfi;
    Button proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization);
        perCalories = findViewById(R.id.perCalories);
        perFoodItem = findViewById(R.id.perFoodItem);
        proceed = findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pc=perCalories.getText().toString();
                pfi=perFoodItem.getText().toString();
                startActivity(new Intent(Personalization.this,Preferred.class));
            }
        });
    }
}