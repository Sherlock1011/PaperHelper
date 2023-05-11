package com.example.paperhelper.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.lib_common_ui.StatusBarManager;
import com.example.paperhelper.R;
import com.example.paperhelper.view.fragment.adapter.MyFragmentAdapter;
import com.example.paperhelper.view.fragment.PaperLibFragment;
import com.example.paperhelper.view.fragment.PersonalFragment;
import com.example.paperhelper.view.fragment.ScheduleFragment;
import com.example.paperhelper.view.fragment.SearchPaperFragment;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Sherlock
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fragment_vp)
    QMUIViewPager viewPager;
    @BindView(R.id.bottom_tool)
    RadioGroup bottom_tool;
    @BindView(R.id.search_paper_rb)
    RadioButton searchPaperRb;


    private List<Fragment> fragmentList;
    private FragmentPagerAdapter fpAdapter;

    private SearchPaperFragment searchPaperFragment;
    private PaperLibFragment paperLibFragment;
    private ScheduleFragment scheduleFragment;
    private PersonalFragment personalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarManager.setStatusBarFull(this,getWindow(),getResources());
        ButterKnife.bind(this);

        initView();

        //初始化fragment
        initFragment();

        //初始化fragmentAdapter
        initFragmentPagerAdapter();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        //设置控件字体
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/text.ttf");
        for(int i = 0; i < bottom_tool.getChildCount();i++){
            RadioButton radioButton = (RadioButton)bottom_tool.getChildAt(i);
            radioButton.setTypeface(typeface);
        }
    }

    /**
     * initial fragment
     */
    private void initFragment(){
        fragmentList = new ArrayList<>();

        //addition fragment
        searchPaperFragment = new SearchPaperFragment(this,this);
        paperLibFragment = new PaperLibFragment(this, this);
        scheduleFragment = new ScheduleFragment(this);
        personalFragment = new PersonalFragment();

        fragmentList.add(searchPaperFragment);
        fragmentList.add(paperLibFragment);
        fragmentList.add(scheduleFragment);
        fragmentList.add(personalFragment);
    }

    /**
     * initial fragmentAdapter
     */
    private void initFragmentPagerAdapter(){
        fpAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fpAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        bottom_tool.setOnCheckedChangeListener(checkedChangeListener);
    }

    /**
     * set ViewPager Listener
     */
    private QMUIViewPager.OnPageChangeListener pageChangeListener = new QMUIViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton)bottom_tool.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * set RadioGroup Listener
     */
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            for(int i = 0; i < radioGroup.getChildCount();i++){
                if(radioGroup.getChildAt(i).getId() == checkId){
                    viewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };
}