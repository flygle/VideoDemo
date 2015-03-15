package zhy.flygle.viewdemo.manager;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.SurfaceHolder;

public class MediaPlayerManager implements OnCompletionListener {

	private static MediaPlayerManager mediaPlayerManager;

	private OnMediaPlayerListener onMediaPlayerListener;

	private SurfaceHolder surfaceHolder;

	private MediaPlayer mediaPlayer;
	
	private String playpath;

	public MediaPlayerManager(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
	}

	public static MediaPlayerManager getInstance(SurfaceHolder surfaceHolder) {
		if (null == mediaPlayerManager) {
			synchronized (MediaPlayerManager.class) {
				if (null == mediaPlayerManager) {
					mediaPlayerManager = new MediaPlayerManager(surfaceHolder);
				}
			}
		}
		return mediaPlayerManager;
	}

	public void setOnMediaPlayerListener(
			OnMediaPlayerListener onMediaPlayerListener) {
		this.onMediaPlayerListener = onMediaPlayerListener;
	}

	public interface OnMediaPlayerListener {

		void onPlayeStart(String filepath);

		void onPause(String playpath);

		void onStopPlay();

		void onCompletion(MediaPlayer mp);
		
	}

	public boolean isPlayer() {
		return mediaPlayer.isPlaying();
	}

	public void startPlay(String filepath) throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {
		this.playpath=filepath;
		if (null == mediaPlayer) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnCompletionListener(this);
		}else{
			mediaPlayer.reset();
		}
		
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setDisplay(surfaceHolder);
		// 设置显示视频显示在SurfaceView上
		mediaPlayer.setDataSource(filepath);
		mediaPlayer.prepare();
		mediaPlayer.start();
		
		onMediaPlayerListener.onPlayeStart(filepath);
		
	}

	public void stopPlay() {
		if(null!=mediaPlayer){
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer=null;
		}
		onMediaPlayerListener.onStopPlay();
	}

	public void destroyPlay() {
		stopPlay();
		mediaPlayer=null;
		
	}

	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
		onMediaPlayerListener.onPause(playpath);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		onMediaPlayerListener.onCompletion(mp);
	}

	public void onContinue() {
		mediaPlayer.start();
	}

	
	
	

}
