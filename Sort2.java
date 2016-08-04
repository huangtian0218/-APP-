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
	private String[] data = new String[]{"�����Ƽ�","Ʒ����װ","��������","���õ���","���԰칫",
  			"�ֻ�����","ĸӤƵ��","ͼ��","�ҾӼҷ�","�Ӽ�����","���Ի�ױ","Ьѥ���","�ݳ���Ʒ","�鱦��Ʒ"};
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���ر�����
      	requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.sort);
        
        //�Զ��������
      	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_menu);
      	//��Ʒ����Ĭ����ʾ�����Ƽ�
      	changeFragment(new wants_hot(1));
      	//������һҳ
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
        //�������ȷ��ÿ��item�ĸ߶��ǹ̶��ģ��������ѡ������������
        //horizontal_item.setHasFixedSize(true);
       
        MyAdapter myAdapter = new MyAdapter(data);
        horizontal_item.setAdapter(myAdapter);
        
      //RecyclerView��ӵ���¼�
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
