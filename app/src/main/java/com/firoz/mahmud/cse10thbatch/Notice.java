package com.firoz.mahmud.cse10thbatch;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Notice extends Fragment {

    //declearing interface
    ListView lv;







    //declearing baseadapter
    BaseAdapter ba;








    //declearing firebase
    ArrayList<UploadItem>list;
    DatabaseReference dr;






    //declearing variables
    ArrayList<Integer>selected;







    //floating action button
    FloatingActionButton download,delete,upload;




    public Notice() {

    }



    public Notice(FloatingActionButton download,FloatingActionButton delete,FloatingActionButton upload){
        this.download=download;
        this.upload=upload;
        this.delete=delete;
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notice, container, false);




        //assining interface
        lv=view.findViewById(R.id.notice_listview);






        //assining firebase
        dr= FirebaseDatabase.getInstance().getReference(KeyF.Notice.toString());
        list=new ArrayList<>();






        //assining variable
        selected=new ArrayList<>();






        //assining baseadapter
        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
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
                    cv=inflater.inflate(R.layout.notice_custom_list,null);
                }
                TextView title=cv.findViewById(R.id.title_textView),
                        details=cv.findViewById(R.id.details_textView)
                        ,time=cv.findViewById(R.id.date_textView);



                title.setText(list.get(position).getTitle());
                details.setText(list.get(position).getDetails());
                time.setText(list.get(position).getTime());



                return cv;
            }
        };









        //seting adapter in listview
        lv.setAdapter(ba);






        //setting onitem click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selected.contains(position)){
                    selected.remove(selected.indexOf(position));
                    view.setBackgroundColor(Color.TRANSPARENT);
                }else {
                    selected.add(position);
                    view.setBackgroundColor(Color.BLUE);
                }








                //showing and hidding fab
                if (selected.size()>0) {
                    upload.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                    download.setVisibility(View.GONE);
                }else {
                    upload.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    download.setVisibility(View.GONE);
                }
            }
        });









        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.removeAll(list);
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    list.add(ds.getValue(UploadItem.class));
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
