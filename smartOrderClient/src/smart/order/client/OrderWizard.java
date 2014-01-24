package smart.order.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class OrderWizard {

	
	public static AlertDialog.Builder getOrderWizardMain(final SmartOrderActivity activity, final int tableNbr) {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(R.string.order_wizard_main)
        .setItems(R.array.order_wizard_array_main, new DialogInterface.OnClickListener() {
        	
            public void onClick(DialogInterface dialog, int which) {
            //TODO: Make this more dynamic
            	switch(which) {
            	case 0: 
            		activity.orderDrinks(tableNbr);
            		break;
            	case 1:
            		activity.orderFood(tableNbr);
            	
            	}
            }
        });
		
	    return builder;
	}
    
}
