package com.example.ninenine;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Camitem extends AppCompatActivity {
    CameraView mCameraView;
    Button btnDetect;
    AlertDialog waitingDialog;
    FirebaseAutoMLRemoteModel remoteModel;
    FirebaseVisionImageLabeler labler;
    FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder optionsBuilder;
    ProgressDialog progressDialog;
    FirebaseModelDownloadConditions conditions;
    FirebaseVisionImage image;


    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camitem);
        mCameraView=findViewById(R.id.camera_view);
        btnDetect=findViewById(R.id.btn_detect);

        mCameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap=cameraKitImage.getBitmap();
                bitmap=Bitmap.createScaledBitmap(bitmap,mCameraView.getWidth(),mCameraView.getHeight(),false);
                mCameraView.stop();
                runDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraView.start();
                mCameraView.captureImage();
            }
        });



    }
    public void showProgressBar(){
        progressDialog=new ProgressDialog(Camitem.this);
        progressDialog.setMessage("PLease wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    private void runDetector(final Bitmap bitmap) {
        showProgressBar();
        remoteModel=new FirebaseAutoMLRemoteModel.Builder("Food_202061222479").build();
        conditions=new FirebaseModelDownloadConditions.Builder().requireWifi().build();
        FirebaseModelManager.getInstance().download(remoteModel,conditions).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setLabelerFromRemoteLabel(bitmap);
            }
        });
    }
    private void setLabelerFromRemoteLabel(final Bitmap bitmap){
        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isDownloaded) {
                if(isDownloaded){
                    optionsBuilder=new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(remoteModel);
                    FirebaseVisionOnDeviceAutoMLImageLabelerOptions options=optionsBuilder.setConfidenceThreshold(0.5f).build();
                    try{
                        labler=FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
                        image=FirebaseVisionImage.fromBitmap(bitmap);
                        processImageLabeler(labler,image);
                    }
                    catch (FirebaseMLException  exception){

                    }
                }
            }
        });

    }


    private void processImageLabeler(FirebaseVisionImageLabeler Labeler,FirebaseVisionImage image){
        labler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                progressDialog.cancel();
                for(FirebaseVisionImageLabel label : labels){
                    if(label.getConfidence()>0.5){
                        Toast.makeText(Camitem.this, label.getText(), Toast.LENGTH_SHORT).show();
                        Calories.camSearch=label.getText();
                        startActivity(new Intent(Camitem.this,Calories.class));
                    }
                }
            }
        });
    }
}
