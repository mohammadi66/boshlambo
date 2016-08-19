package ir.xzn.internetwan.boshlambo;

import java.util.Timer;
import java.util.TimerTask;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;

import android.content.Intent;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import android.widget.ListView;
import android.widget.TextView;

public class IntelliDic extends StandOutWindow {
	IntelliDic self=this;
	
	
	public static final String TAG="IntelliDic";
	private database db;
	MainActivity ma;
	CharSequence text;
	
	TextView tvword;
	ListView lv;
	ListView lv_fa2ar;
	String stest;
	
	private String[] Name;
	private String[] fa;
	private String[] ar;
	private String[] star;
		
	CharSequence text1;			
	
		@SuppressLint("NewApi")
		@Override
		public void onCreate() {
			
			super.onCreate();
			Log.d(TAG, "************ start onCreate **********************");
			db=new database(this);
			
		}
		
	@Override
	public void createAndAttachView(final int id, FrameLayout frame) {
		Log.d(TAG, "**************** createAndAttachView *************");
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.idic, frame, true);	
		
		tvword=(TextView) view.findViewById(R.id.intellidic_tv);
		
		lv=(ListView)view.findViewById(R.id.intellidic_Lv);
		lv_fa2ar=(ListView)view.findViewById(R.id.intellidic_Lv_fa2ar);
		
	
		//String s=String.valueOf(text);
		final ClipboardManager clipboardService = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		
		 text=clipboardService.getPrimaryClip().getItemAt(0).coerceToText(self);
		Log.d(TAG, text+"");
		stest=new String();
		stest=text.toString();
		
		
		  Log.d(TAG, "createAndAttachView the word copied is "+ stest);
		  
		refresher("dictionary", "word", stest, "mean");
		
		lv.setAdapter(new AA());
		
		refresher_fa2ar("fa2ar", "onvan", stest, "matn");
		
		lv_fa2ar.setAdapter(new AA_fa2ar());
		
		 ListUtils.setDynamicHeight(lv);
	        ListUtils.setDynamicHeight(lv_fa2ar);
		
		Timer t=new Timer();
		TimerTask tt=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				StandOutWindow.closeAll(IntelliDic.this, IntelliDic.class);
			}
		};
		
		t.schedule(tt, 10000);
		
	
	}//End createAndAttachView
		
	 public static class ListUtils {
	        public static void setDynamicHeight(ListView mListView) {
	            ListAdapter mListAdapter = mListView.getAdapter();
	            if (mListAdapter == null) {
	                // when adapter is null
	                return;
	            }
	            int height = 0;
	            int desiredWidth = MeasureSpec.makeMeasureSpec(mListView.getWidth(), MeasureSpec.UNSPECIFIED);
	            for (int i = 0; i < mListAdapter.getCount(); i++) {
	                View listItem = mListAdapter.getView(i, null, mListView);
	                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	                height += listItem.getMeasuredHeight();
	            }
	            ViewGroup.LayoutParams params = mListView.getLayoutParams();
	            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
	            mListView.setLayoutParams(params);
	            mListView.requestLayout();
	        }
	    }
	
 class AA extends ArrayAdapter<String>{
	
		public AA(){
			super(IntelliDic.this,R.layout.row_intellidic,Name);			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View row = in.inflate(R.layout.row_intellidic, parent,false);
			
			
		final TextView name = (TextView) row.findViewById(R.id.tv_en);
		final TextView tvfa = (TextView) row.findViewById(R.id.tv_fa);
			//TextView teedad = (TextView) row.findViewById(R.id.teedad_dastan);
			
			name.setText("English: "+Name [position]);
			tvfa.setText("فارسی: "+fa [position]);
			
			//teedad.setText(Teedad [position]);
			//name.setTypeface(MainActivity.font);
			
							
			return (row);
		}
		
	}
 
 class AA_fa2ar extends ArrayAdapter<String>{
		
		public AA_fa2ar(){
			super(IntelliDic.this,R.layout.row_search,ar);			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View row = in.inflate(R.layout.row_search, parent,false);
			
			
		final TextView name = (TextView) row.findViewById(R.id.tv_searched_word);
		
			name.setText("عربي: " + ar[position]);
			
			
							
			return (row);
		}
		
	}
 
 
	 
	private void refresher(String table,String field, String word,String field1){
						
	db.open();
		int save = db.shomaresh_field( table,field, word, field1);
		
	
		
		Name = new String[save];
		fa = new String[save];
				
		for (int i = 0; i <save; i++) {
			Name[i] = db.namayesh_fasl(table, i, field, word,field1, 0);
			fa[i] =db.namayesh_fasl(table, i, field, word,field1, 1);
			
			
		}
		
		db.close();
	}
	
	private void refresher_fa2ar(String table,String field, String word,String field1){
		
		db.open();
			int save = db.shomaresh_field( table,field, word, field1);
			
			ar = new String[save];
			
			for (int i = 0; i <save; i++) {
				
				ar[i] = db.namayesh_fasl(table, i, field, word,field1, 2);
				Log.d(TAG, ar[i]);
			}
			
			db.close();
		}
		
	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		return new StandOutLayoutParams(id, 350, 200,
				StandOutLayoutParams.CENTER, StandOutLayoutParams.TOP);
	}

	@Override
	public String getAppName() {
		return "بوشلامبو";
	}

	@Override
	public int getThemeStyle() {
		return android.R.style.Theme_Light;
	}

	@Override
	public int getAppIcon() {
		
		return android.R.drawable.ic_menu_close_clear_cancel;
	}
	
	/*@Override
	public String getPersistentNotificationTitle(int id) {
		return getAppName() + " Running";
	}*/
	
	@Override
	public String getPersistentNotificationTitle(int id) {
		
		return "دیکشنری بوشلامبو";
	}
	
	@Override
	public String getPersistentNotificationMessage(int id) {
		
		return "بستن دیکشنری شناور بوشلامبو";
	}
	
	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return StandOutWindow.getCloseIntent(this, getClass(), id);
	}
	
			
	@Override
	public int getFlags(int id) {
		
		return  StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP;
			}	
				
				
	
}//End IntelliDic Class