package ir.xzn.internetwan.boshlambo;

import java.util.ArrayList;
import java.util.List;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WidgetsWindow extends StandOutWindow {
	
	public static final int DATA_CHANGED_TEXT = 0;
	
	public static final String TAG="WigetsWindow";
	private database db;
	private boolean enablePopup = true;
	private RadioButton rben2fa, rbfa2en, rbfa2ar;
	private String favword;
	private String favstar;
	private String table;
	Button btn_float_delete;
	AutoCompleteTextView actv;
	TextView tvfaword;
	ImageView img_starOff;
	//ImageView img_fav;
	
	private String[] Name;
	private String[] star;
		
							
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
		View view = inflater.inflate(R.layout.widgets, frame, true);
		
		rben2fa=(RadioButton)view.findViewById(R.id.rb_en2fa);
		rbfa2en=(RadioButton)view.findViewById(R.id.rb_fa2en);
		rbfa2ar=(RadioButton)view.findViewById(R.id.rb_fa2ar);
		btn_float_delete=(Button)view.findViewById(R.id.btn_flow_delete);
		tvfaword=(TextView) view.findViewById(R.id.passage_widgets);
		/*img_fav=(ImageView)view.findViewById(R.id.img_favorite);
		img_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			// open favorit class *********************************************************
			Intent favActivity=new Intent(WidgetsWindow.this,Favorit.class);	
			favActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(favActivity);
			}
		});*/
		
		img_starOff = (ImageView)view.findViewById(R.id.img_starOff);
		img_starOff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(favword != null){
					db.open();
				
			if(table.equals("dictionary")){
				
				if (favstar.equals("1")) {
					db.beroozresani_doostdashtaniha(table,"word", favword, "0");
					img_starOff.setImageResource(R.drawable.star_of);
					favstar = "0";
				}else {
					db.beroozresani_doostdashtaniha(table,"word", favword, "1");
					img_starOff.setImageResource(R.drawable.star_on);
					favstar = "1";
				}
				
			}else{
				
				if (favstar.equals("1")) {
					db.beroozresani_doostdashtaniha(table,"onvan", favword, "0");
					img_starOff.setImageResource(R.drawable.star_of);
					favstar = "0";
				}else {
					db.beroozresani_doostdashtaniha(table,"onvan", favword, "1");
					img_starOff.setImageResource(R.drawable.star_on);
					favstar = "1";
				}	
				
			}
			
				
					
			db.close();
				}else{
					//do nothing when no word selected
				}
			

											
			}
		});
		
		
		actv=(AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);			
		actv.setThreshold(1);
		actv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && enablePopup) {
					btn_float_delete.setVisibility(View.VISIBLE);
					if(rben2fa.isChecked()){
						
						refresher("dictionary","word",actv.getText().toString(),0);
						actv.setAdapter(new AA());
				
						
					}else if(rbfa2en.isChecked()){
						Log.d(TAG, "**  in ontextchanged rbfa2en");
						refresher("dictionary","mean",actv.getText().toString(),1);
						actv.setAdapter(new AA());
						
					}else if(rbfa2ar.isChecked()){
						
						refresher("fa2ar", "onvan",actv.getText().toString(),1);
						actv.setAdapter(new AA());
						
					}
					
				} else {
					btn_float_delete.setVisibility(View.INVISIBLE);
					/*refresher(null, null, null, count);
					actv.setAdapter(new AA());*/
				}

				enablePopup = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		btn_float_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				actv.setText("");
				tvfaword.setText("");
								
			}
		});
		
		rben2fa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(WidgetsWindow.this, "ترجمه انگلیسی به فارسی", Toast.LENGTH_SHORT).show();
				
			}
		});
		rbfa2en.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(WidgetsWindow.this, "ترجمه فارسی به انگلیسی", Toast.LENGTH_SHORT).show();
				
			}
		});
		rbfa2ar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(WidgetsWindow.this, "ترجمه فــارسی به عـــــربی", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		
		
	}//End createAndAttachView
		
 class AA extends ArrayAdapter<String>{
	
		public AA(){
			super(WidgetsWindow.this,R.layout.raw_widget,Name);
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View row = in.inflate(R.layout.raw_widget, parent,false);
			
		final TextView name = (TextView) row.findViewById(R.id.item_text);
	
			name.setText(Name [position]);
			
			//name.setTypeface(MainActivity.font);
			
			final int pos = position;
			
			name.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {				
					Log.d(TAG, "********* onClick name ******************");
			
					if(rben2fa.isChecked()){
						
						db.open();
						String faword=db.get_word(1,"dictionary", "word", Name[pos]);
						favword = Name[pos];
						favstar = star[pos];
						table = "dictionary";
					  tvfaword.setText(faword);
						db.close();	
						
						if (star[pos].equals("1")) {
							img_starOff.setImageResource(R.drawable.star_on);
						}else {
							img_starOff.setImageResource(R.drawable.star_of);
						}
						
						
					}else if(rbfa2en.isChecked()){
						
						db.open();
						String en_word=db.get_word(0, "dictionary", "mean", Name[pos]);
						favword = Name[pos];
						favstar = star[pos];
						table = "dictionary";
						tvfaword.setText(en_word);
						tvfaword.setGravity(0);
						db.close();
						
						if (star[pos].equals("1")) {
							img_starOff.setImageResource(R.drawable.star_on);
						}else {
							img_starOff.setImageResource(R.drawable.star_of);
						}
						
					}else if(rbfa2ar.isChecked()){
						
						db.open();
						String ar_word=db.get_word(2, "fa2ar", "onvan", Name[pos]);
						favword = Name[pos];
						favstar = star[pos];
						table = "fa2ar";
						tvfaword.setText(ar_word);
						db.close();
						
						if (star[pos].equals("1")) {
							img_starOff.setImageResource(R.drawable.star_on);
						}else {
							img_starOff.setImageResource(R.drawable.star_of);
						}
						
					}
					
										
					actv.dismissDropDown();
				}
			});	
				
			return (row);
		}
		
	}
 
 
	 
	private void refresher(String table,String field, String entered,int str){
						
		db.open();
		int save = db.shomaresh_field2( table,field, entered);
		Name = new String[save];
		star = new String[save];
		//Teedad = new String[save];
		
		for (int i = 0; i <save; i++) {
			Name[i] = db.search_en( table, i, field,entered,str);
			star[i] = db.search_en( table, i, field,entered, 3);
			//Teedad[i] = db.shomaresh_dastan("datastorys", Name[i].toString())+"";
			
			
		}
		
		db.close();
	}
	
		
	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		return new StandOutLayoutParams(id, 300, 400,
				StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
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
	public int getHiddenIcon() {
		return android.R.drawable.ic_menu_info_details;
	}

	@Override
	public String getHiddenNotificationTitle(int id) {
		return "بوشلامبو پنهان شده است";
	}

	@Override
	public String getHiddenNotificationMessage(int id) {
		return "برای باز شدن دیکشنری شناور روی من کلیک کنید!";
	}

	// return an Intent that restores the WidgetWindow
	@Override
	public Intent getHiddenNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), id);
	}
	
	
	
	@Override
	public List<DropDownListItem> getDropDownItems(int id) {
		
		List<DropDownListItem> list=new ArrayList<StandOutWindow.DropDownListItem>();
		list.add(new DropDownListItem(android.R.drawable.ic_dialog_info, "درباره", new Runnable() {
			
			@Override
			public void run() {
				final Dialog dl=new Dialog(WidgetsWindow.this);
				//dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dl.setContentView(R.layout.info);
				dl.getWindow().getAttributes().
				windowAnimations=R.style.dialog_anim;
				dl.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				//img_boshlambo=(ImageView)dl.findViewById(R.id.img_boshlambo);
				//animBosh();
				dl.setCanceledOnTouchOutside(true);
				dl.setCancelable(true);
				dl.show();
			}
		}));
		
		list.add(new DropDownListItem(R.drawable.ic_launcher, "اپ کامل", new Runnable() {
			
			@Override
			public void run() {
				Intent fullApp=new Intent(getApplicationContext(),MainActivity.class);
				fullApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(fullApp);
				StandOutWindow.closeAll(getApplicationContext(), WidgetsWindow.class);
			}
		}));
		
		return list;
	}
	
	@Override
	public int getFlags(int id) {
		
		return StandOutFlags.FLAG_DECORATION_SYSTEM
				| StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_WINDOW_HIDE_ENABLE
				| StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP
				| StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE
				| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
	}
	
	
}//End WidgetWindow Class