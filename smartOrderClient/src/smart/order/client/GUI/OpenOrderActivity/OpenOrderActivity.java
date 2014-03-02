package smart.order.client.GUI.OpenOrderActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import smart.order.client.R;
import smart.order.client.SmartOrderClient;
import smart.order.client.GUI.OrderActivity.OrderActivity;
import smart.order.client.GUI.OrderActivity.Adapter.OrderListViewAdapter;
import smart.order.client.database.UpdateOrderStatus;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;
import smart.order.client.order.Menu;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OpenOrderActivity extends Activity
{
	private SmartOrderClient smartOrderClient;

	private Vector<Menu> menuVector = null;

	int orderId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.openorderactivity__openorder_activity);

		smartOrderClient  = SmartOrderClient.getInstance();
		smartOrderClient.setOpenOrderActivity(this);

		menuVector = new Vector<Menu>();
		menuVector.clear();

		setOrderId();
		createMenuArray();

		createListView();
		createButtonFinishOrder();
		getSum();
	}


	private void createButtonFinishOrder()
	{
		Button button = (Button)findViewById(R.id.topay_button);
		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				new UpdateOrderStatus().execute(String.valueOf(orderId));
			}
		});
	}


	private void createListView()
	{
		ListView list = (ListView)findViewById(R.id.tobepayed_listview);

		OpenOrderListViewAdapter openOrderListAdapter = new OpenOrderListViewAdapter(SmartOrderClient.getInstance().getOrderActivity(), new ArrayList<Menu>(menuVector));

		list.setAdapter(openOrderListAdapter);
	}

	@SuppressWarnings("unchecked")
	private void createMenuArray()
	{
		ArrayList<HashMap<String, Object>> data = SmartOrderClient.getInstance().getOrderActivity().getOpenOrderArrayList();

		for(int i = 0; i < data.size(); i++)
		{
			if(Integer.valueOf((String) data.get(i).get("order_id")) == orderId)
			{
				Vector<Integer> orderFood = (Vector<Integer>) data.get(i).get("order_food");
				Vector<Integer> orderDrink = (Vector<Integer>) data.get(i).get("order_drink");

				Vector<Drink> drink = SmartOrderClient.getInstance().getDrink();
				Vector<Food> food = SmartOrderClient.getInstance().getFood();
				for(int l = 0; l < food.size(); l++)
				{
					for(int j = 0; j < orderFood.size(); j++)
					{
						if(food.elementAt(l).getId() == orderFood.elementAt(j))
						{
							menuVector.add(new Food(food.elementAt(l)));
						}
					}
				}
				for(int l = 0; l < drink.size(); l++)
				{
					for(int j = 0; j < orderDrink.size(); j++)
					{
						if(drink.elementAt(l).getId() == orderDrink.elementAt(j))
						{
							menuVector.add(new Drink(drink.elementAt(l)));
						}
					}
				}
			}
		}

	}

	private void getSum()
	{
		double sum = 0;

		for(int i = 0; i < menuVector.size(); i++)
		{
			sum += menuVector.elementAt(i).getPrice();
		}

		NumberFormat format = new DecimalFormat("0.00");
		TextView sumTxt = (TextView)findViewById(R.id.sum);
		sumTxt.setText(format.format(sum) + " €");
	}

	private void setOrderId()
	{
		Bundle order = getIntent().getExtras();
		if(order != null)
		{
			orderId = order.getInt("order_id");
		}
	}
}
