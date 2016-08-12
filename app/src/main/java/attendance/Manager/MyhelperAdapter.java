package attendance.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class MyhelperAdapter {

	Myhelper helper;

	public MyhelperAdapter(Context context) {
		helper = new Myhelper(context);
	}

	public Data getData(int id) {
		++id;
		SQLiteDatabase db = helper.getReadableDatabase();
		String str="";
		int a=0,t=0;
		Cursor cursor=null;
		String[] columns = { Myhelper.UID, Myhelper.SUBJECT, Myhelper.ATTEND, Myhelper.TOTAL };
		try {
			cursor = db.query(Myhelper.TABLE_NAME, columns, null, null, null, null, null);
			cursor.move(id);
			str = cursor.getString(cursor.getColumnIndex(Myhelper.SUBJECT));
			a = cursor.getInt(cursor.getColumnIndex(Myhelper.ATTEND));
			t = cursor.getInt(cursor.getColumnIndex(Myhelper.TOTAL));			
		} catch (Exception e) {}
		finally{
			db.close();
			if(cursor!=null)cursor.close();
			return new Data(str, a, t);
		}
	}
	
	public void update(int id, int a, int t, String name) {
		++id;
		SQLiteDatabase db = helper.getWritableDatabase();
		try{
			if (name != null)
				db.execSQL("UPDATE " + Myhelper.TABLE_NAME + " SET " + Myhelper.SUBJECT + " = '" + name + "' WHERE "+ Myhelper.UID + " = " + id);
			if (a >= 0 && t >= 0)
				db.execSQL("UPDATE " + Myhelper.TABLE_NAME + " SET " + Myhelper.TOTAL + " = " + t + ", " + Myhelper.ATTEND+ " = " + a + " WHERE " + Myhelper.UID + " = " + id);
		}catch(Exception e){
			Toast.makeText(helper.context, e+"",Toast.LENGTH_SHORT).show();
		}finally{
			db.close();
		}
	}

	public void remove(int id) {
		++id;
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.delete(Myhelper.TABLE_NAME, Myhelper.UID + " = " + id, null);
		} catch (Exception e) {
			Toast.makeText(helper.context, e + "", Toast.LENGTH_SHORT).show();
		} finally {
			db.close();
		}
	}

	public void removeAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try{
			db.execSQL("DELETE FROM " + Myhelper.TABLE_NAME);
		}catch(Exception e){
			Toast.makeText(helper.context,e+"", Toast.LENGTH_SHORT).show();
		}finally{
			db.close();
		}		
	}
	public void bunkAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try{
			db.execSQL("UPDATE " + Myhelper.TABLE_NAME + " SET " + Myhelper.TOTAL + " = " + Myhelper.TOTAL + " +1 ");
		}catch(Exception e){
			Toast.makeText(helper.context,e+"", Toast.LENGTH_SHORT).show();
		}finally{
			db.close();
		}		
	}
	public void attendAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try{
			db.execSQL("UPDATE " + Myhelper.TABLE_NAME + " SET " + Myhelper.ATTEND + " = " + Myhelper.ATTEND + " +1 ,"+ Myhelper.TOTAL + " = " + Myhelper.TOTAL+" +1 ");
		}catch(Exception e){
			Toast.makeText(helper.context,e+"", Toast.LENGTH_SHORT).show();
		}finally{
			db.close();
		}
	}

	public void storeData(Data data) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(Myhelper.SUBJECT, data.name);
		cv.put(Myhelper.ATTEND, data.a);
		cv.put(Myhelper.TOTAL, data.t);
		db.insert(Myhelper.TABLE_NAME, null, cv);
	}

	public ArrayList<Data> getAllData() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = { Myhelper.UID, Myhelper.SUBJECT, Myhelper.ATTEND, Myhelper.TOTAL };
		Cursor cursor = db.query(Myhelper.TABLE_NAME, columns, null, null, null, null, null);
		ArrayList<Data> list = new ArrayList<Data>();
		String s;
		int a, t;
		while (cursor.moveToNext()) {
			s = cursor.getString(cursor.getColumnIndex(Myhelper.SUBJECT));
			a = cursor.getInt(cursor.getColumnIndex(Myhelper.ATTEND));
			t = cursor.getInt(cursor.getColumnIndex(Myhelper.TOTAL));
			list.add(new Data(s, a, t));
		}
		cursor.close();
		return list;
	}

	static class Myhelper extends SQLiteOpenHelper {

		private static final int DATABASE_VERSION = 1;
		private static final String TABLE_NAME = "Table1";
		private static final String DATABASE_NAME = "MyDatabase";
		private static final String UID = "_id";
		private static final String SUBJECT = "name";
		private static final String ATTEND = "a";
		private static final String TOTAL = "t";
		private static final String CREATE_DATABASE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " "
				+ "INTEGER PRIMARY KEY AUTOINCREMENT," + SUBJECT + " VARCHAR(20), " + ATTEND + " int, " + TOTAL
				+ " int );";
		private static final String DROP_TABLE = "DROP TABLE IF NOT EXISTS " + TABLE_NAME;

		private Context context;

		public Myhelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_DATABASE);
			} catch (Exception e) {
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				db.execSQL(DROP_TABLE);
				onCreate(db);
			} catch (Exception e) {
			}
		}
	}
}
