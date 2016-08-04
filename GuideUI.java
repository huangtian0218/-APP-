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
 *	���������������������
 */
public class GuideUI extends Activity implements OnClickListener,OnPageChangeListener {
	//����ViewPager����
	private ViewPager viewPager;
	
	//����ViewPager������
	private ViewPagerAdapter vpAdapter;
	
	//����һ��ArrayList�����View
	private ArrayList<View> views;

	//����ͼƬ��Դ
    private static final int[] pics = {R.drawable.p1,R.drawable.p5,R.drawable.p3,R.drawable.p4};
    
    //�ײ�С���ͼƬ
    private ImageView[] points;
    
    //���밴ť
    private ImageButton button_in;
    
    //��¼��ǰѡ��λ��
    private int currentIndex;
    
    Timer timer;
	TimerTask task;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_ui);
		
		// ʹ��SharedPreferences����¼�Ƿ����״�ʹ��
		SharedPreferences sp = getSharedPreferences("logindata", MODE_PRIVATE);
		// ȡ����Ӧ��ֵ�����isFirstInΪtrue��˵����δд�룬��true��ΪĬ��ֵ
		Boolean isFirstIn = sp.getBoolean("isFirstIn", true);
		 // �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
        if (isFirstIn == true || isFirstIn == null) {
            //��һ��ʹ��
        	initView();
        	InitUser();
    		initData();	
    		timer = new Timer();
    		task = new TimerTask() {

    			@Override
    			public void run() {
    				// ��Ҫ������:������Ϣ
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
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ����ArrayList����
		views = new ArrayList<View>();
		
		//ʵ����ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		//���밴ť
		button_in=(ImageButton)findViewById(R.id.button_in);
		button_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ʹ��SharedPreferences����¼�Ƿ����״�ʹ��
				SharedPreferences sp = getSharedPreferences("logindata", MODE_PRIVATE);
				Editor edit=sp.edit();
				edit.putBoolean("isFirstIn", false);
				edit.commit();
				finish();
				Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
				startActivity(intent);
			}
		});
		//ʵ����ViewPager������
		vpAdapter = new ViewPagerAdapter(views);
	}
	//��ʼ���û���Ϣ����һ�ε�¼ʱΪ��
	private void InitUser() {
		ACache instanceInfo = ACache.get(this);
		//������һ�ε�¼���û���Ϣ
		instanceInfo.put("uname", "���ϵ�¼", 3 * ACache.TIME_DAY);
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
	 * ��ʼ������
	 */
	private void initData(){
		//����һ�����ֲ����ò���
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                														  LinearLayout.LayoutParams.FILL_PARENT);
       
        //��ʼ������ͼƬ�б�
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        } 
        
        //��������
        viewPager.setAdapter(vpAdapter);
        //���ü���
        viewPager.setOnPageChangeListener(this);
        
        //��ʼ���ײ�С��
        initPoint();
	}
	
	/**
	 * ��ʼ���ײ�С��
	 */
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);       
		
        points = new ImageView[pics.length];

        //ѭ��ȡ��С��ͼƬ
        for (int i = 0; i < pics.length; i++) {
        	//�õ�һ��LinearLayout�����ÿһ����Ԫ��
        	points[i] = (ImageView) linearLayout.getChildAt(i);
        	//Ĭ�϶���Ϊ��ɫ
        	points[i].setEnabled(true);
        	//��ÿ��С�����ü���
        	points[i].setOnClickListener(this);
        	//����λ��tag������ȡ���뵱ǰλ�ö�Ӧ
        	points[i].setTag(i);
        }
        
        //���õ���Ĭ�ϵ�λ��
        currentIndex = 0;
        //����Ϊ��ɫ����ѡ��״̬
        points[currentIndex].setEnabled(false);
	}
	
	/**
	 * ������״̬�ı�ʱ����
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	
	/**
	 * ����ǰҳ�汻����ʱ����
	 */

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
		

	}
	
	/**
	 * ���µ�ҳ�汻ѡ��ʱ����
	 */

	@Override
	public void onPageSelected(int position) {
		//���õײ�С��ѡ��״̬
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
				// ��Ҫ������:������Ϣ
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
		//timer.schedule(task, 1000, 1000); // 1s��ִ��task,����1s�ٴ�ִ��

	}

	/**
	 * ͨ������¼����л���ǰ��ҳ��
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
	 * ���õ�ǰҳ���λ��
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
     * ���õ�ǰ��С���λ��
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
