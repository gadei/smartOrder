package smart.order.client.GUI.OrderActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smart.order.client.R;
import smart.order.client.order.Drink;
import smart.order.client.order.Food;
import smart.order.client.order.Menu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class OrderExpendableListViewAdapter extends BaseExpandableListAdapter
{

	private final Context context;

	private List<?> listDataHeader;
	private HashMap<String, List<Menu>> listDataChild;

	public OrderExpendableListViewAdapter(Context context, List<?> listDataHeader,
			HashMap<String, List<Menu>> listDataChild)
	{
		this.context = context;
		this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
	}

	@Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) 
    {
 
    	Object menu = getChild(groupPosition, childPosition);
 
        if (convertView == null) 
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.customadapter__order_list_view_adapter, null);
        }
		
		TextView name = (TextView)convertView.findViewById(R.id.custom_adapter_order_list_title);
		name.setText(((Menu) menu).getName());
		
		TextView price = (TextView)convertView.findViewById(R.id.custom_adapter_order_list_price);
		price.setText(((Menu) menu).getPrice() + " €");
		
		convertView.setTag(R.id.TAG_ID,((Menu) menu).getId());
		
		if(menu.getClass() == Food.class)
		{
			convertView.setTag(R.id.TAG_MENU, "food");
		}
		if(menu.getClass() == Drink.class)
		{
			convertView.setTag(R.id.TAG_MENU, "drink");
		}
		
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) 
    {
        String headerTitle = (String)getGroup(groupPosition);
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.customadapter__order_list_view_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.custom_adapter_group);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
