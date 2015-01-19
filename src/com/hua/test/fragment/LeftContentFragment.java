package com.hua.test.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hua.test.activity.R;
/**
 * 左边的ListView Item 
 * @author yue
 *
 */
public class LeftContentFragment extends Fragment implements OnClickListener {

	private LinearLayout contentLayout;
	private Button pics, video, ties, tianqi, more;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_left, container, false);
	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		initContent(view);
//	}
//
//	public void initContent(View view) {
//		pics = (Button) view.findViewById(R.id.pics);
//		video = (Button) view.findViewById(R.id.video);
//		ties = (Button) view.findViewById(R.id.ties);
//		tianqi = (Button) view.findViewById(R.id.tianqi);
//		more = (Button) view.findViewById(R.id.more);
//
//		pics.setOnClickListener(this);
//		video.setOnClickListener(this);
//		ties.setOnClickListener(this);
//		tianqi.setOnClickListener(this);
//		more.setOnClickListener(this);
//
//	}

	// @Click(R.id.pics)
	public void enterPics(View view) {
		// context.openActivity(TuPianSinaActivity_.class);
//		isShow();
	}

	// @Click(R.id.video)
	public void enterVideo(View view) {
		// context.openActivity(VideoActivity_.class);
//		isShow();
	}

	// @Click(R.id.ties)
	public void enterMessage(View view) {
		// context.openActivity(MessageActivity_.class);
//		isShow();
	}

	// @Click(R.id.tianqi)
	public void enterTianQi(View view) {
		// context.openActivity(WeatherActivity_.class);
//		isShow();
	}

	// @Click(R.id.more)
	public void enterMore(View view) {
		// context.openActivity(MoreActivity_.class);
//		isShow();
	}
//
//	public void isShow() {
//		if (SlidingMenuView.instance().slidingMenu.isMenuShowing()) {
//			SlidingMenuView.instance().slidingMenu.showContent();
//		}
//	}
//
//	@Override
//	public void onClick(View view) {
//
//		int code = view.getId();
//
//		switch (code) {
//		case R.id.pics:
//			enterPics(view);
//			break;
//
//		case R.id.video:
//			enterVideo(view);
//			break;
//
//		case R.id.ties:
//			enterMessage(view);
//			break;
//
//		case R.id.tianqi:
//			enterTianQi(view);
//			break;
//
//		case R.id.more:
//			enterMore(view);
//			break;
//
//		default:
//			break;
//		}
//
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
