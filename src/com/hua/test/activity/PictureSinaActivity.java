package com.hua.test.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hua.test.adapter.NewsFragmentPagerAdapter;
import com.hua.test.app.App;
import com.hua.test.bean.ChannelItem;
import com.hua.test.bean.ChannelManage;
import com.hua.test.contants.MyConstants;
import com.hua.test.fragment.PictureSinaMeiTuFragment;
import com.hua.test.fragment.TestFragment;
import com.hua.test.utils.LogUtils2;
import com.hua.test.view.ColumnHorizontalScrollView;
import com.hua.test.widget.swipeback.SwipeBackActivity;

public class PictureSinaActivity extends SwipeBackActivity{

//    @ViewById(R.id.vPager)
    protected ViewPager mViewPager;
////    @ViewById(R.id.redian)
//    protected RadioButton mJingXuan;
//    @ViewById(R.id.dujia)
//    protected RadioButton mQuTu;
//    @ViewById(R.id.titan)
//    protected RadioButton mGuShi;
//    @ViewById(R.id.mingxing)
//    protected RadioButton mMeiTu;
    private ArrayList<Fragment> fragments;
    
	//use to change the disdance of indicate_bottom_ine
    /**the end postion of the indicate_line*/
	private int endPosition; 
	/**the beginPosition of the indicate_line*/
	private int beginPosition;
	/***/
	private int currentFragmentIndex;
	/**if the viewPage have drag over
	 * */
	private boolean isEnd;

    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /**头部horizontal上 的Item宽度 */
    private int item_width = 0;
    /**Bar 水平部分滑动的距离*/ 
    private int scroll_distance = 0;
    private int bottom_indicate_line_duration =150;
	
    /**
     * Bar 底部的标志划线
     */
//    private ImageView buttom_indicate_line;
    private LinearLayout buttom_indicate_line;
	private NewsFragmentPagerAdapter mNewsFragmentPagerAdapter;
	/** bar部分水平的HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    
	/**
	 * 头部滑动的选项LinearLayout
	 */
	private LinearLayout mRadioGroup_content;
	
    /** 当前选中的栏目 */
    private int columnSelectIndex = 0;
    
    /**Horizontal bar上 item的个数*/
    private int itemCount = 4;
	
    /** 用户选择的新闻分类列表 即在水平Bar上的内容item */
//    private String[] itemTitles = {"美图","趣图","精选","故事"};
    private ArrayList<String> pictureTitles;
	
    /***/
    private boolean isGet_Scroll_Distance;

    public void initContentView(){
    	mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
    	mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
    	buttom_indicate_line = (LinearLayout) findViewById(R.id.bottom_indicate_line);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
//        item_width = (int) (mScreenWidth / itemCount + 0.5f); // 一个Item宽度为屏幕的1/7
//        buttom_indicate_line.getLayoutParams().width = item_width;
    	//
    	initViewPage();
    }

    public void initViewPage(){
        mNewsFragmentPagerAdapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager());
    	mViewPager = (ViewPager) findViewById(R.id.vPager);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setAdapter(mNewsFragmentPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(pageListener);
    }
    
    /**
     * ViewPager切换监听方法
     */
    public OnPageChangeListener pageListener = new OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
			
//        	LogUtils2.i("onPageScrollStateChanged----------------");
        	
			if (state == ViewPager.SCROLL_STATE_DRAGGING) {
				isEnd = false;
			} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
				isEnd = true;
				beginPosition = currentFragmentIndex * scroll_distance;
				if (mViewPager.getCurrentItem() == currentFragmentIndex) {
					// 未跳入下一个页面
					buttom_indicate_line.clearAnimation();
					Animation animation = null;
					// 恢复位置
					animation = new TranslateAnimation(endPosition, currentFragmentIndex * scroll_distance, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(bottom_indicate_line_duration);
					buttom_indicate_line.startAnimation(animation);
					mColumnHorizontalScrollView.invalidate();
					endPosition = currentFragmentIndex * scroll_distance;
				}
			}
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
//        	LogUtils2.e("position == "+position);
			if(!isEnd){
				if(currentFragmentIndex == position){
					endPosition = scroll_distance * currentFragmentIndex + 
							(int)(scroll_distance * positionOffset);
				}
				if(currentFragmentIndex == position+1){
					endPosition = scroll_distance * currentFragmentIndex - 
							(int)(scroll_distance * (1-positionOffset));
				}
				
//				mHorizontalScrollView.smoothScrollTo((currentFragmentIndex - 1) * scroll_distance , 0);
				
				Animation mAnimation = new TranslateAnimation(beginPosition, endPosition, 0, 0);
				mAnimation.setFillAfter(true);
				mAnimation.setDuration(bottom_indicate_line_duration);
				buttom_indicate_line.startAnimation(mAnimation);
				mColumnHorizontalScrollView.invalidate();
				beginPosition = endPosition;
//				LogUtils2.w("onPageScrolled over-----");
			}
        	
        }

        @Override
        public void onPageSelected(int position) {
        	
//        	LogUtils2.i("******onPageSelected scroll_distance= "+scroll_distance);
//        	LogUtils2.e("******onPageSelected endPosition = "+endPosition);
			Animation animation = new TranslateAnimation(endPosition, position* scroll_distance, 0, 0);
			animation.setInterpolator(new AccelerateDecelerateInterpolator());
			beginPosition = position * scroll_distance;
			endPosition = position * scroll_distance;
//			LogUtils2.w("******onPageSelected beginPosition = "+beginPosition);
//			LogUtils2.d("******onPageSelected endPosition = "+endPosition);
			currentFragmentIndex = position;
    		
        	mViewPager.setCurrentItem(position);
           int temp_scroll_distance = selectTab(position);
           
			if (animation != null) {
//				LogUtils2.d("******onPageSelected");
				animation.setFillAfter(true);
				animation.setDuration(bottom_indicate_line_duration);
				buttom_indicate_line.startAnimation(animation);
//				mColumnHorizontalScrollView.smoothScrollTo((currentFragmentIndex - 1) * temp_scroll_distance , 0);
				mColumnHorizontalScrollView.smoothScrollTo(temp_scroll_distance , 0);
			}
            
        }
    };
	

    /**
     * 选择的Column里面的Tab
     */
    private int selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        LogUtils2.i("selectTab  == "+tab_postion);
        int scrollDistance = 0 ;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
             scrollDistance = l + k / 2 - mScreenWidth / 2;
//            LogUtils2.i("scrollDistance  == "+scrollDistance);
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
//            mColumnHorizontalScrollView.smoothScrollTo(scrollDistance, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        // 判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
        
        return scrollDistance;
    }
    
    /**
     * 当栏目项发生变化时候调用
     */
    public void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragments();
    }
    
    
    /***/
    public void initFragments() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new PictureSinaMeiTuFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        fragments.add(new TestFragment());
        LogUtils2.i("mNewsFragmentPagerAdapter == "+mNewsFragmentPagerAdapter);
        LogUtils2.i("fragments.size == "+fragments.size());
        mNewsFragmentPagerAdapter.appendList(fragments);
    }
    
    /** 获取Column栏目 数据  这里直接设置成 hard code*/
    private void initColumnData() {
        pictureTitles = new ArrayList<String>();
        for(String title : MyConstants.Picture_Item_Titles){
        	pictureTitles.add(title);
        }
    }
    
    /**
     * 初始化水平BarColumnItem的栏目项
     */
    @SuppressLint("NewApi")
	private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = pictureTitles.size();
//        mColumnHorizontalScrollView.setParam(this, mScreenWidth, null, null,
//                null, null, null);
        for (int i = 0; i < count; i++) {
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LayoutParams.WRAP_CONTENT);
//            params.leftMargin = 5;
//            params.rightMargin = 5;
            params.weight = 1;
            // TextView localTextView = (TextView)
            // mInflater.inflate(R.layout.column_radio_item, null);
            final TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(getApplicationContext(), R.style.top_category_scroll_view_item_text);
            // localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
//            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(pictureTitles.get(i));
            columnTextView.setTextColor(getResources().getColorStateList(
                    R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            final FrameLayout.LayoutParams mRadioGroup_contentParams = new FrameLayout.LayoutParams(mScreenWidth,
                    LayoutParams.WRAP_CONTENT);
            mRadioGroup_contentParams.gravity = Gravity.CENTER_VERTICAL;
            mRadioGroup_content.setLayoutParams(mRadioGroup_contentParams);
            mRadioGroup_content.addView(columnTextView, i, params);
            
            
            if(!isGet_Scroll_Distance){
            	isGet_Scroll_Distance = true;
            	ViewTreeObserver vto2 = columnTextView.getViewTreeObserver(); 
            	vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            		@Override 
            		public void onGlobalLayout() { 
            			columnTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this); 

            	        item_width = columnTextView.getWidth(); // 一个Item宽度为屏幕的1/7
//            	        LogUtils2.i("margin2.item_width  = "+item_width);
            	        buttom_indicate_line.getLayoutParams().width = item_width;
            			scroll_distance = item_width; 
            			
//            			LogUtils2.e("scroll_distance11  ="+scroll_distance);
            		} 
            	});
            }
            
        }
    }
	
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
//		initFragments();
		initContentView();
		setChangelView() ;
	}
	

	@Override
	public void onStart() {
		super.onStart();
		LogUtils2.w("***onStart***");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LogUtils2.w("***onResume***");
	}

	

    @Override
    public void onStop() {
    	super.onStop();
    	LogUtils2.w("***onStop***");
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	LogUtils2.w("***onDestroy***");
//    	newAdapter.getLists().clear();
    }
    

}
