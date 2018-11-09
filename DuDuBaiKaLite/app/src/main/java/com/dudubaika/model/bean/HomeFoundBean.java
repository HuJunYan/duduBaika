package com.dudubaika.model.bean;

import java.util.List;

/**
 * 发现首页bean
 */
public class HomeFoundBean {

        /**
         * discuss_list : [{"discuss_id":"1","discuss_logo":"http://logo","discuss_name":"1777777777","discuss_title":"论坛标题"}]
         * left_pic : pic
         * right_pic : pic
         * slogn_one : 无需认证无需审核，直接拿钱，真不是套路！
         * slogn_two : 你不来是你的错，来了又走是我的错
         */

        private String is_discuss;
        private String left_pic;
        private String right_pic;
        private List<DiscussListBean> discuss_list;
        private List<ActiveListBean> active_list;
        private List<TagsArrBean> tags_arr;

    public List<TagsArrBean> getTags_arr() {
        return tags_arr;
    }

    public void setTags_arr(List<TagsArrBean> tags_arr) {
        this.tags_arr = tags_arr;
    }

    public List<ActiveListBean> getActive_list() {
        return active_list;
    }

    public void setActive_list(List<ActiveListBean> active_list) {
        this.active_list = active_list;
    }

    public String getIs_discuss() {
        return is_discuss;
    }

    public void setIs_discuss(String is_discuss) {
        this.is_discuss = is_discuss;
    }

    public String getLeft_pic() {
            return left_pic;
        }


        public void setLeft_pic(String left_pic) {
            this.left_pic = left_pic;
        }

        public String getRight_pic() {
            return right_pic;
        }

        public void setRight_pic(String right_pic) {
            this.right_pic = right_pic;
        }


        public List<DiscussListBean> getDiscuss_list() {
            return discuss_list;
        }

        public void setDiscuss_list(List<DiscussListBean> discuss_list) {
            this.discuss_list = discuss_list;
        }

        public static class DiscussListBean {
            /**
             * discuss_id : 1
             * discuss_logo : http://logo
             * discuss_name : 1777777777
             * discuss_title : 论坛标题
             */

            private String discuss_type;
            private String discuss_logo;
            private String discuss_name;
            private String discuss_title;

            public String getDiscuss_type() {
                return discuss_type;
            }

            public void setDiscuss_id(String discuss_type) {
                this.discuss_type = discuss_type;
            }

            public String getDiscuss_logo() {
                return discuss_logo;
            }

            public void setDiscuss_logo(String discuss_logo) {
                this.discuss_logo = discuss_logo;
            }

            public String getDiscuss_name() {
                return discuss_name;
            }

            public void setDiscuss_name(String discuss_name) {
                this.discuss_name = discuss_name;
            }

            public String getDiscuss_title() {
                return discuss_title;
            }

            public void setDiscuss_title(String discuss_title) {
                this.discuss_title = discuss_title;
            }
        }


        public static class ActiveListBean{

            private String active_img;
            private String active_url;
            private String active_id;

            public String getActive_img() {
                return active_img;
            }

            public void setActive_img(String active_img) {
                this.active_img = active_img;
            }

            public String getActive_url() {
                return active_url;
            }

            public void setActive_url(String active_url) {
                this.active_url = active_url;
            }

            public String getActive_id() {
                return active_id;
            }

            public void setActive_id(String active_id) {
                this.active_id = active_id;
            }
        }

        public static class TagsArrBean{

           private String tag_id;
           private String tag_logo;
           private String tag_name;

            public String getTag_id() {
                return tag_id;
            }

            public void setTag_id(String tag_id) {
                this.tag_id = tag_id;
            }

            public String getTag_logo() {
                return tag_logo;
            }

            public void setTag_logo(String tag_logo) {
                this.tag_logo = tag_logo;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }
        }
}
