package com.hua.test.activity;

import java.util.List;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.hua.test.adapter.ImageAdapter;
import com.hua.test.bean.NewDetailModle;
import com.hua.test.bean.NewModle;
import com.hua.test.widget.flipview.FlipView;
import com.hua.test.widget.flipview.FlipView.OnFlipListener;
import com.hua.test.widget.flipview.FlipView.OnOverFlipListener;
import com.hua.test.widget.flipview.OverFlipMode;
import com.hua.test.widget.swipeback.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;

//@EActivity(R.layout.activity_image)
public class ImageDetailActivity extends SwipeBackActivity implements
		OnFlipListener, OnOverFlipListener {
	// @ViewById(R.id.flip_view)
	protected FlipView mFlipView;
	// @ViewById(R.id.new_title)
	protected TextView newTitle;
	private NewModle newModle;
	// @Bean
	protected ImageAdapter imageAdapter;
	private List<String> imgList;
	private NewDetailModle newDetailModle;
	private String titleString;

	// @AfterInject
	public void init() {
		try {
			if (getIntent().getExtras().getSerializable("newDetailModle") != null) {
				newDetailModle = (NewDetailModle) getIntent().getExtras()
						.getSerializable("newDetailModle");
				imgList = newDetailModle.getImgList();
				titleString = newDetailModle.getTitle();
			} else {
				newModle = (NewModle) getIntent().getExtras().getSerializable(
						"newModle");
				imgList = newModle.getImagesModle().getImgList();
				titleString = newModle.getTitle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @AfterViews
	public void initView() {

		mFlipView = (FlipView) findViewById(R.id.flip_view);
		newTitle = (TextView) findViewById(R.id.new_title);
//		imageAdapter = ImageAdapter.getImageAdapter(getApplicationContext());
		imageAdapter = new ImageAdapter(getApplicationContext());
		try {
			newTitle.setText(titleString);
			imageAdapter.appendList(imgList);
			mFlipView.setOnFlipListener(this);
			mFlipView.setAdapter(imageAdapter);
			// mFlipView.peakNext(false);
			mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
			mFlipView.setOnOverFlipListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
	}

	@Override
	public  void onCreate(Bundle arg0) {
		super.onCreate(arg0);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image);
		init();
		initView();
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

}
