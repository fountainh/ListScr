package com.example.listscr;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.EnumSet;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.enumeration.ProfileType;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Company;
import com.google.code.linkedinapi.schema.Connections;
import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.Educations;
import com.google.code.linkedinapi.schema.EndDate;
import com.google.code.linkedinapi.schema.Patents;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.Position;
import com.google.code.linkedinapi.schema.Positions;
import com.google.code.linkedinapi.schema.StartDate;
import com.google.code.linkedinapi.schema.ThreeCurrentPositions;
import com.google.code.linkedinapi.schema.ThreePastPositions;

public class registrationpage extends Activity {

	TextView username;
	TextView password; 
	
	public static final String CONSUMER_KEY = "da72mof4ixnl";
	public static final String CONSUMER_SECRET = "pfQfQsZbYxRvch1D";
	public static final String APP_NAME = "AppTrader";
	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;
	static final String OAUTH_QUERY_TOKEN = "oauth_token";
	static final String OAUTH_QUERY_VERIFIER = "oauth_verifier";
	static final String OAUTH_QUERY_PROBLEM = "oauth_problem";
	static final String VISIT_TABLE = "VisitTable";
	static final String PREF_TOKEN = "linkedin_token";
	static final String PREF_TOKENSECRET = "linkedin_token_secret";
	static final String PREF_REQTOKENSECRET = "linkedin_request_token_secret";

	static final String firstName = "firstName";
	static final String lastName = "lastName";
	static final String HeadLine = "HeadLine";
	static final String Location = "Location";
	static final String Industry = "Industry";
	static final String Employer = "Employer";
	static final String University = "University";
	static final String NoOfConnections = "NoOfConnections";
	static final String Experience = "Experience";
	
	static final String Title1="Title1";
	static final String Company1 = "Company1";
	static final String StartDate1 = "StartDate1";
	static final String EndDate1 = "EndDate1";
	static final String Description1 = "Description1";
	
	static final String Title2 = "Title2";
	static final String Company2 = "Company2";
	static final String StartDate2 = "StartDate2";
	static final String EndDate2 = "EndDate2";
	static final String Description2 = "Description2";

	static final String Skills = "Skills";
	static final String SkillSet = "SkillSet";
	static final String Certification = "Certification";
	static final String CertificationSet = "CertificationSet";
	private loginDBAdapter dbHelper;
	
	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
			.getInstance().createLinkedInOAuthService(CONSUMER_KEY,
					CONSUMER_SECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(CONSUMER_KEY, CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	LinkedInApiClient client;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.login);
		  Button button= (Button) findViewById(R.id.buttonloginRegister);		  
		  dbHelper = new loginDBAdapter(this);
		  dbHelper.open();
		  button.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  
		    	  username=(TextView)findViewById(R.id.textloginChosenUserName);
				  password=(TextView)findViewById(R.id.textloginPassword);  
				  dbHelper.insertAUser(username.getText().toString(),password.getText().toString(),"0","0");
				  
				   Intent intent=new Intent(registrationpage.this,LITestActivity.class);				   
				   intent.putExtra("username", username.getText());				   
				   Log.w("Registration","About to start LinkedIn activity");
				   startActivity(intent);
		      }
		  });
		  		 		 
		  GetToken();
		  
	 }		     
		      public void  GetToken() {
		  		Log.w("Inside Start", "Ln");
		  		try{
		  		Thread LinkedInThread = new Thread() {// added because this will make code work on post API 10
		  			@Override
		  			public void run() {
		  				final LinkedInRequestToken liToken = oAuthService
		  						.getOAuthRequestToken(OAUTH_CALLBACK_URL);
		  				final String uri = liToken.getAuthorizationUrl();
		  				final SharedPreferences pref = getSharedPreferences(VISIT_TABLE,
								MODE_PRIVATE);
		  				if(pref!=null)
		  				{
						Log.w("Must Be redirected", "Ln");
						Log.w("Ln","shared pref not null");
						SharedPreferences.Editor editor = pref.edit();
						editor.putString(PREF_TOKEN, liToken.getToken());
		  				editor.putString(PREF_REQTOKENSECRET, liToken.getTokenSecret());
		  				editor.commit();
		  				/*Log.w("Ln","Editor Updated");
		  				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
						startActivity(i);
						Log.w("Ln","Launching Second Activity");
		  				*/
		  				Log.w("Ln",liToken.getToken());
		  				Log.w("Ln",liToken.getTokenSecret());
		  				}
		  				else
		  				{
		  					Log.w("Ln","shared pref null");
		  				}
		  				}
		  		};		  				  
		  		}
				catch (LinkedInApiClientException ex) {
					Log.w("Ln","LinkedinApiException");
				}
		  		
		      }// end method

	 
}