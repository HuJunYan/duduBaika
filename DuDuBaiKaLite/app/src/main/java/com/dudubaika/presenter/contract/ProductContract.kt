package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.HomeButtomDialogBean
import com.dudubaika.model.bean.ProductInfoListBean

class ProductContract{


   interface View: BaseContract.BaseView{
       fun getPrductInfo(data: ProductInfoListBean?)
       fun getButtomDialogData(data: HomeButtomDialogBean){}
   }

    interface Presenter:BaseContract.BasePresenter<View>{


        //得到产品列表
        fun getProductInfo(product_type:String,current_page:String,page_size:String)

        fun getButtomDialog(){}

    }

}