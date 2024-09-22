package com.chandra.clubnorms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chandra.clubnorms.authentication.SignInEmailActivity;
import com.chandra.clubnorms.authentication.SignUpWithEmailActivity;

public class StartActivity extends AppCompatActivity {

    Button signInWithEmail,signInWithGoogle;
    SharedPreferences token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        signInWithEmail = findViewById(R.id.emailSignInButton);
        signInWithGoogle = findViewById(R.id.googleSignInButton);

        token = getSharedPreferences("clubnorms_token",MODE_PRIVATE);
        Boolean isLoggedIn = token.getBoolean("isLoggedIn",false);
        if(isLoggedIn){
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }

        signInWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, SignInEmailActivity.class));
            }
        });

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}