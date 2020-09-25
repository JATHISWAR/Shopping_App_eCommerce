package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myproject.Model.Products;
import com.example.myproject.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String productId="";
    private String state="";
    private Button addToCartButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");


        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);

        getProductDetails(productId);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state.equals("Order Placed")||state.equals("Order Shipped")){
                    final AlertDialog.Builder msg = new AlertDialog.Builder(ProductDetailsActivity.this);
                    msg.setTitle("Cannot add to Cart");
                    msg.setMessage("You can purchase more products , once your order is shipped or confirmed");
                    msg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                           startActivity(intent);
                        }
                    });
                    msg.show();
                }

                else
                {
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void  addingToCartList(){

        String saveCurrentTime,saveCurrentDate;


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart  List");
        final HashMap<String,Object> CartMap = new HashMap<>();
        CartMap.put("pid",productId);
        CartMap.put("pname",productName.getText().toString());
        CartMap.put("price",productPrice.getText().toString());
        CartMap.put("date",saveCurrentDate);
        CartMap.put("time", saveCurrentTime);
        CartMap.put("quantity",numberButton.getNumber());
        CartMap.put("discount","");
        cartListRef.child("User View").child(Prevelant.currentonlineUsers.getPhone())
                .child("Products").child(productId)
                .updateChildren(CartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevelant.currentonlineUsers.getPhone())
                                    .child("Products").child(productId)
                                    .updateChildren(CartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(ProductDetailsActivity.this);
                                            alert.setTitle("Added Successfully");
                                            alert.setMessage("Your item was added successfully to Cart");
                                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            alert.create().show();



                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(String productId) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText("Rs."+products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelant.currentonlineUsers.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped")){


                        state="Order Shipped";

                    }

                    else if(shippingState.equals("not shipped")){

                        state="Order Placed";
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}