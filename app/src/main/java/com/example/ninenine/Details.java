package com.example.ninenine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Details extends AppCompatActivity {
    private SeekBar seek_bar1;
    private TextView text_view1;
    private SeekBar seek_bar2;
    private TextView text_view2;
    public RadioButton radio_btn;
    public RadioGroup radio_group;
    private Button done;
    public int height;
    public int weight;
    public String gender;
    private ProgressBar bmiBar;
    private TextView editBmi;
    double BMI;

    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        seek_bar1 = (SeekBar)findViewById(R.id.seekHeight);
        text_view1 =(TextView)findViewById(R.id.heightVal);
        seek_bar2 = (SeekBar)findViewById(R.id.seekWeight);
        text_view2 =(TextView)findViewById(R.id.weightVal);
        radio_group=findViewById(R.id.RadGroup);
        done=findViewById(R.id.Done);
        editBmi=findViewById(R.id.editBmi);
        bmiBar=findViewById(R.id.bmiBar);

        fStore = FirebaseFirestore.getInstance();

        enterGender();
        enterHeight();
        enterWeight();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDatabase();
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });


    }
    public void enterGender(){
        final int radioID= radio_group.getCheckedRadioButtonId();

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_btn=findViewById(checkedId);
                gender=radio_btn.getText().toString();
            }
        });


    }
    public void enterHeight(){

        text_view1.setText((seek_bar1.getProgress()+100) + " Cm");
        seek_bar1.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        text_view1.setText((seek_bar1.getProgress()+100) + " Cm");
                        height = Integer.parseInt(text_view1.getText().toString().replaceAll("[^0-9]", ""));
                        setBMI();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view1.setText((seek_bar1.getProgress()+100) + " Cm");

                    }
                }
        );
    }
    public void enterWeight(){
        text_view2.setText((seek_bar2.getProgress()+20) + " Kg");
        seek_bar2.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        text_view2.setText((seek_bar2.getProgress()+20) + " Kg");
                        weight = Integer.parseInt(text_view2.getText().toString().replaceAll("[^0-9]", ""));
                        setBMI();
                        //Toast.makeText(Details.this,"SeekBar in progress",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view2.setText((seek_bar2.getProgress()+20) + " Kg");

                        //Toast.makeText(Details.this,"SeekBar in StopTracking",Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
    public void setBMI(){
        DecimalFormat df2 = new DecimalFormat("#.##");
        BMI=weight/Math.pow((height),2)*10000;
        bmiBar.setProgress((int) BMI*3);
        if(BMI<=18.5){
            editBmi.setText("BMI: "+df2.format(BMI)+" Underweight");
            bmiBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        }
        else if(BMI<24.9){
            bmiBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            editBmi.setText("BMI: "+df2.format(BMI)+" Normal weight");
        }
        else if(BMI<29.9){
            bmiBar.getProgressDrawable().setColorFilter(Color.rgb(255, 165, 0), PorterDuff.Mode.SRC_IN);
            editBmi.setText("BMI: "+df2.format(BMI)+" Overweight");
        }
        else{
            bmiBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            editBmi.setText("BMI: "+df2.format(BMI)+" Obesity");
        }


    }
    public void addToDatabase(){
        Map<String,Object> userDetails = new HashMap<>();
        userDetails.put("Gender",gender);
        userDetails.put("Height",height);
        userDetails.put("Weight",weight);
        userDetails.put("BMI",BMI);
       final DocumentReference documentReference = fStore.collection("users").document(Login.userID);
        documentReference.set(userDetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("onSuccess: user Profile is created for "+ Login.userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println( "onFailure: " + e.toString());
            }
        });

    }
}
