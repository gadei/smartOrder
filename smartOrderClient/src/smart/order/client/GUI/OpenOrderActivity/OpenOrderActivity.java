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

	private void createMenuArray()
	{
		ArrayList<HashMap<String, String>> data = SmartOrderClient.getInstance().getOrderActivity().getOpenOrderArrayList();

		for(int i = 0; i < data.size(); i++)
		{
			if(Integer.valueOf(data.get(i).get("order_id")) == orderId)
			{
				Vector<Drink> drink = SmartOrderClient.getInstance().getDrink();
				if(data.get(i).get("drink").length() > 0)
				{
					String[] drinkStringArray = data.get(i).get("drink").split(",");

					for(int j = 0; j < drinkStringArray.length ; j++)
					{
						for(int l = 0; l < drink.size(); l++)
						{
							if(drink.elementAt(l).getId() == Integer.valueOf(drinkStringArray[j]))
							{
								menuVector.add(new Drink(drink.elementAt(l)));
							}
						}
					}
				}

				Vector<Food> food = SmartOrderClient.getInstance().getFood();
				if(data.get(i).get("food").length() > 0)
				{
					String[] foodStringArray = data.get(i).get("food").split(",");

					for(int j = 0; j < foodStringArray.length ; j++)
					{
						for(int l = 0; l < food.size(); l++)
						{
							if(food.elementAt(l).getId() == Integer.valueOf(foodStringArray[j]))
							{
								menuVector.add(new Food(food.elementAt(l)));
							}
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
