package com.electronic_market;
import com.electronic_market.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class recharge extends Activity implements OnClickListener {
	
	protected static final int SEND_SMS_TYPE = 0;
	private ImageButton recharge_10;
	private ImageButton recharge_20;
	private ImageButton recharge_30;
	private ImageButton recharge_50;
	private ImageButton recharge_100;
	private ImageButton recharge_200;
	private ImageButton paynow;
	//联系人按钮
	private ImageButton contact_person;
	private EditText edit_phone;
	private TextView back;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recharge);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.recharge_topmenu);
	
		recharge_10=(ImageButton) findViewById(R.id.recharge_10);
		recharge_10.setOnClickListener(this);
		recharge_20=(ImageButton) findViewById(R.id.recharge_20);
		recharge_20.setOnClickListener(this);
		recharge_30=(ImageButton) findViewById(R.id.recharge_30);
		recharge_30.setOnClickListener(this);
		recharge_50=(ImageButton) findViewById(R.id.recharge_50);
		recharge_50.setOnClickListener(this);
		recharge_100=(ImageButton) findViewById(R.id.recharge_100);
		recharge_100.setOnClickListener(this);
		recharge_200=(ImageButton) findViewById(R.id.recharge_200);
		recharge_200.setOnClickListener(this);
		back=(TextView)findViewById(R.id.getback);
		back.setOnClickListener(this);
		paynow=(ImageButton) findViewById(R.id.paynow);
		paynow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "支付成功！", 0).show();
				finish();
			}
		});
		
		edit_phone=(EditText) findViewById(R.id.edit_phone);
	
		//获取手机联系人
		contact_person=(ImageButton) findViewById(R.id.contact_person);
		contact_person.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
				recharge.this.startActivityForResult(intent1, 1);
				
			}
		});
	}
	

	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		recharge_10.setBackgroundResource(R.drawable.recharge_10);
		recharge_20.setBackgroundResource(R.drawable.recharge_20);
		recharge_30.setBackgroundResource(R.drawable.recharge_30);
		recharge_50.setBackgroundResource(R.drawable.recharge_50);
		recharge_100.setBackgroundResource(R.drawable.recharge_100);
		recharge_200.setBackgroundResource(R.drawable.recharge_200);
		paynow.setBackgroundResource(R.drawable.pay);
		
		switch (v.getId()) {
		case R.id.recharge_10:			
			recharge_10.setBackgroundResource(R.drawable.recharge_10_focus);			
			break;
		case R.id.recharge_20:
			recharge_20.setBackgroundResource(R.drawable.recharge_20_focus);
			break;
		case R.id.recharge_30:
			recharge_30.setBackgroundResource(R.drawable.recharge_30_focus);
			break;
		case R.id.recharge_50:
			recharge_50.setBackgroundResource(R.drawable.recharge_50_focus);
			break;
		case R.id.recharge_100:
			recharge_100.setBackgroundResource(R.drawable.recharge_100_focus);
			break;
		case R.id.recharge_200:
			recharge_200.setBackgroundResource(R.drawable.recharge_200_focus);
			break;
		case R.id.getback:
			finish();
			break;
		case R.id.contact_person:
			Intent intent = new Intent(Intent.ACTION_PICK,
			ContactsContract.Contacts.CONTENT_URI);
			recharge.this.startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
		
	}
	
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if(resultCode==RESULT_OK){
				Uri contactData=data.getData();
				Cursor cursor=managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				String num=this.getContactPhone(cursor);
				edit_phone.setText(num);//将选择的联系人的手机号显示在EditText中
				
			}
			break;

		default:
			break;
		}
	}
	
	//获取联系人的手机号

    private String getContactPhone(Cursor cursor) {
        // TODO Auto-generated method stub
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
                    /*switch (phone_type) {
                    case 2:
                    	result = phoneNumber;
                    	break;
                    default:
                    	break;
                    }*/
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result;
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	
}
