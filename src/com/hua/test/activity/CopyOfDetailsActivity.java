package com.hua.test.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hua.test.bean.NewDetailModle;
import com.hua.test.bean.NewModle;
import com.hua.test.network.http.json.NewDetailJson;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.Options;
import com.hua.test.utils.StringUtils;
import com.hua.test.view.ProgressPieView;
import com.hua.test.widget.htmltextview.HtmlTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.analytics.MobclickAgent;

//@EActivity(R.layout.activity_details)
public class CopyOfDetailsActivity extends BaseActivity implements
		ImageLoadingListener, ImageLoadingProgressListener {

	// @ViewById(R.id.new_title)
	protected TextView newTitle;
	// @ViewById(R.id.new_time)
	protected TextView newTime;
	// @ViewById(R.id.wb_details)
	protected HtmlTextView webView;
	// @ViewById(R.id.progressPieView)
	protected ProgressPieView mProgressPieView;
	// @ViewById(R.id.new_img)
	protected ImageView newImg;
	// @ViewById(R.id.img_count)
	protected TextView imgCount;
	// @ViewById(R.id.play)
	protected ImageView mPlay;
	private String newUrl;
	private NewModle newModle;
	private String newID;
	protected ImageLoader imageLoader;
	private String imgCountString;

	protected DisplayImageOptions options;

	private NewDetailModle newDetailModle;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_details);
		init() ;
		initContentView();

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void initContentView() {

		newTitle = (TextView) findViewById(R.id.new_title);
		newTime = (TextView) findViewById(R.id.new_time);
		webView = (HtmlTextView) findViewById(R.id.wb_details);
		newImg = (ImageView) findViewById(R.id.new_img);
		newImg.setOnClickListener(new MyOnClickListener());
		imgCount = (TextView) findViewById(R.id.img_count);
		mPlay = (ImageView) findViewById(R.id.play);
		mProgressPieView = (ProgressPieView) findViewById(R.id.progressPieView);
		initWebView();
	}

	// @AfterInject
	public void init() {
		try {
			newModle = (NewModle) getIntent().getExtras().getSerializable(
					"newModle");
			newID = newModle.getDocid();
			newUrl = getUrl(newID);
			LogUtils2.e("newUrl==== "+newUrl);
			imageLoader = ImageLoader.getInstance();
			options = Options.getListOptions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("JavascriptInterface")
	// @AfterViews
	public void initWebView() {
		try {
			mProgressPieView.setShowText(true);
			mProgressPieView.setShowImage(false);
			// WebSettings settings = webView.getSettings();
			// settings.setJavaScriptEnabled(true);// 设置可以运行JS脚本
			// settings.setDefaultFontSize(16);
			// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			// settings.setSupportZoom(false);// 用于设置webview放大
			// settings.setBuiltInZoomControls(false);
			// webView.setBackgroundResource(R.color.transparent);
			// webView.setWebViewClient(new MyWebViewClient());
			showProgressDialog();
			loadData(newUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadData(String url) {
		if (hasNetWork()) {
			loadNewDetailData(url);
		} else {
			dismissProgressDialog();
			showShortToast(getString(R.string.not_network));
			String result = getCacheStr(newUrl);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	// @Background
	public void loadNewDetailData(String url) {
		String result;
		try {
			// result = HttpUtil.getByHttpClient(this, url);
			// getResult(result);
			new GetDataTask().execute(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @UiThread
	public void getResult(String result) {
		newDetailModle = NewDetailJson.instance(this).readJsonNewModles(result,
				newID);
		if (newDetailModle == null)
			return;
		setCacheStr(newUrl, result);
		if (!"".equals(newDetailModle.getUrl_mp4())) {
			imageLoader.displayImage(newDetailModle.getCover(), newImg,
					options, this, this);
			newImg.setVisibility(View.VISIBLE);
		} else {
			if (newDetailModle.getImgList().size() > 0) {
				imgCountString = "共" + newDetailModle.getImgList().size() + "张";
				imageLoader.displayImage(newDetailModle.getImgList().get(0),
						newImg, options, this, this);
				newImg.setVisibility(View.VISIBLE);
			}
		}
		newTitle.setText(newDetailModle.getTitle());
		newTime.setText("来源：" + newDetailModle.getSource() + " "
				+ newDetailModle.getPtime());
		String content = newDetailModle.getBody();
		content = content.replace("<!--VIDEO#1--></p><p>", "");
		content = content.replace("<!--VIDEO#2--></p><p>", "");
		content = content.replace("<!--VIDEO#3--></p><p>", "");
		content = content.replace("<!--VIDEO#4--></p><p>", "");
		content = content.replace("<!--REWARD#0--></p><p>", "");
		webView.setHtmlFromString(content, false);
		dismissProgressDialog();
		// webView.loadDataWithBaseURL(null, content, "text/html", "utf-8",
		// null);
	}

	// @Click(R.id.new_img)

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			imageMore(v);
		}

	}

	public void imageMore(View view) {
		try {
			Bundle bundle = new Bundle();
			bundle.putSerializable("newDetailModle", newDetailModle);
			LogUtils2.e("newDetailModle.getUrl_mp4()==  "
					+ newDetailModle.getUrl_mp4());
			if (!"".equals(newDetailModle.getUrl_mp4())) {
				bundle.putString("playUrl", newDetailModle.getUrl_mp4());
				// openActivity(VideoPlayActivity_.class, bundle, 0);
			} else {
				openActivity(ImageDetailActivity.class, bundle, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			// progressBar.setVisibility(View.GONE);
			dismissProgressDialog();
			webView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// progressBar.setVisibility(View.GONE);
			dismissProgressDialog();
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		mProgressPieView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (!"".equals(newDetailModle.getUrl_mp4())) {
			mPlay.setVisibility(View.VISIBLE);
		} else {
			imgCount.setVisibility(View.VISIBLE);
			imgCount.setText(imgCountString);
		}
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onProgressUpdate(String imageUri, View view, int current,
			int total) {
		int currentpro = (current * 100 / total);
		if (currentpro == 100) {
			mProgressPieView.setVisibility(View.GONE);
			mProgressPieView.setShowText(false);
		} else {
			mProgressPieView.setVisibility(View.VISIBLE);
			mProgressPieView.setProgress(currentpro);
			mProgressPieView.setText(currentpro + "%");
		}
	}

	private static final int RESPONSE_OK = 0;

	Handler mHandler = new Handler() {
		public void handleMessage(Message message) {
			int what = message.what;
			int numchange = what;
			// LogUtils2.i("what==" + what);
			switch (what) {

			// case START_BAR:
			// if (mViewPager != null) {
			// LogUtils2.d("999999999utyuiyiyiuyui");
			// mViewPager.setCurrentItem(currentItem);
			// LogUtils2.d("mViewPager.getCurrentItem()=="
			// + mViewPager.getCurrentItem());
			// adapter.notifyDataSetChanged();
			//
			// }
			//
			case RESPONSE_OK:
				String result = (String) message.obj;
				getResult(result);
				// mHandler.obtainMessage(ShowFootView).sendToTarget();
				break;
			//
			// case ShowFootView:
			// if(mFooterView.getVisibility() == View.GONE){
			// mFooterView.setVisibility(View.VISIBLE);
			// }
			// isShowFirstIn = true;
			// /**
			// * 停止加载更多 恢复原样
			// */
			// stopLoadMore();
			//
			// break;
			default:
				break;
			}

		};
	};

	private class GetDataTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LogUtils2.e("GetDataTask onPreExecute ----");
		}

		// 后台处理部分
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = HttpUtil.getByHttpClient(getApplicationContext(),
						params[0]);

			} catch (Exception e) {
				e.printStackTrace();
				LogUtils2.e("GetDataTask get Data error ----");
			}
			LogUtils2.i("get data from network result == " + result);
			return result;
		}

		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(String result) {
			// 在头部增加新添内容
			// Toast.makeText(getActivity(), "lal", 300).show();
			super.onPostExecute(result);// 这句是必有的，AsyncTask规定的格式
			// if (mPullRefreshListView != null) {
			Message msg = new Message();
			msg.obj = result;
			msg.what = RESPONSE_OK;
			mHandler.sendMessage(msg);
			// mPullRefreshListView.onRefreshComplete();
			// }

		}
	}

}
