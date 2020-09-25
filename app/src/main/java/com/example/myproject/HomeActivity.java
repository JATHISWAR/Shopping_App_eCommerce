package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.Model.Products;
import com.example.myproject.Prevelant.Prevelant;
import com.example.myproject.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import androidx.appcompat.widget.Toolbar;
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DatabaseReference ProductsRef,SearchRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private EditText searchtext;
    private Button searchbtn;
    private String searchinput,categoryinput;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent = getIntent();
        categoryinput = intent.getStringExtra("category");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        searchtext = (EditText)findViewById(R.id.search_product);
        searchbtn = (Button)findViewById(R.id.search_btn);

        Paper.init(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);

        userNameTextView.setText("Hello, "+Prevelant.currentonlineUsers.getName());
        Picasso.get().load(Prevelant.currentonlineUsers.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchinput = searchtext.getText().toString();
                SearchRef = FirebaseDatabase.getInstance().getReference().child("Products");


                    FirebaseRecyclerOptions<Products> options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(SearchRef.orderByChild("pname").startAt(searchinput), Products.class)
                                    .build();

                if(searchinput.isEmpty()){
                   options =
                            new FirebaseRecyclerOptions.Builder<Products>()
                                    .setQuery(SearchRef,Products.class)
                                    .build();
                }


                FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter =
                        new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {


                                holder.txtProductName.setText(model.getPname());
                                holder.txtProductDescription.setText(model.getDescription());
                                holder.txtProductPrice.setText("Price :Rs." + model.getPrice());
                                Picasso.get().load(model.getImage()).into(holder.imageView);




                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if(type.equals("Admin")){






                                        }

                                        else
                                        {
                                            Intent intent = new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);
                                        }




                                    }
                                });





                            }

                            @NonNull
                            @Override
                            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                                ProductViewHolder holder = new ProductViewHolder(view);
                                return holder;
                            }
                        };

                recyclerView.setAdapter(adapter);
                adapter.startListening();

            }
        });








    }


    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("category").startAt(categoryinput).endAt(categoryinput), Products.class)
                        .build();

       if(categoryinput==null) {
           options = new FirebaseRecyclerOptions.Builder<Products>()
                   .setQuery(ProductsRef, Products.class)
                   .build();
       }






        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price :Rs." + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                 intent.putExtra("pid",model.getPid());
                                 startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            Intent intent = new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_orders){
            Intent intent = new Intent(HomeActivity.this,UserOrders.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_categories)
        {
            Intent intent = new Intent(HomeActivity.this,UserCategory.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
        Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
        startActivity(intent);

        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}