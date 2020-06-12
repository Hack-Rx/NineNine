package com.example.ninenine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.Timestamp;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class History extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirestoreRecyclerOptions<FoodItems> options;
    Adapter mAdapter;
    FirebaseFirestore fstore=FirebaseFirestore.getInstance();

    CalendarView mCalendarView;
    Button delete;Button AddCals;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String Date=timestamp.toString().substring(0,10);
    CollectionReference notebookRef=fstore.collection("users").document(Login.userID).collection("daily").document(Date).collection("FoodItems");
    ArrayList<FoodItems> data;
    TextView daycalories;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);
            data=new ArrayList<>();
            delete=findViewById(R.id.delete);
            mCalendarView=findViewById(R.id.calendarView);
            mRecyclerView=findViewById(R.id.recyclerView);
            daycalories=findViewById(R.id.DayCal);
            AddCals = findViewById(R.id.btnAddCal);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            getData();

            mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    if ((month+1)/10==0 && dayOfMonth/10==0)Date=year+"-0"+(month+1)+"-0"+dayOfMonth;
                    else if ((month+1)/10==0 && dayOfMonth/10!=0)Date=year+"-0"+(month+1)+"-"+dayOfMonth;
                    else if ((month+1)/10!=0 && dayOfMonth/10==0)Date=year+"-"+(month+1)+"-0"+dayOfMonth;
                    else Date=year+"-"+(month+1)+"-"+dayOfMonth;
                    FirebaseFirestore fStore=FirebaseFirestore.getInstance();
                    DocumentReference Ref = fStore.collection("users").document(Login.userID).collection("daily").document(Date);
                    Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot snapshot,
                                            FirebaseFirestoreException e) {
                            if (e != null) {

                                System.out.println("Listen failed."+ e);
                                return;
                            }
                            if (snapshot != null && snapshot.exists()) {
                                daycalories.setText(snapshot.get("Day_Total").toString());


                            } else {
                                System.out.println("Current data: null");
                            }
                        }
                    });
                  notebookRef=fstore.collection("users").document(Login.userID).collection("daily").document(Date).collection("FoodItems");
                    getData();
                }
            });
            AddCals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(History.this,Calories.class));
                }
            });





        }
    public void getData(){
        options=new FirestoreRecyclerOptions.Builder<FoodItems>().setQuery(notebookRef,FoodItems.class).build();
        mAdapter=new Adapter(options);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void OnDeleteClick(int position) {
                mAdapter.deleteItem(position);
            }
        });


    }
}


