package com.dudubaika.model.bean;

import java.util.List;

/**
 * 快讯消息列表
 */
public class MsgListBean {

        /**
         * mobile : 1777777777
         * report_list : [{"report_id":"1","report_name":"垃圾营销"},{"report_id":"1","report_name":"有害信息"}]
         */

        private List<String> msg_list;



        public List<String> getMsg_list() {
            return msg_list;
        }

        public void setMsg_list(List<String> report_list) {
            this.msg_list = report_list;
        }


}
