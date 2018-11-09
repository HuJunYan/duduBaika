package com.dudubaika.model.bean;

import java.util.List;

/**
 * 首页底部弹窗的对象
 */
public class HomeButtomDialogBean {

        /**
         * desc_is_show : 1
         * product_desc : 近期放款审批严格， 建议申请 3 家以上机构， 保证 100 % 下款
         * list_is_show : 1
         * product_list : [{"product_id":"产品方id","product_logo":"产品logo","product_name":" //产品名称","quota_name":"额度","quota_start_value":"500","quota_start_unit":"元","quota_end_value":"20","quota_end_unit":"万","rate_name":"利率","rate_value":"0.32","rate_unit":"月","loan_time_value":"1","loan_name":"下款时长","loan_time_unit":"天","product_tag":"为你速配"}]
         */

        private String desc_is_show;
        private String product_desc;
        private String list_is_show;
        private List<ProductListBean> product_list;

        public String getDesc_is_show() {
            return desc_is_show;
        }

        public void setDesc_is_show(String desc_is_show) {
            this.desc_is_show = desc_is_show;
        }

        public String getProduct_desc() {
            return product_desc;
        }

        public void setProduct_desc(String product_desc) {
            this.product_desc = product_desc;
        }

        public String getList_is_show() {
            return list_is_show;
        }

        public void setList_is_show(String list_is_show) {
            this.list_is_show = list_is_show;
        }

        public List<ProductListBean> getProduct_list() {
            return product_list;
        }

        public void setProduct_list(List<ProductListBean> product_list) {
            this.product_list = product_list;
        }

        public static class ProductListBean {
            /**
             * product_id : 产品方id
             * product_logo : 产品logo
             * product_name :  //产品名称
             * quota_name : 额度
             * quota_start_value : 500
             * quota_start_unit : 元
             * quota_end_value : 20
             * quota_end_unit : 万
             * rate_name : 利率
             * rate_value : 0.32
             * rate_unit : 月
             * loan_time_value : 1
             * loan_name : 下款时长
             * loan_time_unit : 天
             * product_tag : 为你速配
             */

            private String product_id;
            private String product_logo;
            private String product_name;
            private String quota_name;
            private String quota_start_value;
            private String quota_start_unit;
            private String quota_end_value;
            private String quota_end_unit;
            private String rate_name;
            private String rate_value;
            private String rate_unit;
            private String loan_time_value;
            private String loan_name;
            private String loan_time_unit;
            private String product_tag;

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

            public String getQuota_name() {
                return quota_name;
            }

            public void setQuota_name(String quota_name) {
                this.quota_name = quota_name;
            }

            public String getQuota_start_value() {
                return quota_start_value;
            }

            public void setQuota_start_value(String quota_start_value) {
                this.quota_start_value = quota_start_value;
            }

            public String getQuota_start_unit() {
                return quota_start_unit;
            }

            public void setQuota_start_unit(String quota_start_unit) {
                this.quota_start_unit = quota_start_unit;
            }

            public String getQuota_end_value() {
                return quota_end_value;
            }

            public void setQuota_end_value(String quota_end_value) {
                this.quota_end_value = quota_end_value;
            }

            public String getQuota_end_unit() {
                return quota_end_unit;
            }

            public void setQuota_end_unit(String quota_end_unit) {
                this.quota_end_unit = quota_end_unit;
            }

            public String getRate_name() {
                return rate_name;
            }

            public void setRate_name(String rate_name) {
                this.rate_name = rate_name;
            }

            public String getRate_value() {
                return rate_value;
            }

            public void setRate_value(String rate_value) {
                this.rate_value = rate_value;
            }

            public String getRate_unit() {
                return rate_unit;
            }

            public void setRate_unit(String rate_unit) {
                this.rate_unit = rate_unit;
            }

            public String getLoan_time_value() {
                return loan_time_value;
            }

            public void setLoan_time_value(String loan_time_value) {
                this.loan_time_value = loan_time_value;
            }

            public String getLoan_name() {
                return loan_name;
            }

            public void setLoan_name(String loan_name) {
                this.loan_name = loan_name;
            }

            public String getLoan_time_unit() {
                return loan_time_unit;
            }

            public void setLoan_time_unit(String loan_time_unit) {
                this.loan_time_unit = loan_time_unit;
            }

            public String getProduct_tag() {
                return product_tag;
            }

            public void setProduct_tag(String product_tag) {
                this.product_tag = product_tag;
            }
        }

}
