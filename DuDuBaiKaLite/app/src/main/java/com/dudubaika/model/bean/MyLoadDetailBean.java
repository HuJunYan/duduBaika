package com.dudubaika.model.bean;

import java.util.List;

public class MyLoadDetailBean {

        /**
         * total_loan_title : 待还款总额（ 元）
         * total_loan_value :  还款总额5000
         * left_title :  贷款记录
         * left_value : 待还n笔
         * right_title :  增加账单记录
         * right_value : 记账不逾期
         * payback_list : [{"title":" 今日待还","loan_value":"1000","date_flag":"1 //1：今日, 2: 7日, 3：30日"}]
         * paynote_title :  待还账目清单
         * paynote_list : [{"product_id":"产品方id","product_logo":"产品logo","product_name":"//产品名称","quota_value":"5000","quota_title":"待还款金额","lastdate_title":"距离还款日","lastdate_value":"20天","jump_url":"如果url为空， 跳转到详情， 不为空跳h5"}]
         */

        private String total_loan_title;
        private String total_loan_value;
        private String left_title;
        private String left_value;
        private String right_title;
        private String right_value;
        private String paynote_title;
        private List<PaybackListBean> payback_list;
        private List<PaynoteListBean> paynote_list;

        public String getTotal_loan_title() {
            return total_loan_title;
        }

        public void setTotal_loan_title(String total_loan_title) {
            this.total_loan_title = total_loan_title;
        }

        public String getTotal_loan_value() {
            return total_loan_value;
        }

        public void setTotal_loan_value(String total_loan_value) {
            this.total_loan_value = total_loan_value;
        }

        public String getLeft_title() {
            return left_title;
        }

        public void setLeft_title(String left_title) {
            this.left_title = left_title;
        }

        public String getLeft_value() {
            return left_value;
        }

        public void setLeft_value(String left_value) {
            this.left_value = left_value;
        }

        public String getRight_title() {
            return right_title;
        }

        public void setRight_title(String right_title) {
            this.right_title = right_title;
        }

        public String getRight_value() {
            return right_value;
        }

        public void setRight_value(String right_value) {
            this.right_value = right_value;
        }

        public String getPaynote_title() {
            return paynote_title;
        }

        public void setPaynote_title(String paynote_title) {
            this.paynote_title = paynote_title;
        }

        public List<PaybackListBean> getPayback_list() {
            return payback_list;
        }

        public void setPayback_list(List<PaybackListBean> payback_list) {
            this.payback_list = payback_list;
        }

        public List<PaynoteListBean> getPaynote_list() {
            return paynote_list;
        }

        public void setPaynote_list(List<PaynoteListBean> paynote_list) {
            this.paynote_list = paynote_list;
        }

        public static class PaybackListBean {
            /**
             * title :  今日待还
             * loan_value : 1000
             * date_flag : 1 //1：今日, 2: 7日, 3：30日
             */

            private String title;
            private String loan_value;
            private String date_flag;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLoan_value() {
                return loan_value;
            }

            public void setLoan_value(String loan_value) {
                this.loan_value = loan_value;
            }

            public String getDate_flag() {
                return date_flag;
            }

            public void setDate_flag(String date_flag) {
                this.date_flag = date_flag;
            }
        }

        public static class PaynoteListBean {
            /**
             * product_id : 产品方id
             * product_logo : 产品logo
             * product_name : //产品名称
             * quota_value : 5000
             * quota_title : 待还款金额
             * lastdate_title : 距离还款日
             * lastdate_value : 20天
             * jump_url : 如果url为空， 跳转到详情， 不为空跳h5
             *
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

            public String getNote_status() {
                return note_status;
            }

            public void setNote_status(String note_status) {
                this.note_status = note_status;
            }

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
        }
}
