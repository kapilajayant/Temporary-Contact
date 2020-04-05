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
            Intent intent = new Intent(mContext, Main2Activity.class);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
//            addContactDialog();
        }

    }
//    public void addContactDialog() {
//        final Dialog mDialog = new Dialog(mContext);
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setContentView(R.layout.add_contact);
//        final EditText et_contact = mDialog.findViewById(R.id.et_contact);
//        TextView ok,cancel;
//        ok=(TextView) mDialog.findViewById(R.id.ok);
//        cancel=(TextView) mDialog.findViewById(R.id.cancel);
//        ok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "Contact Added", Toast.LENGTH_LONG).show();
//                mDialog.cancel();
//
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_LONG).show();
//                mDialog.cancel();
//            }
//        });
//        mDialog.show();
//    }
}
