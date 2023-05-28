package com.androiddev.jobfinder.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androiddev.jobfinder.Modules.UserModule;
import com.androiddev.jobfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText  editTextPhone;
    FrameLayout buttonContinue;
    TextInputEditText name_box,pwd_box,pwd_confrim_box;
    Spinner gender_sp;
    String gender;
    DatabaseReference databaseReference;

    TextView loginhere_tv;
    String email;

    TextView asar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Liaison des variables déclarées avec les composants d'interface utilisateur correspondants dans le layout
        setContentView(R.layout.activity_register);
        editTextPhone = findViewById(R.id.phone_box);
        buttonContinue = findViewById(R.id.signup_btn);
        name_box = findViewById(R.id.name_box);
        pwd_box = findViewById(R.id.pwd_box);
        pwd_confrim_box = findViewById(R.id.cpwd_box);
        gender_sp = findViewById(R.id.gender_sp);
        loginhere_tv = findViewById(R.id.loginhere_tv);

        // Configuration du listener pour le TextView 'loginhere_tv'. Quand on clique dessus, on est redirigé vers l'activité SigninActivity
        loginhere_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,SigninActivity.class));
                finish();
            }
        });

        asar = findViewById(R.id.asar);
        asar.setText("As a User");




        // Configuration du Spinner pour le choix du genre
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_sp.setAdapter(adapter);

        gender_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Récupération de l'option choisie
                gender = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Configuration du listener pour le bouton 'buttonContinue'
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des informations entrées par l'utilisateur
                String phoneNumber = editTextPhone.getText().toString().trim();
                String name = name_box.getText().toString().trim();
                String pwd = pwd_box.getText().toString().trim();
                String pwd_cnfrm  = pwd_confrim_box.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                // Vérification de la validité des informations
                // Si le nom est vide
                if (name.isEmpty()){
                    name_box.setError("Name is required"); // Afficher une erreur
                    name_box.requestFocus(); // Demander le focus
                    return;
                }
                // Si le mot de passe ou la confirmation du mot de passe sont vides
                if (pwd.isEmpty() || pwd_cnfrm.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please Enter Passwords", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Si le mot de passe et la confirmation ne correspondent pas
                if (!pwd.equals(pwd_cnfrm)){
                    Toast.makeText(RegisterActivity.this, "Please confirm Passwords", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Si le mot de passe a moins de 8 caractères
                if (pwd.length()<8){
                    Toast.makeText(RegisterActivity.this, "Password must be of 8 chars", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Si le numéro de téléphone est vide
                if (phoneNumber.isEmpty() ) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }



                // Si le genre n'est pas sélectionné
                if (gender.equals("Gender")){
                    Toast.makeText(RegisterActivity.this, "Please Select your gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Initialisation de la référence à la base de données Firebase
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                // Vérification de l'existence de l'utilisateur dans la base de données
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Si l'utilisateur existe déjà
                            if (snapshot.hasChild(phoneNumber)){
                                Toast.makeText(RegisterActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // Création d'un nouveau module utilisateur
                            UserModule module=new UserModule(name,phoneNumber,email,pwd,gender);
                            // Ajout du module utilisateur à la base de données
                            databaseReference.child(phoneNumber).setValue(module).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // Après l'ajout réussi, redirection vers l'activité SigninActivity
                                    Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

//                                    LoginUserToApp(RegisterActivity.this,name,phoneNumber,email);
                                }
                            });

//                        Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
//                        intent.putExtra("phoneNumber", number);
//                        intent.putExtra("name",name);
//                        intent.putExtra("pwd",pwd);
//                        startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });








            }
        });


    }
}