package com.dudubaika.model.bean;

import java.util.List;

/*
* 信用卡首页bean
 */
public class HomeCreditCardBean {



        /**
         * ability_title : 功能卡专区
         * quality_title : 精品推荐
         * banner_list : [{"imgUrl":"展示图片链接","jumpUrl":"跳转链接","cardId":"信用卡id","type":"1跳转到信用卡详情页,2跳转到H5页面,3不做任何跳转"}]
         * bank_list : [{"bank_logo":"银行图片链接","bank_name":"银行名称","bank_id":"银行id"}]
         * ability_list : [{"ability_logo":"银行图片链接","ability_name":"银行名称","ability_id":"银行id","ability_des":"描述"}]
         * quality_list : [{"credit_logo":"银行卡图片链接","credit_name":"银行卡名称","credit_id":"银行卡id","credit_des":"描述","credit_apply":"立即办卡"}]
         */

        private String ability_title;
        private String quality_title;
        private List<BannerListBean> banner_list;
        private List<BankListBean> bank_list;
        private List<AbilityListBean> ability_list;
        private List<QualityListBean> quality_list;

        public String getAbility_title() {
            return ability_title;
        }

        public void setAbility_title(String ability_title) {
            this.ability_title = ability_title;
        }

        public String getQuality_title() {
            return quality_title;
        }

        public void setQuality_title(String quality_title) {
            this.quality_title = quality_title;
        }

        public List<BannerListBean> getBanner_list() {
            return banner_list;
        }

        public void setBanner_list(List<BannerListBean> banner_list) {
            this.banner_list = banner_list;
        }

        public List<BankListBean> getBank_list() {
            return bank_list;
        }

        public void setBank_list(List<BankListBean> bank_list) {
            this.bank_list = bank_list;
        }

        public List<AbilityListBean> getAbility_list() {
            return ability_list;
        }

        public void setAbility_list(List<AbilityListBean> ability_list) {
            this.ability_list = ability_list;
        }

        public List<QualityListBean> getQuality_list() {
            return quality_list;
        }

        public void setQuality_list(List<QualityListBean> quality_list) {
            this.quality_list = quality_list;
        }

        public static class BannerListBean {
            /**
             * imgUrl : 展示图片链接
             * jumpUrl : 跳转链接
             * cardId : 信用卡id
             * type : 1跳转到信用卡详情页,2跳转到H5页面,3不做任何跳转
             */

            private String imgUrl;
            private String jumpUrl;
            private String cardId;
            private String cardName;
            private String type;

            public String getCardName() {
                return cardName;
            }

            public void setCardName(String cardName) {
                this.cardName = cardName;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getJumpUrl() {
                return jumpUrl;
            }

            public void setJumpUrl(String jumpUrl) {
                this.jumpUrl = jumpUrl;
            }

            public String getCardId() {
                return cardId;
            }

            public void setCardId(String cardId) {
                this.cardId = cardId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class BankListBean {
            /**
             * bank_logo : 银行图片链接
             * bank_name : 银行名称
             * bank_id : 银行id
             */

            private String bank_logo;
            private String bank_name;
            private String bank_id;

            public String getBank_logo() {
                return bank_logo;
            }

            public void setBank_logo(String bank_logo) {
                this.bank_logo = bank_logo;
            }

            public String getBank_name() {
                return bank_name;
            }

            public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
            }

            public String getBank_id() {
                return bank_id;
            }

            public void setBank_id(String bank_id) {
                this.bank_id = bank_id;
            }
        }

        public static class AbilityListBean {
            /**
             * ability_logo : 银行图片链接
             * ability_name : 银行名称
             * ability_id : 银行id
             * ability_des : 描述
             */

            private String ability_logo;
            private String ability_name;
            private String ability_id;
            private String ability_des;

            public String getAbility_logo() {
                return ability_logo;
            }

            public void setAbility_logo(String ability_logo) {
                this.ability_logo = ability_logo;
            }

            public String getAbility_name() {
                return ability_name;
            }

            public void setAbility_name(String ability_name) {
                this.ability_name = ability_name;
            }

            public String getAbility_id() {
                return ability_id;
            }

            public void setAbility_id(String ability_id) {
                this.ability_id = ability_id;
            }

            public String getAbility_des() {
                return ability_des;
            }

            public void setAbility_des(String ability_des) {
                this.ability_des = ability_des;
            }
        }

        public static class QualityListBean {
            /**
             * credit_logo : 银行卡图片链接
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
