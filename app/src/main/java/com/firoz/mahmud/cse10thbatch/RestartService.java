package com.firoz.mahmud.cse10thbatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,NoticeSender.class));
    }
}
