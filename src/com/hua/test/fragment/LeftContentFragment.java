package com.hua.test.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hua.test.activity.MainActivityPhone;
import com.hua.test.activity.PictureSinaActivity;
import com.hua.test.activity.R;
import com.hua.test.activity.TestActivity;
import com.hua.test.activity.VideoActivity;
import com.hua.test.activity.WelcomeActivity;
import com.hua.test.utils.LogUtils2;
/**
 * 左边的ListView Item 
 * @author yue
 *
 */
public class LeftContentFragment extends Fragment implements OnClickListener {

	private View contentView;
	private Context context;
	private MainActivityPhone mainActivityPhone;
	private Button pics, video, ties, tianqi, more;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			
			mainActivityPhone = (MainActivityPhone) activity;
			LogUtils2.e("----------mainActivityPhone == "+mainActivityPhone);
			
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BaseActivity");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		contentView = inflater.inflate(R.layout.activity_left, container, false); 
		initContent(contentView);
		return contentView;
	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		initContent(view);
//	}
//
	public void initContent(View view) {
		pics = (Button) view.findViewById(R.id.pics);
		video = (Button) view.findViewById(R.id.video);
		ties = (Button) view.findViewById(R.id.ties);
		tianqi = (Button) view.findViewById(R.id.tianqi);
		more = (Button) view.findViewById(R.id.more);

		pics.setOnClickListener(this);
		video.setOnClickListener(this);
		ties.setOnClickListener(this);
		tianqi.setOnClickListener(this);
		more.setOnClickListener(this);

	}

	// @Click(R.id.pics)
	public void enterPics(View view) {
//		baseActivity.openActivity(WelcomeActivity.class,0);
		context.startActivity(new Intent(context, PictureSinaActivity.class));
		isShow();
	}

	// @Click(R.id.video)
	public void enterVideo(View view) {
		context.startActivity(new Intent(context, VideoActivity.class));
		isShow();
	}

	// @Click(R.id.ties)
	public void enterMessage(View view) {
		context.startActivity(new Intent(context, WelcomeActivity.class));
		isShow();
	}

	// @Click(R.id.tianqi)
	public void enterTianQi(View view) {
		context.startActivity(new Intent(context, WelcomeActivity.class));
		isShow();
	}

	// @Click(R.id.more)
	public void enterMore(View view) {
		context.startActivity(new Intent(context, WelcomeActivity.class));
		isShow();
	}
//
	public void isShow() {
		
		if (mainActivityPhone.getSlidingMenu().isMenuShowing()) {
			mainActivityPhone.getSlidingMenu().showContent();
		}
	}
//
	@Override
	public void onClick(View view) {

		int code = view.getId();

		switch (code) {
		case R.id.pics:
			enterPics(view);
			break;

		case R.id.video:
			enterVideo(view);
			break;

		case R.id.ties:
			enterMessage(view);
			break;

		case R.id.tianqi:
			enterTianQi(view);
			break;

		case R.id.more:
			enterMore(view);
			break;

		default:
			break;
		}

	}

}
