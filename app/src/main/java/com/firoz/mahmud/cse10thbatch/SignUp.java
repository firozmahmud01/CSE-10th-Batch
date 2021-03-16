package com.firoz.mahmud.cse10thbatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUp extends AppCompatActivity {
    //declearing interface
    EditText username,password,cpassword,email,studentid;





    //declearing firebase
    FirebaseAuth auth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_backg));
        setContentView(R.layout.activity_sign_up);



        //assining interface
        username=findViewById(R.id.signup_fullname_edittext);
        password=findViewById(R.id.signup_password_editText);
        cpassword=findViewById(R.id.signup_confirm_password_editText);
        email=findViewById(R.id.signup_email_editText);
        studentid=findViewById(R.id.signup_student_id_editText);







        //assining firebase
        auth=FirebaseAuth.getInstance();





        //setting onclicklistener
        findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking validity
                if (username.getText().toString().isEmpty()){
                    username.setError("Fill it.");
                    return;
                }
                if (password.getText().toString().isEmpty()){
                    password.setError("Fill it.");
                    return;
                }
                if (cpassword.getText().toString().isEmpty()){
                    cpassword.setError("Fill it.");
                    return;
                }
                if (email.getText().toString().isEmpty()){
                    email.setError("Fill it.");
                    return;
                }
                if (studentid.getText().toString().isEmpty()){
                    studentid.setError("Fill it.");
                    return;
                }
                if (!password.getText().toString().equals(cpassword.getText().toString())){
                    cpassword.setError("Both password should be same.");
                    return;
                }









                //creating user
                auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        doEverythingYouNeed(authResult);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }










    private void doEverythingYouNeed(final AuthResult authResult){
        //adding name
        UserProfileChangeRequest upcr=new UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString()).build();
        final FirebaseUser user=authResult.getUser();
        user.updateProfile(upcr);




        //uploading user details in database
        UserCreadit uc=new UserCreadit(
                username.getText().toString()
                ,email.getText().toString()
                ,password.getText().toString(),studentid.getText().toString());

        DatabaseReference sr= FirebaseDatabase.getInstance().getReference(KeyF.UserFile.toString());
        sr.push().setValue(uc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull final Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                    finish();
                }else {
                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            auth.signOut();
                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
