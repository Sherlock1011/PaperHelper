package com.example.paperhelper.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lib_common_util.JsonModuleUtil;
import com.example.paperhelper.R;
import com.example.paperhelper.model.bean.NotebookItem;
import com.example.paperhelper.utils.FileHelper;
import com.widemouth.library.toolitem.WMToolAlignment;
import com.widemouth.library.toolitem.WMToolBackgroundColor;
import com.widemouth.library.toolitem.WMToolBold;
import com.widemouth.library.toolitem.WMToolImage;
import com.widemouth.library.toolitem.WMToolItalic;
import com.widemouth.library.toolitem.WMToolItem;
import com.widemouth.library.toolitem.WMToolListBullet;
import com.widemouth.library.toolitem.WMToolListClickToSwitch;
import com.widemouth.library.toolitem.WMToolListNumber;
import com.widemouth.library.toolitem.WMToolQuote;
import com.widemouth.library.toolitem.WMToolSplitLine;
import com.widemouth.library.toolitem.WMToolStrikethrough;
import com.widemouth.library.toolitem.WMToolTextColor;
import com.widemouth.library.toolitem.WMToolTextSize;
import com.widemouth.library.toolitem.WMToolUnderline;
import com.widemouth.library.wmview.WMEditText;
import com.widemouth.library.wmview.WMToolContainer;

/**
 * @author Sherlock
 * 笔记本页面
 */
public class NotebookFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private View view;
    private ImageView back;
    private TextView noteTitle;
    private ImageView noteMenu;

    private WMToolContainer toolContainer;
    private WMEditText noteEditText;
    private NotebookItem notebook;

    public NotebookFragment(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notebook, container, false);
        initView(view);
        addTools();

        //加载笔记内容
        loadNotebook();
        return view;
    }

    /**
     * 初始化啊控件
     * @param view
     */
    private void initView(View view){
        back = (ImageView) view.findViewById(R.id.note_back_btn);
        noteTitle = (TextView) view.findViewById(R.id.note_title_tv);
        noteMenu = (ImageView) view.findViewById(R.id.note_menu_btn);
        toolContainer = (WMToolContainer) view.findViewById(R.id.rich_text_tool_container);
        noteEditText = (WMEditText) view.findViewById(R.id.note_edit_text);

        back.setOnClickListener(this);
        noteMenu.setOnClickListener(this);
    }

    /**
     * 添加富文本编辑工具
     */
    private void addTools(){
        WMToolItem toolBold = new WMToolBold();
        toolContainer.addToolItem(toolBold);
        WMToolItem toolItalic = new WMToolItalic();
        toolContainer.addToolItem(toolItalic);
        final WMToolItem toolUnderline = new WMToolUnderline();
        toolContainer.addToolItem(toolUnderline);
        WMToolItem toolStrikethrough = new WMToolStrikethrough();
        toolContainer.addToolItem(toolStrikethrough);
        WMToolItem toolImage = new WMToolImage();
        toolContainer.addToolItem(toolImage);
        WMToolItem toolTextColor = new WMToolTextColor();
        toolContainer.addToolItem(toolTextColor);
        WMToolItem toolBackgroundColor = new WMToolBackgroundColor();
        toolContainer.addToolItem(toolBackgroundColor);
        WMToolItem toolTextSize = new WMToolTextSize();
        toolContainer.addToolItem(toolTextSize);
        WMToolItem toolListNumber = new WMToolListNumber();
        toolContainer.addToolItem(toolListNumber);
        WMToolItem toolListBullet = new WMToolListBullet();
        toolContainer.addToolItem(toolListBullet);
        WMToolItem toolAlignment = new WMToolAlignment();
        toolContainer.addToolItem(toolAlignment);
        WMToolItem toolQuote = new WMToolQuote();
        toolContainer.addToolItem(toolQuote);
        WMToolItem toolListClickToSwitch = new WMToolListClickToSwitch();
        toolContainer.addToolItem(toolListClickToSwitch);
        WMToolItem toolSplitLine = new WMToolSplitLine();
        toolContainer.addToolItem(toolSplitLine);

        noteEditText.setupWithToolContainer(toolContainer);
    }

    /**
     * 加载笔记内容
     */
    private void loadNotebook(){
        Bundle bundle = getArguments();
        String notebook_json = bundle.getString("notebook_json");
        notebook = (NotebookItem) JsonModuleUtil.parseJsonToModule(notebook_json, NotebookItem.class);
        noteTitle.setText(notebook.getFileName());
        Log.e("HTML", notebook.getContent());
//        noteEditText.fromHtml(notebook.getContent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.note_back_btn:
                //返回到上一级fragment
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.note_menu_btn:
                popUpMenuShow(context, noteMenu, R.menu.menu_notebook);
                break;

            default:

                break;
        }
    }

    /**
     *
     * @param context 上层Activity的上下文
     * @param view
     * @param menuId
     */
    private void popUpMenuShow(Context context, View view, int menuId){
        //创建弹出式菜单对象
        PopupMenu menu = new PopupMenu(context, view);
        //获取菜单填充器
        MenuInflater inflater = menu.getMenuInflater();
        //填充菜单
        inflater.inflate(menuId, menu.getMenu());
        //绑定菜单项的点击事件
        setPopupMenuClick(menu);
        menu.show();
    }

    /**
     * 设置弹出菜单的点击事件
     * @param menu
     */
    private void setPopupMenuClick(PopupMenu menu){
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.save_notebook:
                        saveNote(notebook);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 保存文件
     */
    private void saveNote(NotebookItem notebook){
        notebook.setContent(noteEditText.getHtml());
        String json = JsonModuleUtil.parseModuleToJson(notebook);
        FileHelper.getInstance().writeFile(notebook.getPath(), json, false);
    }
}
