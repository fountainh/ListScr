 package com.example.listscr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class loginDBAdapter {
 
 public static final String KEY_ROWID = "_id";
 public static final String KEY_USERNAME = "username";
 public static final String KEY_PASSWORD = "password";
 public static final String KEY_TOKEN = "token";
 public static final String KEY_RATINGS = "ratings";
 public static final String TAG="TAGUser";
 private DatabaseHelper mDbHelper;
 private SQLiteDatabase mDb;
 
 private static final String DATABASE_NAME = "AppTraderDB";
 private static final String SQLITE_TABLE = "AppTraderUser";
 private static final int DATABASE_VERSION = 1;
 
 private final Context mCtx;
 
 private static final String DATABASE_CREATE =
  "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
  KEY_ROWID + " integer PRIMARY KEY autoincrement," +
  KEY_USERNAME + "," +
  KEY_PASSWORD + "," +
  KEY_TOKEN + "," +
  KEY_RATINGS +  ");";
 
 private static class DatabaseHelper extends SQLiteOpenHelper {
 
  DatabaseHelper(Context context) {
   super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
 
 
  @Override
  public void onCreate(SQLiteDatabase db) {
   Log.w(TAG, DATABASE_CREATE);
   db.execSQL(DATABASE_CREATE);
  }
 
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
     + newVersion + ", which will destroy all old data");
   db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
   onCreate(db);
  }
 }
 
 public loginDBAdapter(Context ctx) {
  this.mCtx = ctx;
 }
 
 public loginDBAdapter open() throws SQLException {
  mDbHelper = new DatabaseHelper(mCtx);
  mDb = mDbHelper.getWritableDatabase();
  return this;
 }
 
 public void close() {
  if (mDbHelper != null) {
   mDbHelper.close();
  }
 }
 
 public long createUser(String username, String password, 
   String token, String ratings) {
 
  ContentValues initialValues = new ContentValues();
  
  initialValues.put(KEY_USERNAME,username );
  initialValues.put(KEY_PASSWORD, password);
  initialValues.put(KEY_TOKEN, token);
  initialValues.put(KEY_RATINGS, ratings);
  
 
  return mDb.insert(SQLITE_TABLE, null, initialValues);
 }
 
 public boolean deleteAllUsers() {
 
  int doneDelete = 0;
  doneDelete = mDb.delete(SQLITE_TABLE, null , null);
  Log.w(TAG, Integer.toString(doneDelete));
  return doneDelete > 0;
 
 }
 
 public boolean validatePassword(String username,String password) throws SQLException {
	 
	 try
	 {	 
  String[] whereArgs = new String[2];
  whereArgs[0]=username;
  whereArgs[1]=password;
  String sql="select * from AppTraderUser where (username=? AND password=?);"; 
  Cursor mCursor=mDb.rawQuery(sql, whereArgs);
  if(mCursor.getCount()>0) return true;
  }
  catch(SQLException e)
  {
	Log.w("SQLException","error while querying login creds");
	return false;
  } 
	 return false;
  
 }
 
 public String GetToken (String username)
 {
 String result;
 Cursor mCursor=null;
 try
 {	 
String[] whereArgs = new String[1];
whereArgs[0]=username;
String sql="select token from AppTraderUser where username=?"; 
mCursor=mDb.rawQuery(sql, whereArgs);
}
catch(SQLException e)
{
Log.w("SQLException","error while querying Token");
} 
 Log.w("result Token","About to get result");
 result=mCursor.getString(0);
 Log.w("result Token",result);
  return result;
 }
 
 
 public void insertAUser(String username, String password, String token,String ratings) 
 {	 
	  createUser(username,password,token,ratings);
 }
 
 
 public void insertSomeUsers() {
 
  createUser("Raman","Raman@123","0","3.5");
  createUser("Mike","Mike@123","0","4.5");
  createUser("Krishnan","Krishnan@123","0","5");
  createUser("Peter","Peter@123","0","4");
  createUser("Sam","Sam@123","0","2"); 
 }
 
}
