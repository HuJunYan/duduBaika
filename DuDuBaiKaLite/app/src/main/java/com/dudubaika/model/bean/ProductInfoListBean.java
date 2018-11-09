package com.dudubaika.model.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 热门产品
 */
public class ProductInfoListBean {


    /**
     * code : 0
     * msg : success
     * data : {"product_title":"热门推荐","product_list":[{"product_id":"产品方id","product_logo":"产品logo ","product_name":"产品名称 ","quota_name":"额度","quota_start_value":"500","quota_start_unit":"元","quota_end_value":"20","quota_end_unit":"万","rate_name":"利率","rate_value":"0.32 %","rate_unit":"月","term_name":"借款期限","term_start_value":"20","term_start_unit":"天","term_end_value":"36","term_end_unit":"月","loan_time_value":"1","loan_name":"下款时长","loan_time_unit":"天","apply_count":"1234","activity_name":"活动文字","product_tags":[{"tag_name":"放款快","is_light":"1"},{"tag_name":"周期长","is_light":"1"}]}]}
     */


        /**
         * product_title : 热门推荐
         * product_list : [{"product_id":"产品方id","product_logo":"产品logo ","product_name":"产品名称 ","quota_name":"额度","quota_start_value":"500","quota_start_unit":"元","quota_end_value":"20","quota_end_unit":"万","rate_name":"利率","rate_value":"0.32 %","rate_unit":"月","term_name":"借款期限","term_start_value":"20","term_start_unit":"天","term_end_value":"36","term_end_unit":"月","loan_time_value":"1","loan_name":"下款时长","loan_time_unit":"天","apply_count":"1234","activity_name":"活动文字","product_tags":[{"tag_name":"放款快","is_light":"1"},{"tag_name":"周期长","is_light":"1"}]}]
         */

        private String product_title;
        private ArrayList<ProductListBean> product_list;

        public String getProduct_title() {
            return product_title;
        }

        public void setProduct_title(String product_title) {
            this.product_title = product_title;
        }

        public ArrayList<ProductListBean> getProduct_list() {
            return product_list;
        }

        public void setProduct_list(ArrayList<ProductListBean> product_list) {
            this.product_list = product_list;
        }

        public static class ProductListBean {
            /**
             * product_id : 产品方id
             * product_logo : 产品logo
             * product_name : 产品名称
             * quota_name : 额度
             * quota_start_value : 500
             * quota_start_unit : 元
             * quota_end_value : 20
             * quota_end_unit : 万
             * rate_name : 利率
             * rate_value : 0.32 %
             * rate_unit : 月
             * term_name : 借款期限
             * term_start_value : 20
             * term_start_unit : 天
             * term_end_value : 36
             * term_end_unit : 月
             * loan_time_value : 1
             * loan_name : 下款时长
             * loan_time_unit : 天
             * apply_count : 1234
             * activity_name : 活动文字
             * product_tags : [{"tag_name":"放款快","is_light":"1"},{"tag_name":"周期长","is_light":"1"}]
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
            private String term_name;
            private String term_start_value;
            private String term_start_unit;
            private String term_end_value;
            private String term_end_unit;
            private String loan_time_value;
            private String loan_name;
            private String loan_time_unit;
            private String apply_count;
            private String activity_name;
            private List<ProductTagsBean> product_tags;

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

            public String getTerm_name() {
                return term_name;
            }

            public void setTerm_name(String term_name) {
                this.term_name = term_name;
            }

            public String getTerm_start_value() {
                return term_start_value;
            }

            public void setTerm_start_value(String term_start_value) {
                this.term_start_value = term_start_value;
            }

            public String getTerm_start_unit() {
                return term_start_unit;
            }

            public void setTerm_start_unit(String term_start_unit) {
                this.term_start_unit = term_start_unit;
            }

            public String getTerm_end_value() {
                return term_end_value;
            }

            public void setTerm_end_value(String term_end_value) {
                this.term_end_value = term_end_value;
            }

            public String getTerm_end_unit() {
                return term_end_unit;
            }

            public void setTerm_end_unit(String term_end_unit) {
                this.term_end_unit = term_end_unit;
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

            public String getApply_count() {
                return apply_count;
            }

            public void setApply_count(String apply_count) {
                this.apply_count = apply_count;
            }

            public String getActivity_name() {
                return activity_name;
            }

            public void setActivity_name(String activity_name) {
                this.activity_name = activity_name;
            }

            public List<ProductTagsBean> getProduct_tags() {
                return product_tags;
            }

            public void setProduct_tags(List<ProductTagsBean> product_tags) {
                this.product_tags = product_tags;
            }

            public static class ProductTagsBean {
                /**
                 * tag_name : 放款快
                 * is_light : 1
                 */

                private String tag_name;
                private String is_light;

                public String getTag_name() {
                    return tag_name;
                }

                public void setTag_name(String tag_name) {
                    this.tag_name = tag_name;
                }

                public String getIs_light() {
                    return is_light;
                }

                public void setIs_light(String is_light) {
                    this.is_light = is_light;
                }
            }
        }
    }

