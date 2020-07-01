package com.example.iwimapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iwimapplication.Modal.Emploi;
import com.example.iwimapplication.Modal.Matiere;
import com.example.iwimapplication.Modal.Notifications;
import com.example.iwimapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AddMatiere extends AppCompatActivity {
    private static final String TAG = "AddMatiere" ;
    //declaration of attribut

    FirebaseAuth auth;
    FirebaseUser user;
    String userId;

    EditText Label ;
    EditText proff ;
    Spinner mspinner2;
    Spinner mspinner3 ;
    Button btn ;

    public Matiere matiere;
    //fire base cloud
    // FirebaseStorage firebaseStorage;
    //StorageReference storageReference ;
    // StorageReference ref ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matiere);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        Label =  findViewById(R.id.LabelMatiere);
        proff =  findViewById(R.id.Prof);
        mspinner2= findViewById(R.id.spinner2);
        mspinner3= findViewById(R.id.spinner3);
        btn = findViewById(R.id.btn_affecter);

        matiere = new Matiere();


        mspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                matiere.setSemestre(parent.getItemAtPosition(position).toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mspinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                matiere.setPeriode(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matiere.setLabel(Label.getText().toString());
                matiere.setProfesseur(proff.getText().toString());
                AddInfoMatiere(matiere);
                startActivity(new Intent(getApplicationContext(), Home.class));
                Toast.makeText(getApplicationContext(), "matière affectée", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void AddInfoMatiere(Matiere matiere){
        Map<String, Object> table = new HashMap<>();
        table.put("Label", matiere.getLabel());
        table.put("Semestre", matiere.getSemestre());
        table.put("Periode",matiere.getPeriode());
        table.put("Professeur",matiere.getProfesseur());

        final String label = matiere.getLabel();
        final String professeur = matiere.getProfesseur();
        final String name = matiere.getSemestre()+" "+matiere.getPeriode();
        db.collection("Matiere")
                .add(table)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Notifications.AddNotif(userId,"Chef de filière","La matière: "+label+" a été ajouté à Mr. "+professeur+"( "+name+" )","professeurs");
                        Notifications.AddNotif(userId,"Chef de filière","La matière: "+label+" a été ajouté à Mr. "+professeur+"( "+name+" )","etudiants");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
