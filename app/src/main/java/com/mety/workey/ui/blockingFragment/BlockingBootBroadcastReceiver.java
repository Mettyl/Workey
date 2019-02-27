package com.mety.workey.ui.blockingFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mety.workey.ui.base.Logger;


public class BlockingBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i("onReceive: Service restarted after boot.");
        Intent service = new Intent(context, BlockingService.class);
        context.startService(service);
    }
}
