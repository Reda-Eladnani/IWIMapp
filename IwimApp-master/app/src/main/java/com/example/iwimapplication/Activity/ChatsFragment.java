package com.example.iwimapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iwimapplication.Modal.Chat;
import com.example.iwimapplication.Modal.User;
import com.example.iwimapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsFragment extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UsersFragment";

    ImageView back;
    ListView list;
    String statut;
    static ArrayList<User> myList;

    FirebaseFirestore firestore;
    DatabaseReference reference;
    private List<String> utilisateurs;
    FirebaseUser fuser;
    private List<User> mUsers;
    FirebaseAuth auth;
    String userId;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chats);

        back = findViewById(R.id.ab_back);

        back.setOnClickListener(this);

        list = findViewById(R.id.usersList);
        list.setDivider(null);
        list.setDividerHeight(20);

        statut = "Etudiant";

        myList = new ArrayList<>();
        utilisateurs = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                utilisateurs.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(userId)){
                        utilisateurs.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(userId)){
                        utilisateurs.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void readChats() {
        mUsers = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mUsers.clear();
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    User user = Objects.requireNonNull(documentSnapshot.toObject(User.class));
                    for (String id : utilisateurs) {
                        if (user.getUid()==id) {
                            if (mUsers.size() != 0) {
                                for (User user1 : mUsers) {
                                    if (!user.getUid().equals(user1.getUid())) {
                                        mUsers.add(user);
                                    }
                                }
                            } else {
                                mUsers.add(user);
                            }
                        }
                    }
                }
                myAdapter adapter = new myAdapter(getApplicationContext(), (ArrayList<User>) mUsers);
                list.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ab_back){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<User> usersList;

        myAdapter(Context context, ArrayList<User> list){
            super(context, R.layout.useritem);
            this.context=context;
            this.usersList=list;
//
            if(this.usersList.size()==0){
                Toast.makeText(getApplicationContext(),"Pas de conversations",
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
            ImageView messagehim = row.findViewById(R.id.messagehim);

            User user = this.usersList.get(position);

            final String uid = user.getUid();

            photo.setImageResource(R.drawable.defavatar);

            nom.setText(user.getNom()+" "+user.getPrenom());
            email.setText(user.getEmail());

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
