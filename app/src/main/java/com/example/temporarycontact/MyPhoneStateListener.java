package com.example.temporarycontact;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyPhoneStateListener extends PhoneStateListener {

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
