package com.dudubaika.ui.activity

import android.os.Bundle
import com.dudubaika.R
import com.dudubaika.base.SimpleActivity
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.Utils
import kotlinx.android.synthetic.main.activity_about_we.*

class AboutWeActivity : SimpleActivity() {


    override fun getLayout(): Int = R.layout.activity_about_we

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_about)
        defaultTitle = "关于我们"
        tv_about.text  = defaultTitle
        iv_about_we.setOnClickListener({backActivity()})
    }

    override fun initData() {
        tv_version.text = "版本号："+ Utils.getVersion(mActivity)
        tv_version_content.text="< 嘟嘟白卡 > 专注网贷、信用卡信息分发。通过大数据分析，为您批量匹配最适合您的网贷和信用卡产品，实现真正的“一站式”服务。海量机构，多家申请，多家下款，安全便捷。\n" +
                "\n" +
                "更多资源获取，欢迎加入【 嘟嘟官方用户 QQ 群：230457516 】\n" +
                "官方客服微信：dudubaika001\n" +
                "\n" +
                "\uFEFF我们的工作人员会耐心解答您提出的每一个问题，更有众多骨灰级线上借贷变现玩家分享干货心得，为小白用户指点迷津。"
    }

    override fun onStart() {
        super.onStart()
        //统计
    }

}
