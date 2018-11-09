package com.dudubaika.model.bean;

import java.util.List;

/**
 * 信用卡和借款list bean
 */
public class CardMoneyListBean {


        /**
         * discuss_list : [{"discuss_id":"1","discuss_logo_list":["http://logo","http://logo","http://logo"],"discuss_name":"1777777777","discuss_title":"论坛标题"}]
         * is_publish : 1/2
         */

        private String is_publish;
        private List<DiscussListBean> discuss_list;

        public String getIs_publish() {
            return is_publish;
        }

        public void setIs_publish(String is_publish) {
            this.is_publish = is_publish;
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
             * discuss_logo_list : ["http://logo","http://logo","http://logo"]
             * discuss_name : 1777777777
             * discuss_title : 论坛标题
             */

            private String discuss_logo_url; // 用于拼接的URL字段
            private String discuss_id;
            private String discuss_name;
            private String discuss_title;
            private String discuss_logo;
            private List<String> discuss_logo_list;

            public String getDiscuss_logo_url() {
                return discuss_logo_url;
            }

            public void setDiscuss_logo_url(String discuss_logo_url) {
                this.discuss_logo_url = discuss_logo_url;
            }

            public String getDiscuss_logo() {
                return discuss_logo;
            }

            public void setDiscuss_logo(String discuss_logo) {
                this.discuss_logo = discuss_logo;
            }

            public String getDiscuss_id() {
                return discuss_id;
            }

            public void setDiscuss_id(String discuss_id) {
                this.discuss_id = discuss_id;
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

            public List<String> getDiscuss_logo_list() {
                return discuss_logo_list;
            }

            public void setDiscuss_logo_list(List<String> discuss_logo_list) {
                this.discuss_logo_list = discuss_logo_list;
            }
        }
}
