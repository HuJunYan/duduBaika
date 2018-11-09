package com.dudubaika.model.bean;

/**
 * 首页弹窗对象 （机构弹窗+消息弹窗）
 */
public class HomeDialogForUser {


    /**
     * code : 0
     * msg : success
     * data : {"is_product_dialog":"1/2","is_message_dialog":"1/2","message_dialog":{"message_id":"111","message_title":"嘟嘟白卡公众号开放","message_time":"2018.12.12 12:20:12","message_desc":"消息内容","message_url":"http://...链接"},"product_dialog":{"product_id":"111","product_logo":"http://图片","product_name":"机构名称","product_desc":"描述？？？"}}
     */

        /**
         * is_product_dialog : 1/2
         * is_message_dialog : 1/2
         * message_dialog : {"message_id":"111","message_title":"嘟嘟白卡公众号开放","message_time":"2018.12.12 12:20:12","message_desc":"消息内容","message_url":"http://...链接"}
         * product_dialog : {"product_id":"111","product_logo":"http://图片","product_name":"机构名称","product_desc":"描述？？？"}
         */

        private String is_product_dialog;
        private String is_message_dialog;
        private MessageDialogBean message_dialog;
        private ProductDialogBean product_dialog;

        public String getIs_product_dialog() {
            return is_product_dialog;
        }

        public void setIs_product_dialog(String is_product_dialog) {
            this.is_product_dialog = is_product_dialog;
        }

        public String getIs_message_dialog() {
            return is_message_dialog;
        }

        public void setIs_message_dialog(String is_message_dialog) {
            this.is_message_dialog = is_message_dialog;
        }

        public MessageDialogBean getMessage_dialog() {
            return message_dialog;
        }

        public void setMessage_dialog(MessageDialogBean message_dialog) {
            this.message_dialog = message_dialog;
        }

        public ProductDialogBean getProduct_dialog() {
            return product_dialog;
        }

        public void setProduct_dialog(ProductDialogBean product_dialog) {
            this.product_dialog = product_dialog;
        }

        public static class MessageDialogBean {
            /**
             * message_id : 111
             * message_title : 嘟嘟白卡公众号开放
             * message_time : 2018.12.12 12:20:12
             * message_desc : 消息内容
             * message_url : http://...链接
             */

            private String message_type;
            private String message_id;
            private String message_title;
            private String message_time;
            private String message_desc;
            private String message_url;

            public String getMessage_type() {
                return message_type;
            }

            public void setMessage_type(String message_type) {
                this.message_type = message_type;
            }

            public String getMessage_id() {
                return message_id;
            }

            public void setMessage_id(String message_id) {
                this.message_id = message_id;
            }

            public String getMessage_title() {
                return message_title;
            }

            public void setMessage_title(String message_title) {
                this.message_title = message_title;
            }

            public String getMessage_time() {
                return message_time;
            }

            public void setMessage_time(String message_time) {
                this.message_time = message_time;
            }

            public String getMessage_desc() {
                return message_desc;
            }

            public void setMessage_desc(String message_desc) {
                this.message_desc = message_desc;
            }

            public String getMessage_url() {
                return message_url;
            }

            public void setMessage_url(String message_url) {
                this.message_url = message_url;
            }
        }

        public static class ProductDialogBean {
            /**
             * product_id : 111
             * product_logo : http://图片
             * product_name : 机构名称
             * product_desc : 描述？？？
             */

            private String product_id;
            private String product_logo;
            private String product_name;
            private String product_desc;

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

            public String getProduct_desc() {
                return product_desc;
            }

            public void setProduct_desc(String product_desc) {
                this.product_desc = product_desc;
            }
        }

}
