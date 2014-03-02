package smart.order.client.GUI.OrderActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.GUI.OpenOrderActivity.OpenOrderActivity;
import smart.order.client.GUI.OrderActivity.Adapter.OrderExpendableListViewAdapter;
import smart.order.client.GUI.OrderActivity.Adapter.OrderListViewAdapter;
import smart.order.client.database.GetOpenOrders;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;
import smart.order.client.order.Menu;
import smart.order.client.order.Order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	private static final String TAG_DRINK = "Getränke";

	private ArrayList<HashMap<String, Object>> openOrderArrayList = null;

	private Order order = null;
	private int tableId = 0;

	private OrderListViewAdapter orderListAdapter;
	private ArrayList<Menu> orderListData = new ArrayList<Menu>();

	private ArrayAdapter<String> openOrderListAdapter;
	private ArrayList<String> openOrderListData = new ArrayList<String>();

	private TextView sum = null;

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

		setTxtSum();
	}

	public void setTxtSum()
	{
		if(sum == null)
		{
			sum = (TextView)findViewById(R.id.sum_orderactivity);
		}

		if(order != null)
		{
			NumberFormat format = new DecimalFormat("0.00");
			sum.setText(format.format(order.getSum()) + " €");
		}
		else
		{
			sum.setText("0.00 €");
		}
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
					orderListData.add(menuItem);
					orderListAdapter.notifyDataSetChanged();
					setTxtSum();
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

		orderListAdapter = new OrderListViewAdapter(SmartOrderClient.getInstance().getOrderActivity(), orderListData);

		list.setAdapter(orderListAdapter);
	}

	private void createListViewOpenOrder()
	{
		OrderActivity activity = SmartOrderClient.getInstance().getOrderActivity();
		final ListView list = (ListView)activity.findViewById(R.id.orderFragmentDetail_listView2);

		openOrderListAdapter = new ArrayAdapter<String>(SmartOrderClient.getInstance().getOrderActivity(),
				android.R.layout.simple_list_item_1, openOrderListData);

		list.setAdapter(openOrderListAdapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int pos, long arg3)
			{
				String selectedFromList = (list.getItemAtPosition(pos).toString());
				String[] parts = selectedFromList.split(" ");

				for(int i = 0; i < openOrderArrayList.size(); i++)
				{
					if(Integer.valueOf((String) openOrderArrayList.get(i).get("order_id")) == Integer.valueOf(parts[2]))
					{
						Intent intent= new Intent(OrderActivity.this, OpenOrderActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						intent.putExtra("order_id", Integer.valueOf(parts[2]));
						startActivity(intent);
						return;
					}
				}

			}
		});
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
					Toast.makeText(getApplicationContext(), "Neue Bestellung erstellt. Jetzt Speisen/Getränke hinzufügen!",
							Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Aktuelle Bestellung zuerst abschließen.",
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
					if(!order.getDrinkItems().isEmpty() || !order.getFoodItems().isEmpty())
					{
						order.sendOrder();
						order = null;
						orderListData.clear();
						orderListAdapter.notifyDataSetChanged();
						setTxtSum();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Es wurde noch nichts bestellt!",
								Toast.LENGTH_SHORT).show();
					}
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
					setTxtSum();

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
			tableId = Integer.valueOf(table.getString("table"));
		}

		TextView text = (TextView) findViewById(R.id.table_OrderFragmentDetail);
		text.setText("Tisch " + tableId);
	}

	public void setOpenOrderList(ArrayList<HashMap<String, Object>> openOrderList)
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

	public ArrayList<Menu> getOrderListData()
	{
		return orderListData;
	}
	public Order getOrder()
	{
		return order;
	}

	public int getTableId()
	{
		return tableId;
	}

	public ArrayList<HashMap<String, Object>> getOpenOrderArrayList()
	{
		return openOrderArrayList;
	}


} 