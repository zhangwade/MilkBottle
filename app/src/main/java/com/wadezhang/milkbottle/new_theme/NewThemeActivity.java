package com.wadezhang.milkbottle.new_theme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.OnClickListenerToPostDetail;
import com.wadezhang.milkbottle.theme.Theme;
import com.wadezhang.milkbottle.theme_category.ThemeCategory;
import com.wadezhang.milkbottle.theme_post_list.ThemePostListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class NewThemeActivity extends BaseActivity {

    @BindView(R.id.activity_new_theme_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_new_theme_text_select_category) TextView mSelectCategory;
    @BindView(R.id.activity_new_theme_edit_name) EditText mEditName;
    @BindView(R.id.activity_new_theme_text_name_wordcount) TextView mNameWordCount;
    @BindView(R.id.activity_new_theme_btn_nextstep) Button mBtnNextStep;
    @BindView(R.id.activity_new_theme_second_tip) TextView mSecondTip;
    @BindView(R.id.activity_new_theme_edit_introduction) EditText mEditIntroduction;
    @BindView(R.id.activity_new_theme_text_introduction_wordcount) TextView mIntroductionWordCount;
    @BindView(R.id.activity_new_theme_btn_create) Button mBtnCreate;

    Context mContext;

    private final int THEME_NAME_WORD_LIMIT = 30;
    private final int THEME_INTRODUCTION_WORD_LIMIT = 100;

    Theme mExistedTheme;
    String mDecidedName;
    String mDecidedIntroduction;
    String mSelectCategoryId;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, NewThemeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_new_theme);
        ButterKnife.bind(this);
        init();
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewThemeActivity.this, SelectThemeCategoryActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        editNameOnChange();
        btnNextOnClick();
        editIntroductionOnChange();
        btnCreateOnClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1 :
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    mSelectCategoryId = bundle.getString("themeCategoryId");
                    mSelectCategory.setText(bundle.getString("themeCategoryTitle"));
                }
                break;
            default:
        }
    }

    public void init(){
        mEditName.setFilters(new InputFilter[]{getInputFilterForSpaceAndeEnter()});
        mNameWordCount.setVisibility(View.INVISIBLE);
        mBtnNextStep.setVisibility(View.INVISIBLE);
        mSecondTip.setVisibility(View.INVISIBLE);
        mEditIntroduction.setVisibility(View.INVISIBLE);
        mIntroductionWordCount.setVisibility(View.INVISIBLE);
        mBtnCreate.setVisibility(View.INVISIBLE);
    }

    public void editNameOnChange(){
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mNameWordCount.setText(Integer.toString(mEditName.getText().length()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNameWordCount.setText(Integer.toString(mEditName.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mEditName.getText().toString().trim().equals("")){
                    if(mEditName.getText().length() <= THEME_NAME_WORD_LIMIT){
                        mBtnNextStep.setVisibility(View.VISIBLE);
                        mNameWordCount.setText(Integer.toString(mEditName.getText().length()));
                    } else{
                        mBtnNextStep.setVisibility(View.INVISIBLE);
                        mNameWordCount.setText(Integer.toString(THEME_NAME_WORD_LIMIT - mEditName.getText().length()));
                    }
                    mNameWordCount.setVisibility(View.VISIBLE);
                }else {
                    mBtnNextStep.setVisibility(View.INVISIBLE);
                    mNameWordCount.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void btnNextOnClick(){
        mBtnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("正在加载...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                mDecidedName = mEditName.getText().toString().trim();
                BmobQuery<Theme> query = new BmobQuery<Theme>();
                query.addWhereEqualTo("name", mDecidedName);
                query.addQueryKeys("objectId,name");
                query.findObjects(new FindListener<Theme>() {
                    @Override
                    public void done(List<Theme> list, BmobException e) {
                        mProgressDialog.dismiss();
                        if(e == null){
                            if(!list.isEmpty()){
                                mExistedTheme = list.get(0);
                                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                                mAlertDialog.setTitle("已有相同的话题");
                                mAlertDialog.setMessage("不能创建完全相同的话题哦~~");
                                mAlertDialog.setCancelable(true);
                                mAlertDialog.setPositiveButton("去看看", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ThemePostListActivity.actionStart(mContext, mExistedTheme);
                                    }
                                });
                                mAlertDialog.setNegativeButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                mAlertDialog.show();
                            }else{
                                mBtnNextStep.setVisibility(View.GONE);
                                mEditName.setEnabled(false);
                                mSecondTip.setVisibility(View.VISIBLE);
                                mEditIntroduction.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Toast.makeText(mContext, "网络出了点小差哦~~", Toast.LENGTH_SHORT);
                            Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }

    public void editIntroductionOnChange(){
        mEditIntroduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mIntroductionWordCount.setText(Integer.toString(mEditIntroduction.getText().length()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIntroductionWordCount.setText(Integer.toString(mEditIntroduction.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mEditIntroduction.getText().toString().trim().equals("")){
                    if(mEditIntroduction.getText().length() <= THEME_INTRODUCTION_WORD_LIMIT){
                        mBtnCreate.setVisibility(View.VISIBLE);
                        mIntroductionWordCount.setText(Integer.toString(mEditIntroduction.getText().length()));
                    } else{
                        mBtnCreate.setVisibility(View.INVISIBLE);
                        mIntroductionWordCount.setText(Integer.toString(THEME_INTRODUCTION_WORD_LIMIT - mEditIntroduction.getText().length()));
                    }
                    mIntroductionWordCount.setVisibility(View.VISIBLE);
                }else {
                    mBtnCreate.setVisibility(View.INVISIBLE);
                    mIntroductionWordCount.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void btnCreateOnClick(){
        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectCategoryId == null){
                    Toast.makeText(mContext, "还没有选择话题类别哦~~", Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("正在创建...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                mDecidedIntroduction = mEditIntroduction.getText().toString();
                Theme theme = new Theme();
                ThemeCategory themeCategory = new ThemeCategory();
                themeCategory.setObjectId(mSelectCategoryId);
                theme.setCategory(themeCategory);
                theme.setName(mDecidedName);
                theme.setIntroduction(mDecidedIntroduction);
                theme.setPostCount(0);
                User author = new User();
                author.setObjectId("C0NeXXX3"); //TODO: 注册登录模块完成后要修改这里
                theme.setAuthor(author);
                theme.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        progressDialog.dismiss();
                        if(e == null){
                            finish();
                        }else{
                            Toast.makeText(mContext, "网络错误，请重试~~", Toast.LENGTH_LONG).show();
                            Log.d(getClass().getSimpleName(), "bmob添加Theme失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
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
}
