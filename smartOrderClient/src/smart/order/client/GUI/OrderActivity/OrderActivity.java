package smart.order.client.GUI.OrderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.order.Menu;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class OrderActivity extends Activity{

	private SmartOrderClient smartOrderClient = null;
	
	private static final String TAG_FOOD = "Speisen";
	private static final String TAG_DRINK = "Getränke";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.orderactivity__order_activity);
		
		smartOrderClient  = SmartOrderClient.getInstance();
		smartOrderClient.setOrderActivity(this);

		setTableId();

		createListView();
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
		
		OrderExpendableListViewAdapter listAdapter = new OrderExpendableListViewAdapter(this, listDataHeader, listDataChild);
		
		listView.setAdapter(listAdapter);
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