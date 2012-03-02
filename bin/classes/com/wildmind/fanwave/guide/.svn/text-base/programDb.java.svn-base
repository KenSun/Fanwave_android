package com.wildmind.fanwave.guide;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.program.TVProgram;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class programDb extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "ProgramDb.db";
	private static final int DATABASE_VERSION = 10;
	SQLiteDatabase db;
	Context context;

	public programDb() {
		super(ApplicationManager.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);			
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("Create Table IF NOT EXISTS  Programs("
				+ "pgname," + "subtitle," + "pgid," + "chcode," + "country," + "start," + "end,"
				+ "has_video," + "is_hot," + "is_recommend," 
				+ "has_event, PRIMARY KEY ( chcode , start , pgid ))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS config");	
	}
	

	public boolean AddNew(TVProgram tvpg , String country) {

		try { 
			db = this.getWritableDatabase();
			db.execSQL("INSERT OR IGNORE INTO Programs(pgname,subtitle,pgid,chcode,country,start,end,has_video,is_hot,is_recommend,has_event) values('"
					+ encode(tvpg.getTitle())
					+ "','"
					+ encode(tvpg.getSubTitle())
					+ "','"
					+ tvpg.getPgid()
					+ "','"
					+ tvpg.getChannelCode()
					+ "','"
					+ country
					+ "','"
					+ tvpg.getStartTime()
					+ "','"
					+ tvpg.getEndTime()
					+ "','"
					+ tvpg.hasVideo()
					+ "','"
					+ tvpg.isHot()
					+ "','"
					+ tvpg.isRecommend()
					+ "','"
					+ tvpg.hasEvent()
					+ "')");

			return true;

		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean deleteoldertime(String time) {
		try {
			db = this.getWritableDatabase();
			db.delete("programs", "end <= '" + time + "'", null);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean deleteall() {
		String sql = "";
		try {

			sql = "delete from Programs";
			db = this.getWritableDatabase();
			db.execSQL(sql);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public ArrayList<TVProgram> getData(String start,String end) {
		ArrayList<TVProgram> Lp = new ArrayList<TVProgram>(); 

		Cursor cur = null;
		try {
			db = this.getReadableDatabase();
			 cur = db.rawQuery(
					"SELECT DISTINCT * FROM Programs WHERE end > '" + start + "' And start < '" + end + "' GROUP BY chcode, Start ORDER BY chcode, start", null);
			cur.moveToFirst();

			for (int i = 0; i < cur.getCount(); i++) {
				
				boolean has_video = false;
				boolean is_hot = false;
				boolean is_recommend = false;
				boolean has_event = false;
				has_video = cur.getInt(7) == 1 ? true : false;
				is_hot = cur.getInt(8) ==1 ? true : false;
				is_recommend = cur.getInt(9) ==1 ? true : false;
				has_event = cur.getInt(10) ==1 ? true : false;
				
				TVProgram tvp = new TVProgram( decode(cur.getString(0)),  decode(cur.getString(1)), cur.getString(2), cur.getString(3), 
						"", cur.getString(4), "", cur.getString(5), cur.getString(6), has_video, is_hot, is_recommend, has_event);
				Lp.add(tvp);
				cur.moveToNext();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (cur != null) {
				cur.close();
				cur = null;
			}
		}finally {
			if (cur != null) {
				cur.close();
				cur = null;
			}
		}

		return Lp;

	}

	public TVProgram getpgname(String pgname,String time) {
		
		Cursor cur =null;
			try {
				db = this.getReadableDatabase();
				cur = db.rawQuery(
				"select * from Programs where pgname='" + pgname + "' and end >= '"+time+"' order by end asc limit 1", null);
				cur.moveToFirst();
				boolean has_video = false;
				boolean is_hot = false;
				boolean is_recommend = false;
				boolean has_event = false;
				has_video = cur.getInt(7) ==1 ? true : false;
				is_hot = cur.getInt(8) ==1 ? true : false;
				is_recommend = cur.getInt(9) ==1 ? true : false;
				has_event = cur.getInt(10) ==1 ? true : false;
				
				TVProgram tvp = new TVProgram( decode(cur.getString(0)),  decode(cur.getString(1)), cur.getString(2), cur.getString(3), 
						"", cur.getString(4), "", cur.getString(5), cur.getString(6), has_video, is_hot, is_recommend, has_event);
				cur.close();
				return tvp;

			} catch (Exception e) {
				if (cur != null) {
					cur.close();
					cur = null;
				}
				return null;
			}finally {
				if (cur != null) {
					cur.close();
					cur = null;
				}
			}


	}

	public void close() {
		db.close();
		
	}

	
	public String encode(String data) throws UnsupportedEncodingException{
		return URLEncoder.encode(data,"UTF-8");
	}
	
	public String decode(String data) throws UnsupportedEncodingException{
		return URLDecoder.decode(data,"UTF-8");
	}
}
