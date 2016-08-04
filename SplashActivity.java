package com.electronic_market.home;
import com.electronic_market.MainActivity;
import com.electronic_market.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Ӧ������ ����
 * 
 * @author: wll
 */
public class SplashActivity extends Activity {

	public static final String TAG = SplashActivity.class.getSimpleName();

	private ImageView mSplashItem = null;

	protected void findViewById() {
		mSplashItem = (ImageView) findViewById(R.id.splash_loading_item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);



		new Handler(getMainLooper());
		findViewById();
		initView();
	}

	protected void initView() {
		// ����Ч��
		Animation translate = AnimationUtils.loadAnimation(this,
				R.anim.splash_loading);
		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				// enterAnim���붯��,exitAnim�˳�����
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				SplashActivity.this.finish();
			}
		});
		mSplashItem.setAnimation(translate);
	}

}
