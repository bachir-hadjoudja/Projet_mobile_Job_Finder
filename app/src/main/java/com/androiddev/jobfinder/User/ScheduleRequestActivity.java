package com.androiddev.jobfinder.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.jobfinder.Modules.ScheduleModule;
import com.androiddev.jobfinder.MyviewHolders.MyViewHolder_Schedule;
import com.androiddev.jobfinder.R;
import com.androiddev.jobfinder.Recruiter.CvActivity;
import com.androiddev.jobfinder.Recruiter.ScheduleRequestManagerActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ScheduleRequestActivity extends AppCompatActivity {

    RecyclerView rv;
    ImageView back_btn;


    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<ScheduleModule, MyViewHolder_Schedule> order_Adapter;
    FirebaseRecyclerOptions<ScheduleModule> orders_options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_request);
        back_btn = findViewById(R.id.back_btn);
        rv = findViewById(R.id.sr_rv);

        // Définition du comportement du bouton de retour pour quitter l'activité lorsqu'il est cliqué.
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Configuration de la RecyclerView pour utiliser un LinearLayoutManager.
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        // Récupération du numéro de téléphone de l'utilisateur à partir des préférences partagées.
        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);
        String phone = preferences.getString("phone","");
        // Chargement des données dans l'adaptateur de la RecyclerView.
        load_orderAdapter(phone);
    }

    // Cette méthode charge les données de Firebase dans l'adaptateur de la RecyclerView.
    public void load_orderAdapter(String string) {

        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules");
        // Création d'une requête pour obtenir les données qui correspondent au numéro de téléphone de l'utilisateur
        Query query = databaseReference.orderByChild("sphone").equalTo(string);
        // Construction des options pour l'adaptateur de la RecyclerView
        orders_options = new FirebaseRecyclerOptions.Builder<ScheduleModule>().setQuery(query, ScheduleModule.class).build();
        order_Adapter  = new FirebaseRecyclerAdapter<ScheduleModule, MyViewHolder_Schedule>(this.orders_options) {
            // Cette méthode lie les données à la vue pour chaque élément de la RecyclerView.
            public void onBindViewHolder(MyViewHolder_Schedule holder, int i, ScheduleModule module) {
                // Utilisation de Picasso pour charger l'image dans l'ImageView.
                Picasso.with(ScheduleRequestActivity.this).load(module.getTimage()).placeholder(R.drawable.round_logo).into(holder.imageView);
                // Affectation des données aux différentes vues
                holder.name_tv.setText(module.getTname());
                holder.address_tv.setVisibility(View.GONE);
                holder.date_tv.setText(module.getDate()+"");
                holder.time_tv.setText(module.getTime());

                holder.call_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Déclenchement de l'Intent pour composer le numéro de téléphone
                        startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+module.getTphone())));

                    }
                });

                holder.view_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Déclenchement de l'intention pour afficher l'image dans une nouvelle activité
                        Intent intent = new Intent(ScheduleRequestActivity.this, CvActivity.class);
                        intent.putExtra("image",module.getCvImage());
                        startActivity(intent);
                    }
                });


                holder.accept_btn.setVisibility(View.GONE);

                holder.remove_btn.setOnClickListener(new View.OnClickListener() {  // Configuration du bouton de suppression
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleRequestActivity.this);
                        builder.setTitle((CharSequence) "Confirm");
                        builder.setMessage((CharSequence) "Are you sure you want to Remove?");
                        // Configuration du bouton "Non" pour fermer la boîte de dialogue.
                        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        // Configuration du bouton "Supprimer" pour supprimer l'élément de la base de données.
                        builder.setNegativeButton((CharSequence) "Remove", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child(module.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ScheduleRequestActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }
                        });
                        builder.show();
                    }
                });


            }
            // Cette méthode crée le ViewHolder pour la RecyclerView
            public MyViewHolder_Schedule onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder_Schedule(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_adapter_layout, viewGroup, false));
            }
        };
//        this.order_Adapter = order_Adapter;
        // Démarrage de l'écoute des modifications de données dans la base de données Firebase
        order_Adapter.startListening();
        // Configuration de l'adaptateur pour la RecyclerView.
        this.rv.setAdapter(this.order_Adapter);
    }

}