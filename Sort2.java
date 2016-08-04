package com.electronic_market.home;

import com.electronic_market.R;
import com.electronic_market.MyAdapter.MyAdapter;
import com.electronic_market.goodssort.wants_hot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


public class Sort2 extends FragmentActivity {
	
	private TextView getback;
	private RecyclerView horizontal_item;
	private String[] data = new String[]{"热门推荐","品牌男装","内衣配饰","家用电器","电脑办公",
  			"手机数码","母婴频道","图书","家居家纺","居家生活","个性化妆","鞋靴箱包","奢侈礼品","珠宝饰品"};
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
      	requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.sort);
        
        //自定义标题栏
      	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_menu);
      	//商品分类默认显示热门推荐
      	changeFragment(new wants_hot(1));
      	//返回上一页
      	getback=(TextView) findViewById(R.id.getback);
      	getback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
      	
      	horizontal_item=(RecyclerView) findViewById(R.id.horizontal_item);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontal_item.setLayoutManager(layoutManager1);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        //horizontal_item.setHasFixedSize(true);
       
        MyAdapter myAdapter = new MyAdapter(data);
        horizontal_item.setAdapter(myAdapter);
        
      //RecyclerView添加点击事件
        horizontal_item.addOnItemTouchListener(new RecyclerTouchListener( getApplicationContext(), horizontal_item, new ClickListener() {
        	@Override
        	public void onClick(View view, int position) {
        		 changeFragment(new wants_hot(position+1));
        		Toast.makeText(getApplicationContext(),data[position], Toast.LENGTH_SHORT).show();
        	}
        	@Override
        	public void onLongClick(View view, int position) { }
        }));

	}
	private void changeFragment(Fragment targetFragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.sort_switch, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
}
