package ir.xzn.internetwan.boshlambo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBack extends Activity {
	
	private TextView tv_counter;
	private EditText ed_FeedBack;
	private int defualtTextColor;
	private String phoneNo="+989172361871";
	private String ed_text;
	private Button SendSmsbtn;
	/*private String s_toast_txt;
	private String f_toast_sms;*/
	SmsManager smsm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		tv_counter=(TextView)findViewById(R.id.tv_counter);
		ed_FeedBack=(EditText)findViewById(R.id.ed_feedBack);
		/*s_toast_txt=getResources().getString(R.string.success_toast_text);
		f_toast_sms=getResources().getString(R.string.failed_sms_toast);*/
		
		SendSmsbtn=(Button)findViewById(R.id.btnSendSms);
		SendSmsbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendSmsFeedBack();
			}

			
		});
		defualtTextColor=tv_counter.getTextColors().getDefaultColor();
		
		ed_FeedBack.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				int count=140-ed_FeedBack.length();
				tv_counter.setText(Integer.toString(count));
				tv_counter.setTextColor(Color.GREEN);
					if(count<10)
				tv_counter.setTextColor(Color.RED);
					else
						tv_counter.setTextColor(defualtTextColor);
			}
		});
		
	}//End onCreate
	
	private void sendSmsFeedBack() {
		ed_text=ed_FeedBack.getText().toString();
		
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
	    share.setType("message/rfc822");
	    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	     share.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
	    
	    share.putExtra(Intent.EXTRA_EMAIL, new String[]{"manyohi@yahoo.com"});
	    share.putExtra(Intent.EXTRA_TEXT, ed_text+"\n Boshlambo");
	 
	    startActivity(Intent.createChooser(share, "FEEDBACK"));
		
		/*
		try{
			ed_text=ed_FeedBack.getText().toString();
			smsm=SmsManager.getDefault();
			smsm.sendTextMessage(phoneNo, null, ed_text+"\n Boshlambo ", null, null);
			Toast.makeText(getApplicationContext(), "FEEDBACK SENT", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "FAILED TO SEND FEEDBACK PLEASE TRY AGAIN LATER", Toast.LENGTH_SHORT).show();
		}
	*/
	    }

	
	
}
