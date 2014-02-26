package smart.order.client.GUI.OrderActivity.Adapter;

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

public class OrderListViewAdapter extends ArrayAdapter<Menu>
{
	private final Context context;
	private final ArrayList<Menu> values;
	private Order order = null;
	
	private int id = 0;

	public OrderListViewAdapter(Context context, ArrayList<Menu> arrayList) 
	{
		super(context, R.layout.customadapter__order_adapter, arrayList);
		this.context = context;
		this.values = arrayList;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		Menu menu = values.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.customadapter__order_adapter, parent, false);
		
		
		TextView name = (TextView)rowView.findViewById(R.id.actualOrderName);
		name.setText(((Menu) menu).getName());
		
		TextView price = (TextView)rowView.findViewById(R.id.actualOrderPrice);
		price.setText(((Menu) menu).getPrice() + " €");
		
		Button minus = (Button)rowView.findViewById(R.id.minus);
		minus.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int id = SmartOrderClient.getInstance().getOrderActivity().getOrderListData().get(position).getId();
				
				if(values.get(position).getClass() == Food.class)
				{
					SmartOrderClient.getInstance().getOrderActivity().getOrder().removeFood(id);
					SmartOrderClient.getInstance().getOrderActivity().getOrderListData().remove(position);
					OrderListViewAdapter.this.notifyDataSetChanged();
					SmartOrderClient.getInstance().getOrderActivity().setTxtSum();
				}
				else if(values.get(position).getClass() == Drink.class)
				{
					SmartOrderClient.getInstance().getOrderActivity().getOrder().removeDrink(id);
					SmartOrderClient.getInstance().getOrderActivity().getOrderListData().remove(position);
					OrderListViewAdapter.this.notifyDataSetChanged();
					SmartOrderClient.getInstance().getOrderActivity().setTxtSum();
				}
			}
		});

		id = ((Menu) menu).getId();
		
		return rowView;
	}
	
	public int getId()
	{
		return this.id;
	}
}
