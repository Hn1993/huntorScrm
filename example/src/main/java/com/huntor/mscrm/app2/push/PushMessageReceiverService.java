package com.huntor.mscrm.app2.push;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.IBinder;
import com.huntor.mscrm.app2.utils.MyLogger;

/**
 * 通过该服务与服务器建立TCP长链接
 * 
 * @author C
 */
public class PushMessageReceiverService extends Service {

    private static final String TAG = "PushMessageReceiverService" ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLogger.i(TAG,"onStartCommand__startConnect");
        TCPLongLinkManager.getInstance(this).startConnect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        MyLogger.i(TAG,"onDestroy__stopConnect");
        TCPLongLinkManager.getInstance(this).stopConnect();
        super.onDestroy();
    }

}
