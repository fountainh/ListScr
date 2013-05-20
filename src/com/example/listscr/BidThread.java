package com.example.listscr;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BidThread extends Activity {
	 
	 private BidsDBAdapter dbHelper;
	 NumberPicker numpick;
	 DatePicker datepick;
	 String timestamp;
	 private SimpleCursorAdapter dataAdapter;
	 final Context context = this;
	 String name,job;

	 
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	 
		 Bundle b = new Bundle();
		 b = getIntent().getExtras();
		 name = b.getString("name");
		 job = b.getString("job");
		 
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.expandedmessagethreadv2);
	  dbHelper = new BidsDBAdapter(this);
	  dbHelper.open();
	  //Clean all data
	  //dbHelper.deleteAllbids();
	  //Add some data
	  Log.w("ABout to insert bids","bidout");
	  dbHelper.insertSomebids();
	  Log.w("Insertedbids","bidOut");
	  //Generate ListView from SQLite Database
	  displayListView();

	  
	  
	  

	 
	 }
	 
	 private void displayListView() {
	 
	 
	  Cursor cursor = dbHelper.fetchbidconv(name,job);
	  Integer i1= new Integer(cursor.getCount());
	  Log.w("Bids",i1.toString());
	 
	  // The desired columns to be bound
	  String[] columns = new String[] {
	    BidsDBAdapter.KEY_TITLE,
	    BidsDBAdapter.KEY_SUMMARY,
	    BidsDBAdapter.KEY_COST,
	    BidsDBAdapter.KEY_ECD,
	    BidsDBAdapter.KEY_SENDERID,
	    BidsDBAdapter.KEY_RECVID,
	    BidsDBAdapter.KEY_TIMESTAMP	    
	  };
	 
	  // the XML defined views which the data will be bound to
	  int[] to = new int[] { 
	    R.id.textthreadProjectTitle,
	    R.id.textthreadSummary,
	    R.id.textthreadBudget,
	    R.id.textthreadECD,	    
	    R.id.textthreadSender,
	    R.id.textthreadReceiver,
	    R.id.textthreadtimestamp
	  };
	 
	  // create the adapter using the cursor pointing to the desired data 
	  //as well as the layout information
	  dataAdapter = new SimpleCursorAdapter(
	    this, R.layout.threadunit, 
	    cursor, 
	    columns, 
	    to,
	    0);
	  
	  
	  if(dataAdapter==null)
	  {
		  Log.w("Adapter is Null","bidOut");
	  }
	  Log.w("CreatedAdapter","bidOut");
	  ListView listView = (ListView) findViewById(R.id.listView2);
	  if(listView==null)
	  {
		  Log.w("Null - List View","bidOut");
	  }
	  else
	  {  
	  Log.w("Got the List View","bidOut");
	  }
	  // Assign adapter to ListView
	  listView.setAdapter(dataAdapter);
	  
	  Log.w("Set the Adapter","bidOut");
	 
	 }
	}
