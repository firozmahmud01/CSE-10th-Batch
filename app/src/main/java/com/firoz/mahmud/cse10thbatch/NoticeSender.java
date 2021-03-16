package com.firoz.mahmud.cse10thbatch;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NoticeSender extends Service {
    public NoticeSender() {
    }
    public static boolean isRunning=false;
    public static DatabaseReference dr;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        isRunning=true;
        final SharedPreferences sp=getSharedPreferences("Data",MODE_PRIVATE);
        dr= FirebaseDatabase.getInstance().getReference(KeyF.Notice.toString());
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String all=sp.getString("Data","null");
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UploadItem ui=ds.getValue(UploadItem.class);
                    if (!all.contains(ui.getKey())) {
                        SharedPreferences.Editor she=sp.edit();
                        she.putString("Data",all+"//"+ui.getKey());
                        she.commit();
                        sendNotification(ui.getTitle(), ui.getDetails());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
        Intent restartService = new Intent("RestartService");
        sendBroadcast(restartService);
    }



    private void sendNotification(String title,String details){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(details)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat nmc=NotificationManagerCompat.from(this);
        nmc.notify(new Random().nextInt(1000),builder.build());
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
