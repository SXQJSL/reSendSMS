package com.example.xiaoquan;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button btn1;
    private Button btn2;
    private TextView tv1;
    private TextView tv2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv1= (TextView) findViewById(R.id.EditText01);
		tv2= (TextView) findViewById(R.id.EditText2);
		btn1=(Button)findViewById(R.id.button1);
		btn2=(Button)findViewById(R.id.button2);
	    btn1.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	          // TODO Auto-generated method stub
	        	
	        	Intent intent = new Intent(MainActivity.this, ContactsListMultiple.class);
	        	startActivityForResult(intent,1);
	   
	        }
	      });
	    btn2.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	          // TODO Auto-generated method stub
	        	String tv1content=tv1.getText().toString().trim();
	        	String tv2content=tv2.getText().toString().replace("\n","");
	        	if(tv1content.equals("")||tv2content.equals("")){
	        		Toast.makeText(getBaseContext(), "没有短信内容或收件人", Toast.LENGTH_SHORT).show();
	        	}else{
	        	String[] contact_user=tv2content.split(",");
	        	int a=0;
                for(int n=0;n<contact_user.length;n+=2){
                	a=n+1;
                	String remessage=this.replacecontent(tv1content, contact_user[n]);
                	this.sendSMS(contact_user[a], remessage);
                	String sendsu=contact_user[n]+"已发送成功";
                	Toast.makeText(getBaseContext(), sendsu, Toast.LENGTH_SHORT).show();
                }}
	        }
            private void sendSMS(String phoneNumber,String message){
            	SmsManager sms =SmsManager.getDefault();
            	sms.sendTextMessage(phoneNumber, null, message, null, null);
            }
	    	private String replacecontent(String recontent,String send_name){
	    		String recontented="";
	    		recontented=recontent.replace("（称呼）", send_name);
	    		return recontented;
	    	}


	      });
	}
	

	
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode==1&&resultCode==2){
    	ArrayList<String> infoList = new ArrayList<String>();  
    	infoList = data.getStringArrayListExtra("GET_CONTACT");
    	String content="";
    	  for(int   i=0;i<infoList.size();i++){
    	      Object   o=infoList.get(i); 
    	      content = content +o+",\n";
    	  } 
        tv2.setText(content);
    	}
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

}
