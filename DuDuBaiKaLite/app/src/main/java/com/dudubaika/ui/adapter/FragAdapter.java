package com.dudubaika.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragAdapter extends FragmentPagerAdapter {

    private List<Fragment> list2;
    public FragAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.list2 =list;
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

    @Override
    public int getCount() {
        return list2.size();
    }




}
