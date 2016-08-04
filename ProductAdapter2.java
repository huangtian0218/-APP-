package com.electronic_market.home;

import java.util.List;

import com.electronic_market.R;
import com.electronic_market.data.Product;
import com.loopj.android.image.SmartImageView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 产品适配器
 * 
 * @author: wll
 */
public class ProductAdapter2 extends BaseAdapter {
	List<Product> products;
	private Context context;
	//private ImageLoader imageLoader = null;
	//private DisplayImageOptions options = null;

	public ProductAdapter2(Context context, List<Product> products) {
		super();
		this.context = context;
		this.products = products;
		//imageLoader = ImageLoader.getInstance();
		//imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		//options = new DisplayImageOptions.Builder()
		//		.showImageOnFail(R.drawable.ic_launcher)
		//		.displayer(new RoundedBitmapDisplayer(0xff000000, 10))
		//		.cacheInMemory().cacheOnDisc().build();
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products.addAll(products);
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Object getItem(int position) {
		return products.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//RelativeLayout relativeLayout;
		LinearLayout relativeLayout;
		if (convertView != null) {
			relativeLayout = (LinearLayout) convertView;
		} else {
			relativeLayout = (LinearLayout) View.inflate(context,
					R.layout.product_item2, null);
		}
	//	ImageView imageView = (ImageView) relativeLayout
	//			.findViewById(R.id.main_listview_image);
        String url=products.get(position).getImgUrl();
	//	imageLoader.displayImage(url,
	//			imageView, options);
        
        ((SmartImageView) relativeLayout.
                       findViewById(R.id.main_listview_image))
                       .setImageUrl(url);
        
       // imageView.setImageUrl(url);

		((TextView) relativeLayout
				.findViewById(R.id.main_listview_text_accessory_name))
				.setText(products.get(position).getName());
		((TextView) relativeLayout
				.findViewById(R.id.main_listview_text_accessory_describe))
				.setText(products.get(position).getDescription());
		((TextView) relativeLayout
				.findViewById(R.id.main_listview_text_accessory_sale))
				.setText("已售"+products.get(position).getSale()+"件");
		((TextView) relativeLayout
				.findViewById(R.id.main_listview_text_accessory_price))
				.setText("￥"+products.get(position).getPrice());
		return relativeLayout;
	}

}
