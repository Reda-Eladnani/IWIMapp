package com.example.iwimapplication.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iwimapplication.Modal.Notification;
import com.example.iwimapplication.Modal.Notifications;
import com.example.iwimapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class Home extends AppCompatActivity  {

    private static final String TAG = "Home";
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    String userId;

    ImageView profile, message, notification;
    TextView nb_notif;
    TextView non_valide;
    CardView notif_cd;
    Button logout;

    ListView menu;
    String enabled = "false";

    public void getNumberNotifications(String usrId){
        FirebaseFirestore firestore;
        firestore = FirebaseFirestore.getInstance();
        String path="users/"+userId+"/Notifications";
        firestore.collection(path).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Snapshot", "Error: " + e.getMessage());
                }
                int ivf=0;
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if(doc.getDocument().getBoolean("Is_read")==false) {
                        ivf++;
                    }
                }
                Log.d("nombre",String.valueOf(ivf));
                if(ivf!=0) {
                    notif_cd.setVisibility(VISIBLE);
                    nb_notif.setText(String.valueOf(ivf));
                }
            }
        });
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        logout = findViewById(R.id.logout);

        firebaseFirestore = FirebaseFirestore.getInstance();

        profile = findViewById(R.id.ab_profile);
        message = findViewById(R.id.ab_message);
        notification = findViewById(R.id.ab_notification);
        notif_cd=findViewById(R.id.notification_number);
        non_valide = findViewById(R.id.pasvalide);
        nb_notif= findViewById(R.id.number_notifications);
        menu = findViewById(R.id.homeMenu);
        menu.setDivider(null);
        menu.setDividerHeight(20);

        if (userId != null) {

            getNumberNotifications(userId);
            if(user.getDisplayName().equals("Chef")){

                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        finish();
                    }
                });

//                message.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(getApplicationContext(), UsersFragment.class));
//                    }
//                });
                notification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Show_notifications.class));
                        finish();
                    }
                });
                String titres[] = {"Gérer l'emploi du temps", "Gérer les matières", "Accepter les professeurs", "Accepter les étudiants", "Gérer les étudiants", "Gérer les professeurs"};
                int images[] = { R.drawable.timetable, R.drawable.subjects, R.drawable.teachers, R.drawable.students, R.drawable.teachers, R.drawable.students};

                myAdapter adapter = new myAdapter(this, titres, images);

                menu.setAdapter(adapter);
                menu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            startActivity(new Intent(getApplicationContext(), ManageTimetable.class));
                        }else if(position == 1){
                            startActivity(new Intent(getApplicationContext(), ManageSubjects.class));
                        }else if(position == 2){
                            Intent intent = new Intent(getApplicationContext(), ManageTeachers.class);
                            startActivity(intent);
                        }else if(position == 3){
                            Intent intent = new Intent(getApplicationContext(), ManageStudents.class);
                            startActivity(intent);
                        }else if(position == 4){
                            Intent intent = new Intent(getApplicationContext(), ManageStudentsS.class);
                            startActivity(intent);
                        }else if(position == 5){
                            Intent intent = new Intent(getApplicationContext(), ManageTeachersS.class);
                            startActivity(intent);
                        }
                    }
                });
            }else{
                firebaseFirestore.collection("users").document(userId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        enabled = documentSnapshot.getString("enabled");

                        if(enabled.equals("false")){

                            menu.setVisibility(View.INVISIBLE);

                            non_valide.setVisibility(View.VISIBLE);

                            logout.setVisibility(VISIBLE);
                            logout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            });

                        }else {
                            profile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getApplicationContext(), Profile.class));
                                }
                            });

                            message.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getApplicationContext(), UsersFragment.class));
                                }
                            });

                            notification.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getApplicationContext(), Show_notifications.class));
                                    finish();
                                    //Notifications.AddNotif(userId,"nomsnder","notificationffdText","etudiants");
                                }
                            });

                            if(user.getDisplayName().equals("Etudiant")){

                                String titres[] = {"Consulter un emploi du temps","Consulter les matières"};
                                int images[] = { R.drawable.timetable, R.drawable.subjects};

                                myAdapter adapter = new myAdapter(getApplicationContext(), titres, images);

                                menu.setAdapter(adapter);
                                menu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(position == 0){
                                            startActivity(new Intent(getApplicationContext(), DownloadTimetable.class));
                                        }else if(position == 1){
                                            startActivity(new Intent(getApplicationContext(), ListMatiere.class));
                                        }
                                    }
                                });
                            }
                            else if(user.getDisplayName().equals("Professeur")){
                                String titres[] = {"Consulter l'emploi du temps", "Consulter mes matières"};
                                int images[] = { R.drawable.timetable, R.drawable.subjects};

                                myAdapter adapter = new myAdapter(getApplicationContext(), titres, images);

                                menu.setAdapter(adapter);
                                menu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(position == 0){
                                            startActivity(new Intent(getApplicationContext(), DownloadTimetable.class));
                                        }else if(position == 1){
                                            startActivity(new Intent(getApplicationContext(), ListMatiere.class));
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }

        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }


    class myAdapter extends ArrayAdapter<String>{
        Context context;
        String titles[];
        int icons[];

        myAdapter(Context context, String titles[], int icons[]){
            super(context, R.layout.itemmenu, titles);
            this.context=context;
            this.titles=titles;
            this.icons=icons;
        }

        public View getView (int position,@Nullable View converView, @Nullable ViewGroup parent){
            LayoutInflater layoutInflater= (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.itemmenu, parent, false);

            ImageView icon = row.findViewById(R.id.iconMenu);
            TextView title = row.findViewById(R.id.titleMenu);

            icon.setImageResource(this.icons[position]);
            title.setText(this.titles[position]);
            return row;
        }
    }

}


