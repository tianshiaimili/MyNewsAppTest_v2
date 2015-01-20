
package com.hua.test.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.hua.test.activity.BaseActivity;
import com.hua.test.activity.DetailsActivity;
import com.hua.test.activity.ImageDetailActivity;
import com.hua.test.activity.R;
import com.hua.test.adapter.CardsAnimationAdapter;
import com.hua.test.adapter.YuLeAdapter;
import com.hua.test.bean.NewModle;
import com.hua.test.contants.Url;
import com.hua.test.fragment.FoodBallFragment.MyFoodBallListViewItemListener;
import com.hua.test.initView.InitView;
import com.hua.test.network.http.json.NewListJson;
import com.hua.test.network.utils.HttpUtil;
import com.hua.test.utils.LogUtils2;
import com.hua.test.utils.StringUtils;
import com.hua.test.widget.swipelistview.SwipeListView;
import com.hua.test.widget.viewimage.Animations.DescriptionAnimation;
import com.hua.test.widget.viewimage.Animations.SliderLayout;
import com.hua.test.widget.viewimage.SliderTypes.BaseSliderView;
import com.hua.test.widget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.hua.test.widget.viewimage.SliderTypes.TextSliderView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.umeng.analytics.MobclickAgent;

//@EFragment(R.layout.activity_main)
public class YuLeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnSliderClickListener {
	/** 头部的横幅滑动布局*/
    protected SliderLayout mDemoSlider;
//    @ViewById(R.id.swipe_container)
    /**the SwipeRefreshLayout which can pull to refresh*/
    protected SwipeRefreshLayout swipeLayout;
//    @ViewById(R.id.listview)
    /*** 整个布局的listview*/
    protected SwipeListView mSwipeListView;
//    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    /**作为head bar部分 的图片的跳转对应连接的url集合*/
    protected HashMap<String, String> url_maps;
    /**作为head bar部分的图片的url集合*/
    protected HashMap<String, NewModle> newHashMap;

//    @Bean
    protected YuLeAdapter yuLeAdapter;
    /**数据集合 ,用于点击item时 传入的数据*/
    protected List<NewModle> listsModles = new ArrayList<NewModle>();
    /**全局的View*/
    private View contentView;
    /***/
    private Context mContext;
    /**标记获取了的页数下标*/
    private int index = 0;
    private boolean isRefresh = false;
    private static final int RESPONSE_OK = 0;

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
				// mHandler.obtainMessage(ShowFootView).sendToTarget();
				break;
			default:
				break;
			}

		};
	};
    
    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**初始化 数据源 例如 集合、url的map集合、等*/
    private void init() {
    	LogUtils2.d("what is the value of index = "+index);
    	mContext = getActivity();
//        listsModles = new ArrayList<NewModle>();
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
        yuLeAdapter = YuLeAdapter.getYuLeAdapter(mContext);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(yuLeAdapter);
        animationAdapter.setAbsListView(mSwipeListView);
        mSwipeListView.setAdapter(animationAdapter);
        
        ///load data
        loadData(getCommonUrl(index + "", Url.YuLeId),cacheName);

        mSwipeListView.setOnBottomListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPagte++;
                index = index + 20;
                LogUtils2.i("onButtomListener  the index is "+index);
                loadData(getCommonUrl(index + "", Url.YuLeId),cacheName);
            }
        });
        
        mSwipeListView.setOnItemClickListener(new MyYuLeListViewItemListener());
        
        mSwipeListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				LogUtils2.i("******onScrollStateChanged**********");
				
//				 LinearLayout layout = MainActivityPhone.getTab_Bar_Container();
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
//					MainActivityPhone.getTab_Bar_Container().setVisibility(View.VISIBLE);;
					
//					 mAnimation = new AlphaAnimation(1, 0);
//					 layout.startAnimation(mAnimation);
//					mAnimation = 
//					layout.scrollTo(0, 50);
				}else {
					
//					MainActivityPhone.getTab_Bar_Container().setVisibility(View.GONE);
//					 LinearLayout layout = MainActivityPhone.getTab_Bar_Container();
//					 mAnimation = new AlphaAnimation(0, 1);
//					 layout.startAnimation(mAnimation);
//					layout.scrollTo(0, -50);
					
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
        
        
	}
    
	/**加载数据*/
	public void loadData(String url,String cacheFragmentName){
		LogUtils2.e("commentUrl = "+this.getClass().getSimpleName()+"   cacheName ="+cacheName);
        if (getMyActivity().hasNetWork()) {
            loadNewList(url);
        } else {
            mSwipeListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            getMyActivity().showShortToast(getString(R.string.not_network));
            String result = getMyActivity().getCacheStr(cacheFragmentName + currentPagte);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
        }
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
			yuLeAdapter.appendList(newModles,index);
		}
	
//	    @UiThread
	    public void getResult(String result) {
	        getMyActivity().setCacheStr(this.getClass().getSimpleName()+ currentPagte, result);
	        if (isRefresh) {
	            isRefresh = false;
	            yuLeAdapter.clear();
	            listsModles.clear();
	        }
	        mProgressBar.setVisibility(View.GONE);
	        swipeLayout.setRefreshing(false);
	        List<NewModle> list = NewListJson.instance(getActivity()).readJsonNewModles(result,
	                Url.YuLeId);
	        if (index == 0) {
//	        	LogUtils2.i("is first come in************");
	            initSliderLayout(list);
	        } else {
//	        	LogUtils2.i("add data to the listView************");
	        	yuLeAdapter.appendList(list,index);
	        }
	        
	        if(yuLeAdapter.isNeedUplistsModlesData(index)){
	        	listsModles.addAll(list);
	        }
	        mSwipeListView.onBottomComplete();
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
	    

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPagte = 1;
                isRefresh = true;
                index = 0;
                loadData(getCommonUrl(0 + "", Url.YuLeId),cacheName);
                url_maps.clear();
                mDemoSlider.removeAllSliders();
            }
        }, 2000);
    }


//  @Background
    public void loadNewList(String url) {
  	LogUtils2.i("loadNewList.url = "+url);
      String result;
      try {
//          result = HttpUtil.getByHttpClient(getActivity(), url);
          new GetDataTask().execute(url);
//          getResult(result);
      } catch (Exception e) {
          e.printStackTrace();
          LogUtils2.e("loadNewList error -----");
      }
  }
    
    
    
    ////////////////////////////////////////
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils2.i("***onCreateView***");
		contentView = inflater.inflate(R.layout.news_activity_main, null);
		init() ;
//		LayoutUtils.init(getActivity(),listsModles,url_maps,newHashMap);
		initContentView(contentView);
		return contentView;
	}

    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils2.i("***onCreate***");
		mContext = getActivity();
		
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils2.i("***onViewCreated***");
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils2.i("***onActivityCreated***");
	}
	

	@Override
	public void onStart() {
		super.onStart();
		LogUtils2.i("***onStart***");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LogUtils2.i("***onResume***");
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
	}

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
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
//    	yuLeAdapter.getLists().clear();
    }
    
    
    ////////////////////////////////////////

	class MyYuLeListViewItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			LogUtils2.e("in the onItemClick the position = "+position);
//			Toast.makeText(mContext, "  pos== "+position, 300).show();
			   NewModle newModle = listsModles.get(position - 1);
		        enterDetailActivity(newModle);
		}
		
	}

	//open the head bar pic
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
            class1 = ImageDetailActivity.class;
        } else {
            class1 = DetailsActivity.class;
        }
        ((BaseActivity) getActivity()).openActivity(class1,
                bundle, 0);
    }

    
    
}
