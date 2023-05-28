package com.androiddev.jobfinder.Init;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.jobfinder.Modules.PostModule;
import com.androiddev.jobfinder.MyviewHolders.MyViewHolder_Home;
import com.androiddev.jobfinder.R;
import com.androiddev.jobfinder.TermsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.special.ResideMenu.BuildConfig;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.picasso.Picasso;

// Classe GuestActivity qui hérite de AppCompatActivity
public class GuestActivity extends AppCompatActivity {

    // Déclaration des variables pour les éléments d'interface utilisateur et les références de la base de données
    ImageView nav_btn, myAccount_btn; // Boutons dans l'interface
    RecyclerView rv; // RecyclerView pour afficher les annonces

    // Les éléments du menu résident
    private ResideMenu resideMenu;
    private ResideMenuItem terms_btn, shareUs_btn, rateUse_btn, login_btn;

    // Variable pour suivre si un bouton a été appuyé
    Boolean isPressed = false;

    // Références à la base de données Firebase
    DatabaseReference databaseReference, reference;

    // Adapter pour le RecyclerView
    FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home> order_Adapter;
    // Options pour le FirebaseRecyclerAdapter
    FirebaseRecyclerOptions<PostModule> orders_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        // Initialisation des vues
        nav_btn  = findViewById(R.id.nav_btn);
        myAccount_btn  = findViewById(R.id.my_account_btn);
        rv  = findViewById(R.id.rv);
        nav_btn  = findViewById(R.id.nav_btn);

        // Configuration du RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        // Création du menu résident
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.nav);
        resideMenu.attachToActivity(GuestActivity.this);
        resideMenu.setScaleValue(0.6f);

        // Création des éléments du menu résident
        terms_btn = new ResideMenuItem(this, R.drawable.baseline_info_24,     "Terms & Cond.");
        shareUs_btn = new ResideMenuItem(this, R.drawable.baseline_share_24,     "Share Us");
        rateUse_btn = new ResideMenuItem(this, R.drawable.baseline_star_24,     "Rate Us");
        login_btn = new ResideMenuItem(this, R.drawable.logout_ic,  "Login Here");

        // Ajout des éléments au menu résident
        resideMenu.addMenuItem(terms_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(shareUs_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(rateUse_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(login_btn, ResideMenu.DIRECTION_LEFT);

        // Désactivation du glissement vers la droite pour le menu résident
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // Listener pour le bouton de navigation
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvre le menu résident lorsqu'on clique sur le bouton de navigation
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        // Listener pour le bouton de compte
        myAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Démarre l'activité SigninInitActivity lorsque l'on clique sur le bouton de compte
                startActivity(new Intent(GuestActivity.this, SigninInitActivity.class));
            }
        });

        // Listener pour le bouton des conditions générales
        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Démarre l'activité TermsActivity lorsque l'on clique sur le bouton des conditions générales
                startActivity(new Intent(GuestActivity.this, TermsActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuestActivity.this, SigninInitActivity.class));
                finish();
            }
        });

        shareUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage= "";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        rateUse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage= "";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });




        load_orderAdapter();

    }

    // Cette méthode récupère les données de la base de données Firebase, les parse et
    // les affiche dans le RecyclerView en utilisant un LinearLayoutManager.
    public void load_orderAdapter() {
        // Récupération de la référence à la base de données Firebase
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("ADS");
//        Query query = databaseReference.orderByChild("phone").equalTo(string);
        // Configuration des options pour le FirebaseRecyclerAdapter
        orders_options = new FirebaseRecyclerOptions.Builder<PostModule>().setQuery(databaseReference, PostModule.class).build();
        // Création de l'adapter pour le RecyclerView
        order_Adapter  = new FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home>(this.orders_options) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(MyViewHolder_Home holder, int i, PostModule module) {
                holder.delete_btn.setVisibility(View.GONE);
                Picasso.with(GuestActivity.this).load(module.getImage()).placeholder(R.drawable.round_logo).into(holder.iv_productImage);
                holder.name_tv.setText(module.getName()+", "+module.getEducation());
                holder.location_tv.setText(module.getLocation());
                holder.subject_tv.setText(module.getSubject());
                holder.price_tv.setText(module.getPrice());
                holder.time_tv.setText(module.getTime());
                holder.fav_btn.setVisibility(View.VISIBLE);

                holder.fav_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(GuestActivity.this, SigninInitActivity.class));

                    }
                });

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(GuestActivity.this, SigninInitActivity.class));
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

        AlertDialog.Builder builder = new AlertDialog.Builder(GuestActivity.this);
        builder.setTitle((CharSequence) "Confirm");
        builder.setMessage((CharSequence) "Are you sure you want to Exit?");
        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "Exit", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                GuestActivity.super.onBackPressed();
            }
        });
        builder.show();




    }

}