package smart.order.client.GUI.OrderActivity;

import java.util.ArrayList;
import java.util.Vector;

import smart.order.client.R;
import smart.order.client.database.MenuItems;
import smart.order.client.order.Food;
import smart.order.client.order.Menu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderListViewAdapter<T> extends ArrayAdapter<T>
{
	private final Context context;
	private final ArrayList<T> values;
	
	private int id = 0;

	public OrderListViewAdapter(Context context, ArrayList<T> arrayList) 
	{
		super(context, R.layout.customadapter__order_list_view_adapter, arrayList);
		this.context = context;
		this.values = arrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		T menu = values.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.customadapter__order_list_view_adapter, parent, false);
		
		
		TextView name = (TextView)rowView.findViewById(R.id.custom_adapter_order_list_title);
		name.setText(((Menu) menu).getName());
		
		TextView price = (TextView)rowView.findViewById(R.id.custom_adapter_order_list_price);
		price.setText(((Menu) menu).getPrice() + " €");

		id = ((Menu) menu).getId();
		
		return rowView;
	}
	
	public int getId()
	{
		return this.id;
	}
}
