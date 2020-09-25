package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myproject.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private Button confirmOrderBtn;

    private String totalamount=" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText)findViewById(R.id.shipment_name);
        addressEditText = (EditText)findViewById(R.id.shipment_address);
        cityEditText = (EditText)findViewById(R.id.shipment_city);
        phoneEditText = (EditText)findViewById(R.id.shipment_phone);

        totalamount = getIntent().getStringExtra("Total");


        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Check();
            }


        });

    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this,"Please provide your full name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this,"Please provide your phone number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this,"Please provide your address",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this,"Please provide your city name",Toast.LENGTH_SHORT).show();
        }

        else
        {
            ConfirmOrder();
        }


    }

    private void ConfirmOrder() {

       final String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                        .child("Orders")
                                        .child(Prevelant.currentonlineUsers.getPhone());


        HashMap<String,Object> ordersMap = new HashMap<>();

        ordersMap.put("totalAmount",totalamount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText .getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state","not shipped");

        ref.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String user = Prevelant.currentonlineUsers.getPhone();
                FirebaseDatabase.getInstance().getReference().child("Cart  List").child("User View")
                        .child(user).child("Products")
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    final AlertDialog.Builder msg = new AlertDialog.Builder(ConfirmFinalOrderActivity.this);
                                    msg.setTitle("Order Placed !");
                                    msg.setMessage("Your Order Has Been Placed Successfully");
                                    msg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });
                                    msg.show();
                                }
                            }
                        });
            }
        });




    }


}