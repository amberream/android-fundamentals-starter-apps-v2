package com.amberream.powerreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            String toastMessage = "unknown intent action";
            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                toastMessage = context.getString(R.string.power_connected);
            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                toastMessage = context.getString(R.string.power_disconnected);
            } else if (action.equals(MainActivity.ACTION_CUSTOM_BROADCAST)) {
                toastMessage = context.getString(R.string.custom_broadcast_received);
            }

            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }

    }
}
