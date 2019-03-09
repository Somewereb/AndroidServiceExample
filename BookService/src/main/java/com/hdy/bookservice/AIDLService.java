package com.hdy.bookservice;

import android.app.Notification;
import android.app.Service;
import android.app.job.JobInfo;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2019/3/3 23:48
 * author:somewereb
 * description:
 */
public class AIDLService extends Service {
    private final String TAG = "AIDLService";
    private List<Book> bookList;
    private final String CHANNELID = "Notification_ID";//渠道id
    private final int NotificationID = 11221;//Notification id

    private int inout = 0;
    private int in = 0;
    private int out = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {//保持服务不给杀死，设置成前台服务
            Notification notification = new Notification.Builder(this, CHANNELID).build();
            startForeground(NotificationID, notification);
        }
        initData();
    }

    private void initData() {
        bookList = new ArrayList<>();
        Book book = new Book();
        book.setName("first book");
        Book book2 = new Book();
        book2.setName("second book");
        bookList.add(book);
        bookList.add(book2);
    }

    //无法获取时检查下项目再claer一下项目
    BookControl.Stub stub=new BookControl.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.e(TAG, "书数量：" + bookList.size());
            return bookList;
        }

        @Override
        public void addBookInOut(Book book) throws RemoteException {
            if (book != null) {
                Log.e(TAG, "客户端传来的书名字：" + book.getName());
                inout++;
                book.setName("服务器改了新书的名字 InOut    "+inout);
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 InOut");
            }
        }

        @Override
        public void addBookOut(Book book) throws RemoteException {
            if (book != null) {
                out++;
                Log.e(TAG, "客户端传来的书的名字：" + book.getName());
                book.setName("服务器改了新书的名字 Out    "+out);
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 Out");
            }
        }

        @Override
        public void addBookIn(Book book) throws RemoteException {
            if (book != null) {
                in++;
                Log.e(TAG, "客户端传来的书名字：" + book.getName());
                book.setName("服务器改了新书的名字 In:"+in);
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 In");
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
