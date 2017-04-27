package com.wadezhang.milkbottle.book;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class BookContentPresenter implements BookContentContract.Presenter {

    private BookContentContract.View mBookContentView;

    private Book mBook;
    File mTxtFile;
    private String mContent;
    private long mCurrentPage = 1;
    private int mPageSize;

    private Handler mHandler;

    public BookContentPresenter(BookContentContract.View view){
        mBookContentView = view;
        mBookContentView.setPresenter(this);
    }

    @Override
    public void setPageSize(int pageSize, Handler handler){
        mPageSize = pageSize;
        mHandler = handler;
    }

    @Override
    public void start(){
        BmobQuery<Book> mQuery = new BmobQuery<Book>();
        mQuery.addQueryKeys("content");
        mQuery.getObject("wYpU666s", new QueryListener<Book>() {
            @Override
            public void done(Book book, BmobException e) {
                if(e == null){
                    mBook = book;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloadFile(mBook.getContent());
                        }
                    }).start();
                } else{
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                Log.i("bmob","开始下载");
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    mTxtFile = new File(savePath);
                    Message mMessage = new Message();
                    mMessage.what = 1;
                    mHandler.sendMessage(mMessage);
                }else{
                    Log.i("bmob", "下载失败："+e.getErrorCode()+","+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }

    @Override
    public void getPreviousPage(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ReadTxt mReadTxt = new ReadTxt(mTxtFile, mCurrentPage, mPageSize);
                    mContent = mReadTxt.getPre();
                    mCurrentPage = mReadTxt.getCurPage();
                }
            }).start();
            showBookContent();
    }

    @Override
    public void getNextPage(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ReadTxt mReadTxt = new ReadTxt(mTxtFile, mCurrentPage, mPageSize);
                    mContent = mReadTxt.getNext();
                    mCurrentPage = mReadTxt.getCurPage();
                }
            }).start();
            showBookContent();
    }

    @Override
    public void showBookContent(){
        mBookContentView.updateBookContent(mContent);
    }
}
