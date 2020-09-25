package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myproject.Model.Cart;
import com.example.myproject.Prevelant.Prevelant;
import com.example.myproject.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserOrders extends AppCompatActivity {
    private RecyclerView orderlist;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ordersref;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        userId = Prevelant.currentonlineUsers.getPhone();

        orderlist = findViewById(R.id.products_list);
        layoutManager = new LinearLayoutManager(this);
        orderlist.setLayoutManager(layoutManager);
        ordersref = FirebaseDatabase.getInstance().getReference().child("Cart  List").child("Admin View")
                .child(userId).child("Products");


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(ordersref, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.txtProductQuantity.setText("Quantity: " + model.getQuantity());
                holder.txtProductPrice.setText("Price: " + model.getPrice());
                holder.txtProductName.setText("Name: " + model.getPname());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        orderlist.setAdapter(adapter);
        adapter.startListening();

    }




    }
