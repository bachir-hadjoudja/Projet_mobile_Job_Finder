package com.androiddev.jobfinder.Init;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androiddev.jobfinder.R;


public class intro_page extends AppCompatActivity {
    LinearLayout one_layout,two_layout,three_layout;
    TextView skip;
    ImageView next;
    View view;
    //Initialisation de la variable slide à 1.
    int slide =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Appel de la méthode onCreate de la classe parente.
        super.onCreate(savedInstanceState);
        //Mise en place du layout de l'activité.
        setContentView(R.layout.intro_page);
        //Récupération des vues par leur id.
        one_layout = findViewById(R.id.intro_1st);
        two_layout = findViewById(R.id.intro_2nd);
        three_layout = findViewById(R.id.intro_3rd);
        skip  =findViewById(R.id.skip_intro_btn);
        next = findViewById(R.id.next_intro_btn);

        //Mise en place initiale des vues.
        one_layout.setVisibility(View.VISIBLE);
        two_layout.setVisibility(View.GONE);
        three_layout.setVisibility(View.GONE);

        //Établissement de l'écouteur d'événements sur le bouton "next".
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Condition pour vérifier sur quelle slide on est et ajuster en conséquence.
                if (slide == 1) {
                    //Animation de transition entre les slides.
                    one_layout.animate().translationX(-one_layout.getWidth()).setDuration(1000);
                    //Rendre la deuxième slide visible.
                    two_layout.setVisibility(View.VISIBLE);
                    //On passe à la slide 2.
                    slide =2;
                    return;
                }
                //Même processus pour le passage de la slide 2 à 3.
                if (slide == 2) {
                    two_layout.animate().translationX(-two_layout.getWidth()).setDuration(1000);
                    three_layout.setVisibility(View.VISIBLE);
                    slide = 3;
                    return;
                }
                //Une fois la troisième slide atteinte, on stocke l'information que l'intro a été passée et on redirige vers l'activité principale.
                if (slide == 3) {
                    //Écriture dans les préférences partagées que l'intro a été passée.
                    SharedPreferences.Editor editor=getSharedPreferences("INTRO",MODE_PRIVATE).edit();
                    editor.putBoolean("intro",true);
                    editor.apply();
                    //Redirection vers l'activité principale.
                    Intent i=new Intent(getBaseContext(), GuestActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        //Établissement de l'écouteur d'événements sur le bouton "skip".
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Écriture dans les préférences partagées que l'intro a été passée.
                SharedPreferences.Editor editor=getSharedPreferences("INTRO",MODE_PRIVATE).edit();
                editor.putBoolean("intro",true);
                editor.apply();
                //Redirection vers l'activité principale.
                Intent i=new Intent(getBaseContext(), GuestActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}