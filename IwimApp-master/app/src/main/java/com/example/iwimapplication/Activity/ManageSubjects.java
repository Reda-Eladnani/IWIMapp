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

import com.example.iwimapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ManageSubjects extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ManageSubjects";

    ImageView back;

    ListView menu;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subjects);

        back = findViewById(R.id.ab_back);

        back.setOnClickListener(this);

        menu = findViewById(R.id.subjectMenu);
        menu.setDivider(null);
        menu.setDividerHeight(20);


        String titres[] = {"Ajouter une matière", "Afficher les matières"};
        int images[] = {R.drawable.addsubject, R.drawable.subjects};

        myAdapter adapter = new myAdapter(this, titres, images);

        menu.setAdapter(adapter);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getApplicationContext(), AddMatiere.class ));
                } else if (position == 1) {
                     startActivity(new Intent(getApplicationContext(),ListMatiere.class));
                }
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


