package ir.xzn.internetwan.boshlambo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class Favorit extends Activity {
	private database db;
	private String[] Name;
	private String[] faword;
	private String[] Enword_tblfa2ar;
	private String[] Faword_tblfa2ar;
	ListView lv;
	ListView lv_fa2ar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorit);
		db = new database(this);
		/*refresher();
		setListAdapter(new AA());*/
		TabHost tabs=(TabHost)findViewById(R.id.tabhost);
		tabs.setup();
		
		TabHost.TabSpec spec=tabs.newTabSpec("tag1");
		spec.setContent(R.id.fl1);
		spec.setIndicator("انگلیسی و فارسی");
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("tag2");
		spec.setContent(R.id.fl2);
		spec.setIndicator("فارسی و عربی");
		tabs.addTab(spec);
		
		lv=(ListView)findViewById(R.id.lvfavorit);
		lv.setClickable(true);
		refresher();
		lv.setAdapter(new AA());
		
		lv_fa2ar = (ListView)findViewById(R.id.lvfa2ar);
		lv.setClickable(true);
		ref_fa2ar();
		lv_fa2ar.setAdapter(new AA_fa2ar());
		
		
	}

	public void deletDialog(){}
	
	class AA extends ArrayAdapter<String>{
		
		public AA(){
			super(Favorit.this,R.layout.row_fav,Name);
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater in = getLayoutInflater();
			View row = in.inflate(R.layout.row_fav, parent,false);
			
		
			
		final LinearLayout Ll_row_fav=(LinearLayout)row.findViewById(R.id.Ll_row_fav);
		final TextView name = (TextView) row.findViewById(R.id.tv_rowfav);
		final TextView fa_word = (TextView) row.findViewById(R.id.tv_rowfav_fa);
			
			
			name.setText(Name [position]);
			fa_word.setText(faword [position]);
			
			final int pos=position;
			Ll_row_fav.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					

					final Dialog dl=new Dialog(Favorit.this);
					dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dl.setContentView(R.layout.delete_dialog);
					dl.getWindow().getAttributes().
					windowAnimations=R.style.dialog_anim;
					Button delet=(Button)dl.findViewById(R.id.btn_deletFav);
					delet.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							
						db.open();
							
							db.beroozresani_doostdashtaniha("dictionary", "word", Name[pos], "0");
							refresher();
							lv.setAdapter(new AA());
							
						db.close();
						
						dl.dismiss();
						}
					});
					dl.setCanceledOnTouchOutside(true);
					dl.setCancelable(true);
					dl.show();
				
				}
			});
			
			
			Animation rowanim=AnimationUtils.loadAnimation(Favorit.this, R.anim.row_anim);
			row.setAnimation(rowanim);
			
			return (row);
		}
		
	}

class AA_fa2ar extends ArrayAdapter<String>{
		
		public AA_fa2ar(){
			super(Favorit.this,R.layout.row_fav_fa2ar,Enword_tblfa2ar);
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater in = getLayoutInflater();
			View row = in.inflate(R.layout.row_fav_fa2ar, parent,false);
			
		
			//Ll_row_fav_fa2ar
		final LinearLayout Ll_row_fav_fa2ar=(LinearLayout)row.findViewById(R.id.Ll_row_fav_fa2ar);
		final TextView enwordtblfa2ar = (TextView) row.findViewById(R.id.tv_rowfav_fa2ar_En);
		final TextView fawordtblfa2ar = (TextView) row.findViewById(R.id.tv_rowfav_fa2ar_Fa);
			
		enwordtblfa2ar.setText(Enword_tblfa2ar [position]);
		fawordtblfa2ar.setText(Faword_tblfa2ar [position]);
		
		final int pos=position;
		Ll_row_fav_fa2ar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				

				final Dialog dl=new Dialog(Favorit.this);
				dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dl.setContentView(R.layout.delete_dialog);
				dl.getWindow().getAttributes().
				windowAnimations=R.style.dialog_anim;
				Button delet=(Button)dl.findViewById(R.id.btn_deletFav);
				delet.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
					db.open();
						
						db.beroozresani_doostdashtaniha("fa2ar", "onvan", Enword_tblfa2ar[pos], "0");
						ref_fa2ar();
						lv_fa2ar.setAdapter(new AA_fa2ar());
						
					db.close();
					dl.dismiss();
					}
				});
				dl.setCanceledOnTouchOutside(true);
				dl.setCancelable(true);
				dl.show();
			
			}
		});
			
			Animation rowanim=AnimationUtils.loadAnimation(Favorit.this, R.anim.row_anim);
			row.setAnimation(rowanim);
			
			return (row);
		}
		
	}
	
	private void refresher(){
						
		db.open();
		
		int save = db.searchlist();
		
		Name = new String[save];
		faword = new String[save];	
		
		for (int i = 0; i <save; i++) {			
			Name[i] = db.showList("dictionary", i, 0);
			faword[i] = db.showList("dictionary", i, 1);		
		}
		
		
		db.close();
	}
	
	private void ref_fa2ar(){
		db.open();
		
		int cnt_fa2ar = db.searchlist_fa2artbl();
		
		Enword_tblfa2ar = new String[cnt_fa2ar];
		Faword_tblfa2ar = new String[cnt_fa2ar];
		
		for (int i = 0; i < cnt_fa2ar; i++) {
			Enword_tblfa2ar[i] = db.showList("fa2ar", i, 1);
			Faword_tblfa2ar[i] = db.showList("fa2ar", i, 2);
		}
		
		db.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId()){
		
		case R.id.favmenu_info:
			MainActivity ma=new MainActivity();
			ma.dialogInfo(Favorit.this, R.layout.info);
			return true;
		
		}
		return super.onOptionsItemSelected(item);
		
		
	}
	
	
	
}

