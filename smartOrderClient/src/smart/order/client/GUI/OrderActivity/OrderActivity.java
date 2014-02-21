package smart.order.client.GUI.OrderActivity;

import java.util.ArrayList;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.database.MenuItems;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OrderActivity extends Activity{

	private SmartOrderClient smartOrderClient = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.orderactivity__order_activity);
		
		smartOrderClient  = SmartOrderClient.getInstance();
		smartOrderClient.setOrderActivity(this);

		setTableId();

		createListViewFood();
		createListViewDrink();
	}

	private void createListViewFood()
	{
		ListView listView = (ListView) findViewById(R.id.listFood_OrderFragmentList);
		
		OrderListViewAdapter<Food> orderListViewAdapter = new OrderListViewAdapter<Food>(this, new ArrayList<Food>(MenuItems.foodItemsVector));
		listView.setAdapter(orderListViewAdapter);		
	}
	private void createListViewDrink()
	{
		ListView listView = (ListView) findViewById(R.id.listDrink_OrderFragmentList);
		
		OrderListViewAdapter<Drink> orderListViewAdapter = new OrderListViewAdapter<Drink>(this, new ArrayList<Drink>(MenuItems.drinkItemsVector));
		listView.setAdapter(orderListViewAdapter);
	}

	private void setTableId()
	{
		int tableId = 0;
		Bundle table = getIntent().getExtras();
		if(table != null)
		{
			tableId = table.getInt("table");
		}


		TextView text = (TextView) findViewById(R.id.table_OrderFragmentDetail);
		text.setText("Tisch " + tableId);
	}

} 