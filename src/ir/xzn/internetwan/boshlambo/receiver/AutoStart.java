package ir.xzn.internetwan.boshlambo.receiver;

import ir.xzn.internetwan.boshlambo.ServiceTranslate;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		Intent intent=new Intent(arg0, ServiceTranslate.class);
		arg0.startService(intent);
		Log.i("AutoStart", "Started");
		
	}

}
