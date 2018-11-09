package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.ProductInfoListBean

/**
 * 标签页数据contract
 */
interface TagProductContract{

       interface View :BaseContract.BaseView{

           fun showTagProductListDat(data: ProductInfoListBean)
       }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getTagProductList(id:String,current_page:String,page_size :String)

    }

}
