package com.firoz.mahmud.cse10thbatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.security.spec.ECField;

public class UploadFile extends AppCompatActivity {
    //declearing interface
    ImageView iv;
    ProgressDialog pd;
    EditText name;







    //declearing variable
    Uri urllink=null;
    String url="";
    public static boolean appchooser=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_backg));
        setContentView(R.layout.activity_upload_file);
        //assining interface
        iv=findViewById(R.id.upload_file_imageView);
        pd=new ProgressDialog(this);
        name=findViewById(R.id.file_name_editText);







        //setting up prograced
        pd.setCancelable(false);
        pd.setMessage("Uploading......");






        //choosing file
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Choose galary if it is photo"),4009);
            }
        });







        findViewById(R.id.upload_file_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urllink==null){
                    Toast.makeText(UploadFile.this, "You have to choose a file", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.getText().toString().isEmpty()){
                    name.setError("File name requered");
                    return;
                }






                //showing prograce
                pd.show();



                if (appchooser) {
                    url = name.getText().toString();
                }else{
                    url=name.getText().toString()+".jpg";
                }






                //uploading
                StorageReference sr= FirebaseStorage.getInstance().getReference(KeyF.AllFile.toString());
                final String filtered=url.replaceAll("\\.","");
                sr.child(url).putFile(urllink).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference dr= FirebaseDatabase.getInstance().getReference(KeyF.AllFile.toString());
                                dr.child(filtered)
                                        .setValue(new FileItem(uri.toString(),url,filtered))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            pd.dismiss();
                                            finish();
                                        }else{
                                            pd.dismiss();
                                            Toast.makeText(UploadFile.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadFile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }










    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==4009&&resultCode==Activity.RESULT_OK){
            urllink = data.getData();






            if (appchooser) {
                //comming from this app
                appchooser=false;
                File file=new File(data.getData().toString());
                name.setText(file.getName());
                if (file.getName().endsWith(".jpg") ||file.getName().endsWith(".png")) {
                    Picasso.with(this).load(urllink).into(iv);
                } else {
                    iv.setImageResource(R.drawable.file);
                }





            }else{
                //coming from gralary





                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    iv.setImageBitmap(bitmap);
                    appchooser=false;
                }catch (Exception e){

                }
            }
        }
    }
}
