
package com.hua.test.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hua.test.utils.LogUtils2;
import com.hua.test.view.NewImageView;

//@EBean
public class ImageAdapter extends BaseAdapter {

    public List<String> lists = new ArrayList<String>();

    private Context context;
    private static ImageAdapter mImageAdapter;
    private int oldIndex = -1;
    
//    @RootContext
//    Context context;

    public void appendList(List<String> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

    public static ImageAdapter getImageAdapter(Context tempContext){
    	
    	if(mImageAdapter == null){
    		mImageAdapter = new ImageAdapter(tempContext);
    	}
    	return mImageAdapter;
    	
    }
    
    public ImageAdapter (Context tempContext){
    	if(tempContext != null){
    		context = tempContext;
    	}
    	
//    	if(lists == null){
//    		LogUtils2.i("***********NewAdapter.lists==null******");
//    		 lists = new ArrayList<String>();
//    	}else {
//    		LogUtils2.d("***********NewAdapter.lists != null******");
//			lists = getLists();
//		}
    	
    }


    
    public List<String> getLists() {
		return lists;
	}



	public void setLists(List<String> lists) {
		this.lists = lists;
	}



	public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewImageView newImageView;
        if (convertView == null) {
//            newImageView = NewImageView_.build(context);
        	newImageView = new NewImageView(context);
        } else {
            newImageView = (NewImageView) convertView;
        }

        newImageView.setImage(lists, position);

        return newImageView;
    }

}
