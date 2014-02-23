package smart.order.client.GUI.OrderActivity.Fragments;

import smart.order.client.R;
import smart.order.client.R.layout;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class OrderFragmentList extends Fragment {
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.orderactivity__order_fragment_list,
	        container, false);
	    
	    return view;
	  }
}  
