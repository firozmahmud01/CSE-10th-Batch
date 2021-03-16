package com.firoz.mahmud.cse10thbatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class FileChooser extends AppCompatActivity {
    //declearing variable
    String root="/sdcard";
    File files[];
    String path="/sdcard";
    Context context;






    //declearing baseadapter
    BaseAdapter ba;






    //declearing interface
    GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        //assining interface
        gv=findViewById(R.id.filechooser_grid);






        //assining variables
        files=(new File(root)).listFiles();
        context=this;






        //assining baseadapter
        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return files==null?0:files.length;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
            LayoutInflater li=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @Override
            public View getView(int position, View cv, ViewGroup parent) {
                if(cv==null){
                    cv=li.inflate(R.layout.custom_folder,null);
                }
                ImageView iv=cv.findViewById(R.id.custom_file_imageView);
                TextView tv=cv.findViewById(R.id.custom_file_textView);





                if (files[position].isDirectory()){
                    iv.setImageResource(R.drawable.folder);
                }else{
//                    Picasso.with(context).load(files[position]).into(iv);
                    if (files[position].getName().endsWith(".png")||files[position].getName().endsWith(".jpg")){
                        Picasso.with(context).load(Uri.fromFile(files[position])).into(iv);
                    }else {
                        Picasso.with(context).load(R.drawable.file).into(iv);
                    }
                }
                tv.setText(files[position].getName());
                tv.setTextColor(Color.BLACK);
                return cv;
            }
        };








        //setting baseadapter in list
        gv.setAdapter(ba);








        //setting onitemclick listeber
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (files[position].isDirectory()){


                    path=files[position].getAbsolutePath();
                    files=files[position].listFiles();
                    ba.notifyDataSetChanged();



                }else{
                    Intent intent=new Intent();
                    intent.setData(Uri.fromFile(files[position]));
                    UploadFile.appchooser=true;
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });










        //runtime permission
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            ActivityCompat.requestPermissions(FileChooser.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1&& PackageManager.PERMISSION_GRANTED==grantResults[0]){
            files=(new File(root)).listFiles();
            ba.notifyDataSetChanged();
        }
    }








    @Override
    public void onBackPressed() {
        if (path.equals(root)){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }else {
            path=path.substring(0,path.lastIndexOf("/"));
            files=(new File(path)).listFiles();
            ba.notifyDataSetChanged();
        }
    }
}
