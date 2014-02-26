package smart.order.client.GUI.OpenOrderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;
import smart.order.client.order.Menu;
import smart.order.client.order.Order;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class OpenOrderListViewAdapter extends ArrayAdapter<Menu>
{
	private final Context context;
	private final ArrayList<Menu> values;
	private Order order = null;
	
	private int id = 0;

	public OpenOrderListViewAdapter(Context context, ArrayList<Menu> arrayList) 
	{
		super(context, R.layout.customadapter__openorder_adapter, arrayList);
		this.context = context;
		this.values = arrayList;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		Menu menu = values.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.customadapter__openorder_adapter, parent, false);
		
		
		TextView name = (TextView)rowView.findViewById(R.id.openorder_list_title);
		name.setText(((Menu) menu).getName());
		
		TextView price = (TextView)rowView.findViewById(R.id.openorder_list_price);
		price.setText(((Menu) menu).getPrice() + " €");

		id = ((Menu) menu).getId();
		
		return rowView;
	}
	
	public int getId()
	{
		return this.id;
	}
}
