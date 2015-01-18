package com.hua.test.activity;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.hua.test.contants.Url;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.ACache;

public class BaseActivity extends FragmentActivity {
	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();

	@Override
	protected void onResume() {
		super.onResume();
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnResume();
			}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnPause();
			}
	}

	public static abstract interface BackPressHandler {

		public abstract void activityOnResume();

		public abstract void activityOnPause();

	}
	
	

    public String getUrl(String newId) {
        return Url.NewDetail + newId + Url.endDetailUrl;
    }

    public String getMsgUrl(String index, String itemId) {
        String urlString = Url.CommonUrl + itemId + "/" + index + "-40.html";
        return urlString;
    }

    public String getWeatherUrl(String cityName) {
        String urlString = Url.WeatherHost + cityName + Url.WeatherKey;
        return urlString;
    }
    

    /**
     * 显示LongToast
     */
    public void showShortToast(String pMsg) {
        // ToastUtil.createCustomToast(this, pMsg, Toast.LENGTH_LONG).show();
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    
    
    /**
     * 返回
     */
    public void doBack(View view) {
        onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    	
    }


    /**
     * 判断是否有网络
     * 
     * @return
     */
    public boolean hasNetWork() {
        return HttpUtil.isNetworkAvailable(this);
    }

	
    
    /**
     * 设置缓存数据（key,value）
     */
    public void setCacheStr(String key, String value) {
        ACache.get(this).put(key, value);
    }

    /**
     * 获取缓存数据更具key
     */
    public String getCacheStr(String key) {
        return ACache.get(this).getAsString(key);
    }

	
}
