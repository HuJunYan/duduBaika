package com.dudubaika.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dudubaika.R;
import com.dudubaika.ui.activity.ImageLookActivity;
import com.dudubaika.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class NoScrollGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private String discuss_logo_url;

    public NoScrollGridAdapter(Context context, List<String> list,String discuss_logo_url ){
        this.mContext =context;
        this.mList = list;
        this.discuss_logo_url = discuss_logo_url;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String url = mList.get(position);
        View view = View.inflate(mContext, R.layout.item_single_imageview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_iv);
//        ImageUtil.INSTANCE.loadNoCache(mContext,imageView,discuss_logo_url+url,R.drawable.money_da);
        ImageUtil.INSTANCE.loadWithCache(mContext,imageView,discuss_logo_url+url,R.drawable.money_da);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ImageLookActivity.class);
                intent.putStringArrayListExtra(ImageLookActivity.Companion.getLIST(), (ArrayList<String>) mList);
                intent.putExtra(ImageLookActivity.Companion.getPOSOTION(),position);
                intent.putExtra(ImageLookActivity.Companion.getIMGURL(),discuss_logo_url);
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
