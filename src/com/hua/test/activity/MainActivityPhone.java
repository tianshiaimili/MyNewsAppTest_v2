package com.hua.test.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hua.tes.slidingmenu.BaseSlidingFragmentActivity;
import com.hua.tes.slidingmenu.SlidingMenu;
import com.hua.test.adapter.NewsFragmentPagerAdapter;
import com.hua.test.app.App;
import com.hua.test.bean.ChannelItem;
import com.hua.test.bean.ChannelManage;
import com.hua.test.fragment.FoodBallFragment;
import com.hua.test.fragment.HeadNewsFragment;
import com.hua.test.fragment.LeftContentFragment;
import com.hua.test.fragment.TestFragment;
import com.hua.test.fragment.YuLeFragment;
import com.hua.test.utils.LogUtils2;
import com.hua.test.view.ColumnHorizontalScrollView;
import com.hua.test.view.CustomerToast;
import com.umeng.analytics.MobclickAgent;


public class MainActivityPhone extends BaseSlidingFragmentActivity{


	private NewsFragmentPagerAdapter mNewsFragmentPagerAdapter;
	/** bar部分水平的HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	/**
	 * 头部滑动的选项LinearLayout
	 */
	private LinearLayout mRadioGroup_content;
	/**
	 * 添加更多的 + 图 的LinearLayout 图标
	 */
	protected LinearLayout add_more_columns;
	/**
	 * 添加更多的 + 图 的ImageView
	 */
	protected ImageView button_more_columns;
	/**
	 * 整段水平Bar的view部分 头部滑动的选项RelativeLayout
	 */
	protected RelativeLayout rl_column;
	/**Content的Viewpager*/
	protected ViewPager mViewPager;
    /**
     * Horizontal左边的阴影
     */
    protected ImageView shade_left;
    /**
     * Horizontal右边的阴影
     */
    protected ImageView shade_right;
    /**左边的ListView 菜单*/
//    protected LeftView mLeftView;
    /**滑动的菜单  ..？？*/
    protected SlidingMenu mSlidingMenu;
    
    /**
     * Bar 底部的标志划线
     */
//    private ImageView buttom_indicate_line;
    private LinearLayout buttom_indicate_line;

    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /**头部horizontal上 的Item宽度 */
    private int item_width = 0;
    /**Bar 水平部分滑动的距离*/
    private int scroll_distance = 0;
    /***/
    private boolean isGet_Scroll_Distance;
    /** head 头部 的左侧菜单 按钮 */
    protected ImageView top_head;
    /** head 头部 的右侧菜单 按钮 */
    protected ImageView top_more;
    /** 用户选择的新闻分类列表 即在水平Bar上的内容item */
    private ArrayList<ChannelItem> userChannelList;
    /** 请求CODE */
    public final static int CHANNELR_EQUEST = 1;
    /** 调整返回的RESULTCODE */
    public final static int CHANNEL_RESULT = 10;
    /** 当前选中的栏目 */
    private int columnSelectIndex = 0;
    /**用来封装多个Fragment 如 今日头条 体育 科技等fragment*/
    private ArrayList<Fragment> fragments;

    private Fragment newfragment;
    private double back_pressed;

    public static boolean isChange = false;
    
    /**HomeFragment的内容View*/
//    private View contentView ;
    
    private Context context;
    
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

	private int bottom_indicate_line_duration =150;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	context = getApplicationContext();
//    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	initSlidingMenu();
    	setContentView(R.layout.home_main);
    	initContentView();
    	initViewPager();
    	setChangelView();
    	
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
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }
    
    /**
     * 点击两次返回退出系统
     * 
     * @param view
     */

	/**
	 * 连续按两次返回键就退出
	 */
	private long firstTime;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - firstTime < 3000) {
			finish();
		} else {
			firstTime = System.currentTimeMillis();
//				Toast.makeText(context, "tuichu", 300).show();
				CustomerToast.makeText(context, getResources().getString(R.string.press_again_exit), 1000, 0.35f).show();
		}
	}

    
	/**
	 * initial the FragmentView
	 */
	public void initContentView(){
		
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		add_more_columns = (LinearLayout) findViewById(R.id.add_more_columns);
		button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		top_head = (ImageView) findViewById(R.id.top_head);
		top_more = (ImageView) findViewById(R.id.top_more);
		buttom_indicate_line = (LinearLayout) findViewById(R.id.bottom_indicate_line);
//		buttom_indicate_line.s
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        item_width = (int) (mScreenWidth / 7 + 0.5f); // 一个Item宽度为屏幕的1/7
//        buttom_indicate_line.setLayoutParams(new FrameLayout.LayoutParams(mItemWidth, 3));
        buttom_indicate_line.getLayoutParams().width = item_width;
        userChannelList = new ArrayList<ChannelItem>();
        fragments = new ArrayList<Fragment>();
	}
    
    protected void initSlidingMenu() {
//        mLeftView = new LeftView(context);
//        side_drawer = SlidingMenuView.instance().initSlidingMenuView(this, mLeftView,mScreenWidth);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
//        LogUtils2.i("**initSlidingMenu********");
		setBehindContentView(R.layout.main_left_layout);// 设置左菜单，就是向右滑动显示出来的菜单
//		LogUtils2.d("**initSlidingMenu********");
		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new LeftContentFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		// 设置是左滑还是右滑，还是左右都可以滑，切记 如果设计左右都可以滑动，那么必须也要初始化右边的view，否则报错
		mSlidingMenu.setMode(SlidingMenu.LEFT);
//		mSlidingMenu.setShadowWidth(screenWidth / 60);// 设置阴影宽度 就是滑动时页边的阴影
		mSlidingMenu.setShadowDrawable(R.drawable.shadow_left);// 设置左菜单阴影图片
		mSlidingMenu.setBehindOffset(screenWidth * 2 / 5);// 设置菜单宽度
		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//设置滑动的模式
//		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow_right);// 设置右菜单阴影图片
		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
//		LogUtils2.e("************initSlidingMenu");
    }

    private void initViewPager() {
    	mNewsFragmentPagerAdapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mNewsFragmentPagerAdapter);
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
        initFragment();
    }
    
    

    /** 获取Column栏目 数据 */
    private void initColumnData() {
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(
                App.getApp().getSQLHelper()).getUserChannel());
    }

    /**
     * 初始化水平BarColumnItem的栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left,
                shade_right, add_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(item_width,
                    LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            // TextView localTextView = (TextView)
            // mInflater.inflate(R.layout.column_radio_item, null);
            final TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(context, R.style.top_category_scroll_view_item_text);
            // localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getName());
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
            
            mRadioGroup_content.addView(columnTextView, i, params);
            if(!isGet_Scroll_Distance){
            	isGet_Scroll_Distance = true;
            	ViewTreeObserver vto2 = columnTextView.getViewTreeObserver(); 
            	vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            		@Override 
            		public void onGlobalLayout() { 
            			columnTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//            textView.append("\n\n"+imageView.getHeight()+","+imageView.getWidth()); 
            			int margin_distance = params.leftMargin + params.rightMargin;
            			LogUtils2.e("margin_distance  ="+margin_distance);
//            			contentView.getWidth();
            			scroll_distance = item_width + margin_distance;
            			LogUtils2.e("scroll_distance  ="+scroll_distance);
            		} 
            	});
            }
            
        }
    }
	

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();
        int count = userChannelList.size();
        count = 10;
        for (int i = 0; i < count; i++) {
            // Bundle data = new Bundle();
            String nameString = userChannelList.get(i).getName();
            // data.putString("text", nameString);
            // data.putInt("id", userChannelList.get(i).getId());
            // initFragment(nameString);
            // map.put(nameString, nameString);
            // newfragment.setArguments(data);
            fragments.add(initFragment(nameString));
            // fragments.add(nameString);
        }
        mNewsFragmentPagerAdapter.appendList(fragments);
    }
    
    //TODO 初始化Headitem
    public Fragment initFragment(String channelName) {
        if (channelName.equals("头条")) {
            newfragment = new HeadNewsFragment();
        } else if (channelName.equals("足球")) {
            newfragment = new FoodBallFragment();
        } else if (channelName.equals("娱乐")) {
            newfragment = new YuLeFragment();
        } else if (channelName.equals("体育")) {
            newfragment = new TestFragment();
        } else if (channelName.equals("财经")) {
            newfragment = new TestFragment();
        } else if (channelName.equals("科技")) {
            newfragment = new TestFragment();
        } else if (channelName.equals("电影")) {
            newfragment = new TestFragment();
        } else if (channelName.equals("汽车")) {
            newfragment = new TestFragment();
        } else if (channelName.equals("笑话")) {
            newfragment = new TestFragment();
        } else if (channelName.equals("时尚")) {
            newfragment = new TestFragment();
        } 
        
//        else if (channelName.equals("北京")) {
//            newfragment = new BeiJingFragment_();
//        } else if (channelName.equals("军事")) {
//            newfragment = new JunShiFragment_();
//        } else if (channelName.equals("房产")) {
//            newfragment = new FangChanFragment_();
//        } else if (channelName.equals("游戏")) {
//            newfragment = new YouXiFragment_();
//        } else if (channelName.equals("情感")) {
//            newfragment = new QinGanFragment_();
//        } else if (channelName.equals("精选")) {
//            newfragment = new JingXuanFragment_();
//        } else if (channelName.equals("电台")) {
//            newfragment = new DianTaiFragment_();
//        } else if (channelName.equals("图片")) {
//            newfragment = new TuPianFragment_();
//        } else if (channelName.equals("NBA")) {
//            newfragment = new NBAFragment_();
//        } else if (channelName.equals("数码")) {
//            newfragment = new ShuMaFragment_();
//        } else if (channelName.equals("移动")) {
//            newfragment = new YiDongFragment_();
//        } else if (channelName.equals("彩票")) {
//            newfragment = new CaiPiaoFragment_();
//        } else if (channelName.equals("教育")) {
//            newfragment = new JiaoYuFragment_();
//        } else if (channelName.equals("论坛")) {
//            newfragment = new LunTanFragment_();
//        } else if (channelName.equals("旅游")) {
//            newfragment = new LvYouFragment_();
//        } else if (channelName.equals("手机")) {
//            newfragment = new ShouJiFragment_();
//        } else if (channelName.equals("博客")) {
//            newfragment = new BoKeFragment_();
//        } else if (channelName.equals("社会")) {
//            newfragment = new SheHuiFragment_();
//        } else if (channelName.equals("家居")) {
//            newfragment = new JiaJuFragment_();
//        } else if (channelName.equals("暴雪")) {
//            newfragment = new BaoXueYouXiFragment_();
//        } else if (channelName.equals("亲子")) {
//            newfragment = new QinZiFragment_();
//        } else if (channelName.equals("CBA")) {
//            newfragment = new CBAFragment_();
//        }
        return newfragment;
    }
    
}
