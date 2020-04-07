package com.example.temporarycontact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyPhoneStateListener extends PhoneStateListener {

    Context mContext;
    private static MyPhoneStateListener instance = null;

    public MyPhoneStateListener(Context mContext) {
        this.mContext = mContext;
    }

    public static MyPhoneStateListener getInstance(Context context){
        if (instance == null)
        {
            instance = new MyPhoneStateListener(context);
        }
        return instance;
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
//            Toast.makeText(mContext, "Phone Hung Up", Toast.LENGTH_LONG).show();
            if (!phoneNumber.isEmpty())
            {
                Intent intent = new Intent(mContext, Main2Activity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }
    }
}
