package com.race604.test;

import com.race604.widget.VerticalMarqueeTextView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private VerticalMarqueeTextView mNotifyTv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mNotifyTv = (VerticalMarqueeTextView) findViewById(R.id.tv_notification);
         mNotifyTv.setText("This is a very very very very very very very very very very long long long text!");
         //mNotifyTv.setText("This is short!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
