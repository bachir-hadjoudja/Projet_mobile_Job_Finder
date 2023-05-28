package com.androiddev.jobfinder.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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

import com.androiddev.jobfinder.MainActivity;
import com.androiddev.jobfinder.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SigninActivity extends AppCompatActivity {
    TextInputEditText phone_box, pwd_box;
    FrameLayout login_btn;
    TextView newAccount_tv;
    TextView for_tv;
    Spinner language_sp;
    String language = "English";

    DatabaseReference databaseReference;

    TextView asa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // Références des éléments d'interface utilisateur
        phone_box = findViewById(R.id.phone_box);
        pwd_box = findViewById(R.id.pwd_box);
        login_btn = findViewById(R.id.login_btn);
        newAccount_tv = findViewById(R.id.sighuphere);
        for_tv= findViewById(R.id.for_tv);
        language_sp = findViewById(R.id.language_sp);
        // Écouteur pour le bouton "Forgot Password"
        for_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninActivity.this, ForgotPasswordUser.class));
            }
        });

        asa = findViewById(R.id.asa);
        asa.setText("AS User");
        // Référence à la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // Adapter pour le Spinner de sélection de la langue
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.lang,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language_sp.setAdapter(adapter);
        // Écouteur de sélection de langue
        language_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = parent.getItemAtPosition(position).toString();
                if (language.equals("Select Language")){
                    return;
                }else
                if (language.equals("English")){
                    setlanguage(SigninActivity.this,"en");
                    startActivity(new Intent(SigninActivity.this,SigninActivity.class));
                    finish();

                }else if (language.equals("French")){
                    setlanguage(SigninActivity.this,"po");
                    startActivity(new Intent(SigninActivity.this,SigninActivity.class));
                    finish();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Écouteur pour le texte "New Account"
        newAccount_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this,RegisterActivity.class));
                finish();
            }
        });
        // Écouteur pour le bouton de connexion
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phone_box.getText().toString().trim();
                String pwd = pwd_box.getText().toString().trim();


                // Vérifier les entrées de l'utilisateur
                if (phone.equals("")) {
                    phone_box.setError("Valid number is required");
                    phone_box.requestFocus();
                    return;
                }
                if (pwd.isEmpty()){
                    Toast.makeText(SigninActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length() < 8){
                    Toast.makeText(SigninActivity.this, "Password should have atleast 8 chars", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Vérifier les informations d'identification de l'utilisateur dans la base de données Firebase
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(phone)){
                            if (pwd.equals(snapshot.child(phone).child("password").getValue(String.class))){

                                Toast.makeText(SigninActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                LoginUserToApp(SigninActivity.this,snapshot.child(phone).child("name").getValue(String.class),phone,snapshot.child(phone).child("email").getValue(String.class),"user");
                                startActivity(new Intent(SigninActivity.this,MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(SigninActivity.this, "Check your Password", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(SigninActivity.this, "No user Registered with this Phone", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    // Méthode pour enregistrer les informations de l'utilisateur dans les préférences partagées
    public static void LoginUserToApp(Context context, String uname, String phone, String email,String status){
        SharedPreferences.Editor editor = context.getSharedPreferences("USER",MODE_PRIVATE).edit();
        editor.putString("phone",phone);
        editor.putString("uname",uname);
        editor.putString("status",status);
        editor.putString("email",email);
        editor.apply();
    }
    // Méthode pour définir la langue de l'application
    public void setlanguage (Activity activity, String language){
        Locale locale = new Locale (language);
        Resources resources = activity.getResources ();
        Configuration configuration = resources.getConfiguration ();
        configuration.setLocale (locale);
        resources. updateConfiguration(configuration, resources.getDisplayMetrics ());
    }
}