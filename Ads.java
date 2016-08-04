package com.electronic_market.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.electronic_market.MainActivity;
import com.electronic_market.R;
import com.electronic_market.data.HuoDong_fenlei;
import com.electronic_market.data.Product;
import com.electronic_market.data.ProductList;
import com.electronic_market.hotsale.ProductAdapter;
import com.electronic_market.hotsale.RefreshLayout;
import com.electronic_market.hotsale.RefreshLayout.OnLoadListener;
import com.electronic_market.product_detial.ProductDetial;
import com.electronic_market.server.ServerData;
import com.loopj.android.image.SmartImageView;

import android.accounts.AbstractAccountAuthenticator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ads extends Activity{
	
	private TextView getback;
	private List<Product> products = new ArrayList<Product>();
	private List<Product> products_all = new ArrayList<Product>();
	private List<Product> products_zonghe = new ArrayList<Product>();
	private ListView productListView;
	private ProductAdapter2 adapter;
	private static int curNum=5;
	private static int inum=0;
	private int totalPage;  
	private int totalcount;
	private int campaignId;
	private TextView zonghe;
	private boolean jiage_state=false;//false:降序  true：升序
	private boolean zonghe_state=true;//false:不在综合  true：在综合
	private TextView xiaoliang;
	private TextView jiage;
	private ImageButton up;
	private ImageButton down;
	private int now_huodongfenlei=0;
	private ViewPager viewPager;
	private List<SmartImageView> list;// viewpager资源
	// private ImageView imageView;
	// private ImageView[] imageViews;
	private int num = 300;
//	private LocalActivityManager manager;
	private ImageView imageView1, imageView2, imageView3;
	private static String[] imgUrls={
			"http://7mno4h.com2.z0.glb.qiniucdn.com/560a26d2N78974496.jpg",
			"http://7mno4h.com2.z0.glb.qiniucdn.com/560be0c3N9e77a22a.jpg",
			"http://7mno4h.com2.z0.glb.qiniucdn.com/5608da79Ncefc3eca.jpg"};
	private List<HuoDong_fenlei> huodong_fenlei=new ArrayList<HuoDong_fenlei>();
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	//view = inflater.inflate(R.layout.product_list, container, false);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
      //隐藏标题栏
      		requestWindowFeature(Window.FEATURE_NO_TITLE); 
      		setContentView(R.layout.xmain);
      		//自定义标题栏
      		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.ads_top); 
      	//返回上一页
        Intent intent=getIntent();
        campaignId=intent.getIntExtra("campaignId", 1);
        zonghe=(TextView)findViewById(R.id.zonghe);
        xiaoliang=(TextView)findViewById(R.id.xiaoliang);
        jiage=(TextView)findViewById(R.id.jiage);
        zonghe.setTextColor(getResources().getColor(R.color.tv_Red));
        up=(ImageButton)findViewById(R.id.imageButton_up);
        down=(ImageButton)findViewById(R.id.imageButton_down);
     	getback=(TextView) findViewById(R.id.getback);
     	  if(campaignId<4){
     		 getback.setText("< 超值购");
     		 now_huodongfenlei=0;
          }
     	  else if(campaignId<7)
     	  {
     		 getback.setText("< 热门活动");
     		now_huodongfenlei=1;
     	  }
     	 else if(campaignId<10)
    	  {
    		 getback.setText("< 有利可图");
    		 now_huodongfenlei=2;
    	  }
     	 else if(campaignId<13)
   	     {
   		     getback.setText("< 特色市场");
   		     now_huodongfenlei=3;
   	      }
     	else if(campaignId<16)
     	  {
     		 getback.setText("< 品牌街");
     		 now_huodongfenlei=4;
     	  }
     	else{
     		getback.setText("< 金融街");
     		now_huodongfenlei=5;
     	}
     	  
     	  
     	imageView1 = (ImageView) findViewById(R.id.ad1);
 		imageView2 = (ImageView) findViewById(R.id.ad2);
 		imageView3 = (ImageView) findViewById(R.id.ad3);
 		list = new ArrayList<SmartImageView>();  
 		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		
		//对应每个活动分类的具体内容
		        list.clear();
				huodong_fenlei=ServerData.getHuodong_fenlei();
				if(huodong_fenlei.size()>0)
				{
				
							SmartImageView imageView1 = new SmartImageView(this);
							SmartImageView imageView2 = new SmartImageView(this);
							SmartImageView imageView3 = new SmartImageView(this);
							imageView1.setImageUrl(huodong_fenlei.get(now_huodongfenlei).getCpOne().getImgUrl());
							imageView2.setImageUrl(huodong_fenlei.get(now_huodongfenlei).getCpTwo().getImgUrl());
							imageView3.setImageUrl(huodong_fenlei.get(now_huodongfenlei).getCpThree().getImgUrl());
							//imageView1.setScaleType(ScaleType.FIT_XY);
							//imageView2.setScaleType(ScaleType.FIT_XY);
							//imageView3.setScaleType(ScaleType.FIT_XY);
							list.add(imageView1);
							list.add(imageView2);
							list.add(imageView3);	
				} 

		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new MyListener());
		viewPager.setCurrentItem(300);
		mHandler.postDelayed(mRunnable, 2000);  
     	  
     	  
     	  
     	getback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		}); 
     	
      zonghe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zonghe_state=true;
				zonghe.setTextColor(getResources().getColor(R.color.tv_Red));
				xiaoliang.setTextColor(getResources().getColor(R.color.tv_Black));
				up.setImageResource(R.drawable.up);
				down.setImageResource(R.drawable.down);
				jiage_state=false;
				products.clear();  
				init_data();
				adapter.notifyDataSetChanged();
			}
		}); 
      
      
      xiaoliang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zonghe_state=false;
				zonghe.setTextColor(getResources().getColor(R.color.tv_Black));
				xiaoliang.setTextColor(getResources().getColor(R.color.tv_Red));
				up.setImageResource(R.drawable.up);
				down.setImageResource(R.drawable.down);
				jiage_state=false;
				Comparator comp = new Comparator() { 
				        public int compare(Object o1, Object o2) { 
				        	Product p1 = (Product) o1; 
				        	Product p2 = (Product) o2; 
				        	float a,b;
				        	a=Float.parseFloat(p1.getSale()); 
				        	b=Float.parseFloat(p2.getSale());
				            if (a < b) 
				                return 1; 
				            else if (a == b) 
				                return 0; 
				            else if (a > b) 
				                return -1; 
				            return 0; 
				        } 
				    };
				 products.clear();  
				 Collections.sort(products_all, comp);
				 init_data();
				 adapter.notifyDataSetChanged();
				 
			}
		}); 
      jiage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zonghe_state=false;
				zonghe.setTextColor(getResources().getColor(R.color.tv_Black));
				xiaoliang.setTextColor(getResources().getColor(R.color.tv_Black));
				if(jiage_state==false)
				{
					up.setImageResource(R.drawable.up_focused);
					down.setImageResource(R.drawable.down);
					jiage_state=true;
					Comparator comp = new Comparator() { 
				        public int compare(Object o1, Object o2) { 
				        	Product p1 = (Product) o1; 
				        	Product p2 = (Product) o2; 
				        	float a,b;
				        	a=Float.parseFloat(p1.getPrice()); 
				        	b=Float.parseFloat(p2.getPrice());
				            if (a < b) 
				                return -1; 
				            else if (a == b) 
				                return 0; 
				            else if (a > b) 
				                return 1; 
				            return 0; 
				        } 
				    };
				 products.clear();  
				 Collections.sort(products_all, comp);
				 init_data();
				 adapter.notifyDataSetChanged();
					
					
					
					
				}
				else{

					up.setImageResource(R.drawable.up);
					down.setImageResource(R.drawable.down_focused);
					jiage_state=false;
					Comparator comp = new Comparator() { 
				        public int compare(Object o1, Object o2) { 
				        	Product p1 = (Product) o1; 
				        	Product p2 = (Product) o2; 
				        	float a,b;
				        	a=Float.parseFloat(p1.getPrice()); 
				        	b=Float.parseFloat(p2.getPrice());
				            if (a < b) 
				                return 1; 
				            else if (a == b) 
				                return 0; 
				            else if (a > b) 
				                return -1; 
				            return 0; 
				        } 
				    };
				 products.clear();  
				 Collections.sort(products_all, comp);
				 init_data();
				 adapter.notifyDataSetChanged();
					
				}
			}
		}); 

		ProductList productList=new ProductList();
        productList=ServerData.getProductLists(campaignId,0,1);
		totalcount=productList.getTotalCount();
		totalPage=productList.getTotalPage();
		for(int i=1;i<=totalPage;i++)
		{
			int size_count=0;
			ProductList productLists=new ProductList();
	        productLists=ServerData.getProductLists(campaignId,0,i);
			size_count=productLists.getList().size();
			for(int j=0;j<size_count;j++){
				
				products_all.add(productLists.getList().get(j));
				products_zonghe.add(productLists.getList().get(j));
			}
			
		} 
		//products_zonghe=products_all;
        init_data();
		
		productListView = (ListView) findViewById(R.id.product_listview);
		adapter = new ProductAdapter2(getApplicationContext(), products);
		productListView.setAdapter(adapter);

		adapter.notifyDataSetChanged();
		productListView.setOnItemClickListener(new myOnItemClickListener()); // 单击选项
		
		
		
		// 获取RefreshLayout实例
		final RefreshLayout myRefreshListView = (RefreshLayout) findViewById(R.id.swipe_layout);

		// 设置下拉刷新时的颜色值,颜色值需要定义在xml中
		myRefreshListView.setColorSchemeColors(R.color.swipe_color_1,
				R.color.swipe_color_2, R.color.swipe_color_3,
				R.color.swipe_color_4);
		// 设置下拉刷新监听器
		myRefreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				//Toast.makeText(getApplicationContext(), "正在刷新", Toast.LENGTH_SHORT)
				//		.show();

				myRefreshListView.postDelayed(new Runnable() {

					@Override
					public void run() {
						// 更新数据						
						adapter.notifyDataSetChanged();
						// 更新完后调用该方法结束刷新
						myRefreshListView.setRefreshing(false);
					}
				}, 1000);
			}
		});

		// 加载监听器
		myRefreshListView.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				
				if(curNum<=totalcount && curNum!=inum)
				{
					myRefreshListView.postDelayed(new Runnable() {

						@Override
						public void run() {
					
							if(curNum<=totalcount)
							{
								for(;inum<curNum;inum++)
								{  
									if(zonghe_state==false)
									{
										products.add(products_all.get(inum));
									}
									else{
										products.add(products_zonghe.get(inum));
									}
								}
								
								
								adapter.notifyDataSetChanged();
								myRefreshListView.setLoading(false);
							}
							else {
								myRefreshListView.setLoading(false);
							}
							if(curNum+5>totalcount)
							{
								curNum=totalcount;
							}
							else{
								curNum=curNum+5;
							}
							
						}
					}, 1500);
				}
				else{
					Toast.makeText(getApplicationContext(), "亲~，已经没有更多的数据了", Toast.LENGTH_SHORT)
					.show();
					myRefreshListView.setLoading(false);
				}
				
			}
		});
		

}
	
	
	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {
		public void run() {
			// 每隔多长时间执行一次
			// mHandler.postDelayed(mRunnable, 1000*PhoneConstans.TIMEVALUE);
			mHandler.postDelayed(mRunnable, 1000 * 3);
			num++;
			viewHandler.sendEmptyMessage(num);
		}

	};

	// private View getView(String id, Intent intent) {
	// return manager.startActivity(id, intent).getDecorView();
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			mHandler.removeCallbacks(mRunnable);
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};	
	
	
	public void init_data() {
		inum=0;
		curNum=5;
		if(totalcount>0)
		{
			for(;inum<curNum;inum++)
			{
				if(zonghe_state==false)
				{
					products.add(products_all.get(inum));
				}
				else{
					products.add(products_zonghe.get(inum));
				}
			}
			if(curNum+5>totalcount)
			{
				curNum=totalcount;
			}
			else{
				curNum=curNum+5;
			}
		}
		else{
			inum=curNum;
		}
		
		
	}
	
	private class myOnItemClickListener implements OnItemClickListener {  
		  
		  
		@SuppressWarnings("unchecked")  
		@Override  
		public void onItemClick(AdapterView<?> parent, View view, int position,  
		long id) {  
			Intent intent=new Intent(getApplicationContext(),ProductDetial.class);
			intent.putExtra("name",products.get(position).getName());
			intent.putExtra("url", products.get(position).getImgUrl());
			intent.putExtra("id", products.get(position).getId());
			intent.putExtra("price",products.get(position).getPrice());
			intent.putExtra("salenum",products.get(position).getSale());
			startActivity(intent);
		}  
	}
	
	
	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			// ((ViewPager) arg0).removeView(list.get(arg1 % list.size()));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			try {
				// ((ViewPager) arg0).addView(list.get(arg1),0);
				((ViewPager) arg0).addView((View) list.get(arg1 % list.size()),
						0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			final int i=arg1 % list.size();
			View views=(View)list.get(arg1 % list.size());
			views.setOnClickListener(new OnClickListener() {
				   
				   @Override
				   public void onClick(View v) {
				    // TODO Auto-generated method stub
				    //Toast.makeText(getApplicationContext(), "点击了" + i, 0).show();
					Intent intent_ads=new Intent(getApplicationContext(),Ads.class);
					int campaign=now_huodongfenlei*3+1+i;
					intent_ads.putExtra("campaignId", campaign);
					finish();
					startActivity(intent_ads);
					
				   }
				  });

			return list.get(arg1 % list.size());

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	class MyListener implements OnPageChangeListener {

		// 当滑动状态改变时调用
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			// arg0=arg0%list.size();

		}

		// 当当前页面被滑动时调用
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		// 当新的页面被选中时调用
		@Override
		public void onPageSelected(int arg0) {
			num = arg0;
			arg0 = arg0 % list.size();
			//Toast.makeText(getApplicationContext(), arg0 + "", 0).show();
			switch (arg0) {
			case 0:
				imageView1.setBackgroundResource(R.drawable.ad1);
				imageView2.setBackgroundResource(R.drawable.ad2);
				imageView3.setBackgroundResource(R.drawable.ad2);
				break;
			case 1:
				imageView1.setBackgroundResource(R.drawable.ad2);
				imageView2.setBackgroundResource(R.drawable.ad1);
				imageView3.setBackgroundResource(R.drawable.ad2);
				break;
			case 2:
				imageView1.setBackgroundResource(R.drawable.ad2);
				imageView2.setBackgroundResource(R.drawable.ad2);
				imageView3.setBackgroundResource(R.drawable.ad1);
				break;

			default:
				break;
			}
			System.out.println("当前是第" + arg0 + "页");
		}

	}
	
	
	

}
