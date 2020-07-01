package com.example.iwimapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.iwimapplication.R;
import com.example.iwimapplication.Modal.User;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db ;

    EditText email, password;
    TextView compte;
    Button signin;
    ProgressBar prog;

    Intent intent;

    User usr;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        compte=findViewById(R.id.newAccount);


        signin=(Button)findViewById(R.id.InBtt);

        prog = findViewById(R.id.prog_bar);


        usr=new User();


        signin.setOnClickListener(this);
        compte.setOnClickListener(this);

    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.newAccount){
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.InBtt){
            signIn(email.getText().toString(),password.getText().toString());
        }
    }

    void signIn(String mail, String pass) {

        if(TextUtils.isEmpty(mail)){
            email.setError("Saisir l'email !");
        }else if (!isEmailValid(mail)) {
            email.setError("L'email est invalide");
        } else if(TextUtils.isEmpty(pass)){
            password.setError("Saisir le mot de passe !");
        } else if(password.length() <6 ){
            password.setError("Le mot de passe doit contient au moins 6 caracteres");
        } else {
            prog.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Bienvenue",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),Home.class));
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Ce compte n'existe pas !",
                                        Toast.LENGTH_SHORT).show();
                                prog.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }
}