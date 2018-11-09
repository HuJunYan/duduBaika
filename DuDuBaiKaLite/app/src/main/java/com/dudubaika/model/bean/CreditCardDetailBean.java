package com.dudubaika.model.bean;

/**
 * 信用卡详情界面
 */
public class CreditCardDetailBean {


        /**
         * credit_logo : 银行卡图片链接
         * credit_name  : 银行卡名称
         * credit_id : 银行卡id
         * credit_des : 描述
         * credit_privilege_title : 信用卡特权
         * credit_privilege :
         * credit_application_title : 申请条件
         * credit_application :
         * credit_apply_url : 立即申请点击跳转h5
         */

        private String share_url;
        private String share_title;
        private String share_des;
        private String share_img;

        private String credit_logo;
        private String credit_name;
        private String credit_id;
        private String credit_des;
        private String credit_privilege_title;
        private String credit_privilege;
        private String credit_application_title;
        private String credit_application;
        private String credit_apply_url;
        private String credit_apply_title;

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

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public String getCredit_apply_title() {
        return credit_apply_title;
    }

    public void setCredit_apply_title(String credit_apply_title) {
        this.credit_apply_title = credit_apply_title;
    }

    public String getCredit_logo() {
            return credit_logo;
        }

        public void setCredit_logo(String credit_logo) {
            this.credit_logo = credit_logo;
        }

        public String getCredit_name() {
            return credit_name;
        }

        public void setCredit_name(String credit_name) {
            this.credit_name = credit_name;
        }

        public String getCredit_id() {
            return credit_id;
        }

        public void setCredit_id(String credit_id) {
            this.credit_id = credit_id;
        }

        public String getCredit_des() {
            return credit_des;
        }

        public void setCredit_des(String credit_des) {
            this.credit_des = credit_des;
        }

        public String getCredit_privilege_title() {
            return credit_privilege_title;
        }

        public void setCredit_privilege_title(String credit_privilege_title) {
            this.credit_privilege_title = credit_privilege_title;
        }

        public String getCredit_privilege() {
            return credit_privilege;
        }

        public void setCredit_privilege(String credit_privilege) {
            this.credit_privilege = credit_privilege;
        }

        public String getCredit_application_title() {
            return credit_application_title;
        }

        public void setCredit_application_title(String credit_application_title) {
            this.credit_application_title = credit_application_title;
        }

        public String getCredit_application() {
            return credit_application;
        }

        public void setCredit_application(String credit_application) {
            this.credit_application = credit_application;
        }

        public String getCredit_apply_url() {
            return credit_apply_url;
        }

        public void setCredit_apply_url(String credit_apply_url) {
            this.credit_apply_url = credit_apply_url;
        }
}
