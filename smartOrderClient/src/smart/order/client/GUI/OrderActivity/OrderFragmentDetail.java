package smart.order.client.GUI.OrderActivity;

import smart.order.client.R;
import smart.order.client.R.layout;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OrderFragmentDetail extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.order_fragment_detail,
        container, false);
    
    return view;
  }
} 