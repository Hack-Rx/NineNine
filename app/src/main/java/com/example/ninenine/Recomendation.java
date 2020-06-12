package com.example.ninenine;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recomendation extends AppCompatActivity {
    RecyclerView mRecyclerView;
    FirestoreRecyclerOptions<FoodItems> options;
    Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendation);

        mRecyclerView=findViewById(R.id.recyclerView2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }
    public void getData(){
        FirebaseFirestore fStore=FirebaseFirestore.getInstance();
        CollectionReference Ref = fStore.collection("users").document(Login.userID).collection("Recomendation");
        options=new FirestoreRecyclerOptions.Builder<FoodItems>().setQuery(Ref,FoodItems.class).build();
        mAdapter=new Adapter(options);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
    }
}
