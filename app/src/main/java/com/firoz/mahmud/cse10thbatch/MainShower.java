package com.firoz.mahmud.cse10thbatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainShower extends AppCompatActivity {
    //declearing interface
    ViewPager vp;
    TabLayout tab;
    FloatingActionButton delete,download,upload;







    //fragment declearing
    FilesList fl;
    Notice nt;








    //declearing firebase
    FirebaseUser user;







    //declearing pageadapter
    FragmentPagerAdapter fpa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_backg));
        setContentView(R.layout.activity_main_shower);








        //assining firebase
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();






        //setting title
        String username=user.getDisplayName();
        setTitle(username==null?"":username);












        //assining interface
        vp=findViewById(R.id.main_shower_viewpager);
        tab=findViewById(R.id.main_shower_tabLayout);
        download=findViewById(R.id.downloadfloatingActionButton);
        delete=findViewById(R.id.delete_floatingActionButton);
        upload=findViewById(R.id.addfloatingbutton);






        //setting defauld
        download.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);






        //fragment assining
        fl=new FilesList(download,delete,upload,this);
        nt=new Notice(download,delete,upload);










        //setting up onclick listener
        //download listener
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fl.selected.size()>0){
                    filedownload();
                }
            }
        });
        //delete listener
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fl.selected.size()>0){
                    filedelete();
                }
                if (nt.selected.size()>0){
                    deletenotice();
                }

            }
        });
        //upload listener
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainShower.this,UploadNotice.class));
                overridePendingTransition(R.anim.in,R.anim.out);
            }
        });












        //assining adapter
        fpa=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position==0) {
                    return nt;
                }else {
                    return fl;
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position==0)
                    return "Notice";
                else
                    return "Files";
            }

            @Override
            public int getCount() {
                return 2;
            }
        };








        //setting adapter in pager
        vp.setAdapter(fpa);









        //adding viewpager
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fl.selected.removeAll(fl.selected);
                nt.selected.removeAll(nt.selected);
                if (position==0){
                    download.setVisibility(View.GONE);
                    upload.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                }else{
                    download.setVisibility(View.GONE);
                    upload.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });







        //conecting tablayout with pager
        tab.setupWithViewPager(vp);










        //starting service
        if (!NoticeSender.isRunning) {
            startService(new Intent(MainShower.this, NoticeSender.class));
        }
    }








    //setting up fille download
    private void filedownload(){
        for(int index:fl.selected) {
            String name = fl.list.get(index).getFilename();
            String uri = fl.list.get(index).getUrl();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
            request.setDescription("Wait for it");
            request.setTitle(name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);

            DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            manager.enqueue(request);


        }


    }







    //setting up file delete
    private void filedelete(){
        for(final int index:fl.selected){
            StorageReference sr= FirebaseStorage.getInstance().getReference(KeyF.AllFile.toString());
            sr.child(fl.list.get(index).getFilename()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    DatabaseReference dr=FirebaseDatabase.getInstance().getReference(KeyF.AllFile.toString());
                    dr.child(fl.list.get(index).getDatabasekey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                fl.selected.remove(fl.selected.indexOf(index));
                            }else {
                                Toast.makeText(MainShower.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainShower.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }





    //setting up notice delete
    private void deletenotice(){
        for(final int index:nt.selected){
            UploadItem ui=nt.list.get(index);
            DatabaseReference sr= FirebaseDatabase.getInstance().getReference(KeyF.Notice.toString());
            sr.child(ui.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        nt.selected.remove(nt.selected.indexOf(index));
                        Toast.makeText(MainShower.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainShower.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
