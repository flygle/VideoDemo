package zhy.flygle.viewdemo.db.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import zhy.flygle.viewdemo.bean.VideoFileBean;
import zhy.flygle.viewdemo.db.VideoFileDB;
import zhy.flygle.viewdemo.db.dao.VideoFileDAO;

public class VideoFileImpl implements VideoFileDAO{
	
	private static VideoFileDAO instance;
	private SQLiteDatabase db;
	private VideoFileDB dbhelper;

	
	public VideoFileImpl(Context context) {
		db = new VideoFileDB(context).getWritableDatabase();
	}


	public static VideoFileDAO getInstance(Context context){
		if(null==instance){
			synchronized (VideoFileImpl.class) {
				if(null==instance){
					instance = new VideoFileImpl(context);
				}
			}
		}
		return instance;
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
	}
	
	private void close() {
		if(null!=db){
			db.close();
			db=null;
		}
		if(null!=dbhelper){
			dbhelper.close();
			dbhelper=null;
		}
	}


	@Override
	public void insertVideoFile(VideoFileBean videoFileBean) {
		if(null!=videoFileBean){
			String filepath=videoFileBean.getFilePath();
			if(null!=filepath){
				deleteVideo(filepath);
				ContentValues values = new ContentValues();
				values.put(VideoFileDB.FILEPATH, filepath);
				values.put(VideoFileDB.VIDEONAME, videoFileBean.getVideoName());
				values.put(VideoFileDB.CREATETIME, videoFileBean.getCreateTime());
				db.insert(VideoFileDB.TABLENAME, null, values);
			}
		}
	}

	@Override
	public List<VideoFileBean> findAllVideo() {
		List<VideoFileBean> beans = new ArrayList<VideoFileBean>();
		Cursor cursor = db.query(VideoFileDB.TABLENAME, null, null, null, null, null, VideoFileDB.CREATETIME +" desc");
		VideoFileBean bean =null;
		while(cursor.moveToNext()){
			bean = new VideoFileBean();
			bean.setFilePath(cursor.getString(cursor.getColumnIndex(VideoFileDB.FILEPATH)));
			bean.setVideoName(cursor.getString(cursor.getColumnIndex(VideoFileDB.VIDEONAME)));
			bean.setCreateTime(cursor.getString(cursor.getColumnIndex(VideoFileDB.CREATETIME)));
			beans.add(bean);
		}
		
		return beans;
	}

	@Override
	public void deleteVideo(String filePath) {
		db.delete(VideoFileDB.TABLENAME, VideoFileDB.FILEPATH + "= ? ", new String []{filePath} );
	}


	@Override
	public void clear() {
		db.delete(VideoFileDB.TABLENAME, null, null);
	}

}
