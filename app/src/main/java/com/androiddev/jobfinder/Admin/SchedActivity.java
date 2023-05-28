package com.androiddev.jobfinder.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.jobfinder.Modules.ScheduleModule;
import com.androiddev.jobfinder.MyviewHolders.MyViewHolder_Schedule;
import com.androiddev.jobfinder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SchedActivity extends AppCompatActivity {
    RecyclerView rv;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<ScheduleModule, MyViewHolder_Schedule> order_Adapter;
    FirebaseRecyclerOptions<ScheduleModule> orders_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);
        rv  = findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        load_orderAdapter("");

    }
    // Cette méthode charge l'adaptateur pour afficher les horaires dans la RecyclerView
    public void load_orderAdapter(String string) {

        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules");
        // Configuration des options de l'adaptateur
        orders_options = new FirebaseRecyclerOptions.Builder<ScheduleModule>().setQuery(databaseReference, ScheduleModule.class).build();
        order_Adapter  = new FirebaseRecyclerAdapter<ScheduleModule, MyViewHolder_Schedule>(this.orders_options) {
            /* Méthode permettant de lier les données aux vues dans chaque élément de la RecyclerView */
            public void onBindViewHolder(MyViewHolder_Schedule holder, int i, ScheduleModule module) {
                // Utilisation de la bibliothèque Picasso pour charger l'image depuis l'URL et la placer dans l'ImageView
                Picasso.with(SchedActivity.this).load(module.getTimage()).placeholder(R.drawable.round_logo).into(holder.imageView);
                holder.name_tv.setText(module.getSname());
                holder.date_tv.setText(module.getDate()+"");
                holder.address_tv.setVisibility(View.VISIBLE);
                holder.address_tv.setText(module.getSlocation());
                holder.time_tv.setText(module.getTime());




            }
            // Méthode pour créer les instances de la vue pour chaque élément de la RecyclerView
            public MyViewHolder_Schedule onCreateViewHolder(ViewGroup viewGroup, int i) {
                // Utilisation de l'inflater pour créer les vues à partir du layout spécifié
                return new MyViewHolder_Schedule(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_adapter_layout, viewGroup, false));
            }
        };
        // Démarrage de l'écoute des changements de données pour l'adaptateur
        order_Adapter.startListening();
        this.rv.setAdapter(this.order_Adapter);
    }
}