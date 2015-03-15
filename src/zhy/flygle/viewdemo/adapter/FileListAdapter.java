package zhy.flygle.viewdemo.adapter;

import java.io.File;
import java.util.List;
import zhy.flygle.viewdemo.bean.VideoFileBean;
import zhy.flygle.viewdemo.db.impl.VideoFileImpl;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FileListAdapter extends BaseAdapter{
	
	private List<VideoFileBean> beans;
	private Context context;

	public FileListAdapter(List<VideoFileBean> beans, Context context) {
		this.beans=beans;
		this.context=context;
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(context);
		textView.setSingleLine(true);
		textView.setHeight(100);
		textView.setText(beans.get(position).getVideoName());
		return textView;
	}

	public void addNew(VideoFileBean bean) {
		this.beans.add(bean);
	}

	public void remove(int position) {
		beans.remove(position);
		notifyDataSetChanged();
	}

	public void refresh() {
		VideoFileBean bean=null;
		for (int i = 0; i < beans.size(); i++) {
			bean=beans.get(i);
			String filepath = bean.getFilePath();
			if(null!=filepath){
				if(!new File(filepath).exists()){
					VideoFileImpl.getInstance(context).deleteVideo(filepath);
					beans.remove(i);
				}
			}
		}
		notifyDataSetChanged();
		
	}

	public String filePath(int position) {
		return beans.get(position).getFilePath();
	}

	public String getNext(String filepath) {
		int size = beans.size();
		String path=null;
		for (int i = 0; i < size; i++) {
			if(i!=(size-1)){
				path=beans.get(i).getFilePath();
				if(null!=path&&path.equals(filepath)){
					path=beans.get(i+1).getFilePath();
					break;
				}else{
					path=null;
				}
			}
		}
		return path;
	}

	public void clear() {
		String filepath=null;
		for (int i = 0; i < beans.size(); i++) {
			filepath=beans.get(i).getFilePath();
			if(null!=filepath)
			new File(filepath).delete();
		}
		beans.removeAll(beans);
		notifyDataSetChanged();
	}

}
