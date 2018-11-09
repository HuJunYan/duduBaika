package com.dudubaika.model.bean

/**
 * Created by wang on 2018/1/30.
 */
data class ExtroContactsBean(var eme_contacts: ArrayList<EmeContacts>, var type_list: ArrayList<TypeList>) {
    data class EmeContacts(var type: String, var type_name: String, var contact_name: String, var contact_phone: String)
    data class TypeList(var type: String, var type_name: String)
}