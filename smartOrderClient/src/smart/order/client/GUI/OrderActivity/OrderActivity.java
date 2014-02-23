package smart.order.client.GUI.OrderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.database.GetOpenOrders;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;
import smart.order.client.order.Menu;
import smart.order.client.order.Order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity
{
	private SmartOrderClient smartOrderClient = null;

	private static final String TAG_FOOD = "Speisen";
	private static final String TAG_DRINK = "Getr�nke";

	private ArrayList<HashMap<String, String>> openOrderArrayList = null;

	private Order order = null;
	private int tableId = 0;

	private ArrayAdapter<String> orderListAdapter;
	private ArrayList<String> orderListData = new ArrayList<String>();

	private ArrayAdapter<String> openOrderListAdapter;
	private ArrayList<String> openOrderListData = new ArrayList<String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.orderactivity__order_activity);

		smartOrderClient  = SmartOrderClient.getInstance();
		smartOrderClient.setOrderActivity(this);

		setTableId();

		new GetOpenOrders().execute(tableId);
		
		createListViewMenu();
		createListViewOrder();
		createListViewOpenOrder();

		setButtonNewOrder();
		setButtonCancelOrder();
		setButtonFinishOrder();
	}

	private void createListViewMenu()
	{
		ExpandableListView listView = (ExpandableListView)findViewById(R.id.expendable_OrderFragmentList);

		ArrayList<String> listDataHeader = new ArrayList<String>();
		HashMap<String, List<Menu>> listDataChild = new HashMap<String, List<Menu>>();

		listDataHeader.add(TAG_FOOD);
		listDataHeader.add(TAG_DRINK);

		List<Menu> food = new ArrayList<Menu>(SmartOrderClient.getInstance().getFood());
		List<Menu> drink = new ArrayList<Menu>(SmartOrderClient.getInstance().getDrink());

		listDataChild.put(listDataHeader.get(0), food);
		listDataChild.put(listDataHeader.get(1), drink);

		OrderExpendableListViewAdapter listAdapter = new OrderExpendableListViewAdapter(this, listDataHeader, listDataChild);

		listView.setAdapter(listAdapter);

		listView.setOnChildClickListener(new OnChildClickListener() 
		{
			@Override
			public boolean onChildClick(ExpandableListView arg0, View view,
					int groupPosition, int childPosition, long id)
			{
				if(order != null)
				{
					Menu menuItem = order.addClickedItemToOrder(view);
					orderListData.add(menuItem.getName());
					orderListAdapter.notifyDataSetChanged();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Zuerst neue Bestellung erstellen!",
							Toast.LENGTH_SHORT).show();
				}

				return false;
			}
		});
	}

	private void createListViewOrder()
	{
		OrderActivity activity = SmartOrderClient.getInstance().getOrderActivity();
		ListView list = (ListView)activity.findViewById(R.id.list2_OrderFragmentDetail);

		orderListAdapter = new ArrayAdapter<String>(SmartOrderClient.getInstance().getOrderActivity(),
				android.R.layout.simple_list_item_1, orderListData);

		list.setAdapter(orderListAdapter);
	}

	private void createListViewOpenOrder()
	{
		OrderActivity activity = SmartOrderClient.getInstance().getOrderActivity();
		ListView list = (ListView)activity.findViewById(R.id.orderFragmentDetail_listView2);

		openOrderListAdapter = new ArrayAdapter<String>(SmartOrderClient.getInstance().getOrderActivity(),
				android.R.layout.simple_list_item_1, openOrderListData);

		list.setAdapter(openOrderListAdapter);
	}

	private void setButtonNewOrder()
	{
		Button button1 = (Button)findViewById(R.id.newOrder_OrderFragmentDetail);
		button1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(order == null)
				{
					order = new Order(tableId);
					Toast.makeText(getApplicationContext(), "Neue Bestellung erstellt. Jetzt Speisen/Getr�nke hinzuf�gen!",
							Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Aktuelle Bestellung zuerst abschlie�en.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	private void setButtonFinishOrder()
	{
		Button button2 = (Button)findViewById(R.id.buttonSend_OrderFragmentDetail);
		button2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(order != null)
				{
					order.sendOrder();
					order = null;
					orderListData.clear();
					orderListAdapter.notifyDataSetChanged();

					new GetOpenOrders().execute(tableId);	
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Zuerst neue Bestellung erstellen!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void setButtonCancelOrder()
	{
		Button button3 = (Button)findViewById(R.id.cancelOrder_OrderFragmentDetail);
		button3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(order != null)
				{
					order = null;
					orderListData.clear();
					orderListAdapter.notifyDataSetChanged();

					Toast.makeText(getApplicationContext(), "Bestellung abgebrochen!",
							Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Es wurde noch keine Bestellung erstellt",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void setTableId()
	{
		Bundle table = getIntent().getExtras();
		if(table != null)
		{
			tableId = table.getInt("table");
		}

		TextView text = (TextView) findViewById(R.id.table_OrderFragmentDetail);
		text.setText("Tisch " + tableId);
	}

	public void setOpenOrderList(ArrayList<HashMap<String, String>> openOrderList)
	{
		this.openOrderArrayList = openOrderList;
	}
	
	public void updateOpenOrderList()
	{
		openOrderListData.clear();
		for(int i = 0; i < openOrderArrayList.size(); i++)
		{
			openOrderListData.add("Bestellungs ID: " + openOrderArrayList.get(i).get("order_id"));
		}
		openOrderListAdapter.notifyDataSetChanged();
	}
} 