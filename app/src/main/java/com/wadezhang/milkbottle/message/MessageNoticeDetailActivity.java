package com.wadezhang.milkbottle.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class MessageNoticeDetailActivity extends BaseActivity {

    @BindView(R.id.activity_message_notice_detail_text_content) TextView mContent;
    @BindView(R.id.activity_message_notice_detail_imgbtn_back) ImageButton mBtnBack;

    Context mContext;

    public static void actionStart(Context context, String content){
        Intent intent = new Intent(context, MessageNoticeDetailActivity.class);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notice_detail);
        ButterKnife.bind(this);
        mContext = this;
        Intent mIntent = getIntent();
        mContent.setText(mIntent.getStringExtra("content"));
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
