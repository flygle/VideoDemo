package zhy.flygle.viewdemo.manager;

import java.io.IOException;
import zhy.flygle.viewdemo.bean.VideoFileBean;
import zhy.flygle.viewdemo.manager.MediaPlayerManager.OnMediaPlayerListener;
import zhy.flygle.viewdemo.manager.MediaRecorderManager.OnMediaRecorderListener;
import zhy.flygle.viewdemo.util.Util;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

public class MediaManager implements OnMediaRecorderListener,
		OnMediaPlayerListener {

	private static MediaManager mediaManager;

	private MediaRecorderManager recorderManager;
	private CameraManager cameraManager;
	private MediaPlayerManager mediaPlayerManager;

	private OnMediaListener onMediaListener;
	
	

	private SurfaceHolder surfaceHolder;
	private Context context;
	
	private State state = State.CAMERA;
	
	enum State{
		CAMERA,RECORD,PLAY
	}


	public MediaManager(Context context, SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;

		cameraManager = CameraManager.getInstance(surfaceHolder);

		recorderManager = MediaRecorderManager.getInstance(context,
				surfaceHolder, cameraManager);

		recorderManager.setOnMediaRecorderListener(this);

		mediaPlayerManager = MediaPlayerManager.getInstance(surfaceHolder);

		mediaPlayerManager.setOnMediaPlayerListener(this);

	}

	public static MediaManager getInstance(Context context,
			SurfaceHolder surfaceHolder) {
		if (null == mediaManager) {
			synchronized (MediaManager.class) {
				if (null == mediaManager) {
					mediaManager = new MediaManager(context, surfaceHolder);
				}
			}
		}
		return mediaManager;
	}

	public void setOnMediaListener(OnMediaListener onMediaListener) {
		this.onMediaListener = onMediaListener;
	}

	public interface OnMediaListener {

		void onRecorderStart(VideoFileBean bean);

		void onRecorderStop();

		void onPlayeStart(String filepath);

		void onPause(String playpath);

		void onStopPlay();

		void onCompletion(MediaPlayer mp);

	}

	@Override
	public void onRecorderStart(VideoFileBean bean) {
		onMediaListener.onRecorderStart(bean);
	}

	@Override
	public void onRecorderStop() {
		onMediaListener.onRecorderStop();
	}

	public void recorder() {
		if (state != State.RECORD) {
			try {
				if(state ==State.CAMERA){
					cameraManager.stopForRecord();
				}else{
					mediaPlayerManager.stopPlay();
				}
				recorderManager.startRecorder();
				state = State.RECORD;
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				recorderManager.stopRecorder();
				cameraManager.recorderToCamera();
				state = State.CAMERA;
			}

		} else {
			recorderManager.stopRecorder();
			cameraManager.recorderToCamera();
			state = State.CAMERA;
		}

	}

	//播放
	public void paly(String filepath) {
		if(Util.getFile(filepath)){
			if (state != State.PLAY) {
				try {
					if(state == State.PLAY){
						mediaPlayerManager.onContinue();
					}
					if(state ==State.CAMERA){
						cameraManager.stopForPlay();
					}else if (state == State.RECORD){
						recorderManager.stopRecorder();
					}
					mediaPlayerManager.startPlay(filepath);
					
					state = State.PLAY;
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
					
					playtoCamera();
					state = State.CAMERA;
				}
	
			} else {
				mediaPlayerManager.pause();
			}
		}else{
			Util.showToast("无文件",context);
			playtoCamera();
			state = State.CAMERA;
		}
	}

	private void playtoCamera() {
		mediaPlayerManager.stopPlay();
		cameraManager.playToCamera();
	}

	public void preparaCamera() {
		cameraManager.preparaCamera();
	}

	public void startPreView() {
		cameraManager.startPreView();
		state = State.CAMERA;
	}

	public void destroyMedia() {
		if(state==State.RECORD){
			recorderManager.destroyRecorder();
		}
		cameraManager.destroyCamera();
		mediaPlayerManager.destroyPlay();
	}

	public void stop() {
		if(state==State.PLAY){
			playtoCamera();
		}else if (state==State.RECORD){
			recorderManager.stopRecorder();
			cameraManager.recorderToCamera();
		}
		state = State.CAMERA;
		
	}

	@Override
	public void onPlayeStart(String filepath) {
		onMediaListener.onPlayeStart( filepath);
	}

	@Override
	public void onPause(String playpath) {
		onMediaListener.onPause( playpath);
	}

	@Override
	public void onStopPlay() {
		onMediaListener.onStopPlay();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		
		playtoCamera();
		state = State.CAMERA;
		
//		onMediaListener.onCompletion(mp);
	}

}
