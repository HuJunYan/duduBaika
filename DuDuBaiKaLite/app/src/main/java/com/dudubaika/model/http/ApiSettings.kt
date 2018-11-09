package com.dudubaika.model.http

object ApiSettings {
    const val PATH_ORGANIZATION = "organization"
    const val PARAM_REPOS_TYPE = "type"
    const val PAGE = "page"//加载第几页
    const val PER_PAGE = "per_page"//一次加载多少条数据

    const val PATH_REPO = "repo"
    const val PATH_OWNER = "owner"

    const val ORGANIZATION_REPOS = "orgs/{$PATH_ORGANIZATION}/repos"
    const val REPOSITORY = "/repos/{$PATH_OWNER}/{$PATH_REPO}"


    //得到首页广告图
    const val GET_BANNER_LIST = "getBannerList"

    //得到产品列表
    const val GET_PRODUCT_LIST = "getProductList"
    //得到用户的状态
    const val GET_UESR_AUTH_RESULT = "getUserAuthResult"

    //登录
    const val SIGNIN = "signIn"

    //获取验证码
    const val GET_VERIFY_CODE = "getVerifyCode"

    //注册
    const val SIGN_UP = "signUp"

    //获取用户个人信息中的职业列表
    const val GET_USER_INFO = "getJobList"
    //保存用户个人信息
    const val SAVE_USER_INFO = "saveUserInfo"
    //获取省份列表
    const val GET_PROVINCE = "getProvince"
    //获取城市列表
    const val GET_CITY = "getCity"
    //获取区域列表
    const val GET_COUNTY = "getCounty"
    //获取紧急联系人信息 -->关系列表
    const val GET_EXTRO_CONTACTS = "getRelationshipList"
    //保存紧急联系人信息
    const val SAVE_EXTRO_CONTACTS = "saveExtroContacts"
    //上传通讯录
    const val SAVE_CONTACTS = "saveContacts"
    //设置密码
    const val SET_PASSWORD = "setPassword"
    //信用评估
    const val CREDIT_ASSESS = "creditAssess"
    //上传短信
    const val SAVE_PHONE_INFO = "savePhoneInfo"
    //获取最后一次短信上传时间
    const val GET_LAST_SMS_TIME = "getLastSmsTime"
    //信用认证提交
    const val CREDIT_APPLY = "creditApply"
    //重置密码
    const val RESET_PASSWORD = "resetPassword"
    //图片上传
    const val UPLOAD_IMAGE = "uploadImage"
    //退出登录
    const val LOGOUT = "logout"
    //意见反馈
    const val SUBMIT_FEEDBACK = "submitFeedback"
    //产品推荐
    const val PRODUCT_RECOMMEND = "productRecommend"
    //获取商品详情
    const val PRODUCT_DETAIL = "productDetail"
    //立即申请
    const val NOW_APPLY = "nowApply"
    //检查更新
    const val CHECK_UPGRADE = "checkUpgrade"

    const val SAVE_BACK_ID_CARD_DATA = "saveBackIdCardData"

    const val GET_ID_NUM_INFO = "getIdNumInfo"
    //获得推荐位信息
    const val GET_BANNER_INFO = "getBannerInfo"
    //埋点
    const val BURIED_POINT = "buriedPoint"
    //获取用户认证的状态
    const val GET_USER_AUTH_STATUS = "getUserAuthStatus"
    //获取用户额度后展示的数据
    const val GET_USER_LOAN = "getUserLoan"
    //校验身份一致性并保存
    const val CHECK_FACE = "checkFace"
    //orc身份证
    const val OCR_IDCARD = "v1/ocridcard"
    //人脸对比
    const val VERIFY = "v2/verify"
    //保存用户身份认证信息
    const val SAVE_IDNUM_INFO = "saveIdNumInfo"
    //得到用户身份认证信息
    const val GET_IDNUM_INFO = "getIdNumInfo"
    //得到用户账本信息
    const val GET_LOAN_NOTES = "getLoanNotes"
    //更改用户账本状态
    const val CHANGE_NOTES_STATUS = "changeNotesStatus"
    //获取用户待还/贷款记录
    const val GET_NOTES_LIST = "getNotesList"
    //获取用户账单详情
    const val GET_NOTES_DETAIL = "getNotesDetail"
    //增加或者更改账单
    const val SAVENOTESDETAIL = "saveNotesDetail"
    //获取下款入口
    const val GET_LOAN_NAME_LIST = "getLoanNameList"
   //获取下款入口
    const val CHANGE_MEXI_STATUS = "changeMexiStatus"
    //极光推送消息详情页
    const val GET_MSG_INFO = "getMsgInfo"
    //设置消息为已读
    const val READ_MSG = "readMsg"
    //消息中心列表
    const val GET_MSG_CENTER_LIST = "getMsgCenterList"
   //是否有未读消息
    const val MINE = "mine"
    //是否有未读消息
    const val HOME_POPUP = "homePopUp"
    //帮助中心
    const val HELP_CENTER = "helpCenter"
    //获取信用卡首页数据
    const val HOME_CREDIT_CARD = "homeCreditCard"
    //获取信用卡列表数据
    const val CREDIT_CARD_LIST = "creditCardList"
    //获取信用卡详情数据
    const val CREDIT_CARD_DETIAL = "creditCardDetial"
    //获取筛选初始化数据
    const val SCREEN_PAGE = "screenPage"
    //获取筛选数据
    const val PRODUCT_SEARCH = "productSearch"
    //获取发现首页数据
    const val FIND_PAGE = "findPageNew"
    //获取论坛首页数据
    const val GET_DISCUSS_LIST = "getDiscussList"
    //获取论坛我的帖子数据
    const val MY_DISCUSS_LIST = "myDiscussList"
    //发帖
    const val SAVE_DISCUSS = "saveDiscuss"
    //搜帖
    const val DISCUSS_SEARCH = "discussSearch"
   //帖子详情
    const val DISCUSS_DETAIL = "discussDetail"
    //评论帖子
    const val PUBLISH_COMMENT = "publishComment"
    //评论帖子
    const val REPORTDISCUSS = "reportDiscuss"
    //评论帖子
    const val REPORT_PAGE = "reportPage"
    //快讯消息
    const val NEWS = "news"
    //快讯消息
    const val START_ADVERTISE = "startAdvertise"
    //App启动时间
    const val START_APP = "startapp"
    //app关闭时间
    const val STOP_APP = "stopapp"
    //获取首页弹窗
    const val HOME_DIALOG = "homeDialog"
    //获取首页弹窗
    const val FIND_TAGE_SEARCH = "findTageSearch"


}