package com.androiddev.jobfinder.User;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androiddev.jobfinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MappingActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;// Constante définissant la demande de permission pour l'accès à la localisation
    FusedLocationProviderClient fusedLocationProviderClient; // Client pour interagir avec le service de localisation de Google Play Services
//    Double lat;
//    Double longt;


    String latStr, longtStr; // Chaînes pour stocker les valeurs de latitude et longitude
    String marker_name= ""; // Nom du marqueur à afficher sur la carte

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        // Instancie le client de localisation.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (getApplicationContext().checkSelfPermission(false)){
//
//                    }
            // Vérifie si l'application a les permissions nécessaires pour accéder à la localisation de l'appareil.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Si les permissions ne sont pas accordées, fait une demande à l'utilisateur
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }else {
                // Si les permissions sont accordées, récupère la dernière localisation connue
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Si la localisation n'est pas nulle, récupère la latitude et la longitude, puis affiche la carte
                        if (location != null){
                            latStr = location.getLatitude()+"";
                            longtStr =location.getLongitude()+"";
                            Toast.makeText(MappingActivity.this, "Location success", Toast.LENGTH_SHORT).show();
                            SupportMapFragment supportMapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
                            supportMapFragment.getMapAsync(MappingActivity.this);
//                            Log.e("TAG",lat+" , "+longt);
//                        location_tv.setText("Location Identification Success");
//                        location_tv.setTextColor(getResources().getColor(R.color.green_color));

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG","LOCATION GET FAILED");
                    }
                });
            }



        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Gère les résultats des demandes de permissions.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        // Si la permission est accordée, récupère la dernière localisation connue.
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Si la localisation n'est pas nulle, récupère la latitude et la longitude, puis affiche la carte
                                if (location != null) {
                                    latStr = location.getLatitude() + "";
                                    longtStr = location.getLongitude() + "";
                                    Toast.makeText(MappingActivity.this, "Location success", Toast.LENGTH_SHORT).show();
                                    SupportMapFragment supportMapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
                                    supportMapFragment.getMapAsync(MappingActivity.this);

//                                    Log.e("TAG",lat+" , "+longt);


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("TAG", "LOCATION GET FAILED");
                            }
                        });
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Lorsque la carte est prête, crée un nouveau LatLng avec la latitude et la longitude, puis place un marqueur et zoome dessus
        Double lat = Double.parseDouble("43.610767");
        Double longt = Double.parseDouble("3.876716");

        LatLng latLng =new LatLng(lat, longt);
//        LatLng latLng =new LatLng(30.005630,73.257950);

        SharedPreferences preferences =getSharedPreferences("USER",MODE_PRIVATE);
        // Si les préférences contiennent une valeur "name", récupère cette valeur et l'attribue à marker_name
        if (preferences.contains("name")){
            marker_name = preferences.getString("name","");
        }
        // Crée un nouveau marqueur à la position latLng, avec le titre défini par marker_name
        MarkerOptions markerOptions=new MarkerOptions().position(latLng)
                .title("marker_name");
        // Anime la caméra pour se déplacer jusqu'à la position latLng
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        // Anime la caméra pour zoomer à une échelle de 15.
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        // Ajoute le marqueur à la carte
        googleMap.addMarker(markerOptions);

    }

}