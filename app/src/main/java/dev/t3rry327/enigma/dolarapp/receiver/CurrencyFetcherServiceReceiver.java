package dev.t3rry327.enigma.dolarapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import dev.t3rry327.enigma.dolarapp.Util;

public class CurrencyFetcherServiceReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Util.scheduleJob(context);
    }
}
