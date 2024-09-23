package com.chandra.clubnorms.userProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chandra.clubnorms.MainActivity;
import com.chandra.clubnorms.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserDetailsActivity extends AppCompatActivity {

    private ImageView profileImage;
    private Button letsDiveIn;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText fullname,bio;
    SharedPreferences token;
    EditText Interest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profileImage = findViewById(R.id.imageForProfile);
        letsDiveIn = findViewById(R.id.diveIn);
        fullname = findViewById(R.id.fullName);
        bio = findViewById(R.id.bio);
        Interest = findViewById(R.id.interest);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        letsDiveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String infoBio = bio.getText().toString();
                String fName = fullname.getText().toString();
                String interest = Interest.getText().toString();

                if(!infoBio.isEmpty() && !fName.isEmpty()){
                    updateDataForProfile(getApplicationContext(),fName,infoBio,interest);
                }else{
                    Toast.makeText(UserDetailsActivity.this, "Please fill all values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(),
                                filePath);
                profileImage.setImageBitmap(bitmap);
                UploadImage();
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void UploadImage() {
        if (filePath != null) {
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref
                    = storageReference
                    .child(
                            "profileimage/"
                                    + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UserDetailsActivity.this,"Profile Picture updated", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Download URL available here
                                    String imageUrl = downloadUri.toString();
                                    Toast.makeText(UserDetailsActivity.this, "Image uploaded: " + imageUrl, Toast.LENGTH_SHORT).show();

                                    // Save the image URL to Firestore
                                    saveImageToUrl(getApplicationContext(),imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(UserDetailsActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }


    private void updateDataForProfile(Context context,String fullname,String bio,String interest){
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();

        if(!user.isEmpty()){
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference =  db.collection("usersCollection").document(user);
            Map<String, Object> updateInfo = new HashMap<>();
            updateInfo.put("bio",bio);
            updateInfo.put("fullname",fullname);
            updateInfo.put("interest",interest);

            documentReference.update(updateInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Profile Created Successfully", Toast.LENGTH_SHORT).show();
                    token = getSharedPreferences("clubnorms_token",MODE_PRIVATE);
                    SharedPreferences.Editor editor = token.edit();
                    editor.putString("id",user);
                    editor.apply();
                    startActivity(new Intent(UserDetailsActivity.this,MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed to create profile", Toast.LENGTH_SHORT).show();
                }
            });
            List<String> items = Arrays.asList(interest.split("\\s*,\\s*"));
            for (String s : items) {
               DocumentReference interestDocRef =  db.collection("interest").document(s);
               interestDocRef.update("users", FieldValue.arrayUnion(user))
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Log.d("Firestore", "User added to interest: " + s);
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Map<String, Object> newInterest = new HashMap<>();
                               newInterest.put("users", Arrays.asList(user));

                               // Set the document with the initial 'users' array
                               interestDocRef.set(newInterest)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               Log.d("Firestore", "New interest created and user added: " + s);
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Log.w("Firestore", "Error adding user to interest", e);
                                           }
                                       });
                           }
                       });
            }
        }
    }

    private void saveImageToUrl(Context context,String profileImage){
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();

        if(!user.isEmpty()){
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference =  db.collection("usersCollection").document(user);
            Map<String, Object> updateInfo = new HashMap<>();
            updateInfo.put("profilePicture",profileImage);


            documentReference.update(updateInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Profile Created Successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed to create profile", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}