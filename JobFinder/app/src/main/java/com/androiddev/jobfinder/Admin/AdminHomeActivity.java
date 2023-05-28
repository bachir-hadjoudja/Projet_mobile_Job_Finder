package com.androiddev.jobfinder.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.androiddev.jobfinder.Init.SigninInitActivity;
import com.androiddev.jobfinder.R;

public class AdminHomeActivity extends AppCompatActivity {
    ImageView logout_btn;
    FrameLayout companies_btn, ads_btn, schedules_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        logout_btn = findViewById(R.id.logout_btn);
        companies_btn = findViewById(R.id.companies_btn);
        ads_btn = findViewById(R.id.ads_btn);
        schedules_btn = findViewById(R.id.schedule_btn);

        // Gestion du clic sur le bouton de déconnexion
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeActivity.this);
                builder.setTitle((CharSequence) "Confirm");
                builder.setMessage((CharSequence) "Are you sure you want to LOGOUT?");
                builder.setPositiveButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton((CharSequence) "LOGOUT", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Effacer les informations d'utilisateur stockées dans les préférences partagées
                        SharedPreferences sharedPreferences =getSharedPreferences("USER",MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        // Rediriger vers l'activité de connexion
                        startActivity(new Intent(AdminHomeActivity.this, SigninInitActivity.class));
                        finish();
                    }
                });
                builder.show();
            }
        });
// Gestion du clic sur le bouton des entreprises
        companies_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Rediriger vers l'activité affichant la liste des entreprises
                startActivity(new Intent(AdminHomeActivity.this, CompaniesActivity.class));
            }
        });

        ads_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Rediriger vers l'activité affichant la liste des offres d'emploi
                startActivity(new Intent(AdminHomeActivity.this, JobsActivity.class));
            }
        });

        schedules_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Rediriger vers l'activité affichant la liste des plannings
                startActivity(new Intent(AdminHomeActivity.this, SchedActivity.class));
            }
        });


    }
}