
package com.hua.test.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hua.test.bean.NewModle;
import com.hua.test.utils.LogUtils2;
import com.hua.test.view.NewItemView;

public class YuLeAdapter extends BaseAdapter {
    public static List<NewModle> lists = new ArrayList<NewModle>();
	private String currentItem;
    private Context context;
    private static YuLeAdapter mNewAdapter;
    private int oldIndex = -1;
    private boolean isNeedUplistsModlesData;

    public void appendList(List<NewModle> list,int newIndex) {
//    	LogUtils2.d("list---"+list.size());
//    	LogUtils2.i("newIndex = "+newIndex+"   oldIndex = "+oldIndex);
        if (!lists.contains(list.get(0)) && list != null && list.size() > 0 && newIndex != oldIndex) {
        	if (newIndex == 0 && lists.size() == 0) {
				lists.addAll(list);
				isNeedUplistsModlesData = true;
				oldIndex = -1;
			} else if (newIndex == 0 && lists.size() != 0) {

			} else {
				lists.addAll(list);
				if (newIndex == 0) {
					isNeedUplistsModlesData = true;
					oldIndex = -1;
				} else {
					isNeedUplistsModlesData = true;
					oldIndex = newIndex;
				}
				LogUtils2.e("*********lists.size==***== " + lists.size());
			}
//            LogUtils2.e("*********lists.size==***== " +lists.size());
        }
        notifyDataSetChanged();
    }

    public static YuLeAdapter getYuLeAdapter(Context tempContext){
    	
    	if(mNewAdapter == null){
    		mNewAdapter = new YuLeAdapter(tempContext);
    	}
    	return mNewAdapter;
    	
    }
    
    public YuLeAdapter (Context tempContext){
    	if(tempContext != null){
    		context = tempContext;
    	}
    	
    	if(lists == null){
    		LogUtils2.i("***********NewAdapter.lists==null******");
    		 lists = new ArrayList<NewModle>();
    	}else {
    		LogUtils2.d("***********NewAdapter.lists != null******");
			lists = getLists();
		}
    	
    }


    public boolean isNeedUplistsModlesData(int newIndex){
    	return isNeedUplistsModlesData;
    }


    public List<NewModle> getLists() {
		return lists;
	}

	public void setLists(List<NewModle> lists) {
		this.lists = lists;
	}

    
    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    public void currentItem(String item) {
        this.currentItem = item;
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

        NewItemView newItemView;

        if (convertView == null) {
//            newItemView = NewItemView_.build(context);
//        	LogUtils2.e("NewItemView---context ="+context);
        	newItemView = new NewItemView(context);
        } else {
            newItemView = (NewItemView) convertView;
        }

        NewModle newModle = lists.get(position);
        if (newModle.getImagesModle() == null) {
            newItemView.setTexts(newModle.getTitle(), newModle.getDigest(),
                    newModle.getImgsrc(), currentItem);
        } else {
            newItemView.setImages(newModle);
        }

        return newItemView;
    }
}
