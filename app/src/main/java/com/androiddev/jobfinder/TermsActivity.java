package com.androiddev.jobfinder;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class TermsActivity extends AppCompatActivity {
    WebView webView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        webView = findViewById(R.id.web);
        back = findViewById(R.id.back_btn);

        // Définition de l'action lors du clic sur le bouton de retour
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        // Chargement de l'URL des termes et conditions dans la WebView
        webView.loadUrl("https://www.privacypolicygenerator.info/live.php?token=EBWsDbEAR09vCtKR5de9wOwxedHAFcSt");

        // Définition d'un WebViewClient pour la WebView
        // Ceci est nécessaire pour que toutes les URL soient chargées dans la WebView,
        // et non dans le navigateur par défaut du périphérique
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Chargement de l'URL dans la WebView
                view.loadUrl(url);
                return true;
            }
        });
    }
}