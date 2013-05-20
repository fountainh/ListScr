package com.example.listscr;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
public class mainmenu extends Activity {
	public String username;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.mainmenu);
	    
       Bundle b = new Bundle();
       b = getIntent().getExtras();
       username = b.getString("username");

	    final ListView listview = (ListView) findViewById(R.id.listView2);
	    String[] values = new String[] { "MyProfile", "FindJob", "PostAJob",
	        "MyOrders", "MyBids"};

	    final ArrayList<String> list = new ArrayList<String>();
	    for (int i = 0; i < values.length; ++i) {
	      list.add(values[i]);
	    }
	    final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
	    listview.setAdapter(adapter);
	    listview.setOnItemClickListener(new OnItemClickListener()
	    {
	    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
	    {
	    	String str=listview.getItemAtPosition(position).toString();	    	
	    	if(str=="MyProfile")
	    	{
	    		Intent intent=new Intent(mainmenu.this,LITestActivity.class);				   
	    		intent.putExtra("username", username);				   
	    		Log.w("Login","About to start LinkedIn activity");
	    		startActivity(intent);	
	    	}
	    	else if (str=="FindJob")
	    	{
	    		Intent intent=new Intent(mainmenu.this,JobListings.class);				   
	    		intent.putExtra("username", username);				   
	    		Log.w("Login","About to start FindAJob activity");
	    		startActivity(intent);
	    	
	    	}
	    	else if (str=="PostAJob")
	    	{
	    		Intent intent=new Intent(mainmenu.this,BidPostings.class);				   
	    		intent.putExtra("username", username);				   
	    		Log.w("Login","About to start PostAJob activity");
	    		startActivity(intent);
	    	}
	    	else if (str=="MyBids")
	    	{
	    		Intent intent=new Intent(mainmenu.this,BidListings.class);				   
	    		intent.putExtra("username", username);				   
	    		Log.w("Login","About to start MyBids activity");
	    		startActivity(intent);	    	
	    	}
	    	else if (str=="MyOrders")
	    	{
	    		Intent intent=new Intent(mainmenu.this,BidListings.class);				   
	    		intent.putExtra("username", username);				   
	    		Log.w("Login","About to start MyOrders activity");
	    		startActivity(intent);
	    	}
	    }
	    });
	}
}