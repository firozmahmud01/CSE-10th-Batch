package com.firoz.mahmud.cse10thbatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.security.Key;
import java.util.ArrayList;


public class FilesList extends Fragment {

    //declearing interface
    GridView gv;
    FloatingActionButton delete,download,upload;
    Context context;







    //declearing baseadapter
    BaseAdapter ba;








    //declearing firebase
    DatabaseReference dr;
    ArrayList<FileItem>list;







    //declearing variable
    ArrayList<Integer>selected;

    public FilesList(){

    }

    public FilesList(FloatingActionButton delete,FloatingActionButton download,FloatingActionButton upload,Context context) {
        this.delete=delete;
        this.context=context;
        this.upload=upload;
        this.download=download;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_files_list, container, false);






        //assining variables
        selected=new ArrayList<>();







        //assining firebase
        dr= FirebaseDatabase.getInstance().getReference(KeyF.AllFile.toString());





        //
        list=new ArrayList<FileItem>();







        //assining interface
        gv=view.findViewById(R.id.files_list_files_grid);







        //assining baseadapter
        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size()+1;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }


            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View cv, ViewGroup parent) {
                if (cv==null){
                    cv=inflater.inflate(R.layout.custom_folder,null);
                }



                //declearing and assining custom interface
                ImageView iv=cv.findViewById(R.id.custom_file_imageView);
                TextView tv=cv.findViewById(R.id.custom_file_textView);







                if (position!=list.size()){
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(list.get(position).getFilename());
                    iv.setImageResource(R.drawable.file);
                }else{
                    iv.setImageResource(R.drawable.addfolder);
                    tv.setVisibility(View.GONE);
                }

                return cv;
            }
        };









        //setting baseadapter in listview
        gv.setAdapter(ba);








        //adding onitemclicklistener
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==list.size()){
                    context.startActivity(new Intent(context,UploadFile.class));
                    return;
                }
                if (selected.contains(position)){
                    selected.remove(selected.indexOf(position));
                    view.setBackgroundColor(Color.TRANSPARENT);
                }else {
                    selected.add(position);
                    view.setBackgroundColor(Color.BLUE);
                }








                //showing and hidding fab
                if (selected.size()>0) {
                    delete.setVisibility(View.VISIBLE);
                    download.setVisibility(View.VISIBLE);
                }else {
                    delete.setVisibility(View.GONE);
                    download.setVisibility(View.GONE);
                }
            }
        });









        //getting all files
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.removeAll(list);
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    list.add(ds.getValue(FileItem.class));
                }
                ba.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }
}
