package ir.xzn.internetwan.boshlambo;

import ir.xzn.internetwan.boshlambo.adapter.NavDrawerListAdapter;
import ir.xzn.internetwan.boshlambo.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.Locale;

import wei.mark.standout.StandOutWindow;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;

import android.text.TextWatcher;
import android.util.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String TAG="MainActivity";
	private SQLiteDatabase sqliteDB = null;
	private boolean enablePopup = true;
	private SharedPreferences sp;
	private database db;
	TextToSpeech ttobj;
	TextView write;
	private ImageView img_speaker,img_del;
	public static ImageView img_boshlambo;
	/*private RadioButton rb_en;
	private RadioButton rb_fa;
	private RadioButton rb_ar;*/
	private Button btn_floatwindow;
	private String serStatus;
	private Menu menu;
	
	//slide menu vars 
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	private String lang="fa2en";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		db=new database(this);
		db.useable();
		
		//Show splash screen and StartService for first time ********************************
		showSplashandStartService();
			
		// vars for Drawer navigation **************	
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		//Arabic 2 Persian
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Communities, Will add a counter here
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Pages
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			//displayView(0);
		}
		 
		 ttobj=new TextToSpeech(getApplicationContext(),
				 new TextToSpeech.OnInitListener () {
				 @Override
				 public void onInit(int status) {
				 if (status != TextToSpeech.ERROR){
				 ttobj.setLanguage(Locale.UK);
				 }
				 }
				 });
	// ******************** Copy the Text  ***********************************
		 final ImageView copy=(ImageView) findViewById(R.id.img_copy);
		 copy.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				 write = (TextView) findViewById(R.id.passage);
			        
			   
			        String selectedText = write.getText().toString();                
			        int sdk = android.os.Build.VERSION.SDK_INT;
			        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) 
			            		getSystemService(Context.CLIPBOARD_SERVICE);
			            clipboard.setText(selectedText);
			            
			            Toast .makeText(getApplicationContext(), selectedText+" Copied",
			            		Toast .LENGTH_SHORT).show();
			        } else {
			            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
			            		getSystemService(Context.CLIPBOARD_SERVICE); 
			            android.content.ClipData clip = android.content.ClipData.newPlainText("Boshlambo",selectedText);
			            clipboard.setPrimaryClip(clip);
			          
			            Toast .makeText(getApplicationContext(), selectedText+" Copied",
			            		Toast .LENGTH_SHORT).show();
			            
			            
			        }
			    }				
			
		});
		 

		sqliteDB = SQLiteDatabase.openOrCreateDatabase(
				"/data/data/" + this.getPackageName() + "/databases/dic.db",
				null);
		
		final AutoCompleteTextView textView=(AutoCompleteTextView)findViewById(R.id.searchbox);
		btn_floatwindow=(Button)findViewById(R.id.btn_floatwindow);
		
		/*rb_en=(RadioButton)findViewById(R.id.rb_en);
		rb_fa=(RadioButton)findViewById(R.id.rb_fa);
		rb_ar=(RadioButton)findViewById(R.id.rb_ar);*/
		
		img_speaker=(ImageView)findViewById(R.id.img_speaker);
		img_del=(ImageView)findViewById(R.id.img_del);
		//img_boshlambo=(ImageView)findViewById(R.id.img_boshlambo);
		
		btn_floatwindow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

								
				StandOutWindow.closeAll(MainActivity.this, WidgetsWindow.class);
				StandOutWindow.show(MainActivity.this, WidgetsWindow.class, StandOutWindow.DEFAULT_ID);
				MainActivity.this.finish();
				
			}

			
		});
		
		img_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				textView.setText("");
			}
		});
		// Hide the speaker icon when en&ar radiobutton clicked ****************************************
		/*rb_en.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				img_speaker.setVisibility(View.INVISIBLE);
				 Toast .makeText(getApplicationContext(),"ترجمه انگلیسی به فارسی",
		            		Toast .LENGTH_SHORT).show();
			}
		});
		
		rb_fa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				img_speaker.setVisibility(View.VISIBLE);
				 Toast .makeText(getApplicationContext(),"ترجمه فارسی به انگلیسی ",
		            		Toast .LENGTH_SHORT).show();
			}
		});
		
		rb_ar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				img_speaker.setVisibility(View.INVISIBLE);
				Toast .makeText(getApplicationContext(),"ترجمه عربی به فارسی",
	            		Toast .LENGTH_SHORT).show();
			}
		});*/

		textView.setThreshold(1);
		final Context context = this;
		
		textView.addTextChangedListener(new TextWatcher() {
		
		
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() >= 1 && enablePopup) {
					img_del.setVisibility(View.VISIBLE);
					if(lang=="en2fa"){
									
						Cursor cursor = sqliteDB.rawQuery(
								"SELECT word FROM dictionary WHERE word LIKE '"
										+ s.toString().toLowerCase()
										+ "%' LIMIT 10", null);

						if (cursor.getCount() > 0) {
							cursor.moveToFirst();
							ArrayList<String> word = new ArrayList<String>();

							while (!cursor.isAfterLast()) {
								word.add(cursor.getString(0));//get the EN words
								cursor.moveToNext();
							}

							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, word));
						} else {
							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, new ArrayList<String>()));
						}
					}else if(lang=="fa2en"){
						
											
						Cursor cursor = sqliteDB.rawQuery(
								"SELECT mean FROM dictionary WHERE mean LIKE '"
										+ s.toString().toLowerCase()
										+ "%' LIMIT 10", null);

						if (cursor.getCount() > 0) {
							cursor.moveToFirst();
							ArrayList<String> word = new ArrayList<String>();

							while (!cursor.isAfterLast()) {
								word.add(cursor.getString(0));//get the EN words
								cursor.moveToNext();
							}

							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, word));
						} else {
							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, new ArrayList<String>()));
						}
					}else if(lang=="fa2ar"){
						
						
						
						Cursor cursor = sqliteDB.rawQuery(
								"SELECT onvan FROM fa2ar WHERE onvan LIKE '"
										+ s.toString().toLowerCase()
										+ "%' LIMIT 10", null);

						if (cursor.getCount() > 0) {
							cursor.moveToFirst();
							ArrayList<String> word = new ArrayList<String>();

							while (!cursor.isAfterLast()) {
								word.add(cursor.getString(0));
								cursor.moveToNext();
							}

							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, word));
						} else {
							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, new ArrayList<String>()));
						}
					
					}else if(lang=="ar2fa"){

																		
						Cursor cursor = sqliteDB.rawQuery(
								"SELECT matn FROM fa2ar WHERE matn LIKE '"
										+ s.toString().toLowerCase()
										+ "%' LIMIT 10", null);

						if (cursor.getCount() > 0) {
							cursor.moveToFirst();
							ArrayList<String> word = new ArrayList<String>();

							while (!cursor.isAfterLast()) {
								word.add(cursor.getString(0));
								cursor.moveToNext();
							}

							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, word));
						} else {
							textView.setAdapter(new DropDownAdapter(context,
									sqliteDB, new ArrayList<String>()));
						}
										
					}
					
		
				} else {
					img_del.setVisibility(View.INVISIBLE);
					textView.setAdapter(new DropDownAdapter(context, sqliteDB,
							new ArrayList<String>()));
				}

				enablePopup = true;
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
								
			}
		});
		
		animBosh();
		
	}//end onCreate
	
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {
	// display view for selected nav drawer item
	displayView(position);
}
}
	
	
	@Override
	public void onPause(){
	if (ttobj !=null ){
	ttobj.stop();
	ttobj.shutdown();
	}
	super.onPause();
	}
	//******* speak the text ***************************************************
	public void speakText(View view){
		TextView write = (TextView) findViewById(R.id.passage);
		String toSpeak = write.getText().toString();
		Toast .makeText(getApplicationContext(), toSpeak,
		Toast .LENGTH_SHORT).show();
		ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
		}
	
	
	
	private void showSplashandStartService() {
		final SharedPreferences shp = getSharedPreferences("sett", MODE_PRIVATE);
	        boolean tost = shp.getBoolean("tos", true);
	        SharedPreferences.Editor shpE = shp.edit();
	        shpE.putBoolean("tos",false);
	        shpE.commit();
	  
	        if (tost){
	        	//startSplash();
	        	startservice();
	        }
	}

	@Override
	protected void onDestroy() {
		//sqliteDB.close();
		//db.close();
		super.onDestroy();
	}

	class DropDownAdapter extends ArrayAdapter<String> {
		Context context;
		ArrayList<String> data;
		SQLiteDatabase sqliteDB;

		public DropDownAdapter(Context context, SQLiteDatabase sqliteDB,
				ArrayList<String> data) {
			super(context, R.layout.row_search, data);

			this.context = context;
			this.sqliteDB = sqliteDB;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(LAYOUT_INFLATER_SERVICE);

			View rowView = (View) inflater.inflate(R.layout.row_search,
					parent, false);

			TextView textView = (TextView) rowView.findViewById(R.id.tv_searched_word);
			textView.setText(data.get(position));

			final int pos = position;

			textView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							if(lang=="en2fa"){
								Cursor cursor = sqliteDB
										.rawQuery(
												"SELECT mean FROM dictionary WHERE word='"
														+ data.get(pos)
																.toLowerCase()
														+ "'", null);

								cursor.moveToFirst();
								final String wordMean = cursor.getString(0);
								cursor.close();

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.searchbox);
										TextView mean = (TextView) findViewById(R.id.passage);
										mean.setTypeface(Typeface.createFromAsset(
												getAssets(), "fonts/Nazanin.ttf"));
										mean.setText(wordMean);
										tv.setText(data.get(pos));
										enablePopup = false;
										tv.dismissDropDown();
									}
								});
							
							}else if(lang=="fa2en"){

								Cursor cursor = sqliteDB
										.rawQuery(
												"SELECT word FROM dictionary WHERE mean='"
														+ data.get(pos)
																.toLowerCase()
														+ "'", null);

								cursor.moveToFirst();
								final String wordMean = cursor.getString(0);
								cursor.close();

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.searchbox);
										TextView mean = (TextView) findViewById(R.id.passage);
										mean.setTypeface(Typeface.createFromAsset(
												getAssets(), "fonts/Nazanin.ttf"));
										mean.setGravity(0);
										mean.setText(wordMean);
										tv.setText(data.get(pos));
										enablePopup = false;
										tv.dismissDropDown();
									}
								});
							
							
							}else if(lang=="fa2ar"){

								Cursor cursor = sqliteDB
										.rawQuery(
												"SELECT matn FROM fa2ar WHERE onvan='"
														+ data.get(pos)
																.toLowerCase()
														+ "'", null);

								cursor.moveToFirst();
								final String wordMean = cursor.getString(0);
								cursor.close();

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.searchbox);
										TextView mean = (TextView) findViewById(R.id.passage);
										mean.setTypeface(Typeface.createFromAsset(
												getAssets(), "fonts/Nazanin.ttf"));
										//mean.setGravity(0);
										mean.setText(wordMean);
										tv.setText(data.get(pos));
										enablePopup = false;
										tv.dismissDropDown();
									}
								});
							
							
							
								
							}else if(lang=="ar2fa"){


								Cursor cursor = sqliteDB
										.rawQuery(
												"SELECT onvan FROM fa2ar WHERE matn='"
														+ data.get(pos)
																.toLowerCase()
														+ "'", null);

								cursor.moveToFirst();
								final String wordMean = cursor.getString(0);
								cursor.close();

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.searchbox);
										TextView mean = (TextView) findViewById(R.id.passage);
										mean.setTypeface(Typeface.createFromAsset(
												getAssets(), "fonts/Nazanin.ttf"));
										//mean.setGravity(0);
										mean.setText(wordMean);
										tv.setText(data.get(pos));
										enablePopup = false;
										tv.dismissDropDown();
									}
								});
																		
														
							}
						}
					}).start();

					return true;
				}
			});

			return rowView;
		}
	}
	
	
	public void dialogInfo(Context context,int info){
		final Dialog dl=new Dialog(context);
		dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dl.setContentView(info);
		dl.getWindow().getAttributes().
		windowAnimations=R.style.dialog_anim;
		//img_boshlambo=(ImageView)dl.findViewById(R.id.img_boshlambo);
		//animBosh();
		dl.setCanceledOnTouchOutside(true);
		dl.setCancelable(true);
		dl.show();
	}
	
	private void animBosh() {
		Animation anim=new AlphaAnimation(1, 0);
		anim.setDuration(1000);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setRepeatMode(Animation.REVERSE);
		
		btn_floatwindow.setAnimation(anim);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu=menu;
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		// if nav drawer is opened, hide the action items
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
				menu.findItem(R.id.exit).setVisible(!drawerOpen);
		
		 loadsp();
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		//Fragment fragment = null;
		switch (position) {
		case 0:
			Log.d("MainActivity", "case 0 selected ****************");
			// update selected item and title, then close the drawer
			updateTitleIconCloseDrawer(position);
			img_speaker.setVisibility(View.INVISIBLE);
				lang = "en2fa";		
			//fragment = new HomeFragment();
			break;
		case 1:
			Log.d("MainActivity", "case 1 selected ****************");
			updateTitleIconCloseDrawer(position);
			img_speaker.setVisibility(View.VISIBLE);
				lang = "fa2en";		
			//fragment = new HomeFragment();
			break;
		case 2:
			//fragment = new HomeFragment();
			updateTitleIconCloseDrawer(position);
			img_speaker.setVisibility(View.INVISIBLE);
			lang = "fa2ar";
			break;
		case 3:
			//fragment = new HomeFragment();
			updateTitleIconCloseDrawer(position);
			img_speaker.setVisibility(View.INVISIBLE);
			lang = "ar2fa";
			break;

		default:
			break;
		}

		/*if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.rl_main, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			//************** close the drawer
			mDrawerLayout.closeDrawer(mDrawerList);
			
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}*/
	}
	private void updateTitleIconCloseDrawer(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		//************** close the drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		// toggle nav drawer on selecting action bar app icon/title
				if (mDrawerToggle.onOptionsItemSelected(item)) {
					return true;
				}
		
		switch(item.getItemId()){
		case R.id.exit:
			finish();
			return true;
		case R.id.info:
			dialogInfo(MainActivity.this,R.layout.info);
			return true;
		case R.id.favorite:
			Intent favIntent = new Intent(MainActivity.this,Favorit.class);
			startActivity(favIntent);
			overridePendingTransition(R.anim.in, R.anim.out);
			return true;
		case R.id.intelliDict:
			if(item.isChecked()){
				  item.setChecked(false);
				  serStatus="inactive";
				
					stopservice();
					
				Toast .makeText(getApplicationContext(), "دیکشنری هوشمند غیر فعال شد",
						Toast .LENGTH_SHORT).show();
			}
			else{
				  item.setChecked(true);
				  serStatus="active";
				  
				  
				startservice();
				
				 Toast .makeText(getApplicationContext(), "دیکشنری هوشمند فعال شد",
						Toast .LENGTH_SHORT).show();
			}
			
			sp=getApplicationContext().getSharedPreferences("service", 0);
			Editor edit=sp.edit();
			edit.putString("serStatus", serStatus);
			edit.commit();
			
			return true;
		case R.id.feedback:
			Intent feed = new Intent(MainActivity.this,FeedBack.class);
			startActivity(feed);
			return true;
		}
		return super.onOptionsItemSelected(item);
		
		
	}
	
	@SuppressLint("NewApi")
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_MENU){
			if(mDrawerLayout.isDrawerOpen(mDrawerList)){
				mDrawerLayout.closeDrawer(mDrawerList);
			}else{
				mDrawerLayout.openDrawer(mDrawerList);
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}*/

private void loadsp(){
		
		sp=getApplicationContext().getSharedPreferences("service", 0);
		String s=sp.getString("serStatus", "active");
		Log.d(TAG, "sp is "+s);
		if(s.equals("active")){
			//Check item in option menu
			checkServiseIteminOptionMenu();
		}else{
			//UnCheck item in option menu
			UnCheckIteminOptionMenu();
		}
		
	}
	
	private void UnCheckIteminOptionMenu() {
		// TODO Auto-generated method stub
		MenuItem menuItem=menu.findItem(R.id.intelliDict);
		menuItem.setChecked(false);
	}

	private void checkServiseIteminOptionMenu(){
		
		MenuItem menuItem=menu.findItem(R.id.intelliDict);
		
		if(menuItem.isChecked()){
			
		}else{
			menuItem.setChecked(true);
		}
		
		
	}
	
	private void startservice() {
		Intent service=new Intent(MainActivity.this,ServiceTranslate.class);
		startService(service);
	}

	private void stopservice() {
		Intent service=new Intent(MainActivity.this,ServiceTranslate.class);
		stopService(service);
	}
	
	/*private void intellijentDic(View view){
		Toast .makeText(getApplicationContext(), "chech box clicked !",
				Toast .LENGTH_SHORT).show();
	}*/
		
	/*private void startSplash(){
		Intent intent=new Intent(MainActivity.this,Splash.class);
		MainActivity.this.startActivity(intent);
		finish();
	}*/

}
