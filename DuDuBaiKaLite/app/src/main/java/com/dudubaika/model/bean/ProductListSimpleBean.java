package com.dudubaika.model.bean;

import java.util.List;

/**
 * 下款口子bean
 */
public class ProductListSimpleBean {


        public List<NameListBean> name_list;

        public List<NameListBean> getName_list() {
            return name_list;
        }

        public void setName_list(List<NameListBean> name_list) {
            this.name_list = name_list;
        }

        public static class NameListBean {
            /**
             * title : A
             * list : [{"product_id":"71","product_name":"爱钱钱测试","product_logo":"http://static.tianshenjr.com/admin/daichao/product/201805311527746411.png"},{"product_id":"19","product_name":"爱转测试","product_logo":"http://static.tianshenjr.com/admin/daichao/product/201805111526024179.png"}]
             */

            private String title;
            private List<ListBean> list;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * product_id : 71
                 * product_name : 爱钱钱测试
                 * product_logo : http://static.tianshenjr.com/admin/daichao/product/201805311527746411.png
                 */

                private String product_id;
                private String product_name;
                private String product_logo;

                public String getProduct_id() {
                    return product_id;
                }

                public void setProduct_id(String product_id) {
                    this.product_id = product_id;
                }

                public String getProduct_name() {
                    return product_name;
                }

                public void setProduct_name(String product_name) {
                    this.product_name = product_name;
                }

                public String getProduct_logo() {
                    return product_logo;
                }

                public void setProduct_logo(String product_logo) {
                    this.product_logo = product_logo;
                }
            }
        }
}
