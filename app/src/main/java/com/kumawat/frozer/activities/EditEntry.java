package com.kumawat.frozer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kumawat.frozer.Data;
import com.kumawat.frozer.R;
import com.kumawat.frozer.SurplusPOJO;

public class EditEntry extends AppCompatActivity {

    SurplusPOJO pojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        final EditText place = findViewById(R.id.place);
        final EditText amount = findViewById(R.id.amount);

        Intent intent = getIntent();
        final int index = intent.getIntExtra("position", 0);
        pojo =new SurplusPOJO();
        pojo = Data.arrayList.get(index);
        Button edit = findViewById(R.id.editBtn);

        place.setText(pojo.getPlace());
        amount.setText(pojo.getAmount());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String amountStr= amount.getText().toString(),
                        placeStr = place.getText().toString();
                Log.d("1234", "data: "+amountStr+" -- "+placeStr);
                boolean ok = true;
                if (amountStr.isEmpty()){
                    ok= false;
                    amount.setError("Empty");
                }
                if (placeStr.isEmpty()){
                    ok= false;
                    place.setError("Empty");
                }
                if (ok) {
                    pojo.setPlace(place.getText().toString());
                    pojo.setAmount(amount.getText().toString());
                    Data.arrayList.remove(index);
                    Data.arrayList.add(index, pojo);
                    Toast.makeText(EditEntry.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
