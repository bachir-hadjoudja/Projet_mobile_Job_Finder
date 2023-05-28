package com.androiddev.jobfinder.Init;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.androiddev.jobfinder.Admin.AdminHomeActivity;
import com.androiddev.jobfinder.Admin.SigninActivityAdmin;
import com.androiddev.jobfinder.MainActivity;
import com.androiddev.jobfinder.Manager.ManagerHomeActivity;
import com.androiddev.jobfinder.Manager.SigninActivityManager;
import com.androiddev.jobfinder.R;
import com.androiddev.jobfinder.Recruiter.RecruiterHomeActivity;
import com.androiddev.jobfinder.Recruiter.SigninActivityRecruiter;
import com.androiddev.jobfinder.User.SigninActivity;

import java.util.Locale;

// Cette classe initialise l'activité de connexion.
public class SigninInitActivity extends AppCompatActivity {
    // Déclaration des variables pour les boutons et les champs
    FrameLayout candiate_btn, recruiter_btn, manager_btn, admin_btn;
    TextView register_tv;
    Spinner language_sp;
    String language = "English";

    // Cette méthode est appelée à la création de l'activité
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lecture des préférences partagées
        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);

        // Si l'utilisateur est déjà connecté, il est redirigé vers l'activité appropriée
        if (preferences.contains("phone")){
            if ((preferences.getString("status","")).equals("user")){
                startActivity(new Intent(SigninInitActivity.this, MainActivity.class));
                finish();
            }else if ((preferences.getString("status","")).equals("admin")){
                startActivity(new Intent(SigninInitActivity.this, AdminHomeActivity.class));
                finish();
            }else if ((preferences.getString("status","")).equals("recruiter")){
                startActivity(new Intent(SigninInitActivity.this, RecruiterHomeActivity.class));
                finish();
            }else if ((preferences.getString("status","")).equals("manager")){
                startActivity(new Intent(SigninInitActivity.this, ManagerHomeActivity.class));
                finish();
            }

        }

        // Configuration de la vue
        setContentView(R.layout.activity_signin_init);

        // Liaison des vues aux variables
        candiate_btn = findViewById(R.id.candidate_btn);
        manager_btn = findViewById(R.id.manager_btn);
        admin_btn = findViewById(R.id.admin_btn);
        register_tv = findViewById(R.id.register_instead_tv);
        language_sp = findViewById(R.id.language_sp);

        // Configuration des écouteurs de clic pour chaque bouton
        // Chaque bouton démarre une nouvelle activité lorsqu'il est cliqué.
        candiate_btn.setOnClickListener(new View.OnClickListener() {
            // L'utilisateur est redirigé vers l'activité de connexion.
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninInitActivity.this, SigninActivity.class));
                finish();
            }
        });

        recruiter_btn = findViewById(R.id.recruiter_btn);

        recruiter_btn.setOnClickListener(new View.OnClickListener() {
            // Le recruteur est redirigé vers l'activité de connexion du recruteur.
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninInitActivity.this, SigninActivityRecruiter.class));
                finish();
            }
        });

        manager_btn.setOnClickListener(new View.OnClickListener() {
            // Le manager est redirigé vers l'activité de connexion du manager.
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninInitActivity.this, SigninActivityManager.class));
                finish();
            }
        });

        admin_btn.setOnClickListener(new View.OnClickListener() {
            // L'administrateur est redirigé vers l'activité de connexion de l'administrateur.
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninInitActivity.this, SigninActivityAdmin.class));
                finish();
            }
        });

        // L'utilisateur est redirigé vers l'activité d'inscription lorsqu'il clique sur le texte d'inscription.
        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (SigninInitActivity.this, RegisterInitActivity.class));
                finish();
            }
        });



        // Configuration de l'adaptateur pour le spinner de langue
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.lang,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language_sp.setAdapter(adapter);

        // Définir le comportement lorsque l'utilisateur sélectionne une langue dans le spinner
        language_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = parent.getItemAtPosition(position).toString();
                if (language.equals("Select Language")){
                    return;
                }else
                if (language.equals("English")){
                    // Changer la langue de l'application en anglais
                    setlanguage(SigninInitActivity.this,"en");
                    // Redémarrer l'activité pour que le changement de langue prenne effet
                    startActivity(new Intent(SigninInitActivity.this,SigninInitActivity.class));
                    finish();

                }else if (language.equals("French")){
                    // Changer la langue de l'application en français
                    setlanguage(SigninInitActivity.this,"po");
                    startActivity(new Intent(SigninInitActivity.this,SigninInitActivity.class));
                    finish();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Aucune action si aucune langue n'est sélectionnée
            }
        });







    }
    // Cette méthode change la langue de l'application
    public void setlanguage (Activity activity, String language){
        Locale locale = new Locale (language);
        Resources resources = activity.getResources ();
        Configuration configuration = resources.getConfiguration ();
        configuration.setLocale (locale);
        resources. updateConfiguration(configuration, resources.getDisplayMetrics ());
    }
}