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

public class SignInEmailActivity extends AppCompatActivity {

    EditText usernameEdit,paswordEdit;
    Button signInButton;
    TextView moveToSignUpPage;
    String api = "http://192.168.0.105:3000/user/login";
    SharedPreferences token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_email);

        usernameEdit = findViewById(R.id.username);
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
                String username = usernameEdit.getText().toString();
                String password = paswordEdit.getText().toString();
                if(username.isEmpty() || password.isEmpty()){
                    return;
                }

                signInAccount(getApplicationContext(),username,password);
            }
        });
    }

    private void signInAccount(Context context,String username, String password){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest signInAccountRequest = new JsonObjectRequest(Request.Method.POST, api, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject userObject = response.getJSONObject("user");
                    String msg = userObject.getString("userName");
                    token = getSharedPreferences("clubnorms_token",MODE_PRIVATE);
                    SharedPreferences.Editor editor = token.edit();
                    editor.putBoolean("isLoggedIn",true);
                    editor.commit();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInEmailActivity.this, MainActivity.class));
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
                    body.put("emailId","");
                    body.put("password",password);
                    body.put("userName",username);
                    return  body.toString().getBytes(StandardCharsets.UTF_8);
                }catch (Exception e){
                    Toast.makeText(SignInEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        };
        requestQueue.add(signInAccountRequest);
    }
}