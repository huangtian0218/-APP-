package com.electronic_market.activity;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.net.Uri;

/*import com.bear.zxingdemo.MainActivity;*/
/*import com.bear.zxingdemo.RGBLuminanceSource;*/
/*import com.bear.zxingdemo.MainActivity;*/

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.electronic_market.Home;
import com.electronic_market.R;
import com.electronic_market.camera.CameraManager;
import com.electronic_market.decoding.CaptureActivityHandler;
import com.electronic_market.decoding.InactivityTimer;
import com.electronic_market.view.ViewfinderView;
/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {

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
	private Button cancelScanButton,picture;
	private static final int CHOOSE_PIC = 0;
	private String  imgPath = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		picture=(Button)this.findViewById(R.id.btn_picture);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
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
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent();
				intent1.setAction(Intent.ACTION_PICK);
				
				intent1.setType("image/*");
				
				Intent intent2 =  Intent.createChooser(intent1, "ѡ���ά��ͼƬ");
				startActivityForResult(intent2, CHOOSE_PIC);
				
			}
		});
		//quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		imgPath = null;
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case CHOOSE_PIC:
				String[] proj = new String[]{MediaStore.Images.Media.DATA};
				Cursor cursor =CaptureActivity.this.getContentResolver().query(data.getData(), proj, null, null, null);
				
				if(cursor.moveToFirst()){
					int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
					System.out.println(columnIndex);
					//��ȡ���û�ѡ��Ķ�ά��ͼƬ�ľ���·��
					imgPath = cursor.getString(columnIndex);
				}
				cursor.close();
				
				//��ȡ�������
				Result ret = parseQRcodeBitmap(imgPath);
				
			Toast.makeText(CaptureActivity.this,"���������" + ret.toString(), Toast.LENGTH_LONG).show();
			
				String result=ret.getText();
				if(result.startsWith("http://")||result.startsWith("https://"))
				{
					Intent intent= new Intent();        
			    intent.setAction("android.intent.action.VIEW");    
			    
			    Uri content_url = Uri.parse(result);   
			    intent.setData(content_url);  
			    startActivity(intent);
				}
				else {
					
					Intent intent=new Intent(CaptureActivity.this,detection.class);
					intent.putExtra("String",result); 
					startActivity(intent);
					finish();
				}
				
				
				
				break;
			
			default:
				break;
			}
		}
		
	}
	//������ά��ͼƬ,���ؽ����װ��Result������
	private com.google.zxing.Result  parseQRcodeBitmap(String bitmapPath){
		//����ת������UTF-8
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
		//��ȡ����������ͼƬ
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		//������ǰ�inJustDecodeBounds��Ϊtrue����ôBitmapFactory.decodeFile(String path, Options opt)
		//��������ķ���һ��Bitmap���㣬������������Ŀ���ȡ��������
		options.inJustDecodeBounds = true;
		//��ʱ��bitmap��null����δ���֮��options.outWidth �� options.outHeight����������Ҫ�Ŀ�͸���
		Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath,options);
		//����������ȡ������ͼƬ�ı߳�����ά��ͼƬ�������εģ�����Ϊ400����
		/**
			options.outHeight = 400;
			options.outWidth = 400;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(bitmapPath, options);
		*/
		//����������������Ȼ��bitmap�޶���������Ҫ�Ĵ�С�����ǲ�û�н�Լ�ڴ棬���Ҫ��Լ�ڴ棬���ǻ���Ҫʹ��inSimpleSize�������
		options.inSampleSize = options.outHeight / 400;
		if(options.inSampleSize <= 0){
			options.inSampleSize = 1; //��ֹ��ֵС�ڻ����0
		}
		/**
		 * ������Լ�ڴ�����
		 * 
		 * options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // Ĭ����Bitmap.Config.ARGB_8888
		 * options.inPurgeable = true; 
		 * options.inInputShareable = true; 
		 */
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(bitmapPath, options); 
		//�½�һ��RGBLuminanceSource���󣬽�bitmapͼƬ�����˶���
		RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
		//��ͼƬת���ɶ�����ͼƬ
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
		//��ʼ����������
		QRCodeReader reader = new QRCodeReader();
		//��ʼ����
		Result result = null;
		try {
			result = reader.decode(binaryBitmap, hints);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
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
		//FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
//			System.out.println("Result:"+resultString);
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			this.setResult(-1, resultIntent);
		}
		finish();
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
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

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
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

}