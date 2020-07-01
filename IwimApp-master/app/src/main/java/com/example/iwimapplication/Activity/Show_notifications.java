package com.example.iwimapplication.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iwimapplication.Modal.Notification;
import com.example.iwimapplication.Modal.Notifications;
import com.example.iwimapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

import static android.view.View.GONE;

public class Show_notifications extends AppCompatActivity implements View.OnClickListener{
    ImageView back;
    ListView list;
    TextView topbar;
    static ArrayList<Notification> myList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notifications);
        topbar=findViewById(R.id.titre_top);topbar.setText("Notifications");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        back = findViewById(R.id.abbb_back);
        back.setOnClickListener(this);
        list = findViewById(R.id.Notification_list);
        list.setDivider(null);
        list.setDividerHeight(20);
        myList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        Log.d("ID",userId);
        String path="users/"+userId+"/Notifications";
        firestore.collection(path).orderBy("DateTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Snapshot", "Error: " + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Notification notification = new Notification();

                        notification.setId_sender(doc.getDocument().getString("ID_sender"));
                        notification.setIs_read(doc.getDocument().getBoolean("Is_read"));
                        notification.setText(doc.getDocument().getString("Text"));
                        notification.setTime(doc.getDocument().getTimestamp("DateTime"));
                        notification.setNotificationID(doc.getDocument().getId());

                        myList.add(notification);

                    }
                }

                Show_notifications.myAdapter adapter = new Show_notifications.myAdapter(getApplicationContext(), myList);
                list.setAdapter(adapter);
            }
        });
    }
    public void onClick(View view) {
        if(view.getId()==R.id.abbb_back){
            startActivity(new Intent(Show_notifications.this, Home.class));
            finish();
        }
    }
    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<Notification> Notification_list = null;

        myAdapter(Context context, ArrayList<Notification> list) {
            super(context, R.layout.notificationitem);
            this.context = context;
            this.Notification_list = list;

            if (this.Notification_list.size() == 0) {
                Toast.makeText(getApplicationContext(), "Pas de Notifications",
                        Toast.LENGTH_SHORT).show();
            }
        }


        public int getCount() {
            return Notification_list.size();
        }


        public View getView (int position,@Nullable View converView, @Nullable ViewGroup parent){
            LayoutInflater layoutInflater= (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View row = layoutInflater.inflate(R.layout.notificationitem, parent, false);
            final LinearLayout lyt=row.findViewById(R.id.notif_item_inner);

            final TextView Text = row.findViewById(R.id.notification_text);
            TextView DateTime = row.findViewById(R.id.notification_date);

            Notification notification = this.Notification_list.get(position);

            if(!notification.isIs_read()){
                lyt.setBackgroundColor(Color.parseColor("#E4E4E4"));
                Notifications.seen(notification.getNotificationID(),userId);
            }
            Text.setText(notification.getText() );
            DateTime.setText(notification.getTime().toDate().toString());

            return row;

        }
    }
}
