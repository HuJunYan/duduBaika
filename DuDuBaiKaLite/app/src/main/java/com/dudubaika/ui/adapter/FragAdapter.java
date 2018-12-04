package com.dudubaika.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragAdapter extends FragmentPagerAdapter {

    private List<Fragment> list2;
    private List<String>mTitleList;
    public FragAdapter(FragmentManager fm,List<Fragment> list,List<String>titleList) {
        super(fm);
        this.list2 =list;
        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {


        Fragment page = null;
        if (list2.size() > position) {
            page = list2.get(position);
            if (page != null) {
                return page;
            }
        }

        while (position>=list2.size()) {
            list2.add(null);
        }
        page = list2.get(position);
        list2.set(position, page);
        return page;


//        return list2.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public int getCount() {
        return list2.size();
    }




}
