package com.androiddev.jobfinder.Recruiter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.androiddev.jobfinder.R;
import com.squareup.picasso.Picasso;

// Le recruteur peut visualiser le CV d'un candidat
public class CvActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv);
        imageView = findViewById(R.id.cv_iv);
        // Récupérer l'intent qui a lancé cette activité
        Intent intent = getIntent();
        // Récupérer l'image du CV de l'intent
        String image = intent.getStringExtra("image");
        // Utiliser Picasso, une bibliothèque de gestion d'images, pour charger l'image
        // du CV dans l'ImageView. Si le chargement de l'image est en cours,
        // une image par défaut (ici, round_logo) est affichée
        Picasso.with(CvActivity.this).load(image).placeholder(R.drawable.round_logo).into(imageView);
    }
}