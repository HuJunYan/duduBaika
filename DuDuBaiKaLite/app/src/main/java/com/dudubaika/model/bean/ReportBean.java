package com.dudubaika.model.bean;

import java.util.List;

/**
 * 举报信息的bean
 */
public class ReportBean {

        /**
         * mobile : 1777777777
         * report_list : [{"report_id":"1","report_name":"垃圾营销"},{"report_id":"1","report_name":"有害信息"}]
         */

        private String mobile;
        private List<ReportListBean> report_list;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public List<ReportListBean> getReport_list() {
            return report_list;
        }

        public void setReport_list(List<ReportListBean> report_list) {
            this.report_list = report_list;
        }

        public static class ReportListBean {
            /**
             * report_id : 1
             * report_name : 垃圾营销
             */

            private String report_id;
            private String report_name;

            public String getReport_id() {
                return report_id;
            }

            public void setReport_id(String report_id) {
                this.report_id = report_id;
            }

            public String getReport_name() {
                return report_name;
            }

            public void setReport_name(String report_name) {
                this.report_name = report_name;
            }
        }

}
