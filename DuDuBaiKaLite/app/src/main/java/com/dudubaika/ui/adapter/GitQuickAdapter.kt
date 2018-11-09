package com.dudubaika.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.model.bean.Repository

class GitQuickAdapter() : BaseQuickAdapter<Repository, BaseViewHolder>(R.layout.item_repository) {

    override fun convert(helper: BaseViewHolder?, item: Repository?) {
        helper?.setText(R.id.text_view_title, item?.name)
        helper?.setText(R.id.text_view_description, item?.description)
    }

}