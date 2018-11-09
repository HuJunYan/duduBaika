package com.dudubaika.model.bean;

import java.util.List;

/**
 * 机构搜索初始化数据
 * （顶部 标签 等等）
 */
public class FindInitBean {

        private List<TopListBean> top_list;
        private List<TagListBean> tag_list;
        private List<CornerListBean> corner_list;

        public List<TopListBean> getTop_list() {
            return top_list;
        }

        public void setTop_list(List<TopListBean> top_list) {
            this.top_list = top_list;
        }

        public List<TagListBean> getTag_list() {
            return tag_list;
        }

        public void setTag_list(List<TagListBean> tag_list) {
            this.tag_list = tag_list;
        }

        public List<CornerListBean> getCorner_list() {
            return corner_list;
        }

        public void setCorner_list(List<CornerListBean> corner_list) {
            this.corner_list = corner_list;
        }

        public static class TopListBean {
            /**
             * title : 机构角标
             * title_id : 1
             */

            private String title;
            private String title_id;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle_id() {
                return title_id;
            }

            public void setTitle_id(String title_id) {
                this.title_id = title_id;
            }
        }

        public static class TagListBean {
            /**
             * title : 机构标签
             * title_id : 1
             */

            private String title;
            private String title_id;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle_id() {
                return title_id;
            }

            public void setTitle_id(String title_id) {
                this.title_id = title_id;
            }
        }

        public static class CornerListBean {
            /**
             * title : 机构角标
             * title_id : 1
             */

            private String title;
            private String title_id;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle_id() {
                return title_id;
            }

            public void setTitle_id(String title_id) {
                this.title_id = title_id;
            }
        }

}
