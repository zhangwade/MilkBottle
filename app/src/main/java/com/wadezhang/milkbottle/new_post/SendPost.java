package com.wadezhang.milkbottle.new_post;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.theme.Theme;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class SendPost {

    private Theme mTheme;
    private String mPhotoPath;
    private String mContent;
    private User mAuthor;
    private Handler mHandler;

    public SendPost(Theme theme, String photoPath, String content, User author, Handler handler){
        mTheme = theme;
        mPhotoPath = photoPath;
        mContent = content;
        mAuthor = author;
        mHandler = handler;
        if(mPhotoPath != null) uploadPhoto();
            else uploadPost(null);
    }

    public void uploadPhoto(){
        final BmobFile photoFile = new BmobFile(new File(mPhotoPath));
        photoFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    uploadPost(photoFile);
                }else{
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                    Log.d(getClass().getSimpleName(), "bmob上传图片失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void uploadPost(BmobFile photoFile){
        Post post = new Post();
        post.setTheme(mTheme);
        if(photoFile != null) post.setPhoto(photoFile);
        post.setContent(mContent);
        post.setAuthor(mAuthor);
        post.setLikesCount(0);
        post.setCommentCount(0);
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what = 2;
                    mHandler.sendMessage(message);
                    Log.d(getClass().getSimpleName(), "bmob新增帖子失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
