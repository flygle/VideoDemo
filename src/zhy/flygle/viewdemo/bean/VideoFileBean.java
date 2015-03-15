package zhy.flygle.viewdemo.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import android.os.Environment;

public class VideoFileBean implements Serializable{
	
	private String videoName;
	private String filePath;
	private String createTime;
	
	public VideoFileBean() {
	}
	
	public VideoFileBean(String createTime) throws IOException {
		String dir = Environment.getExternalStorageDirectory()+File.separator+"videodemo";
		File file = new File(dir);
		if(!file.exists()){
			file.mkdir();
		}
		this.createTime = createTime;
		this.videoName = createTime + ".mp4";
		this.filePath = dir+File.separator+videoName;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
