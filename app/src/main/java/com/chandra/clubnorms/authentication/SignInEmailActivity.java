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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chandra.clubnorms.MainActivity;
import com.chandra.clubnorms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SignInEmailActivity extends AppCompatActivity {

    EditText emailEdit,paswordEdit;
    Button signInButton;
    TextView moveToSignUpPage;
    SharedPreferences token;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_email);

        emailEdit = findViewById(R.id.email);
        paswordEdit = findViewById(R.id.password);
        moveToSignUpPage = findViewById(R.id.moveToSignUp);
        signInButton = findViewById(R.id.signInButton);

        moveToSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInEmailActivity.this, SignUpWithEmailActivity.class));
                finish();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdit.getText().toString();
                String password = paswordEdit.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    return;
                }

                signInAccount(getApplicationContext(),email,password);
            }
        });
    }

    private void signInAccount(Context context,String email, String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                                token = getSharedPreferences("clubnorms_token",MODE_PRIVATE);
                                SharedPreferences.Editor editor = token.edit();
                                editor.putString("id",user.getUid());
                                editor.apply();
                                startActivity(new Intent(SignInEmailActivity.this,MainActivity.class));
                                finish();
                            }

                        }else {
                            Toast.makeText(context, "Failed to login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}