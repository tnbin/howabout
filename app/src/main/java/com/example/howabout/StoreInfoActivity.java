package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;;import com.example.howabout.category_search.Document;

import java.net.URL;


public class StoreInfoActivity extends AppCompatActivity {


    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_info);

        Intent intent=getIntent();
        Document document=intent.getParcelableExtra("10");
        String url=document.getPlaceUrl();
        //xml 연결
        webView = findViewById(R.id.web_store);
        //자바스트립트 허용
        webView.getSettings().setJavaScriptEnabled(true);
        //웹뷰 실행 Url 적용
        webView.loadUrl(url);
        //크롬 실행 가능
        webView.setWebChromeClient(new WebChromeClient());
        //기존창에 실행
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static class WebViewClientClass extends WebViewClient {//페이지 이동

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("subin", url);
            view.loadUrl(url);
            return true;
        }
    }

}