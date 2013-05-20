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
import android.widget.Button;
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

public class loginpage extends Activity {

	private loginDBAdapter dbHelper;
	TextView username;
	TextView password;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	 
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.loginpage);
		  dbHelper = new loginDBAdapter(this);
		  dbHelper.open();
		  Button button= (Button) findViewById(R.id.buttonlogin);		  		 
		  username=(TextView)findViewById(R.id.textloginUserName);
		  password = (TextView)findViewById(R.id.textloginPassword);
		 
		  button.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  if(dbHelper.validatePassword(username.getText().toString(),password.getText().toString()))
		    	  {
		    	   Intent intent=new Intent(loginpage.this,mainmenu.class);				   
		   		   intent.putExtra("username", username.getText());				   
		   		   Log.w("Login","About to start Mainmenu activity");
		   		   startActivity(intent);		    	  
		    	  }
		      }});
		 
		   
		 
		 
		   
	 }
}