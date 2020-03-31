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
            //            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//
//            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
//            {
//                Toast.makeText(context, "Ringing State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
//                Log.e("hui",incomingNumber);
//            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener listener = new MyPhoneStateListener(context);
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
//            if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.CALL_STATE_OFFHOOK))
//            {
//                Toast.makeText(context, intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER), Toast.LENGTH_LONG).show();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class MyPhoneStateListener extends PhoneStateListener{

    Context mContext;

    public MyPhoneStateListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {

        Log.d("income",phoneNumber);
        Log.d("income",String.valueOf(state));
        if (state == TelephonyManager.CALL_STATE_RINGING)
        {
            Toast.makeText(mContext, "Incoming Number is: "+phoneNumber, Toast.LENGTH_LONG).show();
        }
        else if (state == TelephonyManager.CALL_STATE_IDLE)
        {
            Toast.makeText(mContext, "Phone Hung Up", Toast.LENGTH_LONG).show();
        }

    }
}