package com.example.listscr;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BidListings extends Activity {
 
 private BidsDBAdapter dbHelper;
 private SimpleCursorAdapter dataAdapter;
 final Context context = this;


 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main_scr);
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
 
 
  Cursor cursor = dbHelper.fetchbidforUser("Raman");
  Log.w("Bids","Got cursor");
 
  // The desired columns to be bound
  String[] columns = new String[] {
    BidsDBAdapter.KEY_TITLE,
    BidsDBAdapter.KEY_COST,    
    BidsDBAdapter.KEY_JOBID    
  };
 
  // the XML defined views which the data will be bound to
  int[] to = new int[] { 
    R.id.textBidProjectTitle,    
    R.id.textBidBudget,
    R.id.textBidJobId
  };

  // create the adapter using the cursor pointing to the desired data 
  //as well as the layout information
  dataAdapter = new SimpleCursorAdapter(
    this, R.layout.resultbid, 
    cursor, 
    columns, 
    to,
    0);
  
  
  if(dataAdapter==null)
  {
	  Log.w("Adapter is Null","bidOut");
  }
  Log.w("CreatedAdapter","bidOut");
  ListView listView = (ListView) findViewById(R.id.listView1);
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
 
 
  listView.setOnItemClickListener(new OnItemClickListener() {
   @Override
   public void onItemClick(AdapterView<?> listView, View view, 
     int position, long id) {
	   
	   Log.w("Printing JobId","About to get JobId");
	   RelativeLayout rl= (RelativeLayout)view;	   
	   TextView jobIdView=(TextView)rl.getChildAt(1);
	   Log.w("Printing JobId","Got the textview");
	   String jobIdInput=jobIdView.getText().toString();
	   Log.w("Printing JobId",jobIdInput);	   
	   String name="Raman";
	   Intent intent=new Intent(BidListings.this,BidThread.class);
	   intent.putExtra("name", name);
	   intent.putExtra("job",jobIdInput);
	   Log.w("Printing JobId","About to start activity");
	   startActivity(intent);
	
	   }
     }
   );
 }
}