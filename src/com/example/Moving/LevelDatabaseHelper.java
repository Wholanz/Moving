package com.example.Moving;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class LevelDatabaseHelper extends SQLiteOpenHelper {

	private final static String LOG_TAG="Database Helper";
	
	private Context mContext;

	private final String CREATE_TABLE_PERMISSION = 
	"create table levellock(id integer primary key autoincrement,"
	+ "level int,"
	+ "lock int,"
	+ "difficulty int"
	+")";
	public LevelDatabaseHelper(Context context,String name,int version) {
		super(context,name,null,version);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PERMISSION);
		initDatabase(db);
		//Toast.makeText(mContext, "Database create success!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		switch(oldversion){
		
		}
		
	}
	
	
	
	public void initDatabase(SQLiteDatabase db){
		final String insertion = "insert into levellock(level,"
				+ "lock,difficulty) "
				+ "values(?,?,?)";
		
		
		db.execSQL(insertion, new Object[]{0,0,0});
		db.execSQL(insertion, new Object[]{1,1,0});
		db.execSQL(insertion, new Object[]{2,1,0});
		db.execSQL(insertion, new Object[]{3,1,0});
		db.execSQL(insertion, new Object[]{4,1,0});
		db.execSQL(insertion, new Object[]{5,1,0});
		db.execSQL(insertion, new Object[]{6,1,0});
		db.execSQL(insertion, new Object[]{0,0,1});
		db.execSQL(insertion, new Object[]{1,1,1});
		db.execSQL(insertion, new Object[]{2,1,1});
		db.execSQL(insertion, new Object[]{3,1,1});
		db.execSQL(insertion, new Object[]{4,1,1});
		db.execSQL(insertion, new Object[]{5,1,1});
		db.execSQL(insertion, new Object[]{6,1,1});
		db.execSQL(insertion, new Object[]{0,0,2});
		db.execSQL(insertion, new Object[]{1,1,2});
		db.execSQL(insertion, new Object[]{2,1,2});
		db.execSQL(insertion, new Object[]{3,1,2});
		db.execSQL(insertion, new Object[]{4,1,2});
		db.execSQL(insertion, new Object[]{5,1,2});
		db.execSQL(insertion, new Object[]{6,1,2});
	}
	public static void resetDatabase(SQLiteDatabase db){
		final String resetLock = "update levellock set lock=? ";
		final String resetNotLock = "update levellock set lock=? where level=? ";
		db.execSQL(resetLock, new Object[]{1});
		db.execSQL(resetNotLock, new Object[]{0,0});
	}
	
	public static void Unlock( SQLiteDatabase db, int level, int difficulty){
		
		final String unlock = "update levellock set lock=? where level=? and difficulty=?";
		db.execSQL(unlock, new Object[]{0,level,difficulty});;
	}
	
}