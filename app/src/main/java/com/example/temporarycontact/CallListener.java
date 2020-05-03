package com.example.temporarycontact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener listener = MyPhoneStateListener.getInstance(context);
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
