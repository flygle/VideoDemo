package zhy.flygle.viewdemo.manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import zhy.flygle.viewdemo.bean.VideoFileBean;
import zhy.flygle.viewdemo.util.Util;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

public class MediaRecorderManager {
	
	private static MediaRecorderManager mediaRecorderManager;
	
	
	private SurfaceHolder surfaceHolder;
	private CameraManager cameraManager;
	private Context context;
	
	private MediaRecorder mediaRecorder;
	
	private OnMediaRecorderListener mediaRecorderListener;
	
	
	public MediaRecorderManager(Context context, SurfaceHolder surfaceHolder,CameraManager cameraManager) {
		this.context = context;
		this.surfaceHolder=surfaceHolder;
		this.cameraManager=cameraManager;
	}

	public static MediaRecorderManager getInstance(Context context, SurfaceHolder surfaceHolder ,CameraManager cameraManager){
		if(null==mediaRecorderManager){
			synchronized (MediaRecorderManager.class) {
				if(null==mediaRecorderManager){
					mediaRecorderManager = new MediaRecorderManager(context ,surfaceHolder,cameraManager);
				}
			}
		}
		return mediaRecorderManager;
	}

	public void startRecorder() throws IOException {
		
		if(null==mediaRecorder){
			mediaRecorder = new MediaRecorder();
		}else{
			mediaRecorder.reset();
		}
		
		
		// 设置录制视频源为Camera(相机) 
		mediaRecorder.setCamera(cameraManager.getCamera());
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediaRecorder.setOrientationHint(90);//视频旋转90度
		// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4  
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//		 设置录制的视频编码h263 h264  
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
		// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错  
		mediaRecorder.setVideoSize(Util.getDisplayPixels(context)[0], Util.getDisplayPixels(context)[1]);
		// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错  
		mediaRecorder.setVideoFrameRate(50);
		//设置视频采样率
		mediaRecorder.setVideoEncodingBitRate(5*1024*1024);
//		设置预览
		mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
		// 设置视频文件输出的路径  
		VideoFileBean bean = new VideoFileBean(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		mediaRecorder.setOutputFile(bean.getFilePath());
					
		// 准备录制  
		mediaRecorder.prepare();  
		// 开始录制 
		mediaRecorder.start();
		
		mediaRecorderListener.onRecorderStart(bean);
		
	}
	
	
	public void stopRecorder() {
		if(null!=mediaRecorder){
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder=null;
		}
		mediaRecorderListener.onRecorderStop();
	}
	
	public void destroyRecorder() {
		
		stopRecorder();
		mediaRecorder=null;
	}

	public void setOnMediaRecorderListener(OnMediaRecorderListener mediaRecorderListener) {
		this.mediaRecorderListener=mediaRecorderListener;
	}
	
	
	public interface OnMediaRecorderListener{
		void onRecorderStart(VideoFileBean bean);
		void onRecorderStop();
		
	}


}
