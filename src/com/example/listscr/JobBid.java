package com.example.listscr;
import java.util.ArrayList;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class JobBid extends Activity {
	private JobsDBAdapter dbHelper;
	String strBudget="",strDesc="",strTitle="",strDate="";
	
public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.submitbid);
	  Button button= (Button) findViewById(R.id.JDsubmit);
	  dbHelper = new JobsDBAdapter(this);
	  dbHelper.open();
	  button.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View v) {
	    	  
	    	  TextView budget = (TextView)findViewById(R.id.JDRespBudget);	    	  
	    	  TextView description = (TextView)findViewById(R.id.JDRespDescription);
	    	  TextView date = (TextView)findViewById(R.id.JDRespDate);
	    	  TextView title = (TextView)findViewById(R.id.JDRespTitle);
	    	  
	    	  String budget_str=budget.getText().toString();
	    	  String description_str=description.getText().toString();
	    	  String date_str=date.getText().toString();
	    	  String title_str=title.getText().toString();
	    	 	    
	    	  Log.w("Posting","Ln");
	    	  Log.w(title_str,"Ln");
	    	  Log.w(description_str,"Ln");
	    	  Log.w("EndPosting","Ln");
	    	  
	    	  
	    	  dbHelper.insertAJob(title_str,description_str,budget_str,"mike",date_str);
	    	 
	    	  
	    	   
	    	  Cursor jobCursor=dbHelper.fetchAllJobs();
	    	  
	    	  
	    	  while (jobCursor.isAfterLast() == false) 
	    	  {
	    	      String  cursorStr= jobCursor.getString(1);	    	      
	    	      Log.w(cursorStr,"Ln");
	    	      jobCursor.moveToNext();
	    	  }
	      }
	  });}
}