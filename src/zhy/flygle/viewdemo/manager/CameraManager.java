package zhy.flygle.viewdemo.manager;

import java.io.IOException;
import java.util.List;

import zhy.flygle.viewdemo.util.CameraUtil;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;

public class CameraManager {
	
	private static CameraManager cameraManager;
	
	private Camera camera;
	
	private SurfaceHolder surfaceHolder;
	
	public CameraManager(SurfaceHolder surfaceHolder) {
		this.surfaceHolder=surfaceHolder;
	}

	public static CameraManager getInstance(SurfaceHolder surfaceHolder) {
		if(null==cameraManager){
			synchronized (CameraManager.class) {
				if(null==cameraManager){
					cameraManager = new CameraManager(surfaceHolder);
				}
			}
		}
		return cameraManager;
	}

	public void initCamera() {
		int CammeraIndex=CameraUtil.FindBackCamera();  
        if(CammeraIndex==-1){  
            CammeraIndex=CameraUtil.FindFrontCamera();  
        }
		camera = Camera.open(CammeraIndex);
		
		camera.setDisplayOrientation(90);
	}
	
	public Camera getCamera() {
		return this.camera;
	}

	public void preparaCamera() {
		if (camera == null) {
 			initCamera();
 			try {
 				autoFocus();
 				camera.setPreviewDisplay(surfaceHolder);
 			} catch (IOException e) {
 				e.printStackTrace();
 				camera.release();
 			}
 		}
	}
	
	private void autoFocus() {
		Parameters parameters = camera.getParameters();
		List<String> focusModes = parameters.getSupportedFocusModes();
		System.out.println("focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO----------"+focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO));
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
		{
		    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}
		camera.setParameters(parameters);
	}

	public void startPreView() {
		camera.startPreview();
	}
	
	
	public void stopForPlay() {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera=null;
		}
	}

	
	public void destroyCamera() {
		stopForPlay();
		camera = null;
	}


	public void playToCamera() {
		preparaCamera();
		camera.startPreview();
	}
	
	public void stopForRecord() {
		if (null!=camera) {
			camera.stopPreview();
		}else{
			initCamera();
		}
		camera.unlock();
	}
	
	
	public void recorderToCamera() {
		try {
			
			autoFocus();
			
				camera.setPreviewDisplay(surfaceHolder);
			} catch (IOException e) {
				e.printStackTrace();
				camera.release();
			}
		camera.startPreview();
	}
	


}
