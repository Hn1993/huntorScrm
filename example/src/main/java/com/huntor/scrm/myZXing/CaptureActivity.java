package com.huntor.scrm.myZXing;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.huntor.scrm.R;
import com.huntor.scrm.utils.Utils;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback,OnClickListener {



	String TAG = "CaptureActivity";

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button cancelScanButton;


	//加入打开手电筒
	boolean isopent=false;
	//Camera.Parameters params;
	private ImageView mImageView;
	/** Called when the activity is first created. */

	//加入手动输入条形码
	private TextView inputBarcode;

	//加入网络状态判断
	private TextView isConnented;
	//加入广播监听
	private BroadcastReceiver myReceiver;
	boolean isConnect=true;//wifi跟网络都无连接
	private CameraManager cameraManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(this);
		//getSystemService(Context.CAMERA_SERVICE,)
		//cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		//cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		//点击打开手电筒
		mImageView= (ImageView) findViewById(R.id.light_on_off);

		inputBarcode= (TextView) findViewById(R.id.input_barcode);
		inputBarcode.setOnClickListener(this);

		isConnented= (TextView) findViewById(R.id.net_ok_or_no);
	}

	private  void registerReceiver(){
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver=new ConnectionChangeReceiver();
		this.registerReceiver(myReceiver, filter);
	}

	private  void unregisterReceiver(){
		this.unregisterReceiver(myReceiver);
	}

	public boolean isConnected(){
		return Utils.isConnected(CaptureActivity.this);
	}


	@Override
	protected void onResume() {
		super.onResume();
		//initSurfaceView();
		registerReceiver();//注册广播

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		final SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(CaptureActivity.this);
			/**
			 * SURFACE_TYPE_PUSH_BUFFERS表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，
			 * 在Camera图像预览中就使用该类型的Surface，有Camera负责提供给预览Surface数据，这样图像预览会比较流畅。
			 */
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		//final Camera.Parameters params = camera.getParameters();
		//开关灯的点击事件
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CameraManager.init(CaptureActivity.this);
				CameraManager.get().flashlightUtils(mImageView);

			}
		});



	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		unregisterReceiver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		//Intent resultIntent = new Intent(this, InteractionLocaleActivity.class);
		//resultIntent.putExtra("list", resultString);
		//startActivity(resultIntent);
		CaptureActivity.this.finish();
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {

		Log.i("CaptureActivityHandler","isconnect=="+isConnect);

		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}

		handler = new CaptureActivityHandler(this, decodeFormats, characterSet,isConnect);


	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;

			Log.i("CaptureActivityHandler","initCamera==============================================");
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.input_barcode:
//				Intent intent=new Intent(this, InputBarcodeActivity.class);
//				startActivity(intent);
				break;
		}
	}


	public void initSurfaceView(){
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(CaptureActivity.this);
			/**
			 * SURFACE_TYPE_PUSH_BUFFERS表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，
			 * 在Camera图像预览中就使用该类型的Surface，有Camera负责提供给预览Surface数据，这样图像预览会比较流畅。
			 */
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	public class ConnectionChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if(mobNetInfo!=null&&wifiNetInfo!=null){
				if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
					//Utils.toast(CaptureActivity.this, "网络无连接");
					isConnect=false;
					initSurfaceView();
					isConnented.setVisibility(View.VISIBLE);
					//改变背景或者 处理网络的全局变量
				}else {
					//Utils.toast(CaptureActivity.this, "网络已恢复");
					isConnect=true;
					initSurfaceView();
					isConnented.setVisibility(View.GONE);
//				//改变背景或者 处理网络的全局变量
				}
			}else{
				Utils.toast(CaptureActivity.this,"获取网络状态失败!");
			}


		}
	}



}