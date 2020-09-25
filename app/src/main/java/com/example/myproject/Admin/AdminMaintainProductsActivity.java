package com.example.myproject.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private Button  apply_changes_button,delete_button;
    private EditText name,price,description;
    private ImageView imageView;
    private DatabaseReference ProductsRef;
    RecyclerView.LayoutManager layoutManager;
    private String product="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        apply_changes_button = (Button) findViewById(R.id.apply_changes_button);
        name = (EditText) findViewById(R.id.edit_product_name);
        price = (EditText) findViewById(R.id.edit_product_price);
        description = (EditText) findViewById(R.id.edit_product_description);
        imageView = (ImageView) findViewById(R.id.edit_product_image);
        delete_button = (Button) findViewById(R.id.delete_product_btn);
        product = getIntent().getStringExtra("pid");


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(product);
        displaySpecificProductInfo();




        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteThisProduct();
            }
        });




        apply_changes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_changes();
            }
        });
    }

    private void displaySpecificProductInfo(){

        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pname = snapshot.child("pname").getValue().toString();
                    String pprice = snapshot.child("price").getValue().toString();
                   String pdescription = snapshot.child("description").getValue().toString();
                   String pimage = snapshot.child("image").getValue().toString();

                   name.setText(pname);
                   price.setText(pprice);
                   description.setText(pdescription);
                   Picasso.get().load(pimage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    private void apply_changes() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if(pName.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Write down the product name",Toast.LENGTH_SHORT).show();
        }

        else if(pName.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Write down the product price",Toast.LENGTH_SHORT).show();
        }

        else if(pDescription.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Write down the product description",Toast.LENGTH_SHORT).show();
        }

        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", product);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            ProductsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this,"Changes Applied Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this,AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });




        }


    }

    private void deleteThisProduct(){

        ProductsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainProductsActivity.this,"The Product is Deleted Successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMaintainProductsActivity.this,AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }


}