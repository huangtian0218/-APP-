package com.electronic_market;
import java.util.ArrayList;
import java.util.List;

import com.electronic_market.hotsale.HotSale;
import com.electronic_market.shoppingcart.ShoppingCartMain;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;


public class MainActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener
{
	public static ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	
	
		
		initView();
		
		
		addFragment();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
	}
		
	private void addFragment() {
		mTabs.add(new Home());
		mTabs.add(new HotSale());
		mTabs.add(new ShoppingCartMain());
		mTabs.add(new MeInfo());
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return mTabs.get(position);
			}
		};
		
	}

	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.content);
		ChangeColorIconWithText home = (ChangeColorIconWithText) findViewById(R.id.home);
		mTabIndicators.add(home);
		ChangeColorIconWithText hot = (ChangeColorIconWithText) findViewById(R.id.hot_sale);
		mTabIndicators.add(hot);
		ChangeColorIconWithText car = (ChangeColorIconWithText) findViewById(R.id.shopping_cart);
		mTabIndicators.add(car);
		ChangeColorIconWithText me = (ChangeColorIconWithText) findViewById(R.id.me_information);
		mTabIndicators.add(me);
		
		home.setOnClickListener(this);
		hot.setOnClickListener(this);
		car.setOnClickListener(this);
		me.setOnClickListener(this);
		home.setIconAlpha(1.0f);

	}
	@Override
	public void onClick(View v)
	{
		resetOtherTabs();
		switch (v.getId())
		{
		case R.id.home:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.hot_sale:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.shopping_cart:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.me_information:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}
	}

	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs()
	{
		for (int i = 0; i < mTabIndicators.size(); i++)
		{
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 滑动内容区域时，底部颜色实现变化
	 * 第1页->第二页，position=0,positionOffset(0.0~1.0)
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels)
	{
		if (positionOffset > 0)
		{
			//left表示被移动的页面，right表示目标页面
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}
	@Override
	public void onPageSelected(int position) {
		switch (position) {
		case 0:
			
			break;

		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;
		}
		else
			return super.onKeyDown(keyCode, event);
	}
}
