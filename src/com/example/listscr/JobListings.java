package com.example.listscr;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class JobListings extends Activity {
 
 private JobsDBAdapter dbHelper;
 private SimpleCursorAdapter dataAdapter;
 final Context context = this;

 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main_scr);
  dbHelper = new JobsDBAdapter(this);
  dbHelper.open();
  //Clean all data
  dbHelper.deleteAllJobs();
  //Add some data
  dbHelper.insertSomeJobs();
  Log.w("InsertedJobs","JobOut");
  //Generate ListView from SQLite Database
  displayListView();
 
 }
 
 private void displayListView() {
 
 
  Cursor cursor = dbHelper.fetchAllJobs();
 
  // The desired columns to be bound
  String[] columns = new String[] {
    JobsDBAdapter.KEY_TITLE,
    JobsDBAdapter.KEY_SUMMARY,
    JobsDBAdapter.KEY_BUDGET,
    JobsDBAdapter.KEY_SOURCER
  };
 
  // the XML defined views which the data will be bound to
  int[] to = new int[] { 
    R.id.textProjectTitle,
    R.id.textSummary,
    R.id.textBudget,
    R.id.textSourcer,
  };
 
  // create the adapter using the cursor pointing to the desired data 
  //as well as the layout information
  dataAdapter = new SimpleCursorAdapter(
    this, R.layout.resultset, 
    cursor, 
    columns, 
    to,
    0);
  if(dataAdapter==null)
  {
	  Log.w("Adapter is Null","JobOut");
  }
  Log.w("CreatedAdapter","JobOut");
  ListView listView = (ListView) findViewById(R.id.listView1);
  if(listView==null)
  {
	  Log.w("Null - List View","JobOut");
  }
  else
  {  
  Log.w("Got the List View","JobOut");
  }
  // Assign adapter to ListView
  listView.setAdapter(dataAdapter);
  
  Log.w("Set the Adapter","JobOut");
 
 
  listView.setOnItemClickListener(new OnItemClickListener() {
   @Override
   public void onItemClick(AdapterView<?> listView, View view, 
     int position, long id) {
	   
	   
	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	// set title
	alertDialogBuilder.setTitle("App Trader");
	// set dialog message
	alertDialogBuilder
					.setMessage("Do you want to bid this project?")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
   // Get the cursor, positioned to the corresponding row in the result set
 /*  Cursor cursor = (Cursor) listView.getItemAtPosition(position);
 
   // Get the state's capital from this row in the database.
   String countryCode = 
    cursor.getString(cursor.getColumnIndexOrThrow("title"));
   Toast.makeText(getApplicationContext(),
     countryCode, Toast.LENGTH_SHORT).show();*/
 
   }
  });
 }
}