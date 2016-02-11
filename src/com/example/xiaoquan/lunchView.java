package com.example.xiaoquan;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
public class lunchView extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunch);
        Handler x = new Handler();
        x.postDelayed(new lunchhandler(), 2500);
        
    }
    
    class lunchhandler implements Runnable{

        public void run() {
            startActivity(new Intent(getApplication(),MainActivity.class));
            lunchView.this.finish();
        }
        
    }
}

