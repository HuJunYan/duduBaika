package com.dudubaika.model.bean;

import java.util.List;

public class HelpCenterBean {

    private List<HelpListBean> help_list;

    public List<HelpListBean> getHelp_list() {
        return help_list;
    }

    public void setHelp_list(List<HelpListBean> help_list) {
        this.help_list = help_list;
    }

    public HelpCenterBean(List<HelpListBean> help_list) {
        this.help_list = help_list;
    }

    public static class HelpListBean {
        /**
         * help_title :
         * help_content :
         */

        private String help_title;
        private String help_content;
        private boolean mOpen;
        private int item_type;

        public HelpListBean() {

        }


        public HelpListBean(String help_title, String help_content) {
            this.help_title = help_title;
            this.help_content = help_content;
        }

        public HelpListBean(String s, String help_content, int typeContent, boolean b) {
            this.help_content = s;
            this.help_content = help_content;
            this.item_type = typeContent;
            this.mOpen = b;
        }

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }

        public String getHelp_title() {
            return help_title;
        }

        public void setHelp_title(String help_title) {
            this.help_title = help_title;
        }

        public String getHelp_content() {
            return help_content;
        }

        public void setHelp_content(String help_content) {
            this.help_content = help_content;
        }

        public void setOpen(boolean open) {
            mOpen = open;
        }

        public boolean isOpen() {
            return mOpen;
        }
    }
}
