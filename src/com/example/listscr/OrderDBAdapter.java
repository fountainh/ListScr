package com.example.listscr;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class OrderDBAdapter {
 
 public static final String KEY_ROWID = "_id";
 public static final String KEY_SENDERID = "senderId";
 public static final String KEY_RECVID = "recvId";
 public static final String KEY_JOBID = "jobId";
 public static final String KEY_TIMESTAMP = "timestamp";
 public static final String KEY_TITLE = "title";
 public static final String KEY_SUMMARY = "summary";
 public static final String KEY_COST = "cost"; 
 public static final String KEY_ECD = "ecd";
 
 
 private static final String TAG = "JobsDbAdapter";
 private DatabaseHelper mDbHelper;
 private SQLiteDatabase mDb;
 
 private static final String DATABASE_NAME = "AppTraderDB22";
 private static final String SQLITE_TABLE = "AppTraderOrders2";
 private static final int DATABASE_VERSION = 1;
 
 private final Context mCtx;
 
 private static final String DATABASE_CREATE =
  "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
  KEY_ROWID + " integer PRIMARY KEY autoincrement,"+
  KEY_SENDERID+ "," +
  KEY_RECVID+ "," +
  KEY_JOBID+ "," +
  KEY_TIMESTAMP + "," +
  KEY_TITLE + "," +
  KEY_SUMMARY + "," +
  KEY_COST + "," +
  KEY_ECD +
  ");";
 
 private static class DatabaseHelper extends SQLiteOpenHelper {
 
  DatabaseHelper(Context context) {

   super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  Log.w("incon","incon");
  }
 
 
  @Override
  public void onCreate(SQLiteDatabase db) {
   Log.w("DBC2",DATABASE_CREATE);	  
   Log.w(TAG, DATABASE_CREATE);
   try{
   db.execSQL(DATABASE_CREATE);
   }
   catch(SQLException e)
   {
	   Log.w("SQLException",DATABASE_CREATE);
   }
   }
  
 
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
     + newVersion + ", which will destroy all old data");
   db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
   onCreate(db);
  }
 }
 
 public OrderDBAdapter(Context ctx) {
  this.mCtx = ctx;
 }
 
 public OrderDBAdapter open() throws SQLException {
  mDbHelper = new DatabaseHelper(mCtx);
  mDb = mDbHelper.getWritableDatabase();
  return this;
 }
 
 public void close() {
  if (mDbHelper != null) {
   mDbHelper.close();
  }
 }
 
 public long createbid(String senderId,String recvId,String jobId,String timestamp,String title, String summary, 
   String cost,String ecd) 
 {
  ContentValues initialValues = new ContentValues();  
  initialValues.put(KEY_SENDERID,senderId);
  initialValues.put(KEY_RECVID,recvId);
  initialValues.put(KEY_JOBID,jobId);
  initialValues.put(KEY_TIMESTAMP,timestamp);  
  initialValues.put(KEY_TITLE, title);
  initialValues.put(KEY_SUMMARY, summary);
  initialValues.put(KEY_COST, cost);  
  initialValues.put(KEY_ECD,ecd);
  try
  {
	  mDb.insert(SQLITE_TABLE, null, initialValues);
  }
  catch(SQLException e)
  {
	  Log.w("SQLException","SQL");
  }
  
  
  
  return 0;
 }
 
 public boolean deleteAllbids() {
 
  int doneDelete = 0;
  doneDelete = mDb.delete(SQLITE_TABLE, null , null);
  Log.w(TAG, Integer.toString(doneDelete));
  return doneDelete > 0;
 
 }
 
 public Cursor fetchbidsByName(String inputText) throws SQLException {
  Log.w(TAG, inputText);
  Cursor mCursor = null;
  if (inputText == null  ||  inputText.length () == 0)  {
   mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,KEY_SENDERID,
		   KEY_RECVID,KEY_JOBID,KEY_TIMESTAMP,
     KEY_TITLE, KEY_SUMMARY, KEY_COST,KEY_ECD}, 
     null, null, null, null, null);
 
  }
  else {
   mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,KEY_SENDERID,
		   KEY_RECVID,KEY_JOBID,KEY_TIMESTAMP,
     KEY_TITLE, KEY_SUMMARY, KEY_COST,KEY_ECD}, 
     KEY_SUMMARY + " like '%" + inputText + "%'", null,
     null, null, null, null);
   
  }
  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 
 }

 
 public Cursor fetchbidforUser(String userId) {
	 Cursor mCursor =null;
try
{
  /*mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,KEY_SENDERID,
		   KEY_RECVID,KEY_JOBID,KEY_TIMESTAMP,
    KEY_TITLE, KEY_SUMMARY, KEY_COST,KEY_ECD},KEY_SENDERID + " equals '%" + userId + "%'", null,
    null, null, null, null);*/
	String[] whereArgs = new String[2];
	whereArgs[0]=userId;
	whereArgs[1]=userId;
	String sql="select * from AppTraderBids2 where (senderid=? OR recvId=?) group by jobId;";
	mCursor=mDb.rawQuery(sql, whereArgs);
}
catch(SQLException e)
{
	Log.w("SQLException","error while querying in fetchbidForUser()");
}

  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 }
 
 public Cursor fetchbidconv(String userId,String jobIdInput) {
	 Cursor mCursor =null;
try
{
  	String[] whereArgs = new String[3];
	whereArgs[0]=jobIdInput;
	whereArgs[1]=userId;
	whereArgs[2]=userId;
	Log.w("Before query",userId);
	Log.w("Before query",jobIdInput);
	String sql="select * from AppTraderBids2 where jobId=? AND (senderid=? OR recvId=?);";
	mCursor=mDb.rawQuery(sql, whereArgs);
}
catch(SQLException e)
{
	Log.w("SQLException","error while querying in fetchbidForUser()");
}

  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 }
 
 
 public Cursor fetchAllbids() {
	 Cursor mCursor =null;
try
{
  mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,KEY_SENDERID,
		   KEY_RECVID,KEY_JOBID,KEY_TIMESTAMP,
    KEY_TITLE, KEY_SUMMARY, KEY_COST,KEY_ECD},null, null, null, null, null,null);
}
catch(SQLException e)
{
	Log.w("SQLException","error while querying");
}

  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 }
 
 public void insertAbid(String senderId,String recvId,String jobId,String timestamp,String Title, String Summary, String cost,String ecd) 
 {	 
	  createbid(senderId,recvId,jobId,timestamp,Title,Summary,cost,ecd);
 }
 
 
 public void insertSomebids() {
 
  createbid("Raman","Mike","23456","051520130730","Design App","Need an aplication to design CAM Products.","500$","02222014");
  createbid("Raghav","Raman","23856","051520130330","Visualization App","Need to design an application to visualize models","800$","02222014");
  createbid("Sam","Raman","23856","051720130330","Visualization App Re:","We Need a product that will simulate Android messaging capabilities with specific enhancements","100$","08202014");
  createbid("Mike","Raghav","23857","051520130530","Encrypted: ","Need an app for encyrpted email facility. We Need mails to be stored as todo lists","800$","05222013");
  
 }
 
}
