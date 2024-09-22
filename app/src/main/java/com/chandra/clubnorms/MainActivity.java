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

public class MainActivity extends AppCompatActivity {

    Button logout;
    SharedPreferences token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
       logout = findViewById(R.id.logout);



       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               token = getSharedPreferences("clubnorms_token",MODE_PRIVATE);
               SharedPreferences.Editor editor = token.edit();
               editor.clear();
               editor.apply();
               startActivity(new Intent(MainActivity.this, StartActivity.class));
               finish();
           }
       });

    }
}