package zhy.flygle.viewdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoFileDB extends SQLiteOpenHelper{
	
	public final static String TABLENAME = "videofile";
	public final static int VERSION = 1;
	public final static String FILEPATH = "filepath";
	public final static String VIDEONAME = "videoname";
	public final static String CREATETIME = "createtime";

	public VideoFileDB(Context context) {
		super(context, TABLENAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder("create table "+TABLENAME+" ( ");
		sql.append(FILEPATH).append(" varchar(50) primary key, ")
		.append(VIDEONAME).append(" varchar(50) , ")
		.append(CREATETIME).append(" timestamp ")
		.append(" )");
		db.execSQL(sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table "+TABLENAME);
		this.onCreate(db);
	}

}
