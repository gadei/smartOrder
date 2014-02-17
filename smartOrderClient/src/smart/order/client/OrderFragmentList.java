package smart.order.client;

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

public class OrderFragmentList extends ListFragment {

	private BuildMenu buildMenu = BuildMenu.getInstance(null);
	
	String[] food = buildMenu.getFoodItemsStringArray();
	String[] drink = buildMenu.getFoodItemsStringArray();
	
	private LayoutInflater inflater;
	
	@Override  
	public void onListItemClick(ListView l, View v, int position, long id) {  
		//TODO
	}  

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
			Bundle savedInstanceState) {  
		this.inflater = inflater;
		ArrayAdapter<String> adapterFood = new ArrayAdapter<String>(  
				inflater.getContext(), android.R.layout.simple_list_item_1,  
				food);
		setListAdapter(adapterFood);
		return super.onCreateView(inflater, container, savedInstanceState);  
	}  
}  
