package smart.order.client;

import smart.order.client.R;
import smart.order.client.R.id;
import smart.order.client.R.layout;
import smart.order.client.R.string;
import smart.order.client.order.Order;
import smart.order.client.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	
	private static final int MAX_TIME_TO_WAIT_FOR_CONNECTION = 3000;
	
	private void connectToServer() {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
		android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Starting client and connecting to server");	
		smartOrderClient = SmartOrderClient.getInstance(this);
		smartOrderClient.initConnection();	
		
		int timeToConnection = 0;
		int cycleTime = 250;
		
		while(!smartOrderClient.clientConnected() && timeToConnection < MAX_TIME_TO_WAIT_FOR_CONNECTION) {
			try {
				Thread.sleep(cycleTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			timeToConnection += cycleTime;
		}
		
		if(smartOrderClient.clientConnected()) {
		
			android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "SUCCESS - Client started and connected to server");	
			
			Intent myIntent = new Intent(this.getApplicationContext(), SmartOrderActivity.class);
	        startActivityForResult(myIntent, 0);
	        this.finish();
	        
		} else {
			
			android.util.Log.e("  ==> SMART_ORDER_CLIENT <==", "ERROR - Failed to connect to the server");
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(R.string.connection_problem_dialog)
	               .setPositiveButton(R.string.connection_problem_dialog_try_again, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "User: Try Again");
	                	   connectToServer();
	                   }
	               })
	               .setNegativeButton(R.string.connection_problem_dialog_cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "User: Cancel");
	                   }
	               });
	        // Create the AlertDialog object and return it
	        builder.create().show();
	        
	        //Close TCP Client after unsucessfull attempt to connect
	        smartOrderClient.disconnectClient();
	        smartOrderClient.tcpClientClosed();
		}
		
	}

	
	public void peep() {
        
        try {
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    Ringtone r = RingtoneManager.getRingtone(this.getApplicationContext(), notification);
		    r.play();
		} catch (Exception e) {}
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
	
	
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	
	private SmartOrderClient smartOrderClient = null;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "display pressed");	
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.connectClientButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				android.util.Log.d("  ==> SMART_ORDER_CLIENT <==", "Connect button pressed");	
				connectToServer();
			}
		});
		
		Order order = new Order(1);
		order.toJson();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
