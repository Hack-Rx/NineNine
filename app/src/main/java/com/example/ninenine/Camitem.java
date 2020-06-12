package com.example.ninenine;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ninenine.Helper.InternetCheck;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;

public class Camitem extends AppCompatActivity {
    CameraView mCameraView;
    Button btnDetect;
    AlertDialog waitingDialog;

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

        waitingDialog= new SpotsDialog.Builder().setContext(this).setMessage("Please Waiting...").setCancelable(false).build();

        mCameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();
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

    private void runDetector(Bitmap bitmap) {

        final FirebaseVisionImage image=  FirebaseVisionImage.fromBitmap(bitmap);

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(boolean internet) {
                if(internet){
                    FirebaseVisionCloudImageLabelerOptions options =new  FirebaseVisionCloudImageLabelerOptions.Builder().build();
                    FirebaseVisionImageLabeler detector=FirebaseVision.getInstance()
                            .getCloudImageLabeler(options);
                    detector.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                            processDataResult(firebaseVisionImageLabels);
                        }
                    });
                }
                else{

                }

            }
        });
    }
    private void processDataResult(List<FirebaseVisionImageLabel> firebaseVisionImageLabels){
        for (FirebaseVisionImageLabel label :firebaseVisionImageLabels){
            Toast.makeText(this,"Cloud Result: "+label,Toast.LENGTH_SHORT).show();
        }
    }
}
