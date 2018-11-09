package com.dudubaika.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 产品详情bean
 */
public class ProductInfoBean {


        /**
         * product_name : 商品名称
         * product_logo_url : 商品logo地址
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
         * loan_time_value : 5
         * loan_name : 最快放款
         * loan_time_unit : 分钟
         * apply_count : 10000
         * product_apply_limit_name : 申请条件
         * product_apply_limit_list : ["申请条件1","申请条件2"]
         * product_credit_name : 认证资料
         * product_credit_list : [{"logo_url:":"logo地址","redit_name":"认证名称"},{"logo_url":"logo地址","credit_name":"认证名称"}]
         * repay_type_name : 还款方式
         * repay_type_des : 自动扣款或到平台主动还款
         * apply_button_name : 立即申请
         */

        //是否是本公司产品
        private String share_img;
        private String share_url;
        private String share_title;
        private String share_des;
        private String is_personal;
        private String product_name;
        private String product_logo_url;
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
        private String product_apply_limit_name;
        private String product_apply_limit_str;
        private String product_credit_name;
        private String repay_type_name;
        private String repay_type_des;
        private String apply_button_name;
        private List<ProductCreditListBean> product_credit_list;

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_des() {
        return share_des;
    }

    public void setShare_des(String share_des) {
        this.share_des = share_des;
    }

    public String getIs_personal() {
        return is_personal;
    }

    public void setIs_personal(String is_personal) {
        this.is_personal = is_personal;
    }

    public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_logo_url() {
            return product_logo_url;
        }

    public String getProduct_apply_limit_str() {
        return product_apply_limit_str;
    }

    public void setProduct_apply_limit_str(String product_apply_limit_str) {
        this.product_apply_limit_str = product_apply_limit_str;
    }

    public void setProduct_logo_url(String product_logo_url) {
            this.product_logo_url = product_logo_url;
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

        public String getProduct_apply_limit_name() {
            return product_apply_limit_name;
        }

        public void setProduct_apply_limit_name(String product_apply_limit_name) {
            this.product_apply_limit_name = product_apply_limit_name;
        }

        public String getProduct_credit_name() {
            return product_credit_name;
        }

        public void setProduct_credit_name(String product_credit_name) {
            this.product_credit_name = product_credit_name;
        }

        public String getRepay_type_name() {
            return repay_type_name;
        }

        public void setRepay_type_name(String repay_type_name) {
            this.repay_type_name = repay_type_name;
        }

        public String getRepay_type_des() {
            return repay_type_des;
        }

        public void setRepay_type_des(String repay_type_des) {
            this.repay_type_des = repay_type_des;
        }

        public String getApply_button_name() {
            return apply_button_name;
        }

        public void setApply_button_name(String apply_button_name) {
            this.apply_button_name = apply_button_name;
        }



        public List<ProductCreditListBean> getProduct_credit_list() {
            return product_credit_list;
        }

        public void setProduct_credit_list(List<ProductCreditListBean> product_credit_list) {
            this.product_credit_list = product_credit_list;
        }

        public static class ProductCreditListBean {
            @SerializedName("logo_url:")
            private String _$Logo_url246; // FIXME check this code
            private String redit_name;
            private String logo_url;
            private String credit_name;

            public String get_$Logo_url246() {
                return _$Logo_url246;
            }

            public void set_$Logo_url246(String _$Logo_url246) {
                this._$Logo_url246 = _$Logo_url246;
            }

            public String getRedit_name() {
                return redit_name;
            }

            public void setRedit_name(String redit_name) {
                this.redit_name = redit_name;
            }

            public String getLogo_url() {
                return logo_url;
            }

            public void setLogo_url(String logo_url) {
                this.logo_url = logo_url;
            }

            public String getCredit_name() {
                return credit_name;
            }

            public void setCredit_name(String credit_name) {
                this.credit_name = credit_name;
            }
        }
}
