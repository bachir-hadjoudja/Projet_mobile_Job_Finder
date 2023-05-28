package com.androiddev.jobfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.jobfinder.Init.GuestActivity;
import com.androiddev.jobfinder.Init.SigninInitActivity;
import com.androiddev.jobfinder.Modules.PostModule;
import com.androiddev.jobfinder.MyviewHolders.MyViewHolder_Home;
import com.androiddev.jobfinder.User.MappingActivity;
import com.androiddev.jobfinder.User.ScheduleActivity;
import com.androiddev.jobfinder.User.ScheduleRequestActivity;
import com.androiddev.jobfinder.User.UserMyAccountActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.special.ResideMenu.BuildConfig;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    //Déclaration des vues
    ImageView nav_btn, myAccount_btn, location_btn;
    RecyclerView rv;

    Boolean isPressed = false;

    // Eléments de navigation
    private ResideMenu resideMenu;
    private ResideMenuItem myApplies;
    private ResideMenuItem myAccount;
    private ResideMenuItem terms_btn;
    private ResideMenuItem shareUs_btn;
    private ResideMenuItem rateUse_btn;
    private ResideMenuItem myPosts_btn;
    private ResideMenuItem logout_btn;


    // Référence de la base de données Firebase
    DatabaseReference databaseReference;
    DatabaseReference reference;
    // Adapter pour le RecyclerView
    FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home> order_Adapter;
    FirebaseRecyclerOptions<PostModule> orders_options;
    // Barre de recherche
    EditText search_bar;
    ImageView search_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialisation des vues
        nav_btn  = findViewById(R.id.nav_btn);
        myAccount_btn  = findViewById(R.id.my_account_btn);
        rv  = findViewById(R.id.rv);
        nav_btn  = findViewById(R.id.nav_btn);
        location_btn = findViewById(R.id.location_btn);
        search_bar = findViewById(R.id.search_bar);
        search_button = findViewById(R.id.search_button);

        EditText searchEditText = findViewById(R.id.search_bar);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Action lors de l'appui sur la touche de recherche du clavier
                Toast.makeText(this, "fine", Toast.LENGTH_SHORT).show();
                String searchText = searchEditText.getText().toString().trim();
                performSearch(searchText);
                return true;
            }
            return false;
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchEditText.getText().toString().trim();
                performSearch(searchText);
            }
        });


        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MappingActivity.class));
            }
        });


        // Initialisation du RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        //This code to create an Navigation drawer, I got this library from github

        // Création du menu de navigation
        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.nav);
        resideMenu.attachToActivity(MainActivity.this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // Création des items du menu
        myApplies = new ResideMenuItem(this, R.drawable.baseline_note_24,     "My Applications");
        myAccount = new ResideMenuItem(this, R.drawable.ic_user_selected,     "My Account");
        terms_btn = new ResideMenuItem(this, R.drawable.baseline_info_24,     "Terms & Cond.");
        shareUs_btn = new ResideMenuItem(this, R.drawable.baseline_share_24,     "Share Us");
        rateUse_btn = new ResideMenuItem(this, R.drawable.baseline_star_24,     "Rate Us");
        logout_btn = new ResideMenuItem(this, R.drawable.logout_ic,  "Logout");


        // Ajout des items au menu
        resideMenu.addMenuItem(myApplies, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(myAccount, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(terms_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(shareUs_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(rateUse_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(logout_btn, ResideMenu.DIRECTION_LEFT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        // Création d'un listener pour le bouton "nav_btn".
        // le menu résidentiel s'ouvre à gauche.
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        // Quand on clique sur ce bouton, l'activité "ScheduleRequestActivity" est lancée
        myApplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScheduleRequestActivity.class));
            }
        });
        // Quand on clique sur ce bouton, l'activité "UserMyAccountActivity" est lancée
        myAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserMyAccountActivity.class));
            }
        });
        // Quand on clique sur ce bouton, l'activité "UserMyAccountActivity" est lancée
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UserMyAccountActivity.class));

            }
        });
        // Quand on clique sur ce bouton, l'activité "TermsActivity" est lancée
        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TermsActivity.class));
            }
        });
        // Quand on clique sur ce bouton, une boîte de dialogue apparaît pour confirmer la déconnexion
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle((CharSequence) "Confirm");
                builder.setMessage((CharSequence) "Are you sure you want to LOGOUT?");
                builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton((CharSequence) "LOGOUT", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences =getSharedPreferences("USER",MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        startActivity(new Intent(MainActivity.this, SigninInitActivity.class));
                        finish();
                    }
                });
                builder.show();
            }
        });
        // Création d'un listener pour le bouton "shareUs_btn"
        shareUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage= "Share us :";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        // Création d'un listener pour le bouton "rateUs_btn"
        rateUse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage= "Rate us : ";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });




        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);
        String phone = preferences.getString("phone","");
        load_orderAdapter();

    }

    // This is the method which is getting data from Firebase database, parsing it and
    // showing in the recyclerview using LinearLayout manager.
    public void load_orderAdapter() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("ADS");
//        Query query = databaseReference.orderByChild("phone").equalTo(string);
        orders_options = new FirebaseRecyclerOptions.Builder<PostModule>().setQuery(databaseReference, PostModule.class).build();
        order_Adapter  = new FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home>(this.orders_options) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(MyViewHolder_Home holder, int i, PostModule module) {
                holder.delete_btn.setVisibility(View.GONE);
                Picasso.with(MainActivity.this).load(module.getImage()).placeholder(R.drawable.round_logo).into(holder.iv_productImage);
                holder.name_tv.setText(module.getName()+", "+module.getEducation());
                holder.location_tv.setText(module.getLocation());
                holder.subject_tv.setText(module.getSubject());
                holder.price_tv.setText(module.getPrice());
                holder.time_tv.setText(module.getTime());

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                        intent.putExtra("name",holder.name_tv.getText().toString());
                        intent.putExtra("phone",module.getPhone());
                        intent.putExtra("desc",module.getDescription());
                        intent.putExtra("id",module.getId());
                        intent.putExtra("image",module.getImage());

                        startActivity(intent);
                    }
                });

                holder.fav_btn.setVisibility(View.VISIBLE);

                holder.fav_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isPressed){
                            isPressed = true;
                            holder.fav_btn.setImageResource(R.drawable.baseline_favorite_24);
                            Toast.makeText(MainActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        }else if (isPressed){
                            isPressed = false;
                            holder.fav_btn.setImageResource(R.drawable.baseline_favorite_border_24);
                            Toast.makeText(MainActivity.this, "Remove from Favorites", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }

            public MyViewHolder_Home onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder_Home(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_adapter_layout, viewGroup, false));
            }
        };
//        this.order_Adapter = order_Adapter;
        order_Adapter.startListening();
        this.rv.setAdapter(this.order_Adapter);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle((CharSequence) "Confirm");
        builder.setMessage((CharSequence) "Are you sure you want to Exit?");
        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "Exit", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.show();




    }

    private void performSearch(String searchText) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ADS");
        Query searchQuery = databaseReference.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<PostModule> searchOptions = new FirebaseRecyclerOptions.Builder<PostModule>()
                .setQuery(searchQuery, PostModule.class)
                .build();

        FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home> searchAdapter = new FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home>(searchOptions) {
            protected void onBindViewHolder(MyViewHolder_Home holder, int position, PostModule module) {
                holder.delete_btn.setVisibility(View.GONE);
                Picasso.with(MainActivity.this).load(module.getImage()).placeholder(R.drawable.round_logo).into(holder.iv_productImage);
                holder.name_tv.setText(module.getName()+", "+module.getEducation());
                holder.location_tv.setText(module.getLocation());
                holder.subject_tv.setText(module.getSubject());
                holder.price_tv.setText(module.getPrice());
                holder.time_tv.setText(module.getTime());

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                        intent.putExtra("name",holder.name_tv.getText().toString());
                        intent.putExtra("phone",module.getPhone());
                        intent.putExtra("desc",module.getDescription());
                        intent.putExtra("id",module.getId());
                        intent.putExtra("image",module.getImage());

                        startActivity(intent);
                    }
                });

                holder.fav_btn.setVisibility(View.VISIBLE);

                holder.fav_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isPressed){
                            isPressed = true;
                            holder.fav_btn.setImageResource(R.drawable.baseline_favorite_24);
                            Toast.makeText(MainActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                        }else if (isPressed){
                            isPressed = false;
                            holder.fav_btn.setImageResource(R.drawable.baseline_favorite_border_24);
                            Toast.makeText(MainActivity.this, "Remove from Favorites", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



                // ...
            }

            public MyViewHolder_Home onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                return new MyViewHolder_Home(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_adapter_layout, viewGroup, false));
            }
        };

        // Update the adapter for the search results
        rv.setAdapter(searchAdapter);
        searchAdapter.startListening();
    }


}