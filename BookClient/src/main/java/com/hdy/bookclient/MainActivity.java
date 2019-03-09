package com.hdy.bookclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.hdy.bookservice.Book;
import com.hdy.bookservice.BookControl;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bindBtn, inOutBtn, inBtn, outBtn, getBtn;
    private final String TAG = "MainActivity";

    private BookControl bookControl;

    private boolean connected;//连接是否中断

    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindBtn = findViewById(R.id.btn_bind);
        inOutBtn = findViewById(R.id.btn_inout);
        inBtn = findViewById(R.id.btn_in);
        outBtn = findViewById(R.id.btn_out);
        getBtn = findViewById(R.id.btn_get);
        bindBtn.setOnClickListener(this);
        inOutBtn.setOnClickListener(this);
        inBtn.setOnClickListener(this);
        outBtn.setOnClickListener(this);
        getBtn.setOnClickListener(this);
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookControl = BookControl.Stub.asInterface(service);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind:
                Intent intent = new Intent();
                intent.setPackage("com.hdy.bookservice");//服务器报名
                intent.setAction("com.hdy.aidlservice.action");
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.btn_get:
                if (connected) {
                    try {
                        bookList = bookControl.getBookList();
                        for (Book book : bookList) {
                            Log.i(TAG, book.toString());
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_inout:
                if (connected) {
                    Book book = new Book();
                    book.setName("inout book");
                    try {
                        bookControl.addBookInOut(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_in:
                if (connected) {
                    Book book = new Book();
                    book.setName("in book");
                    try {
                        bookControl.addBookIn(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_out:
                if (connected) {
                    Book book = new Book();
                    book.setName("out book");
                    try {
                        bookControl.addBookOut(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
