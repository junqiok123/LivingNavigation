package com.jq.livingnavigation.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import com.jq.livingnavigation.R;
import com.jq.livingnavigation.config.Constant;
import com.jq.livingnavigation.view.ProgressWebView;
import com.zzt.inbox.interfaces.OnDragStateChangeListener;
import com.zzt.inbox.widget.InboxBackgroundScrollView;
import com.zzt.inbox.widget.InboxLayoutBase;
import com.zzt.inbox.widget.InboxLayoutScrollView;

public class MainActivity extends ActionBarActivity {

    private InboxLayoutScrollView guessLayout, robotLayout;
    private ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF004567));
        getSupportActionBar().setTitle("首页");

//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, LocationActivity.class));
//            }
//        });

        initInbox();
        init();
    }

    private void initInbox() {
        final InboxBackgroundScrollView inboxBackgroundScrollView = (InboxBackgroundScrollView) findViewById(R.id.scroll);
        guessLayout = (InboxLayoutScrollView) findViewById(R.id.guesslayout);
        guessLayout.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        guessLayout.setCloseDistance(50);
        guessLayout.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff87D3E3));
                        getSupportActionBar().setTitle("松开返回");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF004567));
                        getSupportActionBar().setTitle("首页");
                        break;
                }
            }
        });
        robotLayout = (InboxLayoutScrollView) findViewById(R.id.robotlayout);
        robotLayout.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        robotLayout.setCloseDistance(50);
        robotLayout.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff87D3E3));
                        getSupportActionBar().setTitle("松开返回");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF004567));
                        getSupportActionBar().setTitle("首页");
                        break;
                }
            }
        });
    }


    private void init() {
        final LinearLayout dingdan = (LinearLayout) findViewById(R.id.ding_dan);
        dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle("逗比猜一猜");
                guessLayout.openWithAnim(dingdan);
            }
        });

        final LinearLayout yuding = (LinearLayout) findViewById(R.id.yuding);
        yuding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle("无聊机器人");
                robotLayout.openWithAnim(yuding);
            }
        });

        final LinearLayout tuijian = (LinearLayout) findViewById(R.id.tuijian);
        tuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                inboxLayoutListView.openWithAnim(tuijian);
            }
        });

        webView = (ProgressWebView) findViewById(R.id.hello_world);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setLoadsImagesAutomatically(true);
//        webView.setHorizontalScrollBarEnabled(false);//设置水平滚动条
//        webView.setVerticalScrollBarEnabled(false);//设置竖直滚动条
        webView.loadUrl(Constant.HELLO_WORLD_URL);
//        webView.loadUrl(Constant.HELLO_WORLD_URL);
//        webView.loadDataWithBaseURL(null, Constant.HELLO_WORLD_URL, "text/html", "utf-8", null);
//        webView.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//                view.loadUrl(url);
//                return true;
//            }
//        });
    }
}
