package com.dudubaika.model.bean;

import java.util.List;

public class MineLoanBean {


        /**
         * total_money : 总额
         * total_count : 1
         * paynote_list : [{"product_id":"产品方id","product_logo":" //产品logo","product_name":" //产品名称","quota_value":"5000","quota_title":"待还款金额","lastdate_title":"距离还款日","lastdate_value":"20天","jump_url":"如果url为空， 跳转到详情， 不为空跳h5","note_status":"1:待还，2:已还，3:逾期"}]
         */

        private String total_money;
        private String total_count;
        private List<PaynoteListBean> paynote_list;

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
            this.total_money = total_money;
        }

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public List<PaynoteListBean> getPaynote_list() {
            return paynote_list;
        }

        public void setPaynote_list(List<PaynoteListBean> paynote_list) {
            this.paynote_list = paynote_list;
        }

        public static class PaynoteListBean {
            /**
             * product_id : 产品方id
             * product_logo :  //产品logo
             * product_name :  //产品名称
             * quota_value : 5000
             * quota_title : 待还款金额
             * lastdate_title : 距离还款日
             * lastdate_value : 20天
             * jump_url : 如果url为空， 跳转到详情， 不为空跳h5
             * note_status : 1:待还，2:已还，3:逾期
             */

            private String product_id;
            private String product_logo;
            private String product_name;
            private String quota_value;
            private String quota_title;
            private String lastdate_title;
            private String lastdate_value;
            private String jump_url;
            private String note_status;

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getProduct_logo() {
                return product_logo;
            }

            public void setProduct_logo(String product_logo) {
                this.product_logo = product_logo;
            }

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getQuota_value() {
                return quota_value;
            }

            public void setQuota_value(String quota_value) {
                this.quota_value = quota_value;
            }

            public String getQuota_title() {
                return quota_title;
            }

            public void setQuota_title(String quota_title) {
                this.quota_title = quota_title;
            }

            public String getLastdate_title() {
                return lastdate_title;
            }

            public void setLastdate_title(String lastdate_title) {
                this.lastdate_title = lastdate_title;
            }

            public String getLastdate_value() {
                return lastdate_value;
            }

            public void setLastdate_value(String lastdate_value) {
                this.lastdate_value = lastdate_value;
            }

            public String getJump_url() {
                return jump_url;
            }

            public void setJump_url(String jump_url) {
                this.jump_url = jump_url;
            }

            public String getNote_status() {
                return note_status;
            }

            public void setNote_status(String note_status) {
                this.note_status = note_status;
            }
    }
}
