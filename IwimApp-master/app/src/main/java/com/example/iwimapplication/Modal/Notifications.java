package com.example.iwimapplication.Modal;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifications {
    public static void AddNotif(String ID_sender, String Name_sender, String Text, String type){
        //SETTING FINAL INSTACES
        final String ID_SENDER=ID_sender;
        final String NAME_SENDER=Name_sender;
        final String TEXT=Text;
        final String TYPE=type;
        //GETTING FIRESTORE REFERENCE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //GETTING CURRENT TIMESTAMP
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        final Timestamp DateTime = new Timestamp(System.currentTimeMillis());
        //GETTING A LIST WITH ALL IDS
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Snapshot", "Error: " + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        //INSERTING FOR ALL
                        if(TYPE.equals("all")){
                            String Idusr=doc.getDocument().getId();

                            if(!Idusr.equals(ID_SENDER)){

                                Map<String, Object> table = new HashMap<>();
                                table.put("ID_sender",ID_SENDER);
                                table.put("DateTime", DateTime);
                                table.put("Is_read",false);
                                table.put("Name_sender",NAME_SENDER);
                                table.put("Text",TEXT);


                                FirebaseFirestore dbb = FirebaseFirestore.getInstance();
                                dbb.collection("users/"+Idusr+"/Notifications")
                                        .add(table)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("insertion", "notification added with ID: " + documentReference.getId());

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("insertion", "Error adding document", e);
                                            }
                                        });
                            }}
                        //INSERTING FOR JUST STUDENTS
                        else if(TYPE.equals("etudiants")){
                            String Idusr=doc.getDocument().getId();
                            String Statut=doc.getDocument().getString("statut");
                            if(!Idusr.equals(ID_SENDER) && Statut.equals("Etudiant")){

                                Map<String, Object> table = new HashMap<>();
                                table.put("ID_sender",ID_SENDER);
                                table.put("DateTime", DateTime);
                                table.put("Is_read",false);
                                table.put("Name_sender",NAME_SENDER);
                                table.put("Text",TEXT);


                                FirebaseFirestore dbb = FirebaseFirestore.getInstance();
                                dbb.collection("users/"+Idusr+"/Notifications")
                                        .add(table)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("insertion", "notification added with ID: " + documentReference.getId());

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("insertion", "Error adding document", e);
                                            }
                                        });
                            }
                        }
                        //INSERTING JUST FOR PROFS
                        else if(TYPE.equals("professeurs")){
                            String Idusr=doc.getDocument().getId();
                            String Statut=doc.getDocument().getString("statut");
                            if(!Idusr.equals(ID_SENDER) && Statut.equals("Professeur")){

                                Map<String, Object> table = new HashMap<>();
                                table.put("ID_sender",ID_SENDER);
                                table.put("DateTime", DateTime);
                                table.put("Is_read",false);
                                table.put("Name_sender",NAME_SENDER);
                                table.put("Text",TEXT);


                                FirebaseFirestore dbb = FirebaseFirestore.getInstance();
                                dbb.collection("users/"+Idusr+"/Notifications")
                                        .add(table)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("insertion", "notification added with ID: " + documentReference.getId());

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("insertion", "Error adding document", e);
                                            }
                                        });
                            }
                        }
                        //INSERTING JUST FOR CHEF
                        else if(TYPE.equals("chefs")){
                            String Idusr=doc.getDocument().getId();
                            String Statut=doc.getDocument().getString("statut");
                            if(!Idusr.equals(ID_SENDER) && Statut.equals("Chef")){

                                Map<String, Object> table = new HashMap<>();
                                table.put("ID_sender",ID_SENDER);
                                table.put("DateTime", DateTime);
                                table.put("Is_read",false);
                                table.put("Name_sender",NAME_SENDER);
                                table.put("Text",TEXT);


                                FirebaseFirestore dbb = FirebaseFirestore.getInstance();
                                dbb.collection("users/"+Idusr+"/Notifications")
                                        .add(table)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("insertion", "notification added with ID: " + documentReference.getId());

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("insertion", "Error adding document", e);
                                            }
                                        });
                            }
                        }else{
                            String Idusr=doc.getDocument().getId();
                            if(!Idusr.equals(ID_SENDER) && TYPE.equals(doc.getDocument().getId())){
                                Map<String, Object> table = new HashMap<>();
                                table.put("ID_sender",ID_SENDER);
                                table.put("DateTime", DateTime);
                                table.put("Is_read",false);
                                table.put("Name_sender",NAME_SENDER);
                                table.put("Text",TEXT);


                                FirebaseFirestore dbb = FirebaseFirestore.getInstance();
                                dbb.collection("users/"+Idusr+"/Notifications")
                                        .add(table)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("insertion", "notification added with ID: " + documentReference.getId());

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("insertion", "Error adding document", e);
                                            }
                                        });
                            }
                        }
                    }
                }
            }
        });
    }

    public static void seen(String notificationID,String userID){
        FirebaseFirestore fstore;
        fstore = FirebaseFirestore.getInstance();
        Map<String,Object> notification = new HashMap<>();
        notification.put("Is_read",true);
        String path="users/"+userID+"/Notifications";
        fstore.collection(path).document(notificationID).update(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("successNotification","Nothing gone wrong while updating");
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("errorNotification","something gone wrong while updating");
            }
        });
    }

}
