package com.kumawat.frozer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kumawat.frozer.Data;
import com.kumawat.frozer.R;
import com.kumawat.frozer.SurplusPOJO;
import com.kumawat.frozer.adapters.SurplusRequired;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private EditText amountET, placeET;
    private Button logoutBtn ;
    private RecyclerView recyclerView;
    private SurplusPOJO surplusPOJO;
    private ArrayList<SurplusPOJO> arrayList;
    private String uId;
    private SurplusRequired surplusRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        amountET = findViewById(R.id.amount);
        placeET = findViewById(R.id.place);
        //informBtn= findViewById(R.id.informBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
        //arrayList = new ArrayList<>();

        //Data.arrayList = new ArrayList<>();
        surplusRequired= new SurplusRequired( Dashboard.this, Data.arrayList);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is authenticated and now you can access uesr's properties as followings
            uId = user.getUid();
        } else {
            // User is authenticated. So, let's try to re-authenticate
            AuthCredential credential = EmailAuthProvider
                    .getCredential("vkumawat17@gmail.com", "adminisvimal");

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("1234", "User re-authenticated.");
                            uId = user.getUid();
                        }
                    });
        }
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this, LoginPanel.class));
                finish();
            }
        });
    }

    public void informBtnTap(View v){
        final String amountStr= amountET.getText().toString(),
                placeStr = placeET.getText().toString();
        Log.d("1234", "data: "+amountStr+" -- "+placeStr+"\n\n\n"+uId);

        boolean ok = true;
        if (amountStr.isEmpty()){
            ok= false;
            amountET.setError("Empty");
        }
        if (placeStr.isEmpty()){
            ok= false;
            placeET.setError("Empty");
        }
//        if (FirebaseAuth.getInstance().getCurrentUser()==null) {
//            ok = false;
//            Toast.makeText(Dashboard.this, "Error Occurred", Toast.LENGTH_SHORT).show();
//        }
        if (ok){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
            String key = reference.child(uId).push().getKey();
            SurplusPOJO pojo = new SurplusPOJO();
            pojo.setAmount(amountStr);
            pojo.setOrderId(key);
            pojo.setPlace(placeStr);
            Log.d("1234", "informBtnTap: Sending Info"+key);
            Data.arrayList.add(pojo);
            recyclerView.setAdapter(surplusRequired);
//            reference.child(uId).push().setValue(pojo)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Log.d("1234", "oncomp");
//                        }
//                    })
//                    .addOnCanceledListener(new OnCanceledListener() {
//                        @Override
//                        public void onCanceled() {
//                            Log.d("1234", "onCancel");
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Dashboard.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
//                            Log.d("1234", "onFailure: Errrrreeeeer"+e.getMessage());
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(Dashboard.this, "Information Sent", Toast.LENGTH_SHORT).show();
//                            Log.d("1234", "onSuccess: Yayyy");
//
//                        }
//                    });
        }else{
            Toast.makeText(Dashboard.this, "Error Occurred :(", Toast.LENGTH_SHORT).show();
        }
    }
    private void getSUrplusData(){


        recyclerView.setAdapter(surplusRequired);

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
//        reference.child(uId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                arrayList.clear();
//                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
//                    surplusPOJO = dataSnap.getValue(SurplusPOJO.class);
//                    arrayList.add(surplusPOJO);
//                    Log.d("1234", "onDataChange: "+surplusPOJO.getOrderId());
//                }
//                SurplusRequired surplusRequired= new SurplusRequired( Dashboard.this, arrayList);
//                recyclerView.setAdapter(surplusRequired);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(surplusRequired);
    }
}
