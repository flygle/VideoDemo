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
		
		
		// ����¼����ƵԴΪCamera(���) 
		mediaRecorder.setCamera(cameraManager.getCamera());
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediaRecorder.setOrientationHint(90);//��Ƶ��ת90��
		// ����¼����ɺ���Ƶ�ķ�װ��ʽTHREE_GPPΪ3gp.MPEG_4Ϊmp4  
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//		 ����¼�Ƶ���Ƶ����h263 h264  
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
		// ������Ƶ¼�Ƶķֱ��ʡ�����������ñ���͸�ʽ�ĺ��棬���򱨴�  
		mediaRecorder.setVideoSize(Util.getDisplayPixels(context)[0], Util.getDisplayPixels(context)[1]);
		// ����¼�Ƶ���Ƶ֡�ʡ�����������ñ���͸�ʽ�ĺ��棬���򱨴�  
		mediaRecorder.setVideoFrameRate(50);
		//������Ƶ������
		mediaRecorder.setVideoEncodingBitRate(5*1024*1024);
//		����Ԥ��
		mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
		// ������Ƶ�ļ������·��  
		VideoFileBean bean = new VideoFileBean(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		mediaRecorder.setOutputFile(bean.getFilePath());
					
		// ׼��¼��  
		mediaRecorder.prepare();  
		// ��ʼ¼�� 
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
