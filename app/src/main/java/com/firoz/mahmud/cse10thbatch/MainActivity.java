package com.firoz.mahmud.cse10thbatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
//declearing firebase
    FirebaseUser user;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.gouppic));
        setContentView(R.layout.activity_main);



        //assining variable
        user=FirebaseAuth.getInstance().getCurrentUser();







        //thread
        new Thread(){
            @Override
            public void run(){


                //sleeping
                try {
                    sleep(2000);
                }catch (Exception e){

                }







                //checking
                if (user!=null){
                    user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(MainActivity.this,MainShower.class));
                            }else{
                                startActivity(new Intent(MainActivity.this,Login.class));
                            }
                        }
                    });
                    startActivity(new Intent(MainActivity.this,MainShower.class));
                }else{
                    startActivity(new Intent(MainActivity.this, Login.class));
                }



            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
