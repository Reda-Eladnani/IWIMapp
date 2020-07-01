package com.example.iwimapplication.Activity;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.iwimapplication.Modal.Notification;
import com.example.iwimapplication.Modal.Notifications;
import com.example.iwimapplication.Modal.User;
import com.example.iwimapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ManageStudentsS extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ManageStudentsS";

    FirebaseAuth auth;
    FirebaseUser user;
    String userId;
    ImageView back;
    ListView list;
    String statut;
    static ArrayList<User> myList;

    FirebaseFirestore firestore;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_manage_students2);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        back = findViewById(R.id.ab_back);

        back.setOnClickListener(this);

        list = findViewById(R.id.usersList);
        list.setDivider(null);
        list.setDividerHeight(20);

        statut = "Etudiant";

        myList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").whereIn("statut", Collections.singletonList(statut)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d("Snapshot","Error: "+e.getMessage());
                }
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        User user = new User();

                        String enabled = doc.getDocument().getString("enabled");

                        if(enabled.equals("true")){
                            user.setUid(doc.getDocument().getId());
                            user.setNom(doc.getDocument().getString("nom"));
                            user.setPrenom(doc.getDocument().getString("prenom"));
                            user.setEmail(doc.getDocument().getString("email"));

                            myList.add(user);
                        }
                    }
                }

                myAdapter adapter = new myAdapter(getApplicationContext(), myList);
                list.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ab_back){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
    }

    class myAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<User> usersList = null;

        myAdapter(Context context, ArrayList<User> list){
            super(context, R.layout.useritem2);
            this.context=context;
            this.usersList=list;

            if(this.usersList.size()==0){
                Toast.makeText(getApplicationContext(),"Pas d'etudiants'",
                        Toast.LENGTH_SHORT).show();
            }
        }

        public int getCount() {
            return usersList.size();
        }


        public View getView (int position,@Nullable View converView, @Nullable ViewGroup parent){
            LayoutInflater layoutInflater= (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View row = layoutInflater.inflate(R.layout.useritem2, parent, false);

            ImageView photo = row.findViewById(R.id.userPicture);
            final TextView nom = row.findViewById(R.id.userName);
            TextView email = row.findViewById(R.id.userEmail);

            ImageView delete = row.findViewById(R.id.userDelete);

            User user = this.usersList.get(position);

            final String uid = user.getUid();

            photo.setImageResource(R.drawable.defavatar);

            nom.setText(user.getNom()+" "+user.getPrenom());
            email.setText(user.getEmail());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    firestore.collection("users").document(uid).delete();
                    row.setVisibility(GONE);
                    finish();
                    startActivity(new Intent(getApplicationContext(), ManageStudentsS.class));
                    Toast.makeText(getApplicationContext(),"Le compte de "+nom.getText()+" est supprimé",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return row;
        }
    }

}


