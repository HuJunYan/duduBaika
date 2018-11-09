package com.dudubaika.model.bean;

import java.util.List;

/**
 * 帖子详情bean
 */
public class TalkDetailBean {


        /**
         * discuss_id : 1
         * discuss_content : asdfsadfasdfasdf
         * discuss_title : 论坛标题
         * discuss_push_time : 2018-07-31
         * discuss_logo_list : ["http://url","http://url","http://url"]
         * comment_count : 29
         * comment_list : [{"cus_logo":"logo","cus_name":"177777777","comment_time":"2018-07-31 08:08:04","comment_content":"my name is 。。。。"},{"cus_logo":"logo","cus_name":"177777777","comment_time":"2018-07-31 08:08:04","comment_content":"my name is ddddddd"}]
         */

        private String discuss_logo_url;
        private String dis_comment_status;
        private String discuss_id;
        private String discuss_name;
        private String discuss_logo;
        private String discuss_content;
        private String discuss_title;
        private String discuss_push_time;
        private String comment_count;
        private List<String> discuss_logo_list;
        private List<CommentListBean> comment_list;

    public String getDiscuss_name() {
        return discuss_name;
    }

    public void setDiscuss_name(String discuss_name) {
        this.discuss_name = discuss_name;
    }

    public String getDis_comment_status() {
        return dis_comment_status;
    }

    public void setDis_comment_status(String dis_comment_status) {
        this.dis_comment_status = dis_comment_status;
    }

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

        public String getDiscuss_content() {
            return discuss_content;
        }

        public void setDiscuss_content(String discuss_content) {
            this.discuss_content = discuss_content;
        }

        public String getDiscuss_title() {
            return discuss_title;
        }

        public void setDiscuss_title(String discuss_title) {
            this.discuss_title = discuss_title;
        }

        public String getDiscuss_push_time() {
            return discuss_push_time;
        }

        public void setDiscuss_push_time(String discuss_push_time) {
            this.discuss_push_time = discuss_push_time;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public List<String> getDiscuss_logo_list() {
            return discuss_logo_list;
        }

        public void setDiscuss_logo_list(List<String> discuss_logo_list) {
            this.discuss_logo_list = discuss_logo_list;
        }

        public List<CommentListBean> getComment_list() {
            return comment_list;
        }

        public void setComment_list(List<CommentListBean> comment_list) {
            this.comment_list = comment_list;
        }

        public static class CommentListBean {
            /**
             * cus_logo : logo
             * cus_name : 177777777
             * comment_time : 2018-07-31 08:08:04
             * comment_content : my name is 。。。。
             */

            private String cus_logo;
            private String cus_name;
            private String comment_time;
            private String comment_content;

            public String getCus_logo() {
                return cus_logo;
            }

            public void setCus_logo(String cus_logo) {
                this.cus_logo = cus_logo;
            }

            public String getCus_name() {
                return cus_name;
            }

            public void setCus_name(String cus_name) {
                this.cus_name = cus_name;
            }

            public String getComment_time() {
                return comment_time;
            }

            public void setComment_time(String comment_time) {
                this.comment_time = comment_time;
            }

            public String getComment_content() {
                return comment_content;
            }

            public void setComment_content(String comment_content) {
                this.comment_content = comment_content;
            }
        }

}
