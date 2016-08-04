package com.electronic_market;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.electronic_market.activity.CaptureActivity;
import com.electronic_market.activity.detection;
import com.electronic_market.data.Sort;
import com.electronic_market.home.AdDomain;
import com.electronic_market.home.Ads;
import com.electronic_market.home.Sort2;
import com.electronic_market.web.Browser;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class Home extends Fragment implements OnClickListener{
	String address;
	private static final int PHOTO_PIC = 1;
	private View view;
	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径

	private ViewPager adViewPager;
	private List<ImageView> imageViews;//滑动的图片集合

	private List<View> dots; //图片标题正文的那些点
	private List<View> dotList;

	private TextView tv_date;
	private TextView tv_title;
	private TextView tv_topic_from;
	private TextView tv_topic;
	private int currentItem = 0; // 当前图片的索引号
	// 定义的五个指示点
	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	private View dot5;
	

	private ScheduledExecutorService scheduledExecutorService;

	// 异步加载图片
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	// 轮播banner的数据
	private List<AdDomain> adList;
	
	//实现主页8个按钮
	private ImageButton recharge;
	private ImageButton sort;
	private ImageButton scan_code;
	private ImageButton taodiandian,juhuasuan,tianmao,xianyu,yitao;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};
	private ImageView mobile_buying_theme;
	private ImageView flash_buying;
	private ImageView package_buying;
	private ImageView double_11;
	private ImageView double_10;
	private ImageView lenovo_brand_day;
	private ImageView filling_preference;
	private ImageView watches;
	private ImageView mobiles_for_nationalday;
	private ImageView snack_foods;
	private ImageView charm_show;
	private ImageView jd_love_life;
	private ImageView brands_sale;
	private ImageView brands_show;
	private ImageView very_placard;
	private ImageView play_bigger;
	private ImageView one_for_goods;
	private ImageView blank_note;
	
	
	
	
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.homepage, container, false);
		//自定义标题栏
		getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_menu); 
		
		//分类
		sort=(ImageButton) view.findViewById(R.id.sort);
		sort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_sort=new Intent(getActivity(),Sort2.class);
				startActivity(intent_sort);
			}
		});
		
		//充值
		recharge=(ImageButton) view.findViewById(R.id.recharge);
		recharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v_recharge) {
				// TODO Auto-generated method stub
				Intent intent_recharge=new Intent(getActivity(),recharge.class);
				startActivity(intent_recharge);
				
			}
		});
		
		//扫描
		scan_code=(ImageButton)view.findViewById(R.id.iv_shao);
		scan_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_scan = new Intent(getActivity(), CaptureActivity.class);
				startActivityForResult(intent_scan, PHOTO_PIC);
			}
		});

		taodiandian=(ImageButton)view.findViewById(R.id.imageButton1);
		taodiandian.setOnClickListener(this);

		juhuasuan=(ImageButton)view.findViewById(R.id.imageButton2);
		juhuasuan.setOnClickListener(this);
		
		tianmao=(ImageButton)view.findViewById(R.id.imageButton3);
		tianmao.setOnClickListener(this);
		
		xianyu=(ImageButton)view.findViewById(R.id.imageButton5);
		xianyu.setOnClickListener(this);
		
		yitao=(ImageButton)view.findViewById(R.id.imageButton6);
		yitao.setOnClickListener(this);

		//<!-- 超值购 -->
		mobile_buying_theme=(ImageView)view.findViewById(R.id.mobile_buying_theme);
		mobile_buying_theme.setOnClickListener(this);
		flash_buying=(ImageView)view.findViewById(R.id.flash_buying);
		flash_buying.setOnClickListener(this);
		package_buying=(ImageView)view.findViewById(R.id.package_buying);
		package_buying.setOnClickListener(this);

				
		//<!-- 热门活动 -->		
		
		double_11=(ImageView)view.findViewById(R.id.double_11);
		double_11.setOnClickListener(this);
		double_10=(ImageView)view.findViewById(R.id.double_10);
		double_10.setOnClickListener(this);
		lenovo_brand_day=(ImageView)view.findViewById(R.id.lenovo_brand_day);
		lenovo_brand_day.setOnClickListener(this);
		//<!-- 有利可图 -->
		
		filling_preference=(ImageView)view.findViewById(R.id.filling_preference);
		filling_preference.setOnClickListener(this);
		watches=(ImageView)view.findViewById(R.id.watches);
		watches.setOnClickListener(this);
		mobiles_for_nationalday=(ImageView)view.findViewById(R.id.mobiles_for_nationalday);
		mobiles_for_nationalday.setOnClickListener(this);
		
		 //<!-- 特色市场 -->
		snack_foods=(ImageView)view.findViewById(R.id.snack_foods);
		snack_foods.setOnClickListener(this);
		charm_show=(ImageView)view.findViewById(R.id.charm_show);
		charm_show.setOnClickListener(this);
		jd_love_life=(ImageView)view.findViewById(R.id.jd_love_life);
		jd_love_life.setOnClickListener(this);
		
		
		// <!-- 品牌街 -->
		
		brands_sale=(ImageView)view.findViewById(R.id.brands_sale);
		brands_sale.setOnClickListener(this);
		brands_show=(ImageView)view.findViewById(R.id.brands_show);
		brands_show.setOnClickListener(this);
		very_placard=(ImageView)view.findViewById(R.id.very_placard);
		very_placard.setOnClickListener(this);
		
		
		// <!-- 金融街 -->
		
		play_bigger=(ImageView)view.findViewById(R.id.play_bigger);
		play_bigger.setOnClickListener(this);
		one_for_goods=(ImageView)view.findViewById(R.id.one_for_goods);
		one_for_goods.setOnClickListener(this);
		blank_note=(ImageView)view.findViewById(R.id.blank_note);
		blank_note.setOnClickListener(this);	
		//使用ImageLoader之前初始化
		initImageLoader();
		
		// 获取图片加载实例
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.top_banner_android)
				.showImageForEmptyUri(R.drawable.top_banner_android)
				.showImageOnFail(R.drawable.top_banner_android)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();

		initAdData();

		startAd();
		return view;
	}
	
	//扫描实现
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
				case PHOTO_PIC:
					if(resultCode == -1){
						String result = data.getExtras().getString("result");
						Toast.makeText(getActivity(),"解析结果：" + result, Toast.LENGTH_LONG).show();	
						if(result.startsWith("http://")||result.startsWith("https://"))
						{
							Intent intent= new Intent();        
					    intent.setAction("android.intent.action.VIEW");    
					    
					    Uri content_url = Uri.parse(result);   
					    intent.setData(content_url);  
					    startActivity(intent);
						}
						else if(result!=null)
							{
								Intent intent2=new Intent(getActivity(),detection.class);
							intent2.putExtra("String",result); 
							startActivity(intent2);
							}
							 else
								Toast.makeText(getActivity(), "二维码识别失败", Toast.LENGTH_LONG);
					}		
					break;
				default:
					break;
			}
			
		}

	
	private void initImageLoader() {
		File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
				.getOwnCacheDirectory(getActivity(), IMAGE_CACHE_PATH);

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
				.memoryCacheSize(12 * 1024 * 1024)
				.discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}
	
	private void initAdData() {
		// 广告数据
		adList = getBannerAd();

		imageViews = new ArrayList<ImageView>();

		// 点
		dots = new ArrayList<View>();
		dotList = new ArrayList<View>();
		dot0 = view.findViewById(R.id.v_dot0);
		dot1 = view.findViewById(R.id.v_dot1);
		dot2 = view.findViewById(R.id.v_dot2);
		dot3 = view.findViewById(R.id.v_dot3);
		dot4 = view.findViewById(R.id.v_dot4);
		dot5 = view.findViewById(R.id.v_dot5);
		dots.add(dot0);
		dots.add(dot1);
		dots.add(dot2);
		dots.add(dot3);
		dots.add(dot4);
		dots.add(dot5);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_topic_from = (TextView) view.findViewById(R.id.tv_topic_from);
		tv_topic = (TextView) view.findViewById(R.id.tv_topic);

		adViewPager = (ViewPager) view.findViewById(R.id.vp);
		adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		adViewPager.setOnPageChangeListener(new MyPageChangeListener());
		addDynamicView();
	}
	
	private void addDynamicView() {
		// 动态添加图片和下面指示的圆点
		// 初始化图片资源
		for (int i = 0; i < adList.size(); i++) {
			ImageView imageView = new ImageView(getActivity());
			// 异步加载图片
			mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,
					options);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			dots.get(i).setVisibility(View.VISIBLE);
			dotList.add(dots.get(i));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}


	private void startAd() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
				TimeUnit.SECONDS);
	}
	
	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			synchronized (adViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
	}
	
	private class MyPageChangeListener implements OnPageChangeListener {

		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			AdDomain adDomain = adList.get(position);
			tv_title.setText(adDomain.getTitle()); // 设置标题
			tv_date.setText(adDomain.getDate());
			tv_topic_from.setText(adDomain.getTopicFrom());
			tv_topic.setText(adDomain.getTopic());
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}
	}
	
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return adList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView iv = imageViews.get(position);
			((ViewPager) container).addView(iv);
			final AdDomain adDomain = adList.get(position);
			// 在这个方法里面设置图片的点击事件
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});
			return iv;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}
	
	//轮播广播模拟数据
	public static List<AdDomain> getBannerAd() {
		List<AdDomain> adList = new ArrayList<AdDomain>();
		String[] imgUrls = {
				"http://7mno4h.com2.z0.glb.qiniucdn.com/5608f3b5Nc8d90151.jpg",
				"http://7mno4h.com2.z0.glb.qiniucdn.com/5608eb8cN9b9a0a39.jpg",
				"http://7mno4h.com2.z0.glb.qiniucdn.com/5608cae6Nbb1a39f9.jpg",
				"http://7mno4h.com2.z0.glb.qiniucdn.com/5608b7cdN218fb48f.jpg",
				"http://7mno4h.com2.z0.glb.qiniucdn.com/560b5a7eN214306c8.jpg",
				"http://7mno4h.com2.z0.glb.qiniucdn.com/560a409eN35e252de.jpg"};
				
		
				for (int i = 1; i <= 6; i++) {

			AdDomain adDomain = new AdDomain();
			adDomain.setImgUrl(imgUrls[i - 1]);
			adList.add(adDomain);
		}

		return adList;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageButton1:
			Toast.makeText(getActivity(), "淘点点",
				     Toast.LENGTH_SHORT).show();
			Intent tdIntent=new Intent(getActivity(),Browser.class);
			
			tdIntent.putExtra(address, "http://www.taobao.com/market/dd/");
			getActivity().startActivity(tdIntent);
			
			
			
			break;
		case R.id.imageButton2:
			Toast.makeText(getActivity(), "聚划算",
				     Toast.LENGTH_SHORT).show();
			Intent jhsIntent=new Intent(getActivity(),Browser.class);
			
			jhsIntent.putExtra(address, "https://jhs.m.taobao.com/m/index.html");
			getActivity().startActivity(jhsIntent);
			
			
			
			break;
		case R.id.imageButton3:
			Toast.makeText(getActivity(), "天猫",
				     Toast.LENGTH_SHORT).show();
			Intent tmIntent=new Intent(getActivity(),Browser.class);
			
			tmIntent.putExtra(address, "https://www.tmall.com");
			getActivity().startActivity(tmIntent);
			
			
			
			break;
		case R.id.imageButton5:
			Toast.makeText(getActivity(), "闲鱼",
				     Toast.LENGTH_SHORT).show();
			Intent xyIntent=new Intent(getActivity(),Browser.class);
			
			xyIntent.putExtra(address, "https://2.taobao.com/");
			getActivity().startActivity(xyIntent);
			
			
			break;
		case R.id.imageButton6:
			Toast.makeText(getActivity(), "一淘",
				     Toast.LENGTH_SHORT).show();
			Intent ytIntent=new Intent(getActivity(),Browser.class);
			
			ytIntent.putExtra(address, "http://www.etao.com");
			getActivity().startActivity(ytIntent);	
			break;
			
			
		case R.id.mobile_buying_theme:
			Intent intent_ads1=new Intent(getActivity(),Ads.class);
			intent_ads1.putExtra("campaignId", 1);
			getActivity().startActivity(intent_ads1);
			break;
		case R.id.flash_buying:
			Intent intent_ads2=new Intent(getActivity(),Ads.class);
			intent_ads2.putExtra("campaignId", 2);
			getActivity().startActivity(intent_ads2);
			break;
		case R.id.package_buying:
			Intent intent_ads3=new Intent(getActivity(),Ads.class);
			intent_ads3.putExtra("campaignId", 3);
			getActivity().startActivity(intent_ads3);
			break;
		case R.id.double_11:
			Intent intent_ads4=new Intent(getActivity(),Ads.class);
			intent_ads4.putExtra("campaignId", 4);
			getActivity().startActivity(intent_ads4);
			break;
		case R.id.double_10:
			Intent intent_ads5=new Intent(getActivity(),Ads.class);
			intent_ads5.putExtra("campaignId", 5);
			getActivity().startActivity(intent_ads5);
			break;
		case R.id.lenovo_brand_day:
			Intent intent_ads6=new Intent(getActivity(),Ads.class);
			intent_ads6.putExtra("campaignId", 6);
			getActivity().startActivity(intent_ads6);
			break;
		case R.id.filling_preference:
			Intent intent_ads7=new Intent(getActivity(),Ads.class);
			intent_ads7.putExtra("campaignId", 7);
			getActivity().startActivity(intent_ads7);
			break;
		case R.id.watches:
			Intent intent_ads8=new Intent(getActivity(),Ads.class);
			intent_ads8.putExtra("campaignId", 8);
			getActivity().startActivity(intent_ads8);
			break;
		case R.id.mobiles_for_nationalday:
			Intent intent_ads9=new Intent(getActivity(),Ads.class);
			intent_ads9.putExtra("campaignId", 9);
			getActivity().startActivity(intent_ads9);
			break;
		case R.id.snack_foods:
			Intent intent_ads10=new Intent(getActivity(),Ads.class);
			intent_ads10.putExtra("campaignId", 10);
			getActivity().startActivity(intent_ads10);
			break;
		case R.id.charm_show:
			Intent intent_ads11=new Intent(getActivity(),Ads.class);
			intent_ads11.putExtra("campaignId", 11);
			getActivity().startActivity(intent_ads11);
			break;
		case R.id.jd_love_life:
			Intent intent_ads12=new Intent(getActivity(),Ads.class);
			intent_ads12.putExtra("campaignId", 12);
			getActivity().startActivity(intent_ads12);
			break;
		case R.id.brands_sale:
			Intent intent_ads13=new Intent(getActivity(),Ads.class);
			intent_ads13.putExtra("campaignId", 13);
			getActivity().startActivity(intent_ads13);
			break;
		case R.id.brands_show:
			Intent intent_ads14=new Intent(getActivity(),Ads.class);
			intent_ads14.putExtra("campaignId", 14);
			getActivity().startActivity(intent_ads14);
			break;
		case R.id.very_placard:
			Intent intent_ads15=new Intent(getActivity(),Ads.class);
			intent_ads15.putExtra("campaignId", 15);
			getActivity().startActivity(intent_ads15);
			break;
		case R.id.play_bigger:
			Intent intent_ads16=new Intent(getActivity(),Ads.class);
			intent_ads16.putExtra("campaignId", 16);
			getActivity().startActivity(intent_ads16);
			break;
		case R.id.one_for_goods:
			Intent intent_ads17=new Intent(getActivity(),Ads.class);
			intent_ads17.putExtra("campaignId", 17);
			getActivity().startActivity(intent_ads17);
			break;
		case R.id.blank_note:
			Intent intent_ads18=new Intent(getActivity(),Ads.class);
			intent_ads18.putExtra("campaignId", 18);
			getActivity().startActivity(intent_ads18);
			break;
			

			default:
				break;
		}
	}

	
	
}
