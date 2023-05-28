package com.androiddev.jobfinder.Recruiter;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ScheduleRequestManagerActivity extends AppCompatActivity {

    RecyclerView rv;
    ImageView back_btn;

    // Déclaration de la référence à la base de données Firebase
    DatabaseReference databaseReference;
    // Déclaration de l'adaptateur pour la RecyclerView et les options de cet adaptateur
    FirebaseRecyclerAdapter<ScheduleModule, MyViewHolder_Schedule> order_Adapter;
    FirebaseRecyclerOptions<ScheduleModule> orders_options;
    // Autre référence à la base de données Firebase pour les publicités
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_request);
        // Initialisation du bouton de retour et de la RecyclerView avec les vues correspondantes
        back_btn = findViewById(R.id.back_btn);
        rv = findViewById(R.id.sr_rv);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Initialisation de la référence à la base de données Firebase pour les publicités
        reference = FirebaseDatabase.getInstance().getReference().child("ADS");
        // Configuration de la RecyclerView pour utiliser un LinearLayoutManager
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);
        String phone = preferences.getString("phone","");
        // Récupération du numéro de téléphone à partir des préférences partagées
        load_orderAdapter(phone);
    }

    // Cette méthode récupère les données de la base de données Firebase, les analyse et les affiche dans la RecyclerView en utilisant le LinearLayout manager. Adapter

    public void load_orderAdapter(String string) {
        // Initialisation de la référence à la base de données Firebase pour les horaires
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules");
        // Création d'une requête pour obtenir les horaires associés à un numéro de téléphone spécifique
        Query query = databaseReference.orderByChild("tphone").equalTo(string);
        // Configuration des options de l'adaptateur de la RecyclerView
        orders_options = new FirebaseRecyclerOptions.Builder<ScheduleModule>().setQuery(query, ScheduleModule.class).build();
        order_Adapter  = new FirebaseRecyclerAdapter<ScheduleModule, MyViewHolder_Schedule>(this.orders_options) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(MyViewHolder_Schedule holder, int i, ScheduleModule module) {
                Picasso.with(ScheduleRequestManagerActivity.this).load(module.getTimage()).placeholder(R.drawable.round_logo).into(holder.imageView);
                holder.name_tv.setText(module.getSname());
                holder.date_tv.setText(module.getDate()+"");
                holder.address_tv.setVisibility(View.VISIBLE);
                holder.address_tv.setText(module.getSlocation());
                holder.time_tv.setText(module.getTime());


                holder.call_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+module.getSphone())));

                    }
                });
                holder.view_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ScheduleRequestManagerActivity.this,CvActivity.class);
                        intent.putExtra("image",module.getCvImage());
                        startActivity(intent);
                    }
                });

                holder.remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleRequestManagerActivity.this);
                        builder.setTitle((CharSequence) "Confirm");
                        builder.setMessage((CharSequence) "Are you sure you want to Remove?");
                        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton((CharSequence) "Remove", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child(module.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ScheduleRequestManagerActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }
                        });
                        builder.show();
                    }
                });

                holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleRequestManagerActivity.this);
                        builder.setTitle((CharSequence) "Confirm");
                        builder.setMessage((CharSequence) "Are you sure you want to Accept?");
                        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton((CharSequence) "Accept", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long cid = snapshot.child(module.getAdid()).child("interviewing").getValue(Long.class);
                                        cid ++;
                                        reference.child(module.getAdid()).child("interviewing").setValue(cid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                holder.remove_btn.setVisibility(View.GONE);
                                                holder.accept_tv.setText("ACCEPTED");


                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });






                                //here it is


                            }
                        });
                        builder.show();
                    }
                });




            }

            public MyViewHolder_Schedule onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder_Schedule(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_adapter_layout, viewGroup, false));
            }
        };
        // Démarrage de l'écoute des changements de données pour l'adaptateur
        order_Adapter.startListening();
        this.rv.setAdapter(this.order_Adapter);
    }

}