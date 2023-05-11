package com.example.paperhelper.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.paperhelper.R;

import java.io.File;

/**
 * @author Sherlock
 * PDF展示界面
 */
public class PdfShowFragment extends Fragment {
    private View view;
    private File file;
    private WebView pdfView;

    public PdfShowFragment(File file) {
        this.file = file;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pdf_show, container, false);
        initView(view);
        String pdf_url = "file://" + file.getPath();
        loadWebPage(pdfView, "file:///android_asset/pdfjs/web/viewer.html?file=" + pdf_url);
        return view;
    }

    /**
     * 初始化pdf_show_fragment界面
     * @param view
     */
    private void initView(View view){
        pdfView = view.findViewById(R.id.pdf_view);

        //进行webView配置
        WebSettings webSettings = pdfView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        // 允许加载本地文件
        webSettings.setAllowUniversalAccessFromFileURLs(true);
    }

    /**
     * 加载本地的pdf文件
     * @param webView
     * @param url
     */
    private void loadWebPage(WebView webView, String url){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }
}
