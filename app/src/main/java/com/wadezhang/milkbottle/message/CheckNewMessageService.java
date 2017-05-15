package com.wadezhang.milkbottle.message;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post_detail.Comment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class CheckNewMessageService extends Service {

    private final int READ_NO = 0;
    private final int READ_YES = 1;

    User mUser;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        User user = GetCurrentUser.getCurrentUser(getApplicationContext());
        if(user == null) stopSelf();
        mUser = new User();
        mUser.setObjectId(user.getObjectId());
        checkComment();
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int time = 15 * 60 * 1000;  //间隔 15 分钟
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, CheckNewMessageService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void checkComment(){
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("toWho", mUser);
        commentBmobQuery.addWhereEqualTo("isRead", READ_NO);
        commentBmobQuery.addQueryKeys("objectId");
        commentBmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    sendNewMessageBroadcast();
                }else{
                    checkLikes();
                    //Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void checkLikes(){
        BmobQuery<Likes> likesBmobQuery = new BmobQuery<>();
        likesBmobQuery.addWhereEqualTo("to", mUser);
        likesBmobQuery.addWhereEqualTo("isRead", READ_NO);
        likesBmobQuery.addQueryKeys("objectId");
        likesBmobQuery.findObjects(new FindListener<Likes>() {
            @Override
            public void done(List<Likes> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    sendNewMessageBroadcast();
                }else{
                    checkFans();
                    //Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void checkFans(){
        BmobQuery<Fans> fansBmobQuery = new BmobQuery<>();
        fansBmobQuery.addWhereEqualTo("to", mUser);
        fansBmobQuery.addWhereEqualTo("isRead", READ_NO);
        fansBmobQuery.addQueryKeys("objectId");
        fansBmobQuery.findObjects(new FindListener<Fans>() {
            @Override
            public void done(List<Fans> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    sendNewMessageBroadcast();
                }else{
                    //Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void sendNewMessageBroadcast(){
        Intent intent = new Intent("com.wadezhang.milkbottle.NEW_MESSAGE");
        sendBroadcast(intent);
    }
}
