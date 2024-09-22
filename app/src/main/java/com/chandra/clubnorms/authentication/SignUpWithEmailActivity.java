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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SignUpWithEmailActivity extends AppCompatActivity {

    EditText emailEdit,usernameEdit,passwordEdit,rePasswordEdit;
    Button createAccountButton;
    TextView moveToSignInPage;
    String api = "http://192.168.0.105:3000/user";
    SharedPreferences token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_with_email);

        emailEdit = findViewById(R.id.email);
        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.password);
        rePasswordEdit = findViewById(R.id.rePassword);

        createAccountButton = findViewById(R.id.createAccount);
        moveToSignInPage = findViewById(R.id.moveToSignInPage);

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
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String rePassword = rePasswordEdit.getText().toString();

                if (!email.isEmpty()) {
                    if (!username.isEmpty()) {
                        if (!password.isEmpty()) {
                            if (!rePassword.isEmpty()) {
                                if (password.equals(rePassword)) {
                                    createAccount(getApplicationContext(), email, username, password);
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
                        Toast.makeText(getApplicationContext(), "Please enter your username", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createAccount(Context context,String email,String username,String password){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest createAccountRequest = new JsonObjectRequest(Request.Method.POST, api, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    token = getSharedPreferences("clubnorms_token",MODE_PRIVATE);
                    SharedPreferences.Editor editor = token.edit();
                    editor.putBoolean("isLoggedIn",true);
                    editor.commit();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpWithEmailActivity.this, MainActivity.class));
                    finish();
                }catch (JSONException e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "Unkown Error", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("emailId",email);
                    body.put("userName",username);
                    body.put("password",password);
                    return  body.toString().getBytes(StandardCharsets.UTF_8);
                }catch (Exception e){
                    Toast.makeText(SignUpWithEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        };
        requestQueue.add(createAccountRequest);
    }
}