package com.dudubaika.model.bean;

import java.util.List;

/**
 * 消息中心的list bean
 */
public class MsgCenterListBean {

        /**
         * total_num : 10
         * is_new_msg : 1
         * msg_list : [{"title":"标题","des":"描述描述描述描述描述","date_str":"2017-08-01 13:12","msg_url":"消息的链接","msg_type":"1:链接链接 2:文章","msg_id":"id","is_read":"1","table_identifier":"0"}]
         */

        private String total_num;
        private String is_new_msg;
        private List<MsgListBean> msg_list;

        public String getTotal_num() {
            return total_num;
        }

        public void setTotal_num(String total_num) {
            this.total_num = total_num;
        }

        public String getIs_new_msg() {
            return is_new_msg;
        }

        public void setIs_new_msg(String is_new_msg) {
            this.is_new_msg = is_new_msg;
        }

        public List<MsgListBean> getMsg_list() {
            return msg_list;
        }

        public void setMsg_list(List<MsgListBean> msg_list) {
            this.msg_list = msg_list;
        }

        public static class MsgListBean {
            /**
             * title : 标题
             * des : 描述描述描述描述描述
             * date_str : 2017-08-01 13:12
             * msg_url : 消息的链接
             * msg_type : 1:链接链接 2:文章
             * msg_id : id
             * is_read : 1
             * table_identifier : 0
             */

            private String title;
            private String des;
            private String date_str;
            private String msg_url;
            private String msg_type;
            private String msg_id;
            private String is_read;
            private String table_identifier;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getDate_str() {
                return date_str;
            }

            public void setDate_str(String date_str) {
                this.date_str = date_str;
            }

            public String getMsg_url() {
                return msg_url;
            }

            public void setMsg_url(String msg_url) {
                this.msg_url = msg_url;
            }

            public String getMsg_type() {
                return msg_type;
            }

            public void setMsg_type(String msg_type) {
                this.msg_type = msg_type;
            }

            public String getMsg_id() {
                return msg_id;
            }

            public void setMsg_id(String msg_id) {
                this.msg_id = msg_id;
            }

            public String getIs_read() {
                return is_read;
            }

            public void setIs_read(String is_read) {
                this.is_read = is_read;
            }

            public String getTable_identifier() {
                return table_identifier;
            }

            public void setTable_identifier(String table_identifier) {
                this.table_identifier = table_identifier;
            }
        }
    }
