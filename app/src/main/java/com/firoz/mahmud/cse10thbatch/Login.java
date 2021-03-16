package com.firoz.mahmud.cse10thbatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
//decleare interface
    EditText email,password;
    ProgressDialog pd;





    //Firebase declearation
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_backg));
        setContentView(R.layout.activity_login);


        //Firebase assining
        auth=FirebaseAuth.getInstance();









        //assining interface
        email=findViewById(R.id.login_email_editText);
        password=findViewById(R.id.login_password_editText);









        //assining prograss dialog
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait.....");







        //setting onclikclistener
        //login button
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking validity
                if (email.getText().toString().isEmpty()){
                    email.setError("Submit your email.");
                    return;
                }
                if (password.getText().toString().isEmpty()){
                    password.setError("Submit your password.");
                    return;
                }



                //showing dialog
                pd.show();








                //login
                auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            startActivity(new Intent(Login.this,MainShower.class));
                            overridePendingTransition(R.anim.in,R.anim.out);
                            finish();
                        }else{
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });








        //signup button
        findViewById(R.id.login_sign_up_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignUp.class));
                overridePendingTransition(R.anim.in,R.anim.out);
            }
        });








    }
}
