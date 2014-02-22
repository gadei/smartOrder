package smart.order.client.GUI.OrderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;
import smart.order.client.order.Menu;
import smart.order.client.order.Order;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity{

	private SmartOrderClient smartOrderClient = null;

	private static final String TAG_FOOD = "Speisen";
	private static final String TAG_DRINK = "Getränke";

	private OrderExpendableListViewAdapter listAdapter = null;

	private Order order = null;
	private int tableId = 0;
	
	private ManageClicks click = new ManageClicks();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.orderactivity__order_activity);

		smartOrderClient  = SmartOrderClient.getInstance();
		smartOrderClient.setOrderActivity(this);

		setTableId();

		createListView();
		setButtons();
	}

	private void createListView()
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

		listAdapter = new OrderExpendableListViewAdapter(this, listDataHeader, listDataChild);

		listView.setAdapter(listAdapter);

		listView.setOnChildClickListener(new OnChildClickListener() 
		{
			@Override
			public boolean onChildClick(ExpandableListView arg0, View v,
					int groupPosition, int childPosition, long id)
			{
				click.addItemToOrder(order, v);

				return false;
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

	private void setButtons()
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

		Button button2 = (Button)findViewById(R.id.buttonSend_OrderFragmentDetail);
		button2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				click.sendOrder(order);
				order = null;
			}
		});

		Button button3 = (Button)findViewById(R.id.cancelOrder_OrderFragmentDetail);
		button3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				order = null;
				click.cancel();
				Toast.makeText(getApplicationContext(), "Bestellung abgebrochen!",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

} 