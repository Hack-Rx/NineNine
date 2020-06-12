package com.example.ninenine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileUpdate extends AppCompatActivity {
    DocumentReference Ref=null;
    FirebaseFirestore fStore=FirebaseFirestore.getInstance();

    Button UpdateInfo = null;
    Button ShowInfo;
    TextView profileName=null;
    TextView profileMail=null;
    TextView profilePhone=null;
    TextView profileHeight=null;
    TextView profileWeight=null;
    TextView profileBmi;
    static String pName,pEmail,pPhone,pHeight,pWeight,pBmi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        UpdateInfo= findViewById(R.id.UpdateInfo);
        ShowInfo= findViewById(R.id.showInfo);
        profileName = findViewById(R.id.profileName);
        profileMail = findViewById(R.id.profileMail);
        profilePhone = findViewById(R.id.profilePhone);
        profileHeight = findViewById(R.id.ProfileHeight);
        profileWeight = findViewById(R.id.ProfileWeight);
        profileBmi = findViewById(R.id.ProfileBmi);
        try{
            UpdateInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ProfileUpdate.this,Details.class));
                }
            });
        }catch (NullPointerException ignored){

        }
        ShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  try{
                Ref = fStore.collection("users").document(Login.userID);
            }catch (NullPointerException ignored){

            }
                Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot snapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {

                            System.out.println("Listen failed."+ e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            pName=snapshot.get("fName").toString();
                            pEmail=snapshot.get("email").toString();
                            pPhone=snapshot.get("phone").toString();
                            pHeight=snapshot.get("Height").toString();
                            pWeight=snapshot.get("Weight").toString();
                            pBmi=snapshot.get("BMI").toString();




                            //name.setText(namee);

                        } else {
                            System.out.println("Current data: null");
                        }
                    }
                });
                profileName.setText(pName);
                profileMail.setText(pEmail);
                profilePhone.setText(pPhone);
                profileHeight.setText(pHeight);
                profileWeight.setText(pWeight);
                profileBmi.setText(pBmi);
            }
        });

    }
}