package zhy.flygle.viewdemo.db.dao;

import java.util.List;

import zhy.flygle.viewdemo.bean.VideoFileBean;

public interface VideoFileDAO {
	
	public void insertVideoFile(VideoFileBean videoFileBean);
	
	public List<VideoFileBean> findAllVideo();
	
	public void deleteVideo(String filePath);

	public void clear();

}
