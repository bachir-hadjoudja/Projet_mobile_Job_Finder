package com.androiddev.jobfinder.Admin;


import static com.androiddev.jobfinder.User.SigninActivity.LoginUserToApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androiddev.jobfinder.MainActivity;
import com.androiddev.jobfinder.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SigninActivityAdmin extends AppCompatActivity {
    TextInputEditText phone_box, pwd_box;
    FrameLayout login_btn;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vérifier si l'administrateur est déjà connecté
        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);
        if (preferences.contains("phone")){
            startActivity(new Intent(SigninActivityAdmin.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_signinadmin);

        phone_box = findViewById(R.id.phone_box);
        pwd_box = findViewById(R.id.pwd_box);
        login_btn = findViewById(R.id.login_btn);


        // Obtenir une référence à la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();




        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phone_box.getText().toString().trim();
                String pwd = pwd_box.getText().toString().trim();


            // Validation des champs de saisie
                if (phone.equals("")) {
                    phone_box.setError("Valid number is required");
                    phone_box.requestFocus();
                    return;
                }
                if (pwd.isEmpty()){
                    Toast.makeText(SigninActivityAdmin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length() < 8){
                    Toast.makeText(SigninActivityAdmin.this, "Password should have atleast 8 chars", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Vérification des informations de connexion dans la base de données Firebase
                databaseReference.child("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(phone)){
                            if (pwd.equals(snapshot.child(phone).child("password").getValue(String.class))){
                                // Informations de connexion valides
                                Toast.makeText(SigninActivityAdmin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                // Enregistrement de l'utilisateur dans les préférences partagées
                                LoginUserToApp(SigninActivityAdmin.this,snapshot.child(phone).child("name").getValue(String.class),phone,snapshot.child(phone).child("email").getValue(String.class),"admin");
                                // Redirection vers l'activité principale de l'administrateur
                                startActivity(new Intent(SigninActivityAdmin.this,AdminHomeActivity.class));
                                finish();
                            }else {
                                Toast.makeText(SigninActivityAdmin.this, "Check your Password", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(SigninActivityAdmin.this, "No Admin Registered with this Phone", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


}