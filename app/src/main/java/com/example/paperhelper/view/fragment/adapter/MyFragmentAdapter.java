package com.example.paperhelper.view.fragment.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Sherlock
 * Fragment适配器
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public MyFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.fragmentList == null ? null : this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList == null ? 0 : fragmentList.size();
    }
}
