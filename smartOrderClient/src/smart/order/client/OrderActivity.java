package smart.order.client;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OrderActivity extends Activity{
	
	BuildMenu buildMenu = BuildMenu.getInstance(null);
	String[] food = buildMenu.getFoodItemsStringArray();
	String[] drink = buildMenu.getDrinkItemsStringArray();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_activity);
       
        setTableId();
        
        createListViewFood();
        createListViewDrink();	
    }
    
    private void createListViewFood()
    {
    	ListView listView = (ListView) findViewById(R.id.listView1);
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < food.length; ++i) 
        {
          list.add(food[i]);
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
    }
    private void createListViewDrink()
    {
    	ListView listView = (ListView) findViewById(R.id.listView2);
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < drink.length; ++i) 
        {
          list.add(drink[i]);
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
    }
    
    private void setTableId()
    {
    	int tableId = 0;
    	Bundle table = getIntent().getExtras();
        if(table != null)
        {
        	tableId = table.getInt("table");
        }
        
        
        TextView text = (TextView) findViewById(R.id.textView1);
        text.setText("Tisch " + tableId);
    }
  
} 