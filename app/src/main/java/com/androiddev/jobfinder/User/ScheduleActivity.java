package com.androiddev.jobfinder.User;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androiddev.jobfinder.Modules.ScheduleModule;
import com.androiddev.jobfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextInputEditText name_box, address_box, phone_box; // Ces champs sont utilisés pour recueillir les informations de l'utilisateur
    LinearLayout date_btn, time_btn; // Boutons pour sélectionner la date et l'heure
    TextView date_tv, time_tv, desc_tv; // Affichage de la date, de l'heure et de la description
    FrameLayout confirm_btn; // Bouton de confirmation pour enregistrer les données
    ImageView back_btn; // Bouton pour revenir à l'écran précédent

    DatabaseReference databaseReference;

    DatabaseReference reference;

    // Ces variables sont utilisées pour la gestion d'image
    FrameLayout  addImage_btn;

    public static ImageView imageView;
    private static final int GET_IMAGE_REQUEST_CODE = 100;
    Uri imageFilePath;
    Bitmap imageToStore;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    // Barre de progression pour le chargement de l'image
    private ProgressBar progressBar;

    // Référence pour le stockage de l'image dans Firebase
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        name_box = findViewById(R.id.name_box);
        address_box = findViewById(R.id.address_box);
        phone_box = findViewById(R.id.phone_box);
        date_btn = findViewById(R.id.date_btn);
        time_btn = findViewById(R.id.time_btn);
        date_tv = findViewById(R.id.date_tv);
        time_tv = findViewById(R.id.time_tv);
        desc_tv = findViewById(R.id.desc_tv);
        confirm_btn = findViewById(R.id.confirm_btn);
        back_btn = findViewById(R.id.back_btn);
        addImage_btn = findViewById(R.id.addImage_layout);
        imageView = findViewById(R.id.crtImage_iv);
        progressBar = findViewById(R.id.progressbar);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            } // On termine l'activité et on revient à l'écran précédent
        });
        // On récupère les données passées par l'intent précédent
        Intent intent = getIntent();
        String tname = intent.getStringExtra("name");
        String tphone = intent.getStringExtra("phone");
        String timage = intent.getStringExtra("image");
        String desc = intent.getStringExtra("desc");
        String mid = intent.getStringExtra("id");


        // On récupère les préférences de l'utilisateur pour pré-remplir les champs
        SharedPreferences preferences = getSharedPreferences("USER",MODE_PRIVATE);
        String phone = preferences.getString("phone","");
        String location = preferences.getString("location","");
        String name = preferences.getString("name","");

        name_box.setText(name);
        phone_box.setText(phone);
        address_box.setText(location);
        desc_tv.setText(desc);

        reference = FirebaseDatabase.getInstance().getReference().child("ADS");

        // On attache un OnClickListener au bouton d'ajout d'image pour sélectionner une image depuis la galerie
        addImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //code to Select image form the gallery of the phone
                // and set on the imageview
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_IMAGE_REQUEST_CODE);
            }
        });



        databaseReference  = FirebaseDatabase.getInstance().getReference();
        // On attache un OnClickListener au bouton de date pour afficher un DatePickerDialog
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ScheduleActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.setTitle("Set Schedule Date");
                dpd.show(getSupportFragmentManager(),"DatePicker");;

            }
        });

        // On attache un OnClickListener au bouton de temps pour afficher un TimePickerDialog
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ScheduleActivity.this
                        , now.get(Calendar.HOUR_OF_DAY)
                        ,now.get(Calendar.MINUTE),false);
                timePickerDialog.setTitle("Set Schedule Time");
                timePickerDialog.show(getSupportFragmentManager(),"TimePicker");
            }
        });
        // On attache un OnClickListener au bouton de confirmation pour envoyer les données à Firebase
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = name_box.getText().toString();
                String ph = phone_box.getText().toString();
                String ad = address_box.getText().toString();

                if (nm.equals("")){
                    Toast.makeText(ScheduleActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ph.equals("")){
                    Toast.makeText(ScheduleActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ad.equals("")){
                    Toast.makeText(ScheduleActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((date_tv.getText().toString()).equals("Date")){
                    Toast.makeText(ScheduleActivity.this, "Please Tell Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((time_tv.getText().toString()).equals("Time")){
                    Toast.makeText(ScheduleActivity.this, "Please Tell Time", Toast.LENGTH_SHORT).show();
                    return;
                }

                long random = System.currentTimeMillis();
                String id = "Schedule" + random;





                final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageFilePath));
                fileRef.putFile(imageFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {



                                ScheduleModule module = new ScheduleModule(tname,tphone,timage,nm,ph,ad,date_tv.getText().toString(),time_tv.getText().toString(),id,mid,uri.toString());

                                databaseReference.child("Schedules").child(id).setValue(module).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                long cid = snapshot.child(mid).child("applies").getValue(Long.class);
                                                cid ++;
                                                reference.child(mid).child("applies").setValue(cid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(ScheduleActivity.this, "Schedule Request Sent, Recruiter will contact you soon", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });






                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        confirm_btn.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        confirm_btn.setVisibility(View.VISIBLE);
                        Toast.makeText(ScheduleActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                    }
                });





                // This code is storing the Schedule data on firebase

            }
        });





    }
    // Ces méthodes sont appelées quand l'utilisateur a sélectionné une date ou une heure
    // On les utilise pour mettre à jour l'interface utilisateur avec la date ou l'heure sélectionnée
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
//        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        date_tv.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
//        String time = "You picked the following time: "+hourOfDay+"h"+minute+"m"+second;
//        Toast.makeText(this, "time", Toast.LENGTH_SHORT).show();
        time_tv.setText(hourOfDay+":"+minute+":"+second);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageFilePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                imageView.setImageBitmap(imageToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }




}