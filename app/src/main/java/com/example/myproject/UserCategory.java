package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class UserCategory extends AppCompatActivity {
    private ImageView tshirts,sportsTshirts,femaleDresses,sweaters;
    private ImageView glasses,hats,purses,shoes;
    private ImageView headphones,laptops,watches,mobilephones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);

        tshirts = (ImageView)findViewById(R.id.t_shirts);
        sportsTshirts = (ImageView)findViewById(R.id.sports_t_shirts);
        femaleDresses = (ImageView)findViewById(R.id.female_dresses);
        sweaters= (ImageView)findViewById(R.id.sweater);
        glasses = (ImageView)findViewById(R.id.glasses);
        hats= (ImageView)findViewById(R.id.hats_caps);
        headphones = (ImageView)findViewById(R.id.headphones_handfree);
        laptops = (ImageView)findViewById(R.id.laptops_pc);
        purses = (ImageView)findViewById(R.id.purses_bags_wallets);
        shoes = (ImageView)findViewById(R.id.shoes);
        watches = (ImageView)findViewById(R.id.watches);
        mobilephones = (ImageView)findViewById(R.id.mobilephones);



        select();

    }

    public void select(){

        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Tshirts");
                startActivity(intent);
            }
        });

        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Sports Tshirts");
                startActivity(intent);
            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });


        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Hats");
                startActivity(intent);
            }
        });

        purses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Purses");
                startActivity(intent);
            }
        });


        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Headphones");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });

        mobilephones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Mobilephones");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCategory.this,HomeActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });




    }
}