package com.example.iwimapplication.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iwimapplication.Modal.Matiere;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class ListMatiere extends AppCompatActivity implements View.OnClickListener{
    ImageView back;
    ListView list;
    FirebaseAuth auth;
    FirebaseUser user;
    RadioGroup Grp;
    static ArrayList<Matiere> myList;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_matiere);
        back = findViewById(R.id.abbb_back);

        back.setOnClickListener(this);
        list = findViewById(R.id.SubjectList);
        list.setDivider(null);
        list.setDividerHeight(20);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user.getDisplayName().equals("Professeur")){
            Grp=findViewById(R.id.radioBt);
            Grp.setVisibility(View.VISIBLE);
        }
        getallM();
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.mine:
                if (checked)
                    getM();
                    break;
            case R.id.all:
                if (checked)
                    getallM();
                    break;
        }
    }
    public void getallM(){
        myList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Matiere").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Snapshot", "Error: " + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Matiere matiere = new Matiere();

                        matiere.setCode(doc.getDocument().getId());

                        matiere.setLabel(doc.getDocument().getString("Label"));
                        matiere.setSemestre(doc.getDocument().getString("Semestre"));
                        matiere.setPeriode(doc.getDocument().getString("Periode"));
                        matiere.setProfesseur(doc.getDocument().getString("Professeur"));

                        myList.add(matiere);

                    }
                }

                ListMatiere.myAdapter adapter = new ListMatiere.myAdapter(getApplicationContext(), myList);
                list.setAdapter(adapter);
            }
        });
    }
    public void getM() {
        myList = new ArrayList<>();
        FirebaseFirestore fstore;
        String userId;
        fstore = FirebaseFirestore.getInstance();
        DocumentReference document = fstore.collection("users").document(user.getUid());
        document.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                firestore = FirebaseFirestore.getInstance();
                final String fullname=documentSnapshot.getString("nom")+" "+documentSnapshot.getString("prenom");
                Log.d("nomcomp",fullname);
                firestore.collection("Matiere").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("Snapshot", "Error: " + e.getMessage());
                        }
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                if(doc.getDocument().getString("Professeur").equals(fullname)) {
                                    Matiere matiere = new Matiere();

                                    matiere.setCode(doc.getDocument().getId());

                                    matiere.setLabel(doc.getDocument().getString("Label"));
                                    matiere.setSemestre(doc.getDocument().getString("Semestre"));
                                    matiere.setPeriode(doc.getDocument().getString("Periode"));
                                    matiere.setProfesseur(doc.getDocument().getString("Professeur"));

                                    myList.add(matiere);
                                }
                            }
                        }

                        ListMatiere.myAdapter adapter = new ListMatiere.myAdapter(getApplicationContext(), myList);
                        list.setAdapter(adapter);
                    }
                });
            }

        });
    }
    public void onClick(View v) {
        if(v.getId()==R.id.abbb_back){
            startActivity(new Intent(ListMatiere.this, Home.class));
            finish();
        }
    }

    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<Matiere> SubjectList = null;

        myAdapter(Context context, ArrayList<Matiere> list) {
            super(context, R.layout.useritem);
            this.context = context;
            this.SubjectList = list;

            if (this.SubjectList.size() == 0) {
                Toast.makeText(getApplicationContext(), "Pas de Matiere",
                        Toast.LENGTH_SHORT).show();
            }
        }


    public int getCount() {
        return SubjectList.size();
    }


    public View getView (int position,@Nullable View converView, @Nullable ViewGroup parent){
        LayoutInflater layoutInflater= (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = layoutInflater.inflate(R.layout.matiereitem, parent, false);

        final TextView Label = row.findViewById(R.id.label);
        TextView Semestre = row.findViewById(R.id.semestre);
        TextView Professeur = row.findViewById(R.id.teachername);
        ImageView delete = row.findViewById(R.id.userDelete);

        if(user.getDisplayName().equals("Etudiant") || user.getDisplayName().equals("Professeur")){
            delete.setVisibility(View.INVISIBLE);
        }

        Matiere matiere = this.SubjectList.get(position);

        final String code = matiere.getCode();

        Label.setText("Module " + matiere.getLabel()  );
        Semestre.setText(matiere.getSemestre());
        Professeur.setText("Professeur " + matiere.getProfesseur());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                firestore.collection("Matiere").document(code).delete();
                row.setVisibility(GONE);
                finish();
                startActivity(new Intent(getApplicationContext(), ListMatiere.class));
                Toast.makeText(getApplicationContext(),"Le module "+Label.getText()+" est supprimé",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return row;

    }
}

}


