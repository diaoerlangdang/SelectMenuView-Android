package com.wise.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuruizhi on 2018/3/30.
 */

public class SelectMenuView extends LinearLayout {

    public interface SelectMenuViewDelegate {

        /**
         * 菜单块数
         * @param menuView 菜单view
         * @return 菜单块数
         */
        int onMenuViewSection(SelectMenuView menuView);

        /**
         * 菜单某块的个数
         * @param menuView 菜单view
         * @param section 第几块
         * @return 个数
         */
        int onMenuViewRow(SelectMenuView menuView, int section);

        /**
         * 获取数据
         * @param menuView 菜单view
         * @param section 菜单块
         * @param row 某块的位置
         * @return
         */
        String onMenuViewContent(SelectMenuView menuView, int section, int row);

        /**
         * 菜单选择
         * @param menuView 菜单view
         * @param section 菜单块
         * @param row 某块的位置
         *
         */
        void onMenuViewSelect(SelectMenuView menuView, int section, int row);

        /**
         * 菜单完成
         * @param menuView 菜单
         * @param selectIndexs 选中的位置
         */
        void onMenuViewFinish(SelectMenuView menuView, List<Integer> selectIndexs);

    }

    //默认文字颜色
    private static final int DEFAULT_NORMAL_TEXT_COLOR = Color.parseColor("#000000");

    //默认选中文字颜色
    private static final int DEFAULT_SELECT_TEXT_COLOR = Color.parseColor("#000000");

    //默认背景颜色
    private static final int DEFAULT_NORMAL_BG_COLOR = Color.parseColor("#FFFFFF");

    //默认背景选中颜色
    private static final int DEFAULT_SELECT_BG_COLOR = Color.parseColor("#C0C0C0");

    //默认分隔线颜色
    private static final int DEFAULT_SEPARATE_LINE_COLOR = Color.parseColor("#C0C0C0");

    //默认字体大小
    private static final float DEFAULT_NORMAL_TEXT_SIZE = 15;

    //默认选中字体大小
    private static final float DEFAULT_SELECT_TEXT_SIZE = 15;

    //分隔线左侧边距
    private static final float DEFAULT_SEPARATE_LINE_MARGIN_LEFT = 10;

    //分割线右侧边距
    private static final float DEFAULT_SEPARATE_LINE_MARGIN_RIGHT = 10;


    private static String TAG = "SelectMenuView";

    //listviews 布局
    private LinearLayout mListViewLayout;

    private Context mContext;

    //listview列表
    private List<ListView> mListViews = new ArrayList<>();

    //listview选中的位置数组
    private List<Integer> mSelectIndexs = new ArrayList<>();

    //scrollView
    private HorizontalScrollView mScrollView;

    //代理
    private SelectMenuViewDelegate mDelegate;

    //每个菜单的宽度，默认100
    private int sectionWidth = 100;

    //是否已经显示
    private boolean isShow = false;

    //文字颜色
    private int mTextColor = DEFAULT_NORMAL_TEXT_COLOR;

    //选中文字颜色
    private int mSelectTextColor = DEFAULT_SELECT_TEXT_COLOR;

    //背景颜色
    private int mBgColor = DEFAULT_NORMAL_BG_COLOR;

    //选中背景颜色
    private int mSelectBgColor = DEFAULT_SELECT_BG_COLOR;

    //文字大小
    private float mTextSize = DEFAULT_NORMAL_TEXT_SIZE;

    //选中文字大小
    private float mSelectTextSize = DEFAULT_SELECT_TEXT_SIZE;

    //分隔线颜色
    private int mSeparateLineColor = DEFAULT_SEPARATE_LINE_COLOR;

    //分隔线左侧距离
    private float mSeparateLineMarginLeft = DEFAULT_SEPARATE_LINE_MARGIN_LEFT;

    //分隔线右侧距离
    private float mSeparateLineMarginRight = DEFAULT_SEPARATE_LINE_MARGIN_RIGHT;

    public SelectMenuView(Context context) {
        super(context);
        initView(context, null);
    }

    public SelectMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SelectMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs)
    {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.menuAttrs);
            mTextSize = typedArray.getDimension(R.styleable.menuAttrs_normalTextSize, DEFAULT_NORMAL_TEXT_SIZE);
            mSelectTextSize = typedArray.getDimension(R.styleable.menuAttrs_selectTextSize, DEFAULT_SELECT_TEXT_SIZE);

            mTextColor = typedArray.getColor(R.styleable.menuAttrs_normalTextColor, DEFAULT_NORMAL_TEXT_COLOR);
            mSelectTextColor = typedArray.getColor(R.styleable.menuAttrs_selectTextColor, DEFAULT_SELECT_TEXT_COLOR);

            mBgColor = typedArray.getColor(R.styleable.menuAttrs_normalTextSize, DEFAULT_NORMAL_BG_COLOR);
            mSelectBgColor = typedArray.getColor(R.styleable.menuAttrs_normalTextSize, DEFAULT_SELECT_BG_COLOR);

            mSeparateLineColor = typedArray.getColor(R.styleable.menuAttrs_separateLineColor, DEFAULT_SEPARATE_LINE_COLOR);
            mSeparateLineMarginLeft = typedArray.getDimension(R.styleable.menuAttrs_separateLineMarginLeft, DEFAULT_SEPARATE_LINE_MARGIN_LEFT);
            mSeparateLineMarginRight = typedArray.getDimension(R.styleable.menuAttrs_separateLineMarginRight, DEFAULT_SEPARATE_LINE_MARGIN_RIGHT);
            typedArray.recycle();

        }

        setBackgroundColor(Color.argb(204,0,0,0));
        setVisibility(GONE);

        setOrientation(VERTICAL);
        setWeightSum(2);

        mContext = context;

        mScrollView = new HorizontalScrollView(mContext);
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.setHorizontalFadingEdgeEnabled(false);
        LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        addView(mScrollView, scrollViewLayoutParams);

        mListViewLayout = new LinearLayout(context);
        mListViewLayout.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams listViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mScrollView.addView(mListViewLayout, listViewLayoutParams);

        LinearLayout bottomLayout = new LinearLayout(context);
        bottomLayout.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams bottomLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        addView(bottomLayout, bottomLayoutParams);

//        mListViewLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideMenu();
//            }
//        });
        bottomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });

    }

    /**
     * 设置代理
     * @param delegate 代理
     */
    public void setDelegate(SelectMenuViewDelegate delegate)
    {
        mDelegate = delegate;
    }

    /**
     * 获取每个菜单的宽度
     * @return
     */
    public int getSectionWidth() {
        return sectionWidth;
    }

    /**
     * 设置每个菜单的宽度， 默认100
     * @param sectionWidth 每个菜单的宽度
     */
    public void setSectionWidth(int sectionWidth) {
        this.sectionWidth = sectionWidth;
    }

    /**
     * 是否已显示
     * @return 是否已显示
     */
    public boolean isShow() {
        return isShow;
    }


    /**
     * 获取已选中的位置列表
     * @return 已选中的位置列表
     */
    public List<Integer> getSelectIndexs() {
        return mSelectIndexs;
    }

    /**
     * 显示菜单
     */
    public void showMenu()
    {
        if (isShow) {
            return;
        }

        setVisibility(VISIBLE);

        updateListViews();

        isShow = true;

        Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f );

        translateAnimation.setDuration(300);
        // 固定属性的设置都是在其属性前加“set”，如setDuration（）
        mListViewLayout.startAnimation(translateAnimation);
    }

    /**
     * 隐藏菜单
     */
    public void hideMenu()
    {
        Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f );

        translateAnimation.setDuration(300);

        mListViewLayout.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                isShow = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    //更行listviews
    private void updateListViews() {
        int num = mDelegate.onMenuViewSection(this);
        mListViewLayout.removeAllViews();

        mListViews.clear();
        mSelectIndexs.clear();

        addListView();
    }

    //添加ListView
    private void addListView() {

        ListView listView = new ListView(mContext);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalFadingEdgeEnabled(false);
        mListViews.add(listView);

        //默认不选中
        mSelectIndexs.add(-1);

        final MenuItemAdapter adapter = new MenuItemAdapter(mContext);
        adapter.listView = listView;
        listView.setAdapter(adapter);

        LinearLayout.LayoutParams listViewLayoutParams = new LinearLayout.LayoutParams(dip2px(mContext, sectionWidth), LinearLayout.LayoutParams.MATCH_PARENT);
        listViewLayoutParams.setMargins(dip2px(mContext, 5),0,dip2px(mContext, 5),0);
        mListViewLayout.addView(listView, listViewLayoutParams);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int section = mListViews.indexOf(parent);

                //修改选中列表
                mSelectIndexs.set(section, position);

                int num = mDelegate.onMenuViewSection(SelectMenuView.this);

                mDelegate.onMenuViewSelect(SelectMenuView.this, section, position);

                if (section+1 >= num) {
                    mDelegate.onMenuViewFinish(SelectMenuView.this, mSelectIndexs);
                    hideMenu();
                }
                else {

                    //刷新当前选中的listview
                    MenuItemAdapter adapter = (MenuItemAdapter)(mListViews.get(section).getAdapter());
                    adapter.notifyDataSetChanged();

                    if (section+1 >= mListViews.size()) {
                        addListView();
                    }
                    else {

                        //刷新下一个子listview
                        MenuItemAdapter nextAdapter = (MenuItemAdapter)(mListViews.get(section+1).getAdapter());
                        nextAdapter.notifyDataSetChanged();

                        List<ListView> tmp = new ArrayList<>();
                        for (int i=section+2; i<mListViews.size(); i++) {
                            tmp.add(mListViews.get(i));
                        }
                        for (ListView l:tmp) {
                            int pos = mListViews.indexOf(l);
                            mSelectIndexs.remove(pos);
                            mListViews.remove(l);
                            mListViewLayout.removeView(l);
                        }
                        mSelectIndexs.set(section+1, -1);
                    }
                }
            }
        });


        if (isShow) {
            Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f );

            translateAnimation.setDuration(300);
            // 固定属性的设置都是在其属性前加“set”，如setDuration（）
            listView.startAnimation(translateAnimation);

            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mScrollView.fullScroll(View.FOCUS_RIGHT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    // Adapter for holding devices found through scanning.
    private class MenuItemAdapter extends BaseAdapter
    {
        private LayoutInflater mInflator;
//        public SoftReference<ListView> listView;

        public  ListView listView;

        public MenuItemAdapter(Context context)
        {
            super();

            mInflator = LayoutInflater.from(context);
        }


        @Override
        public int getCount()
        {
            return mDelegate.onMenuViewRow(SelectMenuView.this, mListViews.indexOf(listView));
        }

        @Override
        public String getItem(int i)
        {
            return "";
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null)
            {
                view = mInflator.inflate(R.layout.menu_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = view.findViewById(R.id.menu_item);
                viewHolder.rootLayout = view.findViewById(R.id.item_root);
                viewHolder.separateLine = view.findViewById(R.id.separate_line);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, 0.2f));
                lp.setMargins(dip2px(mContext,mSeparateLineMarginLeft), 0, dip2px(mContext,mSeparateLineMarginRight), 0);

                viewHolder.separateLine.setLayoutParams(lp);

                view.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) view.getTag();
            }


            int section = mListViews.indexOf(listView);
            viewHolder.textView.setText(mDelegate.onMenuViewContent(SelectMenuView.this,section, i));
            viewHolder.separateLine.setBackgroundColor(mSeparateLineColor);


            if (i == mSelectIndexs.get(section).intValue()) {
                viewHolder.rootLayout.setBackgroundColor(mSelectBgColor);
                viewHolder.textView.setTextColor(mSelectTextColor);
                viewHolder.textView.setTextSize(mSelectTextSize);
            }
            else {
                viewHolder.rootLayout.setBackgroundColor(mBgColor);
                viewHolder.textView.setTextColor(mTextColor);
                viewHolder.textView.setTextSize(mTextSize);
            }

            return view;
        }
    }

    static class ViewHolder
    {
        LinearLayout rootLayout;
        TextView textView;
        TextView separateLine;
    }

}
