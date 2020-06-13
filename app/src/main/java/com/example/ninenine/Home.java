package com.example.ninenine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import org.eazegraph.lib.charts.PieChart;

public class Home extends AppCompatActivity {

    private static String TAG = "Home";
    Button FoodRecords,DietSuggestions;
    static double dayTotal;
    static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    static String Date=timestamp.toString().substring(0,10);
    private float[] yData;
    private String[] xData = {"NEEDED", "CONSUMED" };
    PieChart pieChart;
    FirebaseFirestore fStore=FirebaseFirestore.getInstance();
    TextView name=null;TextView mail=null;
    DocumentReference Ref;
    private Button gadd;
    private TextView gaddtext;


    static String namee, emaill,gender,bmi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavigationView navigationView= findViewById(R.id.nav_view);
        gadd=findViewById(R.id.qadd);
        gaddtext=findViewById(R.id.qaddtext);
        gadd.setVisibility(View.INVISIBLE);

        getPrev();
        System.out.println("hey");
    //mail.setText(emaill);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(Home.this,ProfileUpdate.class));
                        break;
                    case R.id.recomend:
                        startActivity(new Intent(Home.this,Recomendation.class));
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
                        builder1.setTitle("Are you sure you want to Log Out?");
                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Home.this, Login.class));

                            }
                        });
                        builder1.setNegativeButton("No", null);
                        builder1.show();
                        break;
                    case R.id.personalization:
                        startActivity(new Intent(Home.this,Personalization.class));
                        break;
                }

            return true;
            }
        });
        DietSuggestions=findViewById(R.id.DietSuggestions);
        FoodRecords=findViewById(R.id.foodRecords);

        fStore = FirebaseFirestore.getInstance();
        DietSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Suggestions.class));
            }
        });
        FoodRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),History.class));
            }
        });
        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setDescription("Consumed and needed chart ");
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(80f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Consumed vs Needed");
        pieChart.setCenterTextSize(15);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!
        Ref = fStore.collection("users").document(Login.userID);
        try{
            Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot snapshot,
                                    FirebaseFirestoreException e) {
                    if (e != null) {

                        System.out.println("Listen failed."+ e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        try{namee=snapshot.get("fName").toString();
                        emaill=snapshot.get("email").toString();
                        gender=snapshot.get("Gender").toString();
                        bmi=snapshot.get("BMI").toString();
                        System.out.println(gender);
                        name = findViewById(R.id.tvName);
                        mail= findViewById(R.id.tvEmail);
                        name.setText(namee);
                        mail.setText(emaill);}
                        catch (NullPointerException ignored){

                        }
                    } else {
                        System.out.println("Current data: null");
                    }
                }
            });
        }catch (NullPointerException ignored){

        }




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

                    dayTotal=Double.parseDouble(""+snapshot.get("Day_Total"));
                    System.out.println(dayTotal);


                } else {
                    System.out.println("Current data: null");
                }
                addDataSet();
            }
        });


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());
                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for(int i = 0; i < yData.length; i++){
                    if(yData[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(Home.this, "Needed Calories: " +2000  + "\n" + "Consumed: " +dayTotal + "K", Toast.LENGTH_LONG).show();
        }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    private void addDataSet(){
        Log.d(TAG, "running");

        Log.d(TAG, "addDataSet started"+gender);
        yData = new float[]{2000f,(float)dayTotal};
        try {
            if (gender.equals("Male")) yData = new float[]{2000f, (float)dayTotal};
            else yData = new float[]{1500f, (float) dayTotal};
        }
        catch (NullPointerException ignored){

        }
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        System.out.println();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Calories");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        pieDataSet.setColors(new int[] {R.color.myblue,R.color.myred}, this);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void getPrev(){
        String yesDay=Date.substring(0,9)+(Integer.parseInt(Date.substring(9,10))-1);
        String qaddfood;
//        Toast.makeText(Home.this, yesDay, Toast.LENGTH_LONG).show();
//        System.out.println(yesDay);
        try{
            final CollectionReference docRef = fStore.collection("users").document(Login.userID).collection("daily").document(yesDay).collection("FoodItems");
            docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    FoodItems item=document.toObject(FoodItems.class);

                                    java.util.Date date=item.getTimestamp();
                                    java.util.Date date1=new java.util.Date(System.currentTimeMillis());
                                    if(date1.getTime()-date.getTime()>82800000 && date1.getTime()-date.getTime()<90000000){
                                        final FoodItems item1=item;
                                        gadd.setVisibility(View.VISIBLE);
                                        gaddtext.setText("Yesterday at this time you had "+item.getFood_Name());
                                        item1.setTimestamp(date1);
                                        final DocumentReference documentReference = fStore.collection("users").document(Login.userID).collection("daily").document(Home.Date).collection("FoodItems").document(document.getId());
                                        gadd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                documentReference.set(item1);
                                            }
                                        });
                                    }
                                    System.out.println(item.getFood_Name());
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }catch (NullPointerException ignored){

        }


    }
}

