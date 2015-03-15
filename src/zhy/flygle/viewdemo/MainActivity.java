package zhy.flygle.viewdemo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import zhy.flygle.viewdemo.adapter.FileListAdapter;
import zhy.flygle.viewdemo.bean.VideoFileBean;
import zhy.flygle.viewdemo.db.impl.VideoFileImpl;
import zhy.flygle.viewdemo.manager.MediaManager;
import zhy.flygle.viewdemo.manager.MediaManager.OnMediaListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements SurfaceHolder.Callback , OnClickListener , OnItemClickListener,OnItemLongClickListener ,OnMediaListener{
	
	private SurfaceView video_surfaceview;
	private Button btn_mediarecorder;
	private Button btn_mediaplay;
	private Button btn_stop_all;
	private Button btn_delete_all;
	private ListView listview_file;
	
	private SurfaceHolder surfaceHolder;
	
	private FileListAdapter adapter;
	
	
	private MediaManager mediaManager;

	private Builder builder;
	
	private String filepath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��  
        // ���ú�����ʾ  
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
        // ѡ��֧�ְ�͸��ģʽ,����surfaceview��activity��ʹ�á�  
        getWindow().setFormat(PixelFormat.TRANSLUCENT);  
		
		setContentView(R.layout.activity_main);
		
		initView();
		
		initData();
	}

	private void initData() {
		
		List<VideoFileBean> beans = VideoFileImpl.getInstance(MainActivity.this).findAllVideo();
			
		adapter = new FileListAdapter(beans,MainActivity.this);
			
		listview_file.setAdapter(adapter);
		
		
		mediaManager = MediaManager.getInstance(MainActivity.this , surfaceHolder);
		
		mediaManager.setOnMediaListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.refresh();
	}

	private void initView() {
		video_surfaceview=(SurfaceView) findViewById(R.id.video_surfaceview);
		btn_mediarecorder=(Button) findViewById(R.id.btn_mediarecorder);
		btn_mediaplay=(Button) findViewById(R.id.btn_mediaplay);
		btn_stop_all=(Button) findViewById(R.id.btn_stop_all);
		btn_delete_all=(Button) findViewById(R.id.btn_delete_all);
		listview_file=(ListView) findViewById(R.id.listview_file);
		
		surfaceHolder = video_surfaceview.getHolder();
		surfaceHolder.addCallback(this);
		 // setType�������ã�Ҫ������.  
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);����
		//����surfaceview��ά���Լ��Ļ����������ǵȴ���Ļ����Ⱦ���潫�������͵��û���ǰ
		
		btn_mediaplay.setOnClickListener(this);
		btn_delete_all.setOnClickListener(this);
		btn_mediarecorder.setOnClickListener(this);
		
		listview_file.setOnItemClickListener(this);
		listview_file.setOnItemLongClickListener(this);
		
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mediarecorder:
			
			mediaManager.recorder();
			
			break;
		case R.id.btn_mediaplay:
			
			mediaManager.paly(filepath);
			
			break;
		case R.id.btn_stop_all:
			
			mediaManager.stop();
			
			break;
		case R.id.btn_delete_all:
			
			if(null==builder){
				builder=new AlertDialog.Builder(MainActivity.this)
				.setTitle("ɾ��")
				.setCancelable(false)
				.setNegativeButton("ȡ��", null);
			}
			builder.setMessage("ɾ������").setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					adapter.clear();
					VideoFileImpl.getInstance(MainActivity.this).clear();
				}
				
			}).show();
			
			break;

		default:
			break;
		}
	}


	/**
	 * ��ʼ¼���ص�
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Override
	public void onRecorderStart(VideoFileBean bean) {
		VideoFileImpl.getInstance(MainActivity.this).insertVideoFile(bean);
		
		adapter.addNew(bean);
		
		changeButtonText(true);
	}

	
	/**
	 * ֹͣ¼���ص�
	 */
	@Override
	public void onRecorderStop() {
		changeButtonText(false);
		
		adapter.notifyDataSetChanged();
		
	}
	
	@Override
	public void onPlayeStart(String filepath) {
		btn_mediaplay.setText("��ͣ");
	}

	@Override
	public void onPause(String playpath) {
		btn_mediaplay.setText("����");
	}

	@Override
	public void onStopPlay() {
		
		btn_mediaplay.setText("����");
	}
	

	@Override
	public void onCompletion(MediaPlayer mp) {
		filepath=adapter.getNext(filepath);
		mediaManager.paly(filepath);
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
     // �������
		
		mediaManager.preparaCamera();
        
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// ��ʼԤ��
		mediaManager.startPreView();
		
	}
	
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		// surfaceDestroyed��ʱ��ͬʱ��������Ϊnull  
        video_surfaceview = null;  
        surfaceHolder = null;  
        mediaManager.destroyMedia();
        
	}
	
	
	
	
	
	
	

	private void changeButtonText(boolean isRecorder) {
		if(!isRecorder){
			btn_mediarecorder.setText(getResources().getString(R.string.btn_mediarecorder_start));
		}else{
			btn_mediarecorder.setText(getResources().getString(R.string.btn_mediarecorder_stop));
		}
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		final VideoFileBean bean = (VideoFileBean) adapter.getItem(position);
		
		if(null==builder){
			builder=new AlertDialog.Builder(MainActivity.this)
			.setTitle("ɾ��")
			.setCancelable(false)
			.setNegativeButton("ȡ��", null);
		}
		builder.setMessage(bean.getVideoName()).setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adapter.remove(position);
				new File(filepath).delete();
				VideoFileImpl.getInstance(MainActivity.this).deleteVideo(bean.getFilePath());
			}
			
		}).show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		filepath = adapter.filePath(position);
		
		mediaManager.paly(filepath);
		
	}
	
}
