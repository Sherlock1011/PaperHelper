package com.example.paperhelper.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.lib_common_ui.StatusBarManager;
import com.example.lib_common_util.JsonModuleUtil;
import com.example.lib_network.okhttp.response.listener.DisposeDataListener;
import com.example.paperhelper.R;
import com.example.paperhelper.model.bean.chat_bean.Message;
import com.example.paperhelper.model.bean.chat_bean.MessagesFixtures;
import com.example.paperhelper.utils.RequestCenter;
import com.example.paperhelper.view.fragment.NotebookListFragment;
import com.example.paperhelper.view.fragment.PdfShowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PdfAndNoteActivity extends AppCompatActivity {
    private File file;
    private QMUITopBar topBar;
    private FloatingActionButton gpt_btn;
    private RelativeLayout gpt_layout;
    private boolean isShowGPT = false;

    private MessagesList messagesList;
    private MessageInput messageInput;
    private MessagesListAdapter<Message> messageListAdapter;
    private Message waitingMessage = MessagesFixtures.getTextMessage("Waiting a moment...", "1");

    private final int SUCCESSFUL = 0;
    private final int FAILURE = 1;

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case SUCCESSFUL:
                    messageListAdapter.delete(waitingMessage);
                    Message answerMessage = MessagesFixtures.getTextMessage(((String) msg.obj).trim(), "1");
                    messageListAdapter.addToStart(answerMessage, true);
                    break;
                case FAILURE:
                    messageListAdapter.delete(waitingMessage);
                    Message failedMessage = MessagesFixtures.getTextMessage("Failed to load response due to "+msg.obj, "1");
                    messageListAdapter.addToStart(failedMessage, true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_and_note);

        StatusBarManager.setStatusBarFull(this,getWindow(),getResources());
        getFile();
        initView();
    }

    /**
     * 获取主页传来的文件地址
     * 初始化一个文件
     */
    private void getFile(){
        Intent intent = getIntent();
        String file_path = intent.getStringExtra("file_path");
        file = new File(file_path);
        Log.e("FILE_PATH", file_path);
    }

    /**
     * 初始化页面
     */
    private void initView(){
        initFragment();

        topBar = (QMUITopBar) findViewById(R.id.pdf_notebook_topbar);
        setTopBar();

        gpt_layout = (RelativeLayout) findViewById(R.id.gpt_layout);

        gpt_btn = (FloatingActionButton) findViewById(R.id.gpt_btn);
        gpt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGPTView();
            }
        });

        //初始化chatView
        initChatView();
    }

    /**
     * 初始化chatgpt聊天界面
     */
    private void initChatView(){
        messagesList = (MessagesList) findViewById(R.id.message_list);
        messageInput = (MessageInput) findViewById(R.id.input);

        setMessagesListAdapter();
        //像chatgpt发送问题
        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                sendMessage(input);
                return true;
            }
        });
    }

    /**
     * 初始化fragment
     */
    private void initFragment(){
        PdfShowFragment pdfShowFragment = new PdfShowFragment(file);
        addFragment(R.id.pdf_container, pdfShowFragment);

        NotebookListFragment bookListFragment = new NotebookListFragment(this, this, getSupportFragmentManager());
        addFragment(R.id.note_book_container, bookListFragment);
    }

    /**
     * 设置pdf_fragment和notebook_fragment
     * @param container fragment容器
     * @param fragment fragment对象
     */
    private void addFragment(int container, Fragment fragment){
        //fragment管理者
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(container, fragment);
        transaction.commit();
    }

    /**
     * 设置topBar样式
     */
    private void setTopBar(){
        topBar.setTitle("Paper Reading");
        QMUIAlphaImageButton backImageBtn = topBar.addLeftImageButton(R.drawable.baseline_arrow_back_ios_24, R.id.top_bar);
        backImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 显示gpt界面
     */
    private void showGPTView(){
        if(isShowGPT){
            gpt_layout.setVisibility(View.GONE);
            isShowGPT = false;
        }
        else {
            gpt_layout.setVisibility(View.VISIBLE);
            isShowGPT = true;
        }
    }

    /**
     * 初始化消息列表适配器
     */
    private void setMessagesListAdapter(){
        messageListAdapter = new MessagesListAdapter<>("0", null);
        messagesList.setAdapter(messageListAdapter);
    }

    /**
     * 像cahtgpt发送问题
     * @param input question
     */
    private void sendMessage(CharSequence input) {
        String question = input.toString();
        //发送信息到ChatGpt
        Message sendMessage = MessagesFixtures.getTextMessage(question, "0");
        messageListAdapter.addToStart(sendMessage, true);

        //向chatGPT发送请求

        messageListAdapter.addToStart(waitingMessage, true);

        RequestCenter.questionToGPT(question, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                android.os.Message message = android.os.Message.obtain();
                message.what = FAILURE;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());

                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String answer = jsonArray.getJSONObject(0).getString("text");
                        android.os.Message message = android.os.Message.obtain();
                        message.what = SUCCESSFUL;
                        message.obj = answer;
                        handler.sendMessage(message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    android.os.Message message = android.os.Message.obtain();
                    message.what = FAILURE;
                    message.obj = response.body();
                    handler.sendMessage(message);
                }
            }
        });


//        RequestCenter.questionToGPT(question, new DisposeDataListener() {
//            @Override
//            public void onSuccess(Object responseObj) {
//                //删除提示信息
//                messageListAdapter.delete(waitingMessage);
//                JSONObject  jsonObject = null;
//                try {
//                    jsonObject = new JSONObject((String) responseObj);
//                    JSONArray jsonArray = jsonObject.getJSONArray("choices");
//                    String answer = jsonArray.getJSONObject(0).getString("text");
//
//                    Message answerMessage = MessagesFixtures.getTextMessage(answer.trim(), "1");
//                    messageListAdapter.addToStart(answerMessage, true);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Object reasonObj) {
//                //删除提示信息
//                messageListAdapter.delete(waitingMessage);
//                Message failedMessage = MessagesFixtures.getTextMessage("Failed to load response due to "+reasonObj.toString(), "1");
//                messageListAdapter.addToStart(failedMessage, true);
//            }
//        });
    }
}