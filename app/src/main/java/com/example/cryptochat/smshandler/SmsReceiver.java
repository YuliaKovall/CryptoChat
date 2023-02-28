package com.example.cryptochat.smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    Bundle bundle;
    SmsMessage[] messages;
    String message = "";
    Object[] pdus;
    Intent broadcastIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();
        if (bundle != null) {
            pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus != null ? pdus.length : 0];
            for (int i=0; i<messages.length;i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) (pdus != null ? pdus[i] :null));
   //             message += messages[i].getOriginatingAddress();
   //             message += ": ";
                message += messages[i].getMessageBody();
            }
            //send a broadcast intent to update the SMS received in a ChatActivity
            broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("message", message);
            context.sendBroadcast(broadcastIntent);
        }
    }
}
