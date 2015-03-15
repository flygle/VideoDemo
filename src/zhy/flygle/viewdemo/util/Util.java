package zhy.flygle.viewdemo.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Util {

	public static int[] getDisplayPixels(Context context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return new int []{displayMetrics.heightPixels,displayMetrics.widthPixels};
	}

	public static boolean getFile(String filepath) {
		return null!=filepath&&new File(filepath).exists();
	}

	public static void showToast(String string, Context context) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
	public static void showLongToast(String string, Context context) {
		Toast.makeText(context, string, Toast.LENGTH_LONG).show();
	}
	
	

}
