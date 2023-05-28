package com.androiddev.jobfinder.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.androiddev.jobfinder.Modules.ManagerModule;
import com.androiddev.jobfinder.MyviewHolders.MyViewHolder_Company;
import com.androiddev.jobfinder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompaniesActivity extends AppCompatActivity {
    RecyclerView rv;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<ManagerModule, MyViewHolder_Company> order_Adapter;
    FirebaseRecyclerOptions<ManagerModule> orders_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);
        rv  = findViewById(R.id.rv);
        // Configuration de la RecyclerView pour utiliser un LinearLayoutManager
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        load_orderAdapter();

    }
    public void load_orderAdapter() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Managers");
        // Configuration des options de l'adaptateur de la RecyclerView
        orders_options = new FirebaseRecyclerOptions.Builder<ManagerModule>().setQuery(databaseReference, ManagerModule.class).build();
        // Initialisation de l'adaptateur de la RecyclerView
        order_Adapter  = new FirebaseRecyclerAdapter<ManagerModule, MyViewHolder_Company>(this.orders_options) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(MyViewHolder_Company holder, int i, ManagerModule module) {
                // Liaison des données du module ManagerModule aux éléments de la vue
                holder.name_tv.setText(module.getCname());
                holder.id_tv.setText(module.getCregis());





            }

            public MyViewHolder_Company onCreateViewHolder(ViewGroup viewGroup, int i) {
                // Création d'un nouveau ViewHolder pour la vue d'un élément de la liste
                return new MyViewHolder_Company(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company_adapter_layout, viewGroup, false));
            }
        };
        // Démarrage de l'écoute des changements de données pour l'adaptateur
        order_Adapter.startListening();
        this.rv.setAdapter(this.order_Adapter);
    }
}