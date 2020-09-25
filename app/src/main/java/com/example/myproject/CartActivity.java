package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.Model.Cart;
import com.example.myproject.Prevelant.Prevelant;
import com.example.myproject.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount,txtMsg1;
    private int overTotalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = (Button)findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView)findViewById(R.id.total_price);
        txtMsg1 = (TextView)findViewById(R.id.msg1);


        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder total = new AlertDialog.Builder(CartActivity.this);
                total.setTitle("Total Amount");
                total.setMessage("Your Total Cost is Rs. "+String.valueOf(overTotalPrice));
                total.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                        intent.putExtra("Total",String.valueOf(overTotalPrice));
                        startActivity(intent);
                    }
                });
                total.show();
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
        String user = Prevelant.currentonlineUsers.getPhone();
        final DatabaseReference cart_list = FirebaseDatabase.getInstance().getReference().child("Cart  List").child("User View")
                .child(user).child("Products");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cart_list,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.txtProductQuantity.setText("Quantity: "+model.getQuantity());
                holder.txtProductPrice.setText("Price: "+model.getPrice());
                holder.txtProductName.setText("Name: "+model.getPname());

                int len = model.getPrice().length();
                String s = model.getPrice().substring(3,len);

                int oneTypeProductTPrice = (Integer.valueOf(s))*(Integer.valueOf(model.getQuantity()));
                overTotalPrice = overTotalPrice+oneTypeProductTPrice;

                txtTotalAmount.setText("Total Price: Rs."+String.valueOf(overTotalPrice));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };

                        final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(i==0){
                                    Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }

                                if(i==1){
                                    cart_list
                                             .child(model.getPid())
                                             .removeValue()
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if(task.isSuccessful()){
                                                         final AlertDialog.Builder delete = new AlertDialog.Builder(CartActivity.this);
                                                         delete.setTitle("Item Deleted");
                                                         delete.setMessage("Your item has been deleted");
                                                         delete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                                 Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                                 startActivity(intent);
                                                             }
                                                         });
                                                         delete.create().show();
                                                     }
                                                 }
                                             });


                                }
                            }
                        });

                        builder.show();

                    }
                });


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        overTotalPrice = 0;
    }

    private void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelant.currentonlineUsers.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();
                    String username = snapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped")){
                        txtTotalAmount.setText("Order Shipped");
                        recyclerView.setVisibility(View.INVISIBLE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Your order has been shipped successfully.Soon you will recieve your order in 2-3 business days");
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchase more products,After you have recieved your first order",Toast.LENGTH_SHORT).show();


                    }

                    else if(shippingState.equals("not shipped")){
                        txtTotalAmount.setText("Order Received");
                        recyclerView.setVisibility(View.INVISIBLE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchase more products,After you have recieved your first order",Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}