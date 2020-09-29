package com.example.smartbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.spark.submitbutton.SubmitButton;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class qrcode extends AppCompatActivity {
    private TextView text_result;
    private SurfaceView surfaceView;
    private QREader qrEader;
    ToggleButton btn_on_off;
    DatabaseReference reference1;
    DatabaseReference Reference2;
    SubmitButton btn;
    FirebaseDatabase firebase;
    String longitude,latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        firebase=FirebaseDatabase.getInstance();
        reference1= firebase.getReference("longitude");
        Reference2=firebase.getReference("latitude");
        btn=findViewById(R.id.submitButton);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupCamera();
                    }



                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(qrcode.this,"you must enable this permission",Toast.LENGTH_SHORT).show();                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(qrcode.this,"Button clicked!!",Toast.LENGTH_SHORT).show();
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        longitude=dataSnapshot.getValue().toString();


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        latitude=dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                String message="Help is needed in"+" "+"longitude = "+"28.7525"+"latitude = "+"77.4988";
                String number="+918527111814";
                SmsManager.getDefault().sendTextMessage(number,null,message,null,null);
                Toast.makeText(qrcode.this,"Message sent!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setupCamera() {
        text_result= findViewById(R.id.code_info);
         btn_on_off= findViewById(R.id.btn_enable_disable);
        btn_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qrEader.isCameraRunning())
                { btn_on_off.setChecked(false);
                    qrEader.stop();}
                else
                {
                    btn_on_off.setChecked(true);
                    qrEader.start();
                }
            }
        });
        surfaceView= findViewById(R.id.camera_view);
        setupQREader();
    }

    private void setupQREader() {
        qrEader=new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                text_result.post(new Runnable() {
                    @Override
                    public void run() {
                        text_result.setText(data);

                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true) .height(surfaceView.getHeight()) .width(surfaceView.getWidth()) .build();}

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(qrEader!=null)
                            qrEader.initAndStart(surfaceView);
                    }



                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(qrcode.this,"you must enable this permission",Toast.LENGTH_SHORT).show();                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();  Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(qrEader!=null)
                            qrEader.releaseAndCleanup();
                    }



                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(qrcode.this,"you must enable this permission",Toast.LENGTH_SHORT).show();                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


    }
}
