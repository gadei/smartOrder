package smart.order.client;

import smart.order.client.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SmartOrderActivity extends Activity {


	private static int VISIBLE = 0;
	private static int INVISIBLE = 1;
	
	public void tableButtonClicked(View v) {
		
		Log.d("  ==> SMART_ORDER_CLIENT <==", "Table Button pressed: " + v.getId());
		
		switch(v.getId()){
	       case R.id.Tisch1: 
	    	   
	    	   break;
		}
	}

	
	
	private void disconnectFromServer() {
		
	}
	
	
	
	
	
	/***
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ON CREATE STUFF!!!!!!!!!!!!!!!!!!!!!!
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ===========================================================================================================
	 * ===========================================================================================================
	 */
	
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static boolean controlElementIsHided = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.smart_order_activity);

		final View controlsView = findViewById(R.id.fullscreen_content_controls_smartOrder);
		final View contentView = findViewById(R.id.fullscreen_content_smartOrder);

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "display pressed");	
				
				
				if (controlElementIsHided) {
					controlsView.setVisibility(INVISIBLE);
					controlElementIsHided = true;
				} else {
					controlsView.setVisibility(VISIBLE);
					controlElementIsHided = false;
				}
				
				return true;
			}
		});
		
		controlsView.setVisibility(INVISIBLE);
		
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.disconnectClientButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Disconnect button pressed");	
				disconnectFromServer();
			}
		});
	}
	
	

}
