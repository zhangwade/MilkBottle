package com.wadezhang.milkbottle.post_detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class WriteCommentActivity extends BaseActivity {

    @BindView(R.id.activity_write_comment_text_cancel) TextView mCancel;
    @BindView(R.id.activity_write_comment_text_send) TextView mSend;
    @BindView(R.id.activity_write_comment_textinputlayout) TextInputLayout mTextInputLayout;
    @BindView(R.id.activity_write_comment_edit) TextInputEditText mEdit;

    private final int TYPE_COMMENT_TO_POST = 0;  //评论帖子
    private final int TYPE_COMMENT_TO_COMMENT = 1;  //评论别人的评论

    private int mType;
    private String mPostId;
    private String mAuthorId;
    private String mAuthorName;

    private final int MAX_NUM = 500;

    Context mContext;

    public static void actionStart(Context context, int type, String postId, String authorId, String authorName){
        Intent intent = new Intent(context, WriteCommentActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("postId", postId);
        intent.putExtra("authorId", authorId);
        intent.putExtra("authorName", authorName);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        ButterKnife.bind(this);
        mContext = this;
        init();
        mSend.setEnabled(false);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editComment();
        sendConfirm();
    }

    public void init(){
        Intent mIntent = getIntent();
        mType = mIntent.getIntExtra("type", 0);
        mPostId = mIntent.getStringExtra("postId");
        if(mType == TYPE_COMMENT_TO_COMMENT){
            mAuthorId = mIntent.getStringExtra("authorId");
            mAuthorName = mIntent.getStringExtra("authorName");
            mEdit.setHint("回复: " + mAuthorName);
        }else{
            mEdit.setHint("写评论...");
        }
    }

    public void editComment(){
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = mEdit.getText().toString().trim().length();
                if(len > 0 && len <= MAX_NUM) mSend.setEnabled(true);
                    else mSend.setEnabled(false);
            }
        });
    }

    public void sendConfirm(){
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEdit.getText().length() > MAX_NUM) return;
                User user = GetCurrentUser.getCurrentUser(mContext);
                if(user == null) return;
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("正在发送...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Comment comment = new Comment();
                comment.setIsRead(0);
                comment.setAuthor(user);
                Post post = new Post();
                post.setObjectId(mPostId);
                comment.setPost(post);
                comment.setContent(mEdit.getText().toString());
                if(mAuthorId != null){
                    User toWho = new User();
                    toWho.setObjectId(mAuthorId);
                    comment.setToWho(toWho);
                }
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            Post post = new Post();
                            post.increment("commentCount");
                            post.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    progressDialog.dismiss();
                                    if(e == null){
                                        Intent intent = new Intent("com.wadezhang.milkbottle.REFRESH_COMMENT_LIST");
                                        sendBroadcast(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(mContext, "发送失败，请检查网络是否开启", Toast.LENGTH_SHORT).show();
                                        Log.d(getClass().getSimpleName(), "bmob更新 commentCount 失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(mContext, "发送失败，请检查网络是否开启", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "bmob添加评论失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }
}
