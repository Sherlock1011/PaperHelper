package com.example.paperhelper.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.lib_common_util.JsonModuleUtil;
import com.example.paperhelper.R;
import com.example.paperhelper.model.Model;
import com.example.paperhelper.model.bean.NotebookItem;
import com.example.paperhelper.model.constant.FinalValue;
import com.example.paperhelper.view.activity.dialog.DialogManager;
import com.example.paperhelper.view.fragment.adapter.MyGridViewAdapter;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.io.File;
import java.io.IOException;

/**
 * @author Sherlock
 * 笔记列表界面
 */
public class NotebookListFragment extends Fragment {
    private Context context;
    private Activity activity;
    private FragmentManager fragmentManager;
    private View view;
    private GridView notebook_gv;
    private BaseAdapter gridViewAdapter;
    private QMUITopBar topBar;
    private final int CREATE_NEW_NOTEBOOK = 0;
    private final int OPEN_EXIST_NOTEBOOK = 1;

    public NotebookListFragment(Context context, Activity activity, FragmentManager fragmentManager) {
        this.context = context;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note_book_list, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view){
        initGridView(view);
        //初始化topBar
        topBar = (QMUITopBar) view.findViewById(R.id.note_list_topbar);
        topBar.setTitle("Notebooks");
        QMUIAlphaImageButton add_btn = topBar.addRightImageButton(R.drawable.baseline_add_24, R.id.note_list_topbar);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNotebook(view);
            }
        });
    }

    /**
     * 初始化gridView
     * @param view
     */
    private void initGridView(View view){
        //初始化gridView
        notebook_gv = (GridView) view.findViewById(R.id.notebook_gv);
        gridViewAdapter = new MyGridViewAdapter<NotebookItem>(Model.getInstance().getNotebookLibManager().getNotebookList(), R.layout.item_notebook) {

            @Override
            public void bindView(ViewHolder holder, NotebookItem item) {
                holder.setText(R.id.note_name, item.getFileName());
                holder.setText(R.id.note_time, item.getTime());
                holder.setImageResource(R.id.note_icon, R.drawable.notebook);
                holder.setOnClickListener(R.id.note_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupMenuShow(context, view, R.menu.notebook_edit_menu, item);
                    }
                });
            }
        };
        notebook_gv.setAdapter(gridViewAdapter);
        notebook_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openNotebook(Model.getInstance().getNotebookLibManager().getNotebookList().get(position));
            }
        });
    }

    /**
     * 添加图书
     */
    private void addNotebook(View view){
        popupMenuShow(context, view, R.menu.menu_add_notebook, null);
    }


    /**
     * 弹出popup菜单
     * @param view
     */
    private void popupMenuShow(Context context, View view, int menuId, NotebookItem item){
        //创建弹出式菜单对象
        PopupMenu menu = new PopupMenu(context, view);
        //获取菜单填充器
        MenuInflater inflater = menu.getMenuInflater();
        //填充菜单
        inflater.inflate(menuId, menu.getMenu());
        //绑定菜单项的点击事件
        setPopupMenuClick(menu,item);
        menu.show();
    }

    /**
     * 设置弹出菜单选项点击响应事件
     * @param menu
     * @param noteBookItem
     */
    private void setPopupMenuClick(PopupMenu menu, NotebookItem noteBookItem) {
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notebook_rename:
                        renameNotebook(noteBookItem);
                        break;

                    case R.id.notebook_delete:
                        deleteNotebook(noteBookItem);
                        break;
                    case R.id.add_notebook:
                        NotebookItem newNotebook = createNewNotebook();
                        openNotebook(newNotebook);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 重命名笔记
     * @param item
     */
    private void renameNotebook(NotebookItem item){
        DialogManager.getInstance().renameItemDialogShow(activity, "重命名", item.getFileName(), InputType.TYPE_CLASS_TEXT, item);
        gridViewAdapter.notifyDataSetChanged();
    }

    /**
     * 删除notebook
     * @param deleteItem 要删除的notebook item
     */
    private void deleteNotebook(NotebookItem deleteItem){
        DialogManager.getInstance().deleteNotebookItemDialogShow(activity, deleteItem,gridViewAdapter);

    }

    /**
     * 新建笔记本
     */
    private NotebookItem createNewNotebook(){
        NotebookItem notebook;
        try {
            String path = FinalValue.NOTEBOOK_SRC + "New notebook";
            File file = new File(path);
            file.createNewFile();
            notebook = new NotebookItem(file);
            return notebook;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void openNotebook(NotebookItem notebookItem){
        NotebookFragment notebookFragment = new NotebookFragment(context, activity);
        //将json对象传给下一级fragment
        Bundle bundle = new Bundle();
        String jsonString = JsonModuleUtil.parseModuleToJson(notebookItem);
        bundle.putString("notebook_json", jsonString);
        notebookFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.note_book_container, notebookFragment, null)
                .addToBackStack(null)
                .commit();
    }
}
