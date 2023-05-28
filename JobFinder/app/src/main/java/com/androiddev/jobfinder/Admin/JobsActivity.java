package com.androiddev.jobfinder.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.jobfinder.Modules.PostModule;
import com.androiddev.jobfinder.MyviewHolders.MyViewHolder_Home;
import com.androiddev.jobfinder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class JobsActivity extends AppCompatActivity {
    RecyclerView rv;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home> order_Adapter;
    FirebaseRecyclerOptions<PostModule> orders_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);
        rv  = findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        // Charger l'adaptateur pour afficher les offres d'emploi
        load_orderAdapter();

    }
    public void load_orderAdapter() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("ADS");
        orders_options = new FirebaseRecyclerOptions.Builder<PostModule>().setQuery(databaseReference, PostModule.class).build();
        order_Adapter  = new FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home>(this.orders_options) {
            // Liaison des données du module PostModule aux éléments de la vue
            public void onBindViewHolder(MyViewHolder_Home holder, int i, PostModule module) {
                holder.delete_btn.setVisibility(View.GONE);
                Picasso.with(JobsActivity.this).load(module.getImage()).placeholder(R.drawable.round_logo).into(holder.iv_productImage);
                holder.name_tv.setText(module.getName()+", "+module.getEducation());
                holder.location_tv.setText(module.getLocation());
                holder.subject_tv.setText(module.getSubject());
                holder.price_tv.setText(module.getPrice());
                holder.time_tv.setText(module.getTime());




            }
            // Création d'un nouveau ViewHolder pour la vue d'un élément de la liste
            public MyViewHolder_Home onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder_Home(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_adapter_layout, viewGroup, false));
            }
        };
        // Démarrage de l'écoute des changements de données pour l'adaptateur
        order_Adapter.startListening();
        this.rv.setAdapter(this.order_Adapter);
    }
}