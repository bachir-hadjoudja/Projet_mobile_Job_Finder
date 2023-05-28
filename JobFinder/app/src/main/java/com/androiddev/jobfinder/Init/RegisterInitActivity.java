package com.androiddev.jobfinder.Init;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androiddev.jobfinder.Manager.RegisterActivityManager;
import com.androiddev.jobfinder.R;
import com.androiddev.jobfinder.Recruiter.RegisterActivityRecruiter;
import com.androiddev.jobfinder.User.RegisterActivity;

public class RegisterInitActivity extends AppCompatActivity {
    // Déclaration des boutons et du TextView.
    FrameLayout candiate_btn, recruiter_btn, manager_btn;
    TextView login_tv;

    // Cette méthode est appelée lors de la création de l'activité.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Définit l'interface utilisateur pour cette activité à partir du fichier XML.
        setContentView(R.layout.activity_register_init);
        // Association des boutons et du TextView avec leurs identifiants respectifs dans le fichier XML.
        candiate_btn = findViewById(R.id.candidate_btn);
        manager_btn = findViewById(R.id.manager_btn);
        login_tv = findViewById(R.id.login_instead_tv);

        // Ajout d'un écouteur d'événements au bouton du candidat.
        candiate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lancement de l'activité d'inscription du candidat lors du clic sur le bouton du candidat.
                startActivity(new Intent(RegisterInitActivity.this, RegisterActivity.class));
                // Fin de l'activité actuelle.
                finish();
            }
        });

        recruiter_btn = findViewById(R.id.recruiter_btn);

        // Ajout d'un écouteur d'événements au bouton du recruteur.
        recruiter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lancement de l'activité d'inscription du recruteur lors du clic sur le bouton du recruteur.
                startActivity(new Intent(RegisterInitActivity.this, RegisterActivityRecruiter.class));
                // Fin de l'activité actuelle.
                finish();
            }
        });

        // Ajout d'un écouteur d'événements au bouton du gestionnaire.
        manager_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lancement de l'activité d'inscription du gestionnaire lors du clic sur le bouton du gestionnaire.
                startActivity(new Intent(RegisterInitActivity.this, RegisterActivityManager.class));
                // Fin de l'activité actuelle.
                finish();
            }
        });

        // Ajout d'un écouteur d'événements au TextView de connexion.
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lancement de l'activité de connexion initiale lors du clic sur le TextView de connexion.
                startActivity(new Intent(RegisterInitActivity.this, SigninInitActivity.class));
                // Fin de l'activité actuelle.
                finish();
            }
        });

    }
}