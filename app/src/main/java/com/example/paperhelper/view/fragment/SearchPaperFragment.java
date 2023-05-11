package com.example.paperhelper.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupMenu;

import com.example.paperhelper.R;
import com.example.paperhelper.model.Model;
import com.example.paperhelper.model.bean.PageUrlModel;
import com.example.paperhelper.view.activity.dialog.DialogManager;
import com.example.paperhelper.utils.MyDownloadStart;
import com.example.paperhelper.view.Interface.OnWebItemClickListener;
import com.example.paperhelper.view.Interface.OnWebLongClickListener;
import com.example.paperhelper.view.fragment.adapter.PaperPageRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomRecyclerView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.List;

public class SearchPaperFragment extends Fragment {
    private Context context;
    private Activity activity;

    private QMUIContinuousNestedBottomRecyclerView webPageRecyclerView;

    private PaperPageRecyclerAdapter recyclerAdapter;
    private WebView webView;
    private QMUITopBar topBar;
    private FloatingActionButton addBtn;
    private List<PageUrlModel> itemList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    webPageRecyclerView.notify();
            }
        }
    };
    public SearchPaperFragment(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_paper, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view){
        webPageRecyclerView = (QMUIContinuousNestedBottomRecyclerView) view.findViewById(R.id.paper_page_rv);
        webView = (WebView) view.findViewById(R.id.web_page_wv);
        initWebView();
        topBar = (QMUITopBar) view.findViewById(R.id.top_bar);
        setTopBar();
        addBtn = (FloatingActionButton) view.findViewById(R.id.add_page_btn);
        initList();
        initRecyclerView();
        //获取列表中的第一个网址显示
        loadWebPage(webView, itemList.get(0).getUrl());

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DialogManager.getInstance().addPageUrlDialogShow(activity);
                DialogManager.getInstance().addPageUrlDialogShow(activity,itemList);
            }
        });
    }

    /**
     * 设置webView相关属性
     */
    private void initWebView(){
        //设置页面适应屏幕
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        //放大设置
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        //download file from webview
        webView.setDownloadListener(new MyDownloadStart(activity, webView));
    }

    /**
     * 设置topBar样式
     */
    private void setTopBar(){
        QMUIAlphaImageButton backImageBtn = topBar.addLeftImageButton(R.drawable.baseline_arrow_back_ios_24, R.id.top_bar);
        backImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.goBack();
            }
        });
    }

    /**
     * 初始化列表数据
     */
    private void initList(){
        itemList = Model.getInstance().getUrlList();
    }

    /**
     * 初始化网址列表
     */
    private void initRecyclerView(){
        recyclerAdapter = new PaperPageRecyclerAdapter(context, webPageRecyclerView, itemList);

        //注册点击事件
        recyclerAdapter.setOnItemClickListener(new OnWebItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, PageUrlModel item) {
                loadWebPage(webView, item.getUrl());
            }
        });

        //注册长按点击事件
        recyclerAdapter.setOnLongClickListener(new OnWebLongClickListener() {
            @Override
            public void onLongClick(RecyclerView parent, View view, int position, PageUrlModel item) {
                popupMenuShow(view, position);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        webPageRecyclerView.setLayoutManager(manager);
        webPageRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * load website
     * @param webView show scholar website
     * @param url to start website
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
        //get website name for setting Tab label
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                topBar.setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });
        webView.loadUrl(url);
    }

    /**
     * 弹出popup菜单
     * @param view
     */
    private void popupMenuShow(View view, int position){
        //创建弹出式菜单对象
        PopupMenu menu = new PopupMenu(context, view);
        //获取菜单填充器
        MenuInflater inflater = menu.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.delete_and_update_menu, menu.getMenu());
        //绑定菜单项的点击事件
        setPopupMenuClick(menu,position);
        menu.show();
    }

    /**
     * 设置popup菜单点击事件
     * @param menu
     * @param position 点击项目在列表中的位置
     */
    private void setPopupMenuClick(PopupMenu menu, int position){
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.update:
//                        DialogManager.getInstance().editPageUrlDialogShow(activity, itemList, position, recyclerAdapter);
                        break;
                    case R.id.delete:
                        DialogManager.getInstance().deletePageUrlDialogShow(activity, itemList, position,recyclerAdapter);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}