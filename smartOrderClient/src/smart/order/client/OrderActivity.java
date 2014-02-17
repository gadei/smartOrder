package smart.order.client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class OrderActivity extends Activity{
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_activity);
        
        TextView text = (TextView) findViewById(R.id.textView1);
        text.setText("Tisch " + getTableId());
    }
    
    private int getTableId()
    {
    	Bundle table = getIntent().getExtras();
        if(table != null)
        {
        	return table.getInt("table");
        }
        return 0;
    }
  
} 