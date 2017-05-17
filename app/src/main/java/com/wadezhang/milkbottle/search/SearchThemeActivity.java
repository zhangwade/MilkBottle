package com.wadezhang.milkbottle.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme.Theme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class SearchThemeActivity extends BaseActivity {

    @BindView(R.id.activity_search_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_search_edit) EditText mEdit;
    @BindView(R.id.activity_search_text_search) TextView mSearch;
    @BindView(R.id.activity_search_recyclerview) RecyclerView mRecyclerView;

    private final int WORD_MAX_NUM = 30;
    //private String previousSearchName = " ";

    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 20; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多

    private List<Theme> mThemeList = new ArrayList<>();
    private SearchThemeAdapter mSearchThemeAdapter;

    private int mType;
    private final int TYPE_NEW_POST = 0;
    private final int TYPE_SEARCH_THEME = 1;

    Context mContext;

    public static void actionStart(Context context, int type){
        Intent intent = new Intent(context, SearchThemeActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mContext = this;
        init();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSearchThemeAdapter = new SearchThemeAdapter(mThemeList, mType);
        mRecyclerView.setAdapter(mSearchThemeAdapter);
        mRecyclerView.addOnScrollListener(new LoadMoreListener());
        mSearch.setEnabled(false);
        mRecyclerView.setVisibility(View.INVISIBLE);
        editThemeName();
        searchConfirm();
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init(){
        Intent mIntent = getIntent();
        mType = mIntent.getIntExtra("type", 0);
    }

    public class LoadMoreListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //Log.d("------->isSlideToBottom:" + isSlideToBottom(recyclerView));
            if (isSlideToBottom(recyclerView)) {
                //srlLayout.setEnabled(true);
                getTheme(STATE_MORE);
            }
        }
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    public void editThemeName(){
        mEdit.setHint("搜索话题");
        mEdit.setFilters(new InputFilter[]{getInputFilterForSpaceAndeEnter()});
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                if(mEdit.getText().length() <= WORD_MAX_NUM && mEdit.getText().length() > 0){
                    mSearch.setEnabled(true);
                }else {
                    mSearch.setEnabled(false);
                }
            }
        });
    }

    public static InputFilter getInputFilterForSpaceAndeEnter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
                if (source.equals(" ") || source.equals("\n"))
                    return "";
                else
                    return null;
            }
        };
    }

    public void searchConfirm(){
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(mEdit.getText().toString().equals(previousSearchName)) return;
                getTheme(STATE_REFRESH);
                //previousSearchName = mEdit.getText().toString();
            }
        });
    }

    public void getTheme(int actionType){
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在加载...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mActionType = actionType;
        BmobQuery<Theme> query = new BmobQuery<>();
        query.addWhereEqualTo("name", mEdit.getText().toString());
        query.order("-createdAt");
        query.addQueryKeys("objectId,name,postCount,createdAt");
        // 如果是加载更多
        if (mActionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                //Log.i("0414", date.toString());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(curPage * limit - 1);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<Theme>() {
            @Override
            public void done(List<Theme> list, BmobException e) {
                progressDialog.dismiss();
                if(e == null){
                    if (!list.isEmpty()) {
                        if (mActionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            // 获取目前最新的时间
                            lastTime = list.get(0).getCreatedAt();
                        }
                        // 这里在每次加载完数据后，将当前页码+1
                        curPage++;
                        showUserList(mActionType, list);
                    } else if (mActionType == STATE_MORE) {
                        Toast.makeText(mContext, "到底了", Toast.LENGTH_SHORT).show();
                    } else if (mActionType == STATE_REFRESH) {
                        Toast.makeText(mContext, "没有找到相关结果", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void showUserList(int actionType, List<Theme> postList){
        if(actionType == STATE_REFRESH) mThemeList.clear();
        mThemeList.addAll(postList);
        mSearchThemeAdapter.notifyDataSetChanged();
        if(mRecyclerView.getVisibility() == View.INVISIBLE) mRecyclerView.setVisibility(View.VISIBLE);
    }
}
