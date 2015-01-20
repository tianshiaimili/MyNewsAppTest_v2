package com.hua.test.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;

import com.hua.test.activity.BaseActivity;
import com.hua.test.activity.DetailsActivity;
import com.hua.test.activity.ImageDetailActivity;
import com.hua.test.activity.R;
import com.hua.test.adapter.CardsAnimationAdapter;
import com.hua.test.adapter.NewAdapter;
import com.hua.test.bean.NewModle;
import com.hua.test.contants.Url;
import com.hua.test.initView.InitView;
import com.hua.test.network.http.json.NewListJson;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.StringUtils;
import com.hua.test.widget.swipelistview.BaseSwipeListViewListener;
import com.hua.test.widget.swipelistview.SwipeListView;
import com.hua.test.widget.viewimage.Animations.DescriptionAnimation;
import com.hua.test.widget.viewimage.Animations.SliderLayout;
import com.hua.test.widget.viewimage.SliderTypes.BaseSliderView;
import com.hua.test.widget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.hua.test.widget.viewimage.SliderTypes.TextSliderView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
/**
 * 今日头条的fragment
 * @author zero
 *
 */
@SuppressLint("NewApi")
public class HeadNewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
OnSliderClickListener{
	/**
	 * 头部的横幅滑动布局
	 */
    protected SliderLayout mDemoSlider;
    /**the SwipeRefreshLayout which can pull to refresh*/
    protected SwipeRefreshLayout swipeLayout;
    /*** 整个布局的listview*/
//    @ViewById(R.id.listview)
    protected SwipeListView mSwipeListView;
//    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    protected HashMap<String, String> url_maps;

    protected HashMap<String, NewModle> newHashMap;

//    @Bean
    protected NewAdapter newAdapter;
    /**数据集合 ,用于点击item时 传入的数据*/
    protected List<NewModle>  listsModles = new ArrayList<NewModle>();
    private int index = 0;
    private boolean isRefresh = false;
    /**全局的View*/
    private View contentView;
    /***/
    private Context mContext;
    private static final int RESPONSE_OK = 0;
    
    /**这是底部显示的tab item部分 ，这里主要是想滑动listview 实现隐藏*/
//    private LinearLayout buttomLayoutTabItem;
//    /***/
    private int buttomLayoutTabItemHeight;
    /**当上滑的时候 记录滑动的距离 一般是正数*/
    private int upScrollDistance ;
    /**当下滑的时候 也记录滑动的距离，
     * 一开始 应该是上滑，那么记录上滑的距离，*/
    private int downScrollDistance;
    /**记录 滑动了的距离，如果新的滑动的距离变小了 说明往下滑动了
     * 那么就计算 新旧的 差值，然后判断是否出现 下tab item*/
    private int oldScrollDistance;
    private boolean isHideTabItem;
    private boolean isFirstScroll;
    private boolean isDownAnimationOver;
    private boolean isUpAnimationOver;
	private TranslateAnimation anim;
	
	private String cacheName =this.getClass().getSimpleName();
    
	Handler mHandler = new Handler() {
		public void handleMessage(Message message) {
			int what = message.what;
			int numchange = what;
//			LogUtils2.i("what==" + what);
			switch (what) {
			case RESPONSE_OK:
				String result = (String) message.obj;
				getResult(result);
				break;
			default:
				break;
			}

		};
	};
    
    /**初始化 数据源 例如 集合、url的map集合、等*/
    private void init() {
    	LogUtils2.d("what is the value of index = "+index);
    	mContext = getActivity();
//        listsModles = new ArrayList<NewModle>();
        LogUtils2.d("what is the value of listsModles.size  = "+listsModles.size());
        url_maps = new HashMap<String, String>();

        newHashMap = new HashMap<String, NewModle>();
    }

	public void initContentView(View tempContentView){
		
		swipeLayout = (SwipeRefreshLayout) tempContentView.findViewById(R.id.swipe_container);
		mSwipeListView = (SwipeListView) tempContentView.findViewById(R.id.listview);
		mProgressBar = (ProgressBar) tempContentView.findViewById(R.id.progressBar);
		
//    	LogUtils2.e("*******initView*************");
    	LogUtils2.e("*******index*************== "+index);
        swipeLayout.setOnRefreshListener(this);
        InitView.instance().initSwipeRefreshLayout(swipeLayout);
        InitView.instance().initListView(mSwipeListView, getActivity());
		///add HeadView
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
        mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
        mSwipeListView.addHeaderView(headView);
        
        //set the adapter of ListView
//        newAdapter = new NewAdapter(mContext);
        newAdapter = NewAdapter.getNewAdapter(mContext);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(newAdapter);
        animationAdapter.setAbsListView(mSwipeListView);
        mSwipeListView.setAdapter(animationAdapter);
        LogUtils2.d("what is the value of index  = "+index);
        loadData(getNewUrl(index + ""),this.getClass().getSimpleName());

        mSwipeListView.setOnBottomListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPagte++;
                index = index + 20;
                LogUtils2.i("cacheFragmentName onButtomListener  the index is "+index+"   name == "+cacheName);
                loadData(getNewUrl(index + ""),cacheName);
            }
        });
        
        
//        mSwipeListView.setSwipeListViewListener(mSwipeListViewListener);
        mSwipeListView.setOnItemClickListener(new MyHeadNewsListViewItemListener());
        
        mSwipeListView.setOnScrollListener(new OnScrollListener() {
			
			@Override  
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				LogUtils2.i("******onScrollStateChanged**********");
				LogUtils2.i("what is the scrollY distance == "+view.getScrollY());
				 
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					LogUtils2.e("the first posiont == "+mSwipeListView.getFirstVisiblePosition());
					LogUtils2.e("the last posiont == "+mSwipeListView.getLastVisiblePosition());
//					mSwipeListView.get
				}
				
//				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
//					
//					if(oldScrollDistance > getScrollY() && !isDownAnimationOver){
//						
//						LogUtils2.i("---------89999--------");
//
//						
//					buttomLayoutTabItem.setVisibility(View.VISIBLE);
//					anim = new TranslateAnimation(0, 0, buttomLayoutTabItem.getHeight(),
//							0);
//					anim.setFillAfter(true);
//					anim.setDuration(300);
//					anim.setAnimationListener(new AnimationListener() {
//						
//						@Override
//						public void onAnimationStart(Animation animation) {
//							
//						}
//						
//						@Override
//						public void onAnimationRepeat(Animation animation) {
//							
//						}
//						
//						@Override
//						public void onAnimationEnd(Animation animation) {
//							isDownAnimationOver = true;
//							isUpAnimationOver = false;
//						}
//					});
//					buttomLayoutTabItem.startAnimation(anim);
//					
//					}else if(oldScrollDistance < getScrollY() && !isUpAnimationOver){
//						
//						LogUtils2.i("+++++++++++88+++++++++++++");
//						
//						buttomLayoutTabItem.setVisibility(View.VISIBLE);
//						anim = new TranslateAnimation(0, 0, 0,
//								buttomLayoutTabItem.getHeight());
//						anim.setFillAfter(true);
//						anim.setDuration(300);
//						anim.setAnimationListener(new AnimationListener() {
//							
//							@Override
//							public void onAnimationStart(Animation animation) {
//								
//							}
//							
//							@Override
//							public void onAnimationRepeat(Animation animation) {
//								
//							}
//							
//							@Override
//							public void onAnimationEnd(Animation animation) {
//								isDownAnimationOver = false;
//								isUpAnimationOver = true;
//							}
//						});
//						buttomLayoutTabItem.startAnimation(anim);
//					}
//					
//				}else {
//				}
//				
//				oldScrollDistance = getScrollY();
//				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
				} else {
				}
				
			}
		});
	}
	
	public boolean isScrollButtomItemTab(){
		
		View c = mSwipeListView.getChildAt(0);
	    int actualyDistance = 0;
	    int buttomTabItemHeight = 0;//buttomLayoutTabItem.getHeight();
//	    LogUtils2.w("buttomTabItemHeight===  "+buttomTabItemHeight);
	    
	    if (c == null) {
	        return false;
	    }
	    int firstVisiblePosition = mSwipeListView.getFirstVisiblePosition();
	    int top = c.getTop();
	    int scrollDistance = -top + firstVisiblePosition * c.getHeight();
	    
	    if(oldScrollDistance > scrollDistance + 5){
	    	LogUtils2.e("scrollDistance === "+scrollDistance);
		    LogUtils2.d("oldScrollDistance == "+oldScrollDistance);
	    	oldScrollDistance = scrollDistance;
	    	return true;
	    }
//	    LogUtils2.e("scrollDistance === "+scrollDistance);
//	    LogUtils2.d("oldScrollDistance == "+oldScrollDistance);
	    
   	 	 oldScrollDistance = scrollDistance;
   	 	 if(oldScrollDistance == scrollDistance){
   	 		return false;
   	 	 }
		
	    return false;
	    
	}
	
	public int getScrollY() {
	    View c = mSwipeListView.getChildAt(0);
	    int actualyDistance = 0;
	    int buttomTabItemHeight = 0;//buttomLayoutTabItem.getHeight();
	    LogUtils2.w("buttomTabItemHeight===  "+buttomTabItemHeight);
	    
	    if (c == null) {
	        return 0;
	    }
	    int firstVisiblePosition = mSwipeListView.getFirstVisiblePosition();
	    int top = c.getTop();
	    int scrollDistance = -top + firstVisiblePosition * c.getHeight();
	    LogUtils2.e("scrollDistance === "+scrollDistance);
	    return scrollDistance;
	}
	
	/**加载数据*/
	public void loadData(String url,String cacheFragmentName){
        if (getMyActivity().hasNetWork()) {
            loadNewList(url);
        } else {
            mSwipeListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            getMyActivity().showShortToast(getString(R.string.not_network));
            LogUtils2.i("cacheFragmentName = "+cacheFragmentName);
            String result = getMyActivity().getCacheStr(cacheFragmentName + currentPagte);
            LogUtils2.i("what is the result = "+result);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
        }
	}
	
//    @Background
    void loadNewList(String url) {
    	LogUtils2.i("loadNewList.url = "+url);
        String result;
        try {
//            result = HttpUtil.getByHttpClient(getActivity(), url);
            new GetDataTask().execute(url);
//            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils2.e("loadNewList error -----");
        }
    }
    

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
				result = HttpUtil.getByHttpClient(mContext, params[0]);

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

    

//    @UiThread
    public void getResult(String result) {
        getMyActivity().setCacheStr("HeadNewsFragment" + currentPagte, result);
        if (isRefresh) {
            isRefresh = false;
            newAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
        List<NewModle> list =
                NewListJson.instance(getActivity()).readJsonNewModles(result,
                        Url.TopId);
        if (index == 0) {
        	LogUtils2.i("is first come in************");
            initSliderLayout(list);
        } else {
        	LogUtils2.d("add data to the listView and the list.size = "+ list.size());
        	newAdapter.appendList(list,index);
        }
        if(newAdapter.isNeedUplistsModlesData(index)){
        	listsModles.addAll(list);
        }
        LogUtils2.d(" the value of listsModles.size  = "+listsModles.size());
        mSwipeListView.onBottomComplete();//TODO
    }
    
    
    /**
	 * 第一次进来时 把banner部分初始化处理
	 * 
	 * @param newModles
	 */
	private void initSliderLayout(List<NewModle> newModles) {

		if (!isNullString(newModles.get(0).getImgsrc()))
			newHashMap.put(newModles.get(0).getImgsrc(), newModles.get(0));
		if (!isNullString(newModles.get(1).getImgsrc()))
			newHashMap.put(newModles.get(1).getImgsrc(), newModles.get(1));
		if (!isNullString(newModles.get(2).getImgsrc()))
			newHashMap.put(newModles.get(2).getImgsrc(), newModles.get(2));
		if (!isNullString(newModles.get(3).getImgsrc()))
			newHashMap.put(newModles.get(3).getImgsrc(), newModles.get(3));

		if (!isNullString(newModles.get(0).getImgsrc()))
			url_maps.put(newModles.get(0).getTitle(), newModles.get(0)
					.getImgsrc());
		if (!isNullString(newModles.get(1).getImgsrc()))
			url_maps.put(newModles.get(1).getTitle(), newModles.get(1)
					.getImgsrc());
		if (!isNullString(newModles.get(2).getImgsrc()))
			url_maps.put(newModles.get(2).getTitle(), newModles.get(2)
					.getImgsrc());
		if (!isNullString(newModles.get(3).getImgsrc()))
			url_maps.put(newModles.get(3).getTitle(), newModles.get(3)
					.getImgsrc());

		for (String name : url_maps.keySet()) {
			TextSliderView textSliderView = new TextSliderView(getActivity());
			textSliderView.setOnSliderClickListener(this);
			textSliderView.description(name).image(url_maps.get(name));

			textSliderView.getBundle().putString("extra", name);
			mDemoSlider.addSlider(textSliderView);
		}

		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
		mDemoSlider
				.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
		mDemoSlider.setCustomAnimation(new DescriptionAnimation());
		LogUtils2.i("*****mViewFlowAdapter.setAdapterData********");
		// mViewFlowAdapter.setAdapterData(newHashMap, url_maps);
		LogUtils2.e("");
		newAdapter.appendList(newModles,index);
	}
    

	//SwipeRefreshLayout.OnRefreshListener
	@Override
	public void onRefresh() {
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				currentPagte = 1;
				isRefresh = true;
//				loadData(getCommonUrl(0 + "", Url.DianYingId));
				index = 0;
				loadData(getNewUrl(index + ""),this.getClass().getSimpleName());
				url_maps.clear();
				mDemoSlider.removeAllSliders();
			}
		}, 2000);
		
	}

	/////////////////////////////////////////

    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils2.w("***onCreate***");
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils2.w("***onCreateView***");
		contentView = inflater.inflate(R.layout.news_activity_main, null);
		init() ;
		initContentView(contentView);
		return contentView;
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils2.w("***onViewCreated***");
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils2.w("***onActivityCreated***");
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
    


	/**
	 * this is for the SwipeListView Listener
	 */
//	BaseSwipeListViewListener mSwipeListViewListener = new BaseSwipeListViewListener() {
//		@Override
//		public void onClickFrontView(int position) {
//				Toast.makeText(mContext, "  pos== "+position, 300).show();
//			   NewModle newModle = listsModles.get(position - 1);
//		        enterDetailActivity(newModle);
//		}
//		
//		@Override
//		public void onClickBackView(int position) {
//			mSwipeListView.closeOpenedItems();// 关闭打开的项
//		}
//	};
	

	///OnSliderClickListener
	@Override
	public void onSliderClick(BaseSliderView slider) {
        NewModle newModle = newHashMap.get(slider.getUrl());
        enterDetailActivity(newModle);
	}
	

    public void enterDetailActivity(NewModle newModle) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("newModle", newModle);
        Class<?> class1;
        if (newModle.getImagesModle() != null && newModle.getImagesModle().getImgList().size() > 1) {
           LogUtils2.i("******** ImageDetailActivity_.class********** ImageList。size = "+newModle.getImagesModle().getImgList().size());
        	class1 = ImageDetailActivity.class;
        } else {
//        	  LogUtils2.i("******** DetailsActivity_.class**********");
            class1 = DetailsActivity.class;
        }
        
        ((BaseActivity) getActivity()).openActivity(class1,
                bundle, 0);
    }
	
	class MyHeadNewsListViewItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			LogUtils2.e("in the onItemClick the position = "+position);
//			Toast.makeText(mContext, "  pos== "+position, 300).show();
			   NewModle newModle = listsModles.get(position - 1);
		        enterDetailActivity(newModle);
			
		}
		
	}
}
