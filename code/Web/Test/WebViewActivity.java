package com.example.weblogindemo;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); //设置支持JavaScript
        webView.getSettings().setSupportZoom(true); //页面能够放缩
        webView.getSettings().setBuiltInZoomControls(true); //支持放缩控制器
        webView.getSettings().setDisplayZoomControls(false);//隐藏Zoom缩放按钮
        webView.loadUrl("https://www.qweather.com/weather");
        //设置防止url使用第三方浏览器来打开；
        webView.setWebViewClient(new WebViewClient());

        //注册点击后退事件，如果点击后退，首先webView回到上一个页面，
        //如果，不能回退，则提示是否退出；
        webView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { //表示按返回键
                        webView.goBack(); // 后退
                        return true; // 已处理
                    } else if (keyCode == KeyEvent.KEYCODE_BACK
                            && !webView.canGoBack()) {
                        showExitAlert(WebViewActivity.this);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    /** 系统退出警告对话框
     * @param context
     */
    private void showExitAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定退出吗？\n");
        builder.setPositiveButton("确 定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        System.exit(0);
                    }
                });
        builder.setNegativeButton("取 消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}
   