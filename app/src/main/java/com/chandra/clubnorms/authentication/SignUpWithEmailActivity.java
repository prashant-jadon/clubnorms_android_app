package com.chandra.clubnorms.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chandra.clubnorms.MainActivity;
import com.chandra.clubnorms.R;
import com.chandra.clubnorms.StartActivity;
import com.chandra.clubnorms.userProfile.UserDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.appwrite.Client;


public class SignUpWithEmailActivity extends AppCompatActivity {

    EditText emailEdit,passwordEdit,rePasswordEdit;
    Button createAccountButton;
    TextView moveToSignInPage;


    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_with_email);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        rePasswordEdit = findViewById(R.id.rePassword);

        createAccountButton = findViewById(R.id.createAccount);
        moveToSignInPage = findViewById(R.id.moveToSignInPage);
        db = FirebaseFirestore.getInstance();


        moveToSignInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpWithEmailActivity.this,SignInEmailActivity.class));

                finish();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String rePassword = rePasswordEdit.getText().toString();

                if (!email.isEmpty()) {
                        if (!password.isEmpty()) {
                            if (!rePassword.isEmpty()) {
                                if (password.equals(rePassword)) {
                                   createAccount(getApplicationContext(), email, password);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please re-enter your password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createAccount(Context context,String email,String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                                Map<String,Object> userBody = new HashMap<>();
                                userBody.put("fullname","");
                                userBody.put("bio","");
                                userBody.put("profilePicture","");
                                userBody.put("email",email);
                                userBody.put("userId",user.getUid());

                                db.collection("usersCollection")
                                        .document(user.getUid())
                                        .set(userBody)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Welcome to the clubnorms", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Error in creating account", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpWithEmailActivity.this, StartActivity.class));
                                                finish();
                                            }
                                        });
                                startActivity(new Intent(SignUpWithEmailActivity.this, UserDetailsActivity.class));
                                finish();
                            }
                        }else{
                            Toast.makeText(SignUpWithEmailActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}