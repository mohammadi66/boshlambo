package ir.xzn.internetwan.boshlambo;

import wei.mark.standout.StandOutWindow;

import android.annotation.SuppressLint;

import android.app.Service;
import android.content.ClipboardManager;

import android.content.ClipboardManager.OnPrimaryClipChangedListener;

import android.content.Intent;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import android.widget.Toast;

public class ServiceTranslate extends Service {
	public static final int DATA_CHANGED_TEXT = 0;
	public static final String TAG="ServiceTranslate";
	ServiceTranslate self = this;
	CharSequence text;
	String stest;
	int save;
	Handler hlr;
	private database db;
	String test;

	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		
		Log.d(TAG, "in onCreate");
		super.onCreate();
		db=new database(this);
		
		final ClipboardManager clipboardService = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	    //final NotificationManager notificationService = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		clipboardService.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {
			
			@Override
			public void onPrimaryClipChanged() {
			
				Log.d("Servicetranslate", "onPrimaryClipChanged");
				
			 text=clipboardService.getPrimaryClip().getItemAt(0).coerceToText(self);
			 
			     Log.d(TAG , "the catched text is"+text);
			     
			     stest=new String();
					stest=text.toString();
					
					  
					refresher("dictionary", "word", stest,"mean");
			 
				}

			
		});
	}
		
	private void refresher(String table,String field, String word,String field1){
		
		db.open();
		
			int save = db.shomaresh_field( table,field, word, field1);
			
			if(save==0){
				//StandOutWindow.closeAll(getApplicationContext(), IntelliDic.class);
				
				Toast.makeText(getApplicationContext(),"دیکشنری بوشلامبو:"+"\n"+"ترجمه کلمه یافت نشد !", Toast.LENGTH_SHORT).show();
			}else{
				StandOutWindow.show(getApplicationContext(), IntelliDic.class,
						StandOutWindow.DEFAULT_ID);
			}
			
		db.close();
		}
	
	/*private  void showtoast() {
		
		Toast.makeText(this, "every one minute this toast will show", Toast.LENGTH_LONG).show();
	}*/
	
	@SuppressWarnings("deprecation")
	@Override
	
	public void onStart(Intent intent, int startId) {
		
		super.onStart(intent, startId);
		Log.d(TAG, "in onStart");
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	
}
