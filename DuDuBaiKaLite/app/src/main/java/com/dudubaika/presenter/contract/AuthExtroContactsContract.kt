package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.ContactsBean
import com.dudubaika.model.bean.ExtroContactsBean
import java.util.*

/**
 * Created by admin on 2018/1/16.
 */
interface AuthExtroContactsContract {

    interface View : BaseContract.BaseView {
        fun processExtroData(data: ExtroContactsBean?)
        fun processUploadContactsResult()
        fun processUploadResult()
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun getExtroContacts()
        fun saveExtroContacts(map: HashMap<String, ExtroContactsBean.EmeContacts>)
        fun uploadContacts(list: List<ContactsBean>)

    }
}