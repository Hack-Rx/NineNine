package com.example.ninenine;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity2 extends AppCompatActivity {

    String serveUnit = "";
    private  int positionFood2;
    TextView food_item,calories,calories_from_fat,protein,serveunit,brand,TotalCalories;
    Button qty,add;
    EditText input;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        Intent intent = getIntent();
        positionFood2 = intent.getIntExtra("position",0);
        fStore = FirebaseFirestore.getInstance();


        getSupportActionBar().setTitle("Details of "+Preferred.calorieCountList2.get(positionFood2).getFooditem());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        qty = findViewById(R.id.btnQty2);
        add = findViewById(R.id.btnAdd2);
        food_item = findViewById(R.id.food_item2);
        calories = findViewById(R.id.calories2);
        calories_from_fat = findViewById(R.id.calories_from_fat2);
        protein= findViewById(R.id.proteins2);
        serveunit = findViewById(R.id.serve_unit2);
        brand = findViewById(R.id.brand2);
        TotalCalories = findViewById(R.id.tvTotalCalories2);

        food_item.setText(Preferred.calorieCountList2.get(positionFood2).getFooditem());
        calories.setText(Preferred.calorieCountList2.get(positionFood2).getCalories());
        calories_from_fat.setText(Preferred.calorieCountList2.get(positionFood2).getCaloriesfromfat());
        protein.setText(Preferred.calorieCountList2.get(positionFood2).getProteins());
        serveunit.setText(Preferred.calorieCountList2.get(positionFood2).getServing());
        brand.setText(Preferred.calorieCountList2.get(positionFood2).getBrand());
        qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adding();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity2.this);
                builder1.setTitle("Are you sure you want to add?");

                builder1.setView(input);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addToDatabase();
                        addDayCalToDatabase();

                    }
                });builder1.setNegativeButton("No",null);
                builder1.show();


            }
        });


        final DocumentReference docRef = fStore.collection("users").document(Login.userID).collection("daily").document(Home.Date);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot,
                                FirebaseFirestoreException e) {
                if (e != null) {
                    System.out.println("Listen failed."+ e);
                    //Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    System.out.println("Current data: " + snapshot.getData());
                    Home.dayTotal=Double.parseDouble(""+snapshot.get("Day_Total"));
                    TotalCalories.setText("Total Calories: "+snapshot.get("Day_Total"));
                } else {
                    System.out.println("Current data: null");
                }            }
        });


    }
    public void adding(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the Serve Unit");
        input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cal =Preferred.calorieCountList2.get(positionFood2).getCalories();
                serveUnit = input.getText().toString();
                double totalcalories = Double.parseDouble(cal)*Double.parseDouble(serveUnit);
                String totalCal = Double.toString(totalcalories);
                Home.dayTotal+=totalcalories;
                try{
                    TotalCalories.setText("Total Calories: "+Home.dayTotal);

                }catch(NullPointerException ignored){

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void addToDatabase(){
        Date timestamp=new Date();
        FoodItems foodDetails=new FoodItems();
        foodDetails.setCalories(Preferred.calorieCountList2.get(positionFood2).getCalories());
        foodDetails.setFood_Name(Preferred.calorieCountList2.get(positionFood2).getFooditem());
        foodDetails.setQuantity(serveUnit);
        foodDetails.setTimestamp(timestamp);
        final DocumentReference documentReference = fStore.collection("users").document(Login.userID).collection("daily").document(Home.Date).collection("FoodItems").document(Preferred.calorieCountList2.get(positionFood2).getFoodid());
        documentReference.set(foodDetails, SetOptions.merge());
    }
    public void addDayCalToDatabase(){
        Map<String,Object> dayCalDetails = new HashMap<>();
        dayCalDetails.put("Day_Total",Home.dayTotal);
        final DocumentReference documentReference = fStore.collection("users").document(Login.userID).collection("daily").document(Home.Date);
        documentReference.set(dayCalDetails);
    }
}