package smart.order.client;

import smart.order.client.R;
import smart.order.client.R.id;
import smart.order.client.R.layout;
import smart.order.client.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SmartOrderActivity extends Activity {

	private boolean insideViewActive = true;
	private SmartOrderClient smartOrderClient = null;
	
	public void tableButtonClicked(View v) {
		
		Log.d("  ==> SMART_ORDER_CLIENT <==", "Table Button pressed: " + v.getId());
		
		 AlertDialog.Builder builder = OrderWizard.getOrderWizardMain(this, v.getId());
		 builder.create().show();
        
		switch(v.getId()){
	       case R.id.Tisch1: 
	    	   
	    	   break;
		}
	}
	
	public void orderDrinks(int tableNbr) {
		Log.d("  ==> SMART_ORDER_CLIENT <==", "Order drinks for table: " + tableNbr);
	}
	
	public void orderFood(int tableNbr) {
		Log.d("  ==> SMART_ORDER_CLIENT <==", "Order food for table: " + tableNbr);
	}

	
	
	public void changeInOutsideButtonClicked(View v) {
		final FrameLayout insideFrame = (FrameLayout) findViewById(R.id.inside_frame);
		final FrameLayout outsideFrame = (FrameLayout) findViewById(R.id.outside_frame);
		
		if(insideViewActive) {
			//switch to outside view!
			setVisivilityOfAllElemts(outsideFrame, View.VISIBLE);
			setVisivilityOfAllElemts(insideFrame, View.GONE);
			
			insideViewActive = false;
		} else {
			//switch to inside view!
			setVisivilityOfAllElemts(outsideFrame, View.GONE);
			setVisivilityOfAllElemts(insideFrame, View.VISIBLE);
			insideViewActive = true;
		}
	}
	
	private void setVisivilityOfAllElemts(View view, int visibility) {
		
		view.setVisibility(visibility);	
		
		if(view instanceof ViewGroup) {

			ViewGroup layout = (ViewGroup)view;

			int count = layout.getChildCount();
			for(int i = 0; i < count; i++) {
				setVisivilityOfAllElemts(layout.getChildAt(i), visibility);
			}
		}
	}
	
	
	private void disconnectFromServer() {
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Disconnecting client-server...");	
		smartOrderClient.disconnectClient();
		
		while(smartOrderClient.clientConnected()) {
			android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Waiting for disconnection...");	
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Disconnected! Switching back to inital activity");	
		Intent myIntent = new Intent(this.getApplicationContext(), FullscreenActivity.class);
        startActivityForResult(myIntent, 0);
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
					controlsView.setVisibility(View.INVISIBLE);
					controlElementIsHided = true;
				} else {
					controlsView.setVisibility(View.VISIBLE);
					controlElementIsHided = false;
				}
				
				return true;
			}
		});
		
		controlsView.setVisibility(View.INVISIBLE);
		
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

		final FrameLayout insideFrame = (FrameLayout) findViewById(R.id.inside_frame);
		final FrameLayout outsideFrame = (FrameLayout) findViewById(R.id.outside_frame);
	
		setVisivilityOfAllElemts(insideFrame, View.VISIBLE);
		setVisivilityOfAllElemts(outsideFrame, View.GONE);
		insideViewActive = true;
		
		smartOrderClient = SmartOrderClient.getInstance(null);
		smartOrderClient.setSmartOrderActivity(this);
	
	}
	
	

}
