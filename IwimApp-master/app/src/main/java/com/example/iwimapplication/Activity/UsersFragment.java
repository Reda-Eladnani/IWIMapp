package com.example.iwimapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iwimapplication.Modal.User;
import com.example.iwimapplication.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersFragment extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UsersFragment";

    ImageView back;
    ListView list;
    String statut;
    static ArrayList<User> myList;
    CircleImageView image;
    FirebaseFirestore firestore;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_users);

        back = findViewById(R.id.ab_back);

        back.setOnClickListener(this);

        list = findViewById(R.id.usersList);
        list.setDivider(null);
        list.setDividerHeight(20);

        statut = "Etudiant";

        myList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").orderBy("nom").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d("Snapshot","Error: "+e.getMessage());
                }
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        User user = new User();
                        String enabled = doc.getDocument().getString("enabled");
                        user.setUid(doc.getDocument().getId());
                        user.setNom(doc.getDocument().getString("nom"));
                        user.setPrenom(doc.getDocument().getString("prenom"));
                        user.setEmail(doc.getDocument().getString("email"));
                        user.setStatut(doc.getDocument().getString("statut"));
                        myList.add(user);
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

    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<User> usersList = null;

        myAdapter(Context context, ArrayList<User> list){
            super(context, R.layout.useritem);
            this.context=context;
            this.usersList=list;

            if(this.usersList.size()==0){
                Toast.makeText(getApplicationContext(),"Pas d'utilisateurs'",
                        Toast.LENGTH_SHORT).show();
            }
        }

        public int getCount() {
            return usersList.size();
        }


        public View getView (int position,@Nullable View converView, @Nullable ViewGroup parent){
            LayoutInflater layoutInflater= (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View row = layoutInflater.inflate(R.layout.userschat, parent, false);

            ImageView photo = row.findViewById(R.id.userPicture);
            final TextView nom = row.findViewById(R.id.userName);
            TextView email = row.findViewById(R.id.userEmail);
            TextView status = row.findViewById(R.id.statut);
            ImageView messagehim = row.findViewById(R.id.messagehim);

            User user = this.usersList.get(position);

            final String uid = user.getUid();

            photo.setImageResource(R.drawable.defavatar);

            nom.setText(user.getNom()+" "+user.getPrenom());
            email.setText(user.getEmail());
            status.setText(user.getStatut());

            messagehim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firestore.collection("users").document(uid);
                    String value = uid;
                    Intent i = new Intent(getApplicationContext(), MessageActivity.class);
                    i.putExtra("receiverId",value);
                    startActivity(i);
                }
            });
            return row;
        }
    }
}