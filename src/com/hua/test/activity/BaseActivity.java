package com.hua.test.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.hua.test.contants.Url;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.ACache;
import com.hua.test.utils.DialogUtil;
import com.hua.test.widget.slideingactivity.IntentUtils;

public class BaseActivity extends FragmentActivity {
	
	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();

	  private Dialog progressDialog;
	
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

	/**
	 * 更具类打开acitvity,并携带参数
	 */
	public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		if (requestCode == 0) {
//			 IntentUtils.startPreviewActivity(this, intent);
			 startActivity(intent);
		} else {
//			 IntentUtils.startPreviewActivity(this, intent, requestCode);
			 startActivityForResult(intent, requestCode);
		}
	}


    /**
     * 显示dialog
     * 
     * @param msg 显示内容
     */
    public void showProgressDialog() {
        try {

            if (progressDialog == null) {
                progressDialog = DialogUtil.createLoadingDialog(this);

            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 隐藏dialog
     */
    public void dismissProgressDialog() {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
}
