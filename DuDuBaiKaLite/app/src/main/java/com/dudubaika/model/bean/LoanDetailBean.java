package com.dudubaika.model.bean;

/**
 * 我的账单详情
 */
public class LoanDetailBean {


    /**
     * code : 0
     * msg : success
     * data : {"product_name":" 产品名称","loan_money":"4000","loan_date":" 2018 年6月4日","loan_term":"20","repay_date":"2018 年6月4日"}
     */

            /**
         * product_name :  产品名称
         * loan_money : 4000
         * loan_date :  2018 年6月4日
         * loan_term : 20
         * repay_date : 2018 年6月4日
         */

        private String product_name;
        private String loan_money;
        private String loan_date;
        private String loan_term;
        private String repay_date;

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getLoan_money() {
            return loan_money;
        }

        public void setLoan_money(String loan_money) {
            this.loan_money = loan_money;
        }

        public String getLoan_date() {
            return loan_date;
        }

        public void setLoan_date(String loan_date) {
            this.loan_date = loan_date;
        }

        public String getLoan_term() {
            return loan_term;
        }

        public void setLoan_term(String loan_term) {
            this.loan_term = loan_term;
        }

        public String getRepay_date() {
            return repay_date;
        }

        public void setRepay_date(String repay_date) {
            this.repay_date = repay_date;
        }

}
