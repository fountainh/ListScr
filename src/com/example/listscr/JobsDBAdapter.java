package com.example.listscr;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class JobsDBAdapter {
 
 public static final String KEY_ROWID = "_id";
 public static final String KEY_TITLE = "title";
 public static final String KEY_SUMMARY = "summary";
 public static final String KEY_BUDGET = "budget";
 public static final String KEY_SOURCER = "sourcer";
 public static final String KEY_ECD = "ECD";
 
 private static final String TAG = "JobsDbAdapter";
 private DatabaseHelper mDbHelper;
 private SQLiteDatabase mDb;
 
 private static final String DATABASE_NAME = "AppTraderDB";
 private static final String SQLITE_TABLE = "AppTraderJobs";
 private static final int DATABASE_VERSION = 1;
 
 private final Context mCtx;
 
 private static final String DATABASE_CREATE =
  "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
  KEY_ROWID + " integer PRIMARY KEY autoincrement," +
  KEY_TITLE + "," +
  KEY_SUMMARY + "," +
  KEY_BUDGET + "," +
  KEY_SOURCER + "," +
  KEY_ECD + "," +
  " UNIQUE (" + KEY_TITLE +"));";
 
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
 
 public JobsDBAdapter(Context ctx) {
  this.mCtx = ctx;
 }
 
 public JobsDBAdapter open() throws SQLException {
  mDbHelper = new DatabaseHelper(mCtx);
  mDb = mDbHelper.getWritableDatabase();
  return this;
 }
 
 public void close() {
  if (mDbHelper != null) {
   mDbHelper.close();
  }
 }
 
 public long createJob(String title, String summary, 
   String budget, String sourcer,String ecd) {
 
  ContentValues initialValues = new ContentValues();
  initialValues.put(KEY_TITLE, title);
  initialValues.put(KEY_SUMMARY, summary);
  initialValues.put(KEY_BUDGET, budget);
  initialValues.put(KEY_SOURCER, sourcer);
  initialValues.put(KEY_ECD,ecd);
 
  return mDb.insert(SQLITE_TABLE, null, initialValues);
 }
 
 public boolean deleteAllJobs() {
 
  int doneDelete = 0;
  doneDelete = mDb.delete(SQLITE_TABLE, null , null);
  Log.w(TAG, Integer.toString(doneDelete));
  return doneDelete > 0;
 
 }
 
 public Cursor fetchJobsByName(String inputText) throws SQLException {
  Log.w(TAG, inputText);
  Cursor mCursor = null;
  if (inputText == null  ||  inputText.length () == 0)  {
   mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
     KEY_TITLE, KEY_SUMMARY, KEY_BUDGET, KEY_SOURCER,KEY_ECD}, 
     null, null, null, null, null);
 
  }
  else {
   mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
     KEY_TITLE, KEY_SUMMARY, KEY_BUDGET, KEY_SOURCER,KEY_ECD}, 
     KEY_SUMMARY + " like '%" + inputText + "%'", null,
     null, null, null, null);
  }
  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 
 }
 
 public Cursor fetchAllJobs() {
 
  Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,KEY_TITLE, KEY_SUMMARY, KEY_BUDGET, KEY_SOURCER,KEY_ECD},null, null, null, null, null);
 
  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 }
 
 public void insertAJob(String Title, String Description, String Budget,String Sourcer,String ecd) 
 {	 
	  createJob(Title,Description,Budget,Sourcer,ecd);
 }
 
 
 public void insertSomeJobs() {
 
  createJob("Design App","Need an aplication to design CAM Products.","500$","Raman","02222014");
  createJob("Visualization App","Need to design an application to visualize models","800$","Raghav","02222014");
  createJob("Messaging App","We Need a product that will simulate Android messaging capabilities with specific enhancements","300$","Sam","08202014");
  createJob("Secure Email","Need an app for encyrpted email facility. We Need mails to be stored as todo lists","800$","Mike","05222013");
  createJob("Weather Trip","Need an app to tell weather along a specfic map route","600$","Han","09202013"); 
 }
 
}
