package com.androiddev.jobfinder.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.androiddev.jobfinder.Modules.PostModule;
import com.androiddev.jobfinder.MyviewHolders.MyViewHolder_Manager;
import com.androiddev.jobfinder.R;

import com.androiddev.jobfinder.Init.SigninInitActivity;
import com.androiddev.jobfinder.Recruiter.PostJobActivity;
import com.androiddev.jobfinder.Recruiter.RecruiterHomeActivity;
import com.androiddev.jobfinder.Recruiter.UpdateAdActivity;
import com.androiddev.jobfinder.TermsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.special.ResideMenu.BuildConfig;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.picasso.Picasso;

public class ManagerHomeActivity extends AppCompatActivity {

    ImageView nav_btn, myAccount_btn;
    RecyclerView rv;

    FloatingActionButton fab;


    private ResideMenu resideMenu;
    private ResideMenuItem myAccount;
    private ResideMenuItem terms_btn;
    private ResideMenuItem shareUs_btn;
    private ResideMenuItem rateUse_btn;
    private ResideMenuItem myPosts_btn;
    private ResideMenuItem logout_btn;



    DatabaseReference databaseReference;
    DatabaseReference reference;
    FirebaseRecyclerAdapter<PostModule, MyViewHolder_Manager> order_Adapter;
    FirebaseRecyclerOptions<PostModule> orders_options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);
        nav_btn  = findViewById(R.id.nav_btn);
        myAccount_btn  = findViewById(R.id.my_account_btn);
        rv  = findViewById(R.id.rv);
        nav_btn  = findViewById(R.id.nav_btn);
        fab  = findViewById(R.id.fab_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManagerHomeActivity.this, "Fine!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ManagerHomeActivity.this, PostJobActivity.class));
            }
        });


        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        //This code to create an Navigation drawer, I got this library from github


        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.nav);
        resideMenu.attachToActivity(ManagerHomeActivity.this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);


        myAccount = new ResideMenuItem(this, R.drawable.ic_user_selected,     "My Account");
        terms_btn = new ResideMenuItem(this, R.drawable.baseline_info_24,     "Terms & Cond.");
        shareUs_btn = new ResideMenuItem(this, R.drawable.baseline_share_24,     "Share Us");
        rateUse_btn = new ResideMenuItem(this, R.drawable.baseline_star_24,     "Rate Us");
        logout_btn = new ResideMenuItem(this, R.drawable.logout_ic,  "Logout");



        resideMenu.addMenuItem(myAccount, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(terms_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(shareUs_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(rateUse_btn, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(logout_btn, ResideMenu.DIRECTION_LEFT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        myAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerHomeActivity.this, ManagerMyAccountActivity.class));
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerHomeActivity.this,ManagerMyAccountActivity.class));

            }
        });
        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerHomeActivity.this, TermsActivity.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerHomeActivity.this);
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
                        startActivity(new Intent(ManagerHomeActivity.this, SigninInitActivity.class));
                        finish();
                    }
                });
                builder.show();
            }
        });
        myAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerHomeActivity.this);
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
                        startActivity(new Intent(ManagerHomeActivity.this, SigninInitActivity.class));
                        finish();
                    }
                });
                builder.show();
            }
        });


        shareUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage= "Share";
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
                    String shareMessage= "Share";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });





        reference = FirebaseDatabase.getInstance().getReference().child("Managers");
        //Logout button code
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerHomeActivity.this, TermsActivity.class));

            }
        });


        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);
        String phone = preferences.getString("phone","");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cid = snapshot.child(phone).child("cregis").getValue(String.class);
                Toast.makeText(ManagerHomeActivity.this, cid, Toast.LENGTH_SHORT).show();
                load_orderAdapter(cid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    // This is the method which is getting data from Firebase database, parsing it and
    // showing in the recyclerview using LinearLayout manager.
    public void load_orderAdapter(String cid) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("ADS");
        Query query = databaseReference.orderByChild("cregis").equalTo(cid);
        orders_options = new FirebaseRecyclerOptions.Builder<PostModule>().setQuery(query, PostModule.class).build();
        order_Adapter  = new FirebaseRecyclerAdapter<PostModule, MyViewHolder_Manager>(this.orders_options) {
            /* access modifiers changed from: protected */
            public void onBindViewHolder(MyViewHolder_Manager holder, int i, PostModule module) {
                Picasso.with(ManagerHomeActivity.this).load(module.getImage()).placeholder(R.drawable.round_logo).into(holder.iv_productImage);
                holder.name_tv.setText(module.getName()+", "+module.getEducation());
                holder.subject_tv.setText(module.getSubject());
                holder.price_tv.setText(module.getPrice());
                holder.applies_tv.setText("Applies: "+module.getApplies());
                holder.employes_tv.setText("Interviews: "+module.getInterviewing());

                holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerHomeActivity.this);
                        builder.setTitle((CharSequence) "Confirm");
                        builder.setMessage((CharSequence) "Are you sure you want to Remove Post?");
                        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton((CharSequence) "Remove", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference.child(module.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ManagerHomeActivity.this, "Post Removed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                        builder.show();

                    }
                });

                holder.edit_btn.setVisibility(View.VISIBLE);
                holder.edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ManagerHomeActivity.this, UpdateAdActivity.class);
                        intent.putExtra("id",module.getId());
                        intent.putExtra("name", module.getName());
                        intent.putExtra("location",module.getLocation());
                        intent.putExtra("skills",module.getSubject());
                        intent.putExtra("education",module.getEducation());
                        intent.putExtra("salary",module.getPrice());
                        intent.putExtra("timing",module.getTime());
                        intent.putExtra("desc",module.getDescription());
                        intent.putExtra("cregis",module.getCregis());
                        intent.putExtra("image",module.getImage());
                        startActivity(intent);
                    }
                });

//


//


            }

            public MyViewHolder_Manager onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new MyViewHolder_Manager(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.manager_adapter_layout, viewGroup, false));
            }
        };
//        this.order_Adapter = order_Adapter;
        order_Adapter.startListening();
        this.rv.setAdapter(this.order_Adapter);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerHomeActivity.this);
        builder.setTitle((CharSequence) "Confirm");
        builder.setMessage((CharSequence) "Are you sure you want to Exit?");
        builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "Exit", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ManagerHomeActivity.super.onBackPressed();
            }
        });
        builder.show();




    }

}