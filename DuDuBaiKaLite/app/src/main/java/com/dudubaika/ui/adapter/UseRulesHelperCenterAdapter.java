package com.dudubaika.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dudubaika.R;
import com.dudubaika.model.bean.HelpCenterBean;

import java.util.List;

/**
 * Created by wang on 2017/12/22.
 * 帮助中心数据适配器
 */

public class UseRulesHelperCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HelpCenterBean.HelpListBean> mData;
    private Activity mActivity;
    public static final int TYPE_TITLE = 0;//标题
    public static final int TYPE_CONTENT = 1;//内容
    public static final int TYPE_TOP_BG = 2;//顶部的背景
    private int currentSelectedPosition = -1;
    private float density;

    public UseRulesHelperCenterAdapter(Activity activity, List<HelpCenterBean.HelpListBean> data) {
        this.mActivity = activity;
        this.mData = data;
        density = mActivity.getResources().getDisplayMetrics().density;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new TitleHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_use_rules_title, parent, false));
        } else if (viewType == TYPE_CONTENT) {
            return new ContentHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_use_rules_content, parent, false));
        } else if (viewType == TYPE_TOP_BG) {
            return new TopBackgroundHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_use_rules_top_bg, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_TITLE) {
            ((TitleHolder) holder).onBindViewHolder(position);
        } else if (getItemViewType(position) == TYPE_CONTENT) {
            ((ContentHolder) holder).onBindViewHolder(position);
        } else if (getItemViewType(position) == TYPE_TOP_BG) {
            ((TopBackgroundHolder) holder).onBindViewHolder(position);
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getItem_type() == TYPE_CONTENT) {
            return TYPE_CONTENT;
        } else if (mData.get(position).getItem_type() == TYPE_TITLE) {
            return TYPE_TITLE;
        } else if (mData.get(position).getItem_type() == TYPE_TOP_BG) {
            return TYPE_TOP_BG;
        }
        return 0;
    }

    public class TitleHolder extends RecyclerView.ViewHolder {
        private TextView tv_rules_item_title;
        private ImageView iv_rules_item_icon;
        private View view_top_line;
        private View view_bottom_line;

        public TitleHolder(View itemView) {
            super(itemView);
            view_top_line = itemView.findViewById(R.id.view_top_line);
            view_bottom_line = itemView.findViewById(R.id.view_bottom_line);
            tv_rules_item_title = (TextView) itemView.findViewById(R.id.tv_rules_item_title);
            iv_rules_item_icon = (ImageView) itemView.findViewById(R.id.iv_rules_item_icon);
        }

        public void onBindViewHolder(final int position) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view_top_line.getLayoutParams();
            if (position == 0) {
                layoutParams.leftMargin = 0;
            } else {
                layoutParams.leftMargin = (int) (15 * density + 0.5f);
            }
            if (position == mData.size() - 1) {
                view_bottom_line.setVisibility(View.VISIBLE);
            } else {
                view_bottom_line.setVisibility(View.GONE);
            }
            view_top_line.setLayoutParams(layoutParams);
            final HelpCenterBean.HelpListBean itemDataBean = mData.get(position);
            boolean open = itemDataBean.isOpen();
            if (open && position == currentSelectedPosition) {// 如果当前位置  标记是展开 且 展开的是这个位置
                iv_rules_item_icon.setRotation(0);
            } else {
                iv_rules_item_icon.setRotation(180);

            }
            tv_rules_item_title.setText(itemDataBean.getHelp_title());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isHide = false;
                    if (position != currentSelectedPosition) { //如果当前位置 不是展开的位置
                        itemDataBean.setOpen(true);  // 标记展开这个位置
                        iv_rules_item_icon.animate().rotation(iv_rules_item_icon.getRotation() + 180).start();
                        if (currentSelectedPosition != -1 && currentSelectedPosition + 1 < mData.size()) {
                            mData.remove(currentSelectedPosition + 1);
                            notifyItemRemoved(currentSelectedPosition + 1);
//                                notifyItemRangeChanged(0, mData.size());
                            isHide = true;
                        }
                        int insertPosition = position;
                        if (isHide) {
                            if (currentSelectedPosition + 1 < position) {
                                insertPosition--;
                            }
                        }
                        if (insertPosition + 1 > mData.size()) {
                            insertPosition = mData.size() - 1;
                        }
                        HelpCenterBean.HelpListBean helperCenterItemDataBean = new HelpCenterBean.HelpListBean("", itemDataBean.getHelp_content(), TYPE_CONTENT, false);
                        mData.add(insertPosition + 1, helperCenterItemDataBean);
                        notifyItemInserted(insertPosition + 1);

                        notifyItemRangeChanged(0, mData.size());
                        currentSelectedPosition = insertPosition;
                    } else { // 如果是当前位置
                        iv_rules_item_icon.animate().rotation(iv_rules_item_icon.getRotation() + 180).start();
                        itemDataBean.setOpen(false); // 关闭这个位置
                        currentSelectedPosition = -1;
                        if (position + 1 < mData.size()) {
                            mData.remove(position + 1);
                            notifyItemRemoved(position + 1);
                            notifyItemRangeChanged(0, mData.size());
                        }
                    }
                }
            });

        }
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        private TextView tv_rules_item_content;
        private View view_item_bottom_line;

        public ContentHolder(View itemView) {
            super(itemView);
            tv_rules_item_content = (TextView) itemView.findViewById(R.id.tv_rules_item_content);
            view_item_bottom_line = itemView.findViewById(R.id.view_item_bottom_line);
        }

        public void onBindViewHolder(int position) {
            tv_rules_item_content.setText(mData.get(position).getHelp_content());
            if (position == mData.size() - 1) {
                view_item_bottom_line.setVisibility(View.VISIBLE);
            } else {
                view_item_bottom_line.setVisibility(View.GONE);
            }
        }
    }

    public class TopBackgroundHolder extends RecyclerView.ViewHolder {
        public TopBackgroundHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = (int) (12 * density + 0.5f);
            itemView.setLayoutParams(layoutParams);
//
//            itemView.setLayoutParams();
        }

        public void onBindViewHolder(int position) {

        }
    }

}
