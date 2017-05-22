package com.wadezhang.milkbottle.post_detail;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.message.Notice;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class CreateNoticeService extends IntentService {

    private String administratorId = "jDMNRNNR";

    public CreateNoticeService(){
        super("CreateNoticeService");
    }

    @Override
    public void onHandleIntent(Intent intent){
        int type = intent.getIntExtra("type", 0); // 0 帖子 1 评论
        String userId = intent.getStringExtra("userId");
        String content = intent.getStringExtra("content");
        if(content.length() > 20) content = content.substring(0, 20);
        User from = new User();
        from.setObjectId(administratorId);
        User to = new User();
        to.setObjectId(userId);
        Notice notice = new Notice();
        notice.setFrom(from);
        notice.setTo(to);
        notice.setIsRead(0);
        if(type == 0) notice.setContent("尊敬的用户，您好！您发布的帖子 「" + content +"...」 因违规已被删除，请注意文明用语，感谢您的配合！");
            else notice.setContent("尊敬的用户，您好！您发布的评论 「" + content +"...」 因违规已被删除，请注意文明用语，感谢您的配合！");
        notice.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){

                }else{
                    Log.d(getClass().getSimpleName(), "bmob创建 Notice 失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
