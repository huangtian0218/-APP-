package com.electronic_market;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.electronic_market.Acache.ACache;
import com.electronic_market.dialog.ActionSheetDialog;
import com.electronic_market.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.electronic_market.dialog.ActionSheetDialog.SheetItemColor;
import com.electronic_market.loginreg.Login;
import com.electronic_market.orderInfo.AllOrder;
import com.electronic_market.orderInfo.OrderInfo;
import com.electronic_market.ormlite.DBHelper;
import com.electronic_market.ormlite.User;
import com.electronic_market.setting.Setting;
import com.j256.ormlite.dao.Dao;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class MeInfo extends Fragment implements OnClickListener{
	private View view;
	private Button setting,out;
	private ImageButton unsend;
	private TextView myname;
	private ImageView login;
	private RelativeLayout allOrdered;
	private String name="";
	private ACache instanceInfo;
	private Dao<User, Integer> userDao;
	private User user;
	private DBHelper dbHelper;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.me, container, false);
		InitView();
		addOnClickListener();
		instanceInfo = ACache.get(getActivity());
		name = instanceInfo.getAsString("uname");
		if(name.equals("马上登录"))
		{
			myname.setText(name);
			setting.setVisibility(View.GONE);
			out.setVisibility(View.GONE);
			login.setClickable(true);
		}
		else
		{
			myname.setText(name);
			InitImage();
			setting.setVisibility(View.VISIBLE);
			out.setVisibility(View.VISIBLE);
			login.setClickable(false);
		}
		return view;
	}
	private void InitView() {
		setting=(Button) view.findViewById(R.id.setting);
		myname=(TextView)view.findViewById(R.id.name);
		login=(ImageView) view.findViewById(R.id.login_logo);
		unsend=(ImageButton)view.findViewById(R.id.unsend);
		allOrdered=(RelativeLayout)view.findViewById(R.id.ordered);
		out=(Button)view.findViewById(R.id.out);
		user=new User();
		dbHelper=new DBHelper(getActivity());
	}
	private void addOnClickListener() {
		setting.setOnClickListener(this);
		login.setOnClickListener(this);
		out.setOnClickListener(this);
		unsend.setOnClickListener(this);
		allOrdered.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			Intent intent=new Intent(getActivity(), Setting.class);
			startActivityForResult(intent,2);
			break;
		case R.id.login_logo:
			Intent intent2=new Intent(getActivity(),Login.class);
			startActivityForResult(intent2, 1);
			break;
		case R.id.out:
			new ActionSheetDialog(getActivity())
			.builder()
			.setCancelable(false)
			.setCanceledOnTouchOutside(false)
			.addSheetItem("退出当前程序", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							getActivity().finish();
						}
					})
			.addSheetItem("切换帐号", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							Intent intent3=new Intent(getActivity(),Login.class);
							startActivityForResult(intent3, 1);
							myname.setText("马上登录");
							login.setImageResource(R.drawable.my_info);
							out.setVisibility(View.GONE);
							setting.setVisibility(View.GONE);
							login.setClickable(true);
							instanceInfo.put("uname","马上登录", 3 * ACache.TIME_DAY);
						}
					})
			.show();
			break;
		case R.id.unsend:
			Intent intent3=new Intent(getActivity(),OrderInfo.class);
			startActivityForResult(intent3, 1);
			break;
		case R.id.ordered:
			Intent intent4=new Intent(getActivity(),AllOrder.class);
			startActivityForResult(intent4, 1);
		default:
			break;
		}	
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if(resultCode==-1)
			{
				name = instanceInfo.getAsString("uname");
				myname.setText(name);
				out.setVisibility(View.VISIBLE);
				login.setClickable(false);
				setting.setVisibility(View.VISIBLE);
				InitImage();
				MainActivity.mViewPager.setCurrentItem(0, false);
				MainActivity.mViewPager.setCurrentItem(3, false);
			}
			if(resultCode==0)
			{
				MainActivity.mViewPager.setCurrentItem(0, false);
				MainActivity.mViewPager.setCurrentItem(3, false);
			}
			break;
		case 2:
			if(resultCode==-1)
			{
				InitImage();
			}
		default:
			break;
		}
	}
	private void InitImage() {
		try {
			userDao= dbHelper.getUserDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<User> list = null;
		try {
			list = userDao.queryForEq("uname", myname.getText().toString());
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(list!=null && list.size()!=0)
			user=list.get(0);
		if(user.getPhoto()!=null && user.getPhoto()!="")
		{
			String path=user.getPhoto();
			try {
				FileInputStream fis = new FileInputStream(path);
				Bitmap image =BitmapFactory.decodeStream(fis);
				login.setImageBitmap(image);
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
}
