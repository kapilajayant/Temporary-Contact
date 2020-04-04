package com.example.temporarycontact;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.core.app.NotificationCompat;

public class CallBackgroundService extends Service {
    public CallBackgroundService() {
    }

    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
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
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter i = new IntentFilter();
        i.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(receiver, i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(new CallListener());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new CallListener().onReceive(this, intent);

        startJob();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startJob() {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_contacts_black_24dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running in background")
                .setContentIntent(pendingIntent)
                .build());

    }
}
