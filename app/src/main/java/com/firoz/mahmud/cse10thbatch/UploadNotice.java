package com.firoz.mahmud.cse10thbatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class UploadNotice extends AppCompatActivity {
    //declearing interface
    EditText title,details;
    ProgressDialog pd;








    //declearing firebase
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_backg));
        setContentView(R.layout.activity_upload_notice);

        //assining interface
        title=findViewById(R.id.upload_notice_title_editText);
        details=findViewById(R.id.upload_notice_details_editText);
        pd=new ProgressDialog(this);





        //prograse setup
        pd.setMessage("Uploading.....");
        pd.setCancelable(false);







        //assining variables
        dr= FirebaseDatabase.getInstance().getReference(KeyF.Notice.toString());








        //setting onclicklistener
        findViewById(R.id.upload_notice_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking validity
                if (title.getText().toString().isEmpty()){
                    title.setError("You have to a title.");
                    return;
                }
                if (details.getText().toString().isEmpty()){
                    details.setError("You have forgotten about details");
                    return;
                }

                pd.show();





                //uploading...
                String key=dr.push().getKey();
                dr.child(key).setValue(new UploadItem(
                        title.getText().toString(),
                        details.getText().toString()
                        , DateFormat.format("dd/MM/yyyy",new Date()).toString()
                        ,key)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            finish();
                        }else{
                            pd.dismiss();
                            Toast.makeText(UploadNotice.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
