package com.dudubaika.model.bean;

import java.util.List;

/**
 * 信用卡列表数据
 */
public class CreditCardListBean {


    /**
     * code : 0
     * msg : success
     * data : {"credit_list":[{"credit_logo":"银行卡图片链","credit_name":"银行卡名称 ","credit_id":"银行卡id","credit_des":"描述","credit_apply":"立即办卡"}]}
     */


        private List<CreditListBean> credit_list;

        public List<CreditListBean> getCredit_list() {
            return credit_list;
        }

        public void setCredit_list(List<CreditListBean> credit_list) {
            this.credit_list = credit_list;
        }

        public static class CreditListBean {
            /**
             * credit_logo : 银行卡图片链
             * credit_name : 银行卡名称
             * credit_id : 银行卡id
             * credit_des : 描述
             * credit_apply : 立即办卡
             */

            private String credit_logo;
            private String credit_name;
            private String credit_id;
            private String credit_des;
            private String credit_apply;

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

            public String getCredit_apply() {
                return credit_apply;
            }

            public void setCredit_apply(String credit_apply) {
                this.credit_apply = credit_apply;
            }
        }

     }
