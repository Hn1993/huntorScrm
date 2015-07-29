package com.huntor.mscrm.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.huntor.mscrm.app.utils.MyLogger;

/**
 * Created by Admin on 2015/7/22.
 */
/*public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static final String TAG = "ConnectionChangeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		MyLogger.e(TAG,"网络状态改变");
		boolean success = false;
		//获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
		if (NetworkInfo.State.CONNECTED == state) { // 判断是否正在使用WIFI网络
			success = true;
		}
		state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
		if (NetworkInfo.State.CONNECTED != state) { // 判断是否正在使用GPRS网络
			success = true;
		}
		if (!success) {
			//Toast.makeText(LocationMapActivity.this， "您的网络连接已中断"， Toast.LENGTH_LONG).show();

		}

	}
}*/
