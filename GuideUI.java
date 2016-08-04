package com.electronic_market.GuideUI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import com.electronic_market.R;
import com.electronic_market.Acache.ACache;
import com.electronic_market.home.SplashActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
/**
 *	功能描述：主程序入口类
 */
public class GuideUI extends Activity implements OnClickListener,OnPageChangeListener {
	//定义ViewPager对象
	private ViewPager viewPager;
	
	//定义ViewPager适配器
	private ViewPagerAdapter vpAdapter;
	
	//定义一个ArrayList来存放View
	private ArrayList<View> views;

	//引导图片资源
    private static final int[] pics = {R.drawable.p1,R.drawable.p5,R.drawable.p3,R.drawable.p4};
    
    //底部小点的图片
    private ImageView[] points;
    
    //进入按钮
    private ImageButton button_in;
    
    //记录当前选中位置
    private int currentIndex;
    
    Timer timer;
	TimerTask task;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_ui);
		
		// 使用SharedPreferences来记录是否是首次使用
		SharedPreferences sp = getSharedPreferences("logindata", MODE_PRIVATE);
		// 取得相应的值，如果isFirstIn为true，说明还未写入，用true作为默认值
		Boolean isFirstIn = sp.getBoolean("isFirstIn", true);
		 // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (isFirstIn == true || isFirstIn == null) {
            //第一次使用
        	initView();
        	InitUser();
    		initData();	
    		timer = new Timer();
    		task = new TimerTask() {

    			@Override
    			public void run() {
    				// 需要做的事:发送消息
    				int position;
    				Toast.makeText(getApplicationContext(), currentIndex, 0).show();
    			}
    		};
	
        } else {
        	finish();
        	Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
			startActivity(intent);
			
        }
	}
	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化ArrayList对象
		views = new ArrayList<View>();
		
		//实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		//进入按钮
		button_in=(ImageButton)findViewById(R.id.button_in);
		button_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 使用SharedPreferences来记录是否是首次使用
				SharedPreferences sp = getSharedPreferences("logindata", MODE_PRIVATE);
				Editor edit=sp.edit();
				edit.putBoolean("isFirstIn", false);
				edit.commit();
				finish();
				Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
				startActivity(intent);
			}
		});
		//实例化ViewPager适配器
		vpAdapter = new ViewPagerAdapter(views);
	}
	//初始化用户信息，第一次登录时为空
	private void InitUser() {
		ACache instanceInfo = ACache.get(this);
		//存放最近一次登录的用户信息
		instanceInfo.put("uname", "马上登录", 3 * ACache.TIME_DAY);
		instanceInfo.put("password", "", 3 * ACache.TIME_DAY);
		instanceInfo.put("sex",-1, 3 * ACache.TIME_DAY);
		instanceInfo.put("phone", "", 3 * ACache.TIME_DAY);
		instanceInfo.put("email", "", 3 * ACache.TIME_DAY);
		instanceInfo.put("addr1", "", 3 * ACache.TIME_DAY);
		instanceInfo.put("addr2", "", 3 * ACache.TIME_DAY);
		instanceInfo.put("time", new Date(), 3 * ACache.TIME_DAY);
		instanceInfo.put("photo", "", 3 * ACache.TIME_DAY);
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		//定义一个布局并设置参数
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                														  LinearLayout.LayoutParams.FILL_PARENT);
       
        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        } 
        
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
        
        //初始化底部小点
        initPoint();
	}
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);       
		
        points = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
        	//得到一个LinearLayout下面的每一个子元素
        	points[i] = (ImageView) linearLayout.getChildAt(i);
        	//默认都设为灰色
        	points[i].setEnabled(true);
        	//给每个小点设置监听
        	points[i].setOnClickListener(this);
        	//设置位置tag，方便取出与当前位置对应
        	points[i].setTag(i);
        }
        
        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
	}
	
	/**
	 * 当滑动状态改变时调用
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	
	/**
	 * 当当前页面被滑动时调用
	 */

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
		

	}
	
	/**
	 * 当新的页面被选中时调用
	 */

	@Override
	public void onPageSelected(int position) {
		//设置底部小点选中状态
        setCurDot(position);
        if(position == 3)
        {
       	 button_in.setVisibility(View.VISIBLE); 
        }
        else{
       	 button_in.setVisibility(View.INVISIBLE); 
        }
       // timer.cancel();
		//timer=null;
		//task=null;
        
       // try {  
       //           Thread.currentThread();  
        //         Thread.sleep(3000);  
         //      } catch (InterruptedException e) {  
         //          e.printStackTrace();  
        //    }
       // timer = new Timer();
		//task = new TimerTask() {

		//	@Override
		//	public void run() {
				// 需要做的事:发送消息
		//		int position;
		//		if(currentIndex == 3)
		//		{
		//			position=0;
		//		}
		//		else
		//		{
		//		position=currentIndex+1;
		//		}
		//		
		//		setCurView(position);
		 //       setCurDot(position);
				
		//	}
		//};
		//timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行

	}

	/**
	 * 通过点击事件来切换当前的页面
	 */
	@Override
	public void onClick(View v) {
		 int position = (Integer)v.getTag();
         setCurView(position);
         setCurDot(position);
         //Toast.makeText(getApplicationContext(), position, 0).show();
         if(position == 3)
         {
        	// Toast.makeText(this, "ffff", 0).show();
        	 button_in.setVisibility(View.VISIBLE); 
         }
         else{
        	 button_in.setVisibility(View.INVISIBLE); 
         }
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position){
         if (position < 0 || position >= pics.length) {
             return;
         }
		//if(position<0)
	//	{
	//		position=position+4;
	//	}
	//	else if(position>3)
	//	{
	//		position=position-4;
	//	}
	//	else {
	//		
	//	}
         viewPager.setCurrentItem(position);
     }

     /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int position){
         if (position < 0 || position > pics.length - 1 || currentIndex == position) {
             return;
         }
    	//if(position<0)
		//{
		//	position=position+4;
		//}
		//else if(position>3)
		//{
		//	position=position-4;
		//}
		//else {
		//	
		//}
         points[position].setEnabled(false);
         points[currentIndex].setEnabled(true);

         currentIndex = position;
     }
	
}
