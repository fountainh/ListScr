
package com.example.listscr;
import java.util.EnumSet;
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
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.Position;
import com.google.code.linkedinapi.schema.Positions;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.enumeration.ProfileType;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;

public class LITestActivity extends Activity {
	public static final String CONSUMER_KEY = "da72mof4ixnl";
	public static final String CONSUMER_SECRET = "pfQfQsZbYxRvch1D"; 
	public static final String APP_NAME                 = "AppTrader";
    public static final String OAUTH_CALLBACK_SCHEME    = "x-oauthflow-linkedin";
    public static final String OAUTH_CALLBACK_HOST      = "litestcalback";
    public static final String OAUTH_CALLBACK_URL       = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
    static final String OAUTH_QUERY_TOKEN               = "oauth_token";
    static final String OAUTH_QUERY_VERIFIER            = "oauth_verifier";
    static final String OAUTH_QUERY_PROBLEM             = "oauth_problem";
    static final String OAUTH_PREF                      = "AppPreferences";
    static final String PREF_TOKEN                      = "linkedin_token";
    static final String PREF_TOKENSECRET                = "linkedin_token_secret";
    static final String PREF_REQTOKENSECRET             = "linkedin_request_token_secret";
    Person p;

    final LinkedInOAuthService oAuthService             = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(CONSUMER_KEY, CONSUMER_SECRET);
    final LinkedInApiClientFactory factory              = LinkedInApiClientFactory.newInstance(CONSUMER_KEY, CONSUMER_SECRET);
    LinkedInRequestToken liToken;
    LinkedInApiClient client;
    
    
    TextView tv = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.profilelayout);
    	
    	Bundle b = new Bundle();
	    b = getIntent().getExtras();
	    String name = b.getString("username");
		

        
        //tv = new TextView(this);
        //setContentView(tv);
        final SharedPreferences pref    = getSharedPreferences(OAUTH_PREF, MODE_PRIVATE);
        final String token              = pref.getString(PREF_TOKEN, null);
        final String tokenSecret        = pref.getString(PREF_TOKENSECRET, null);
        if (token == null || tokenSecret == null) {
            startAutheniticate();
        } else {
            LinkedInAccessToken accessToken = new LinkedInAccessToken(token, tokenSecret);
            showCurrentUser(accessToken);
        }
    }//end method

    void startAutheniticate() {
    	Log.w("Ln","Inside STart");
        new Thread(){//added because this will make code work on post API 10 
            @Override
            public void run(){
                final LinkedInRequestToken liToken  = oAuthService.getOAuthRequestToken(OAUTH_CALLBACK_URL); 
                final String uri                    = liToken.getAuthorizationUrl();
                final SharedPreferences pref        = getSharedPreferences(OAUTH_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor     = pref.edit(); 
                editor.putString(PREF_REQTOKENSECRET, liToken.getTokenSecret());
                editor.commit();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(i);
             }
        }.start();
    }//end method

    void finishAuthenticate(final Uri uri) {
        new Thread(){
            @Override
            public void run(){
                Looper.prepare();
                if (uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
                    final String problem = uri.getQueryParameter(OAUTH_QUERY_PROBLEM);
                    if (problem == null) {
                        final SharedPreferences pref                = getSharedPreferences(OAUTH_PREF, MODE_PRIVATE);
                        final String request_token_secret           = pref.getString(PREF_REQTOKENSECRET, null);
                        final String query_token                    = uri.getQueryParameter(OAUTH_QUERY_TOKEN);
                        final LinkedInRequestToken request_token    = new LinkedInRequestToken(query_token, request_token_secret);
                        final LinkedInAccessToken accessToken       = oAuthService.getOAuthAccessToken(request_token, uri.getQueryParameter(OAUTH_QUERY_VERIFIER));
                        SharedPreferences.Editor editor = pref.edit(); 
                        editor.putString(PREF_TOKEN, accessToken.getToken());
                        editor.putString(PREF_TOKENSECRET, accessToken.getTokenSecret());
                        editor.remove(PREF_REQTOKENSECRET);
                        editor.commit();
                        showCurrentUser(accessToken);
                    } else {
                        Toast.makeText(getApplicationContext(), "Application down due OAuth problem: " + problem, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                Looper.loop();
            }
        }.start();
    }//end method

    void clearTokens() {
        getSharedPreferences(OAUTH_PREF, MODE_PRIVATE).edit().remove(PREF_TOKEN).remove(PREF_TOKENSECRET).remove(PREF_REQTOKENSECRET).commit();
    }//end method

    void showCurrentUser(final LinkedInAccessToken accessToken) {
    	Log.w("Ln","Inside Show current user");
        Thread LThread = new Thread(){
            @Override
            public void run(){
                Looper.prepare();
                final LinkedInApiClient client = factory.createLinkedInApiClient(accessToken);
                try {
                	
                	//final LinkedInApiClient client = factory.createLinkedInApiClient(accessToken);
                	try {
                		Log.w("About to know a person", "Ln");					
                		client.setAccessToken(accessToken);
                		final EnumSet<ProfileField> ProfileParameters = EnumSet.allOf(ProfileField.class);					
                		//p = client.getProfileForCurrentUser();					
                		 p = client.getProfileForCurrentUser(ProfileParameters); 
                	      if(p!=null)
                	         {
                	            Log.w("Ln","Person is not nul");
                	            String pId= p.getPublicProfileUrl();
            					if(pId!=null)
            					{
            					Log.w("Ln",pId);
            					Log.w("Ln",p.getFirstName());
            					Log.w("Ln",p.getLastName());
            					if(p.getHeadline()!=null)
            					Log.w("Ln",p.getHeadline());
            					
            					Positions profilePos=p.getPositions();
            					if (profilePos!=null)
            					{	
            						Log.w("Ln","profilePos not null");
            						List<Position> listPositions=profilePos.getPositionList();
            						if(listPositions!=null)
            						{
            					    Log.w("Ln","listPos not null");
            						Position cPosition=listPositions.get(0);
            						if(cPosition!=null)
            						{
            							Log.w("Ln","cPos not null");
            						Company comp=cPosition.getCompany();
            						if(comp!=null)
            						{            					
            							Log.w("Ln","company not null");
            							Log.w(comp.getName(),"Ln");
            						}
            						else
            						{
            							Log.w("Company Null","Ln");
            						}			
            						String summary = cPosition.getSummary();
            						if(summary!=null)
            						{
            							Log.w("Summary not Null","Ln");
            						Log.w("Ln",summary);
            						}
            						if(p.getHeadline()!=null)
            						{
            						}
            						if(p.getIndustry()!=null)
            						{
            						p.getIndustry();
            						}
            						}
            						}
            					}


            					
            					}
            					else
            					{
            						Log.w("pId is null","Ln");
            					}
                	         
                	            
                	         }
                	         else
                	         {
                	            Log.w("Ln","Person is null");
                	         }
                	}
                	catch(LinkedInApiClientException ex)
                	{
                	  Log.w("Ln","LinkedInException");
                	}

                    /*final Person p = client.getProfileForCurrentUser();
                    if(p!=null)
                    {
                    	Log.w("Ln","Person not nul");
                    }
                    else
                    {
                    Log.w("Ln","Person is null");
                    }*/
                    
                    // /////////////////////////////////////////////////////////
                    // here you can do client API calls ...
                    // client.postComment(arg0, arg1);
                    // client.updateCurrentStatus(arg0);
                    // or any other API call (this sample only check for current user
                    // and shows it in TextView)
                    // /////////////////////////////////////////////////////////             
                    runOnUiThread(new Runnable() {//updating UI thread from different thread not a good idea...
                        public void run() {
                            //tv.setText(p.getLastName() + ", " + p.getFirstName());
                        }
                    });
                    //or use Toast
                    //Toast.makeText(getApplicationContext(), "Lastname:: "+p.getLastName() + ", First name: " + p.getFirstName(), 1).show();
                } catch (LinkedInApiClientException ex) {
                    clearTokens();
                    Toast.makeText(getApplicationContext(),
                        "Application down due LinkedInApiClientException: "+ ex.getMessage() + " Authokens cleared - try run application again.",
                        Toast.LENGTH_LONG).show();
                    finish();
                }
                Looper.loop();
            }
        };
        LThread.start();
        try {
        	Thread.sleep(10000);
			LThread.interrupt();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.w("Ln","Thread Completed");
        
        TextView firstName = (TextView)findViewById(R.id.firstName);
        TextView lastName = (TextView)findViewById(R.id.lastName);
        TextView HeadLine = (TextView)findViewById(R.id.HeadLine);
        TextView Industry = (TextView)findViewById(R.id.Industry);
        TextView Location = (TextView)findViewById(R.id.Location);
        TextView ProfileURL = (TextView)findViewById(R.id.ProfileURL);
        TextView Company1 = (TextView)findViewById(R.id.LiCompany1);
        TextView Title1 =  (TextView)findViewById(R.id.LiTitle1);
        TextView Description1 = (TextView)findViewById(R.id.LiDescription1);
        
        Log.w("Ln","Begin Logging");
        firstName.setText(p.getFirstName());
        lastName.setText(p.getLastName());
        HeadLine.setText(p.getHeadline());
        Industry.setText(p.getIndustry());
        Location.setText(p.getLocation().getName());        
        ProfileURL.setText(p.getPublicProfileUrl());
        
        Positions profilePos=p.getPositions();
		if (profilePos!=null)
		{	
			Log.w("Ln","profilePos not null");
			List<Position> listPositions=profilePos.getPositionList();
			if(listPositions!=null)
			{
		    Log.w("Ln","listPos not null");
			Position cPosition=listPositions.get(0);
			if(cPosition!=null)
			{
				Log.w("Ln","cPos not null");
			Company comp=cPosition.getCompany();
			Title1.setText(cPosition.getTitle());
			
			if(comp!=null)
			{            					
				Log.w("Ln","company not null");
				Log.w(comp.getName(),"Ln");
				Company1.setText(comp.getName());
			}
			else
			{
				Log.w("Company Null","Ln");
			}			
			String summary = cPosition.getSummary();
			if(summary!=null)
			{
				Log.w("Summary not Null","Ln");
		  	    Log.w("Ln",summary);
			Description1.setText(summary);
			}
			}
			}
		}
        
        
        
    } //end method

    @Override
    protected void onNewIntent(Intent intent) {
        finishAuthenticate(intent.getData());
    }//end method
}//end class